package org.tinygroup.tinyscript.interpret.attribute;

import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.tinyscript.interpret.AttributeProcessor;

/**
 * 数组的length是指令，不是字段属性，无法通过反射获取
 * @author yancheng11334
 *
 */
public class ArrayLengthProcessor implements AttributeProcessor{

	public boolean isMatch(Object object, Object name) {
		return "length".equals(name) && object!=null && object.getClass().isArray();
	}

	public Object getAttribute(Object object, Object name) throws Exception {
		return ArrayUtil.arrayLength(object);
	}

}
