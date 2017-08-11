package org.tinygroup.tinyscript.collection.function.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ScriptResult;

/**
 * 组合函数
 * @author yancheng11334
 *
 */
public class CombinationFunction extends AbstractScriptFunction {

	public String getNames() {
		return "combine";
	}
	
	public String getBindingTypes() {
		return "java.util.Collection";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
        	if(parameters == null || parameters.length == 0){
			   throw new ScriptException(String.format("%s函数的参数为空!", getNames()));
			}else if(checkParameters(parameters, 2)){
				//默认实现Cmm(m=n)
				Collection c = (Collection) parameters[0];
				LambdaFunction function = (LambdaFunction) parameters[1];
				Object[] source = new ArrayList(c).toArray();
			    return getCombinations(source,function,context);
			}else if(checkParameters(parameters, 3)){
				//实现Cmn(用户需要指定n)
				Collection c = (Collection) parameters[0];
				Integer length = (Integer) parameters[1];
				if(length<1 || length>c.size()){
					throw new ScriptException(String.format("%s函数的参数格式不正确:length超出正常范围", getNames(),length));
				}
				LambdaFunction function = (LambdaFunction) parameters[2];
				Object[] source = new ArrayList(c).toArray();
			    return getCombinations(source,function,context,length);
			}
        	throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
        }catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	protected ScriptResult getCombinations(Object[] source,LambdaFunction function,ScriptContext context,int len) throws Exception{
		List<Object> temp = new ArrayList<Object>();
		getCombinations(function,context,source,temp,0,len);
		return ScriptResult.VOID_RESULT;
	}
	
	protected ScriptResult getCombinations(Object[] source,LambdaFunction function,ScriptContext context) throws Exception{
		List<Object> temp = new ArrayList<Object>();
		int len = source.length+1;
		for(int i = 1 ; i != len ; i ++){
			getCombinations(function,context,source,temp,0,i);
		}
		return ScriptResult.VOID_RESULT;
	}
	
	protected void getCombinations(LambdaFunction function,ScriptContext context,Object[] source,List<Object> temp,int start,int len) throws Exception{
		if(len==0){
		   //执行代码块
		   function.execute(context, temp);
		   return ;
		}
		if(start==source.length){
		   return ;
		}
		temp.add(source[start]);
		getCombinations(function,context,source,temp,start+1,len-1);
		temp.remove(temp.size()-1);
		getCombinations(function,context,source,temp,start+1,len);
	}

}
