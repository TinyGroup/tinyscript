package org.tinygroup.tinyscript.collection.function.math;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptResult;

/**
 * 排列函数
 * 
 * @author yancheng11334
 *
 */
public class PermutationFunction extends AbstractScriptFunction {

	public String getNames() {
		return "permute";
	}

	public String getBindingTypes() {
		return "java.util.Collection";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 2)) {
				Collection c = (Collection) parameters[0];
				LambdaFunction function = (LambdaFunction) parameters[1];
				Object[] source = new ArrayList(c).toArray();
				return getPermutations(function, context, source, source.length);
			} else if (checkParameters(parameters, 3)) {
				Collection c = (Collection) parameters[0];
				Integer length = (Integer) parameters[1];
				if (length < 1) {
					throw new ScriptException(
							ResourceBundleUtil.getResourceMessage("collection", "collection.length.error", getNames()));
				}
				LambdaFunction function = (LambdaFunction) parameters[2];
				Object[] source = new ArrayList(c).toArray();
				return getPermutations(function, context, source, length);
			}
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	protected ScriptResult getPermutations(LambdaFunction function, ScriptContext context, Object[] source, int len)
			throws Exception {
		getPermutations(len, function, context, source);
		return ScriptResult.VOID_RESULT;
	}

	protected void getPermutations(int len, LambdaFunction function, ScriptContext context, Object[] source, int... is)
			throws Exception {
		if (is != null && is.length == len) {
			List<Object> list = new ArrayList<Object>();
			for (int i = 0; i < len; i++) {
				list.add(source[is[i]]);
			}
			// 添加表达式或lambda过滤处
			function.execute(context, list);
			return;
		}
		for (int i = 0; i < source.length; i++) {
			getPermutations(len, function, context, source, merge(is, i));
		}

	}

	protected int[] merge(int[] is, int item) {
		int[] nis;
		if (is == null) {
			nis = new int[1];
			nis[0] = item;
		} else {
			nis = new int[is.length + 1];
			for (int i = 0; i < is.length; i++) {
				nis[i] = is[i];
			}
			nis[is.length] = item;
		}
		return nis;
	}
}
