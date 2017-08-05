package org.tinygroup.tinyscript.dataset;

import java.util.Date;

/**
 * Created by luoguo on 2014/7/6.
 */
public enum FieldType {
    INTEGER(Integer.class),
    LONG(Long.class),
    STRING(String.class),
    FLOAT(Float.class),
    DOUBLE(Double.class),
    DATE(Date.class),
    BYTE(Byte.class),
    BOOLEAN(Boolean.class);

    FieldType(Class type) {
        this.type = type;
    }

    private Class type;

    public Class<?> getType() {
        return type;
    }


    public <T> T valueOf(FieldType fieldType, Object object) {
        //TODO 转换object为fieldType对应的类型的值
        return null;
    }
    public static FieldType getFieldType(String typeName){
        for(FieldType fieldType:values()){
            if(fieldType.toString().equalsIgnoreCase(typeName) || fieldType.getType().getName().equalsIgnoreCase(typeName)){
                return fieldType;
            }
        }
        return null;
    }

}
