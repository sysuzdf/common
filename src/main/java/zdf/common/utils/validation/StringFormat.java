package zdf.common.utils.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * String field validation rule
 */

@Target({ElementType.ANNOTATION_TYPE,ElementType.METHOD,ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {StringFormatValidator.class})
public @interface StringFormat {
    String message() default "invalid string format";

    /**
     * minimum string length validation
     * -1 : don't validate minimum length
     * @return
     */
    int minLength() default 0;

    /**
     * maximum string length validation
     * -1 : don't validate maximum length
     * @return
     */
    int maxLength() default -1;

    /**
     * validate by regex
     * @return
     */
    String regex() default "";

    /**
     * whether nullable
     * @return
     */
    boolean nullable() default false;

    /**
     * validate rule group
     */
    Class<?> [] groups() default {};

    /**
     * javax validation need this
     * @return
     */
    Class<? extends Payload>[] payload() default {};
}
