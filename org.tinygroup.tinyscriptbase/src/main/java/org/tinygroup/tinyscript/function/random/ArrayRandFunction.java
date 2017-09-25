package org.tinygroup.tinyscript.function.random;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.call.JavaMethodUtil;

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
        	   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			}else if(checkParameters(parameters, 1)){
			   Object obj = getValue(parameters[0]);
			   if(obj.getClass().isArray()){
				  return randArray(obj,1);
			   }else if(obj instanceof Collection){
				  return randCollection((Collection)obj,1,false);
			   }
			}else if(checkParameters(parameters, 2)){
			    Object obj = getValue(parameters[0]);
			    int num = (Integer)getValue(parameters[1]);
				if(obj.getClass().isArray()){
				   return randArray(obj,num);
				}else if(obj instanceof Collection){
				   return randCollection((Collection)obj,num,false);
				}
			}else if(checkParameters(parameters, 3)){
			    Object obj = getValue(parameters[0]);
			    int num = (Integer)getValue(parameters[1]);
			    boolean variable = (Boolean)getValue(parameters[2]);
				if(obj.getClass().isArray()){
				   if(variable){
					  throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.randarray.unsupport"));
				   }
				   return randArray(obj,num);
				}else if(obj instanceof Collection){
				   return randCollection((Collection)obj,num,variable);
				}
			}
        	throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
        }catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()),e);
		}
	}
	
	/**
	 * 数组获取若干随机元素，注意不支持改变数组
	 * @param array
	 * @param num
	 * @return
	 * @throws Exception 
	 */
	private Object randArray(Object array,int num) throws Exception{
		int length = Array.getLength(array);
		if(length>0 && num>0){
		   Random r = new Random();
		   if(num==1){
			  //返回单个元素
			  return Array.get(array, r.nextInt(length));   
		   }else if(num<=length){
			  //返回数组
			  Map<Integer,Object> map = new HashMap<Integer,Object>();
			  while(map.size()<num){
				 int p = r.nextInt(length);
				 map.put(p, Array.get(array,p));
			  }
			  return map.values().toArray();
		   }else{
			  throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.randarray.error", num,length));
		   }
		  
		}
		return null;
	}
	
	/**
	 * 集合获取若干随机元素
	 * @param collection
	 * @param num
	 * @param variable
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object randCollection(Collection collection,int num,boolean variable) throws Exception{
		int size = collection.size();
		if(size>0 && num>0){
		   List list = new ArrayList(collection);
		   Random r = new Random();
		   if(num==1){
			  //返回单个元素 
			  int p = r.nextInt(size);
			  Object obj = list.get(p);
			  if(variable){
				 list.remove(p);
			  }
			  return obj;
		   }else if(num <= size){
			  //返回集合
			   Map<Integer,Object> map = new HashMap<Integer,Object>();
			   Collection result = (Collection) JavaMethodUtil.clone(collection);
			   result.clear();
			   while(map.size()<num){
				   int p = r.nextInt(size);
				   map.put(p, list.get(p));
			   }
			   result.addAll(map.values());
			   if(variable){
				  collection.removeAll(result);
			   }
			   return result;
		   }else{
			   throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.randarray.error", num,size));
		   }
			   
		   
		}
		return null;
	}

}
