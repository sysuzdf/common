package zdf.common.api;

/**
 * Response code
 */
public enum ResponseCode implements IBaseEnum{

    /**
     * Successfully handled by the backend.
     * Maybe "just accepted (not really success under some async scenarios)" or "the business is success", depends on the actual api contract
     */
    SUCCESS("SUC","successfully handled"),
    /**
     * Fail to handled by the backend.
     * The business is fail (under sync scenario),like business logic fail or param checks fail.
     * The detail fail reason should define elsewhere by the api.
     */
    FAIL("FAIL","fail to handle"),
    /**
     * Unknown error happened, like network\hardware failures
     * It's not the end state, so you had better query or retry.
     */
    UNKNOWN("UN","unknown error"),

    /**
     * The request is fail because of the input format validation fail.
     */
    INVALID_FORMAT("INVALID_FORMAT","input validation fail")
    ;

    private String code;
    private String description;

    ResponseCode(String code,String description){
        this.code=code;
        this.description=description;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }
}
