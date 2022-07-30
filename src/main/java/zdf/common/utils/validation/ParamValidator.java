package zdf.common.utils.validation;

import zdf.common.api.ApiResponse;
import zdf.common.api.ResponseCode;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

public class ParamValidator {
    private final static Validator validator= Validation.buildDefaultValidatorFactory().getValidator();

    public static <T> ApiResponse validate(T param, Class<?>... groups){
        ApiResponse resp=new ApiResponse();
        resp.setCode(ResponseCode.SUCCESS);
        Set<ConstraintViolation<T>> violations=validator.validate(param,groups);
        StringBuilder errs=new StringBuilder("");
        Iterator<ConstraintViolation<T>> iter=violations.iterator();
        while(iter.hasNext()){
            resp.setCode(ResponseCode.INVALID_FORMAT);
            errs.append(iter.next().getMessage()+"\n");
        }
        resp.setErrMsg(errs.toString());
        return resp;
    }
}
