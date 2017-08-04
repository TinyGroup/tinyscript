package org.tinygroup.tinyscript.interpret.newinstance;

import java.lang.reflect.Constructor;
import java.util.List;

import org.springframework.util.CollectionUtils;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.NewInstanceProcessor;
import org.tinygroup.tinyscript.interpret.ScriptUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 基本的java类创建实例
 * @author yancheng11334
 *
 */
public class JavaInstanceProcessor implements NewInstanceProcessor<Class<?>>{

	public Class<?> findClass(ScriptSegment classSegment, String className,
			List<String> importList) {
		return ScriptUtil.findJavaClass(className, classSegment, importList);
	}

	public Object newInstance(Class<?> clazz, ScriptContext context,
			List<Object> paramList) throws Exception {
		if (CollectionUtils.isEmpty(paramList)) {
			// 执行无参构造
			return clazz.newInstance();
		}else{
			// 执行有参构造
			for (Constructor<?> constructor : clazz.getConstructors()) {
				if(checkParameter(constructor,paramList) && JavaConstructorUtil.checkConstructor(constructor, paramList)){ //检查构造函数参数匹配
				   Object[] newParameters = JavaConstructorUtil.wrapper(context,constructor, paramList);
				   return constructor.newInstance(newParameters);
				}
			}
			//抛出不匹配信息
		    throw new NotMatchException(); 
		}
	}
	
	private boolean checkParameter(Constructor<?> constructor,List<Object> paramList){
		int n = constructor.getParameterTypes()==null?0:constructor.getParameterTypes().length;
		int m = paramList==null?0:paramList.size();
		return m==n;
	}

	
}
