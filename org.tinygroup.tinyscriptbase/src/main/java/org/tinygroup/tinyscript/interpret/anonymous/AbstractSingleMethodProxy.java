package org.tinygroup.tinyscript.interpret.anonymous;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public class AbstractSingleMethodProxy {

	protected LambdaFunction lambdaFunction;
	protected ScriptContext scriptContext;
	
	
	public AbstractSingleMethodProxy(LambdaFunction lambdaFunction,
			ScriptContext scriptContext) {
		super();
		this.lambdaFunction = lambdaFunction;
		this.scriptContext = scriptContext;
	}
	
	
}
