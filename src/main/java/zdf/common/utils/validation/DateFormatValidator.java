package zdf.common.utils.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;

/**
 * Validate @DateFormat
 */
public class DateFormatValidator implements ConstraintValidator<DateFormat,String> {
    private String format;
    private boolean nullable;

    @Override
    public void initialize(DateFormat constraintAnnotation) {
        this.format= constraintAnnotation.format();
        this.nullable= constraintAnnotation.nullable();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value==null && !nullable){
            return false;
        }
        if(value!=null && !nullable){
            SimpleDateFormat sdf=new SimpleDateFormat(format);
            try{
                sdf.parse(value);
            }catch (Exception ex){
                return false;
            }
        }
        return true;
    }
}
