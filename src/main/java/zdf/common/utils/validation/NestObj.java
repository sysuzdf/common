package zdf.common.utils.validation;

import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * Nested object fields validation rule
 */
@Target({ElementType.FIELD,ElementType.ANNOTATION_TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NestObj {
    String message() default "Nested object validate fails";

    boolean nullable() default false;

    /**
     * javax validation need this
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}
