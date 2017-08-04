package org.tinygroup.tinyscript.interpret.anonymous;

import java.util.Comparator;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ScriptResult;

public class ComparatorProxy extends AbstractSingleMethodProxy implements Comparator<Object>{

	public ComparatorProxy(LambdaFunction lambdaFunction,
			ScriptContext scriptContext) {
		super(lambdaFunction, scriptContext);
	}

	public int compare(Object o1, Object o2) {
		ScriptResult result;
		try {
			result = lambdaFunction.execute(scriptContext, o1,o2);
			return (Integer) result.getResult();
		} catch (Exception e) {
			throw new RuntimeException("执行Comparator代理类发生异常",e);
		}	
	}

}
