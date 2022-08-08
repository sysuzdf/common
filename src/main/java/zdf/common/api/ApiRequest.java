package zdf.common.api;

import zdf.common.utils.log.LogTool;

import java.util.List;
import java.util.UUID;

/**
 * wrapper bean for request
 * @param <T>
 */
public class ApiRequest<T> {

    /**
     * the api version for this request
     */
    private String version="1.0.0";

    /**
     * actual business payload
     */
    private T body;

    /**
     * help to identify the request, doesn't have any business meanings
     */
    private String requestId= UUID.randomUUID().toString();

    /**
     * help to identify where the request comes from
     */
    private String source;


    /**
     * help to identify which host sent the request
     */
    private List<String> hosts= LogTool.getLocalIpList();

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
