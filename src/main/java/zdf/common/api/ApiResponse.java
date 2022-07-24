package zdf.common.api;

import java.util.UUID;

/**
 * Wrapper bean for api response
 * @param <T>
 */
public class ApiResponse <T>{

    /**
     * the version for this api, should the same with the request served by this response
     */
    private String version="1.0.0";

    /**
     * actual business payload
     */
    private T body;

    /**
     * the source of the request that served by this response
     * @See ApiRequest.getSource();
     */
    private String requestSource;

    /**
     * the requestId of the request that served by this response
     * @See ApiRequest.getSource();
     */
    private String requestId;

    /**
     * help to identify the response, doesn't have any business meanings
     */
    private String responseId = UUID.randomUUID().toString();

    /**
     * help to identify the provider of this response
     */
    private String provider;

    /**
     * help to identify which host sent the response
     */
    private String host;

    /**
     * the response code
     */
    private ResponseCode code;

    /**
     * if code is not SUCCESS, return the detail error code
     */
    private IBaseEnum errorCode;

    /**
     * if code is not SUCCESS, return error messages
     */
    private String errMsg;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getResponseId() {
        return responseId;
    }

    public void setResponseId(String responseId) {
        this.responseId = responseId;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public ResponseCode getCode() {
        return code;
    }

    public void setCode(ResponseCode code) {
        this.code = code;
    }

    public IBaseEnum getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(IBaseEnum errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }
}
