package org.tinygroup.tinyscript.executor;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.executor.TinyScriptOperator;


/**
 * 集算器的模拟器执行
 * @author yancheng11334
 *
 */
public class ComputeEngineExecutor {
    private static TinyScriptOperator operator = new ComputeEngineOperator();
    
    /**
     * 固定入口
     * @param args
     * @throws ScriptException
     */
	public static void main(String[] args) throws ScriptException {
		//执行检验逻辑
		if (args.length == 0) {
			System.out
					.println("Usage:\n\tComputeEngineExecutor tinyScriptFile [relativePath] [absolutePath] [resources] [urlParameters]");
			return;
		}
		//执行模拟器
		operator.execute(args);
	}
}
