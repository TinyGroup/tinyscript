package org.tinygroup.tinyscript.interpret.newinstance;

import java.util.List;

import org.springframework.util.CollectionUtils;
import org.tinygroup.tinyscript.ScriptClass;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.NewInstanceProcessor;

/**
 * tiny脚本类实例化
 * @author yancheng11334
 *
 */
public class ScriptClassInstanceProcessor implements NewInstanceProcessor<ScriptClass>{

	public ScriptClass findClass(ScriptSegment segment, String className,
			List<String> importList) {
		ScriptSegment classSegment = null;
		// 查当前片段
		if(segment.getScriptClass()!=null && className.equals(segment.getScriptClass().getClassName())){
		   return segment.getScriptClass();	
		}
		// 再查询已经注册tiny脚本片段
		classSegment = segment.getScriptEngine()
				.getScriptSegment(className);
		if (classSegment != null) {
			return classSegment.getScriptClass();
		}
		
		//最后根据import再查找脚本片段
		if (!CollectionUtils.isEmpty(importList)) {
			String newClassName = null;
			for (String importPath : importList) {
				if (importPath.endsWith("*")) {
					newClassName = importPath.substring(0,
							importPath.length() - 1) + className;
				} else if(importPath.endsWith("."+className)){
					newClassName = importPath;
				} else{
					continue;
				}
				classSegment = segment.getScriptEngine().getScriptSegment(newClassName);
				if (classSegment != null) {
					return classSegment.getScriptClass();
				}
			}
			
		}
		return null;
	}

	public Object newInstance(ScriptClass clazz, ScriptContext context,
			List<Object> paramList) throws Exception {
		return clazz.newInstance(context,paramList.toArray());
	}


}
