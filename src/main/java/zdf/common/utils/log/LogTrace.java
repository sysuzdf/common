package zdf.common.utils.log;

import java.lang.annotation.*;

/**
 * This annotation can be applied on api-entry level methods ,
 * those methods should accept ApiRequest as input, and return ApiResponse.
 * Methods with this annotation can:
 * 1. Set MDC(LOG_ID) for tracing the whole thread
 * 2. Format validation for ApiRequest
 * 3. Log ApiRequest \ ApiResponse in json format
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogTrace {
    /**
     * whether the method is  api-entry
     * api-entry method should accept only one ApiRequest as input and return ApiResponse
     * @return
     */
    boolean apiEntry() default false;

    /**
     * whether log the input
     * @return
     */
    boolean logInput() default true;

    /**
     * whether log the return value
     * @return
     */
    boolean logReturn() default true;

    /**
     * whether validate input,and the input should be ApiRequest<T> ,
     * ApiRequest<T>.getBody() will be validated
     * @return
     */
    boolean validateInput() default false;

    /**
     * the validation groups for this method
     * @return
     */
    Class<?>[] validateGroups() default {};
}
