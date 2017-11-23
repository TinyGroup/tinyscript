package org.tinygroup.tinyscript.expression.typeconvert;

import org.tinygroup.tinyscript.expression.TypeConvertProcessor;

public class BooleanTypeConvertProcessor implements TypeConvertProcessor {

	public String getName() {
		return "boolean";
	}

	public Object convert(Object... parameters) throws Exception {
		if(parameters!=null && parameters.length>0){
		   return Boolean.parseBoolean(parameters[0].toString());
		}
		return null;
	}

}
