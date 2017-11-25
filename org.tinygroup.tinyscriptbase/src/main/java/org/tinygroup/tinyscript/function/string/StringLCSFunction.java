package org.tinygroup.tinyscript.function.string;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class StringLCSFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "lcs";
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (this.checkParameters(parameters, 2)) {
				String str1 = (String) parameters[0];
				String str2 = (String) parameters[1];
				return calculate(str1, str2);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private String calculate(String strOne, String strTwo) {
		if (StringUtil.isBlank(strOne) || StringUtil.isBlank(strTwo)) {
			return "";
		}
		int[] topLine = new int[strOne.length()];
		int[] currentLine = new int[strTwo.length()];
		int maxLen = 0;
		
		// 矩阵元素最大值出现在第几列
		int pos = 0;
		char ch = ' ';
		for (int i = 0; i < strTwo.length(); i++) {
			ch = strTwo.charAt(i);
			for (int j = 0; j < strOne.length(); j++) {
				if (ch == strOne.charAt(j)) {
					if (j == 0) {
						currentLine[j] = 1;
					} else {
						currentLine[j] = topLine[j - 1] + 1;
					}
					if (currentLine[j] > maxLen) {
						maxLen = currentLine[j];
						pos = j;
					}
				}
			}
			// 将矩阵的当前行元素赋值给topLine数组; 并清空currentLine数组
			for (int k = 0; k < strOne.length(); k++) {
				topLine[k] = currentLine[k];
				currentLine[k] = 0;
			}
		}
		return strOne.substring(pos - maxLen + 1, pos + 1);
	}

}
