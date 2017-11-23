package org.tinygroup.tinyscript.expression.typeconvert;

import org.tinygroup.tinyscript.expression.TypeConvertProcessor;

public class ShortTypeConvertProcessor implements TypeConvertProcessor {

	public String getName() {
		return "short";
	}

	public Object convert(Object... parameters) throws Exception {
		if(parameters!=null && parameters.length>0){
		   return Short.parseShort(parameters[0].toString());
		}
		return null;
	}

}
