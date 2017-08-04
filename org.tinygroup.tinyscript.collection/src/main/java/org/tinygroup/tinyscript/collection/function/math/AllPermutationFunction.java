package org.tinygroup.tinyscript.collection.function.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

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
				return getAllPermutations(source,function,context);
			}
        	throw new ScriptException(String.format("%s函数的参数格式不正确!", getNames()));
        }catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("%s函数执行发生异常:", getNames()),e);
		}
	}
	
	protected  ScriptResult getAllPermutations(Object[] source,LambdaFunction function,ScriptContext context) throws Exception {
		getAllPermutations(function,context, source, 0, source.length);
		return ScriptResult.VOID_RESULT;
	}

	protected  void getAllPermutations(LambdaFunction function,ScriptContext context,
			Object[] source, int start, int len) throws Exception {
		if (start == len) {
			// 未来扩展表达式或lambda函数
			function.execute(context, Arrays.asList(source));
		}
		for (int i = start; i != len; i++) {
			swap(source, i, start);
			getAllPermutations(function,context, source, start + 1, len);
			swap(source, i, start);
		}
	}

	protected  void swap(Object[] source, int i, int j) {
		Object t;
		t = source[i];
		source[i] = source[j];
		source[j] = t;
	}
}
