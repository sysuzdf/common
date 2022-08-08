package zdf.common.utils.validation;

import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * String field date-validation rules
 */
@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DateFormat {
    String message() default "invalid date format";

    /**
     * String date format
     * @return
     */
    String format() default "yyyyMMdd";

    /**
     * whether nullable
     * @return
     */
    boolean nullable() default true;

    /**
     * validation groups
     * @return
     */
    Class<?>[] groups() default {};

    /**
     * javax validation need this
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}
