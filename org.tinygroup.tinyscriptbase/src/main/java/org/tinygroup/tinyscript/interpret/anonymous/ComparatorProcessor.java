package org.tinygroup.tinyscript.interpret.anonymous;

import java.util.Comparator;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;

public class ComparatorProcessor implements SingleMethodProcessor<Comparator>{

	public Class<?> getType() {
		return Comparator.class;
	}

	public Comparator build(LambdaFunction lambdaFunction,
			ScriptContext scriptContext) {
		return new ComparatorProxy(lambdaFunction,scriptContext);
	}

}
