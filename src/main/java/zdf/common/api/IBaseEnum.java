package zdf.common.api;

/**
 * Basic enum
 */
public interface IBaseEnum {
    /**
     * the code of the enum
     * @return
     */
    String getCode();

    /**
     * the description of the enum
     * @return
     */
    String getDescription();

    /**
     * return the enum object by the valid code, otherwise return null
     * @param enumClass
     * @param code
     * @return
     * @param <T>
     */
    public static <T extends Enum<?> & IBaseEnum> T byCode(Class<T> enumClass,String code){
        for(T e : enumClass.getEnumConstants()){
            if(e.getCode().equals(code)){
                return e;
            }
        }
        return null;
    }

}
