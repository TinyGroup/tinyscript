package org.tinygroup.tinyscript.interpret.call;

import org.tinygroup.tinyscript.ScriptCollectionModel;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.collection.CollectionModelUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;

/**
 * 集合模型方法处理
 * @author yancheng11334
 *
 */
public class CollectionModelProcessor extends AbstractMethodProcessor{

	protected Object invokeMethod(ScriptSegment segment, ScriptContext context,
			Object object, String methodName, Object... parameters)
			throws Exception {
		if(object!=null){
			ScriptCollectionModel model = CollectionModelUtil.getScriptCollectionModel(object);
			if(model!=null){
			   return model.executeFunction(segment, context, object, methodName, parameters);
			}
		}
		throw new NotMatchException(); 
	}

}
