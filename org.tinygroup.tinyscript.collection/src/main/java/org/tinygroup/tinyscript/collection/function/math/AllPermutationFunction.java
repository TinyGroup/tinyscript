package org.tinygroup.tinyscript.collection.function.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ScriptResult;

/**
 * 全排列函数
 * @author yancheng11334
 *
 */
public class AllPermutationFunction extends AbstractScriptFunction {

	public String getNames() {
		return "permuteAll";
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
				Collection c = (Collection) parameters[0];
				LambdaFunction function = (LambdaFunction) parameters[1];
				Object[] source = new ArrayList(c).toArray();
				return getAllPermutations(source,function,context,source.length);
			}else if(checkParameters(parameters, 3)){
				Collection c = (Collection) parameters[0];
				Integer length = (Integer) parameters[1];
				if(length<1 || length>c.size()){
					throw new ScriptException(String.format("%s函数的参数格式不正确:length超出正常范围", getNames(),length));
				}
				LambdaFunction function = (LambdaFunction) parameters[2];
				Object[] source = new ArrayList(c).toArray();
				return getAllPermutations(source,function,context,length);
			}
        	throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
        }catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	protected  ScriptResult getAllPermutations(Object[] source,LambdaFunction function,ScriptContext context,int length) throws Exception {
		getCombinations(source,function,context,length);
		return ScriptResult.VOID_RESULT;
	}

	protected  void getPermutations(LambdaFunction function,ScriptContext context,
			Object[] source, int start, int len) throws Exception {
		if (start == len) {
			// 未来扩展表达式或lambda函数
			function.execute(context, Arrays.asList(source));
		}
		for (int i = start; i != len; i++) {
			swap(source, i, start);
			getPermutations(function,context, source, start + 1, len);
			swap(source, i, start);
		}
	}

	protected  void swap(Object[] source, int i, int j) {
		Object t;
		t = source[i];
		source[i] = source[j];
		source[j] = t;
	}
	
	protected ScriptResult getCombinations(Object[] source,LambdaFunction function,ScriptContext context,int length) throws Exception{
		List<Object> temp = new ArrayList<Object>();
		getCombinations(function,context,source,temp,0,length);
		return ScriptResult.VOID_RESULT;
	}
	
	protected void getCombinations(LambdaFunction function,ScriptContext context,Object[] source,List<Object> temp,int start,int len) throws Exception{
		if(len==0){
		   //执行代码块
		   getPermutations(function,context,temp.toArray(),0,temp.size());
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
