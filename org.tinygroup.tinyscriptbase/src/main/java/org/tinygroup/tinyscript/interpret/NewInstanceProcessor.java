package org.tinygroup.tinyscript.interpret;

import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

/**
 * 创建实例处理器
 * 
 * @author yancheng11334
 * 
 * @param <T>
 */
public interface NewInstanceProcessor<T> {

	/**
	 * 查询类
	 * 
	 * @param classSegment
	 * @param className
	 * @param importList
	 * @return
	 */
	T findClass(ScriptSegment classSegment, String className,
			List<String> importList);

	/**
	 * 创建新实例
	 * @param clazz
	 * @param context
	 * @param paramList
	 * @return
	 * @throws Exception
	 */
	Object newInstance(T clazz, ScriptContext context, List<Object> paramList)
			throws Exception;
}
