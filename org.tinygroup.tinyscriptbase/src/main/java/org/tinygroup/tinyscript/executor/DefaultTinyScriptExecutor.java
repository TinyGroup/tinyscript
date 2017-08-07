package org.tinygroup.tinyscript.executor;

import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.ScriptException;

public class DefaultTinyScriptExecutor {
    private static TinyScriptOperator operator = new DefaultTinyScriptOperator();
    
    /**
     * 固定入口
     * @param args
     * @throws ScriptException
     */
	public static void main(String[] args) throws ScriptException {
		//执行检验逻辑
		if (args.length == 0) {
			System.out
					.println("Usage:\n\tDefaultTinyScriptExecutor tinyScriptFile [relativePath] [absolutePath] [resources] ");
			return;
		}
		//初始化Spring上下文
		Runner.init("/application.xml", null);
		//执行模拟器
		operator.execute(args);
	}
	
}
