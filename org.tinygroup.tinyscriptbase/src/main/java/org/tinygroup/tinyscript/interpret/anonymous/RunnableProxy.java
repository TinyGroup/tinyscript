package org.tinygroup.tinyscript.interpret.anonymous;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 代理Runnable接口
 * @author yancheng11334
 *
 */
public class RunnableProxy extends AbstractSingleMethodProxy implements Runnable{

	public RunnableProxy(LambdaFunction lambdaFunction,
			ScriptContext scriptContext) {
		super(lambdaFunction, scriptContext);
	}

	public void run() {
		try {
		    lambdaFunction.execute(scriptContext);
		} catch (Exception e) {
			throw new RuntimeException(ResourceBundleUtil.getDefaultMessage("proxy.run.error", Runnable.class.getName()),e);
		}
	}

}
