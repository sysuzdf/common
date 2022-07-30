package zdf.common.utils.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import zdf.common.api.ApiRequest;
import zdf.common.api.ApiResponse;
import zdf.common.api.ResponseCode;
import zdf.common.utils.validation.NestObj;
import zdf.common.utils.validation.ParamValidator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.UUID;

/**
 * wrap logback
 */
@Aspect
public class LogTool {

    private static Logger logger = LoggerFactory.getLogger(LogTool.class);

    /**
     * the keys for logback mdc
     */
    private static String LOG_ID="LOG_ID";

    public static void beginTrace(String logid){
        MDC.put(LOG_ID,logid);
    }

    public static void endTrace(){
        MDC.remove(LOG_ID);
    }

    /**
     * Provides log trace (MDC) setting \ ApiRequest format validation for (api entry layer) method annotated by @LogTrace
     * If Method annotated by @LogTrace and LogTrace.validateInput=ture, it should return ApiResponse<T>
     * @param jPoint
     * @return ApiResponse
     */
    @Around("@annotation(zdf.common.utils.log.LogTrace)")
    public Object doAroundAdvice(ProceedingJoinPoint jPoint){
        Long startMills=System.currentTimeMillis();
        Object result=null;
        //begin trace if MDC is not set
        boolean logStarter=false;
        if(MDC.get(LOG_ID)==null){
            logStarter=true;
            beginTrace(UUID.randomUUID().toString());
        }
        Object[] input=jPoint.getArgs();
        LogTrace logTraceAnno=getLogTraceAnno(jPoint);
        ObjectMapper objm=new ObjectMapper();
        try{
            if(logTraceAnno!=null){
                if(logTraceAnno.logInput()){
                    String inputJson=objm.writeValueAsString(input);
                    logger.info("class:{} method:{} input:{}",jPoint.getTarget().getClass().getName(),
                            jPoint.getSignature().getName(),inputJson);
                }
                boolean formatValid=true;
                if(logTraceAnno.apiEntry()&&logTraceAnno.validateInput()){
                    ApiResponse validateResult=new ApiResponse();
                    //the method should have only one ApiRequest arguments
                    Object body=((ApiRequest) input[0]).getBody();
                    if(body==null){
                        validateResult.setCode(ResponseCode.INVALID_FORMAT);
                        validateResult.setErrMsg("ApiRequest.getBody() is null!");
                        return validateResult;
                    }
                    validateResult=validateBody(body,logTraceAnno);
                    if(!ResponseCode.SUCCESS.equals(validateResult.getCode())){
                        logger.info("input validation fail. {}",validateResult.getErrMsg());
                        formatValid=false;
                        result=validateResult;
                    }
                }
                if(formatValid){
                    //continue the actual call
                    result=jPoint.proceed();
                }
                if(logTraceAnno.logReturn()){
                    String returnJson=objm.writeValueAsString(result);
                    logger.info("class:{} method:{} return:{}",jPoint.getTarget().getClass().getName(),
                            jPoint.getSignature().getName(),returnJson);
                }
                return result;
            }
        }catch (Throwable th){
            if(logTraceAnno!=null && logTraceAnno.apiEntry()){
                //if it is api entry call, should return UNKNOWN code when uncaught exception happens
                ApiResponse unknownResp=new ApiResponse();
                unknownResp.setCode(ResponseCode.UNKNOWN);
                unknownResp.setErrMsg(getErrorStackAsString(th,0));
                logger.info("class:{} method:{} unknown error : {}",jPoint.getTarget().getClass().getName(),
                        jPoint.getSignature().getName(),unknownResp.getErrMsg());
                return unknownResp;
            }
        }
        finally {
            if(logStarter){
                //before return from the advice, the starter should clean the MDC
                //because thread could be reused, but new logid should be set for the next starter
                endTrace();
            }
            logger.info("class:{} method:{} end, time used : {}",jPoint.getTarget().getClass().getName(),
                    jPoint.getSignature().getName(),System.currentTimeMillis()-startMills);
        }
        return result;
    }

    private LogTrace getLogTraceAnno(ProceedingJoinPoint jPoint){
        LogTrace anno=null;
        Method method=((MethodSignature)jPoint.getSignature()).getMethod();
        try {
            Method realMethod=jPoint.getTarget().getClass().getDeclaredMethod(jPoint.getSignature().getName(),
                    method.getParameterTypes());
            anno=realMethod.getAnnotation(LogTrace.class);
        } catch (NoSuchMethodException e) {
            logger.debug("logTrace get realMethod fail",e);
        }
        if(anno==null){
            anno=method.getAnnotation(LogTrace.class);
        }
        if(anno==null){
            logger.debug("logTrace get annotation fail");
        }
        return anno;
    }

    private ApiResponse validateBody(Object body,LogTrace logTraceAnno){
        ApiResponse chkResp=new ApiResponse();
        chkResp.setCode(ResponseCode.SUCCESS);
        try {
            Class bodyClz = body.getClass();
            Field[] fields = bodyClz.getDeclaredFields();
            //if a body's field is annotated by NestObj, recursive it
            for (Field field : fields) {
                if (field.isAnnotationPresent(NestObj.class)) {
                    field.setAccessible(true);
                    Object nestedObj = field.get(body);
                    NestObj nestObjAnno = field.getAnnotation(NestObj.class);
                    if(nestedObj==null&& !nestObjAnno.nullable()){
                        chkResp.setCode(ResponseCode.INVALID_FORMAT);
                        chkResp.setErrMsg("filed "+field.getName()+" is not nullable");
                        return chkResp;
                    }
                    else if(nestedObj!=null){
                        if(nestedObj instanceof Collection){
                            for(Object childObj:(Collection)nestedObj){
                                ApiResponse childChk=validateBody(childObj,logTraceAnno);
                                if(!ResponseCode.SUCCESS.equals(childChk.getCode())){
                                    return childChk;
                                }
                            }
                        }
                    }
                }
            }
            //check the body itself
            chkResp= ParamValidator.validate(body,logTraceAnno.validateGroups());
        }catch (Exception ex){
            chkResp.setCode(ResponseCode.UNKNOWN);
            chkResp.setErrMsg(getErrorStackAsString(ex,0));
        }
        return chkResp;
    }

    public static String getErrorStackAsString(Throwable th,int limit){
        StringWriter sw=new StringWriter();
        PrintWriter pw=new PrintWriter(sw);
        try{
            th.printStackTrace(pw);
            pw.close();
            sw.close();
            String result=sw.toString();
            if(limit>0){
                result=result.substring(0, Math.min(result.length(), limit));
            }
            return result;
        }catch (Exception ex){
            logger.error("getErrorStackAsString fail",ex);
        }
        return "";
    }
}