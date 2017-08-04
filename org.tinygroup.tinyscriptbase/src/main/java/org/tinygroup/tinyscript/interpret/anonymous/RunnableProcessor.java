package org.tinygroup.tinyscript.interpret.anonymous;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public class RunnableProcessor  implements SingleMethodProcessor<Runnable>{

	public Runnable build(LambdaFunction lambdaFunction,
			ScriptContext scriptContext) {
		return new RunnableProxy(lambdaFunction,scriptContext);
	}

	public Class<?> getType() {
		return Runnable.class;
	}


}
