package org.tinygroup.tinyscript.expression.typeconvert;

import org.tinygroup.tinyscript.expression.TypeConvertProcessor;

public class CharTypeConvertProcessor implements TypeConvertProcessor {

	public String getName() {
		return "char";
	}

	public Object convert(Object... parameters) throws Exception {
		if(parameters!=null && parameters.length>0){
		   char[] cs = parameters[0].toString().toCharArray();
		   if(cs!=null && cs.length>0){
			  return cs[0];
		   }
		}
		return null;
	}

}
