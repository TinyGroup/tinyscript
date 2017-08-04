package org.tinygroup.tinyscript.function.random;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

/**
 * 从集合选择元素作为随机结果
 * @author yancheng11334
 *
 */
public class ArrayRandFunction extends AbstractScriptFunction{

	public String getNames() {
		return "randArray";
	}
	
	public boolean  enableExpressionParameter(){
		return true;
	}

	@SuppressWarnings("rawtypes")
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
        try{
        	if(parameters == null || parameters.length == 0){
			   throw new ScriptException(String.format("%s函数的参数为空!", getNames()));
			}else if(checkParameters(parameters, 1)){
			   Object obj = getValue(parameters[0]);
			   if(obj.getClass().isArray()){
				  return randArray(obj);
			   }else if(obj instanceof Collection){
				  return randCollection((Collection)obj);
			   }
			}

        	throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
        }catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	private Object randArray(Object array){
		int length = Array.getLength(array);
		if(length>0){
		   Random r = new Random();
		   return Array.get(array, r.nextInt(length));
		}
		return null;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object randCollection(Collection collection){
		int size = collection.size();
		if(size>0){
		   List list = new ArrayList(collection);
		   Random r = new Random();
		   return list.get(r.nextInt(size));
		}
		return null;
	}

}
