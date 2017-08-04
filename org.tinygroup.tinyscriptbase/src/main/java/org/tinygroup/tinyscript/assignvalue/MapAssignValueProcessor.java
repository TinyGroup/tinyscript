package org.tinygroup.tinyscript.assignvalue;

import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;

/**
 * 处理map的key赋值
 * @author yancheng11334
 *
 */
public class MapAssignValueProcessor implements AssignValueProcessor{

	@SuppressWarnings("rawtypes")
	public boolean isMatch(String name, ScriptContext context) {
		if(name!=null){
		   String[] ss = name.split("\\.");
		   if(ss!=null && ss.length==2){
			  Object obj = context.get(ss[0]);
			  if(obj instanceof Map){
				 Map map = (Map) obj;
				 return map.containsKey(ss[1]);
			  }
		   }
		}
		return false;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void process(String name, Object value, ScriptContext context)
			throws Exception {
		//前面已经做完验证
		String[] ss = name.split("\\.");
		Map map = (Map) context.get(ss[0]);
		map.put(ss[1], value);
	}

}
