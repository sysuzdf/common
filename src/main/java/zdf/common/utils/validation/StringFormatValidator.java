package zdf.common.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validate @StringFormat
 *
 */
public class StringFormatValidator implements ConstraintValidator<StringFormat,String> {
    private int min;
    private int max;
    private boolean nullable;
    private String regex=null;

    @Override
    public void initialize(StringFormat constraintAnnotation) {
        this.max=constraintAnnotation.maxLength();
        this.min= constraintAnnotation.minLength();
        this.nullable= constraintAnnotation.nullable();
        this.regex=constraintAnnotation.regex();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(null==value&&!nullable){
            return false;
        }
        if(null!=value){
            if(min>=0&&value.length()<min){
                return false;
            }
            if(max>=0&&value.length()>max){
                return false;
            }
            if(regex!=null&&regex.length()>0&&!value.matches(regex)){
                return false;
            }
        }
        return true;
    }
}
