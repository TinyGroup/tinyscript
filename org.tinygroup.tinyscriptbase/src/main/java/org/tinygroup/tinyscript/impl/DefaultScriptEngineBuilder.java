package org.tinygroup.tinyscript.impl;

import java.util.Collection;
import java.util.Iterator;

import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.tinyscript.ScriptCollectionModel;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptFunction;
import org.tinygroup.tinyscript.assignvalue.AssignValueProcessor;
import org.tinygroup.tinyscript.assignvalue.AssignValueUtil;
import org.tinygroup.tinyscript.collection.CollectionModelUtil;
import org.tinygroup.tinyscript.expression.BooleanConverter;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.expression.IteratorConverter;
import org.tinygroup.tinyscript.expression.NumberCalculator;
import org.tinygroup.tinyscript.expression.Operator;
import org.tinygroup.tinyscript.expression.OperatorWithContext;
import org.tinygroup.tinyscript.interpret.AttributeProcessor;
import org.tinygroup.tinyscript.interpret.AttributeUtil;
import org.tinygroup.tinyscript.interpret.ClassInstanceUtil;
import org.tinygroup.tinyscript.interpret.FunctionCallProcessor;
import org.tinygroup.tinyscript.interpret.FunctionCallUtil;
import org.tinygroup.tinyscript.interpret.InstanceOfProcessor;
import org.tinygroup.tinyscript.interpret.NewInstanceProcessor;
import org.tinygroup.tinyscript.interpret.anonymous.SingleMethodProcessor;
import org.tinygroup.tinyscript.interpret.call.JavaMethodUtil;
import org.tinygroup.tinyscript.interpret.call.MethodParameterRule;
import org.tinygroup.tinyscript.interpret.newinstance.ConstructorParameterRule;
import org.tinygroup.tinyscript.interpret.newinstance.JavaConstructorUtil;
import org.tinygroup.tinyscript.objectitem.ObjectItemProcessor;
import org.tinygroup.tinyscript.objectitem.ObjectItemUtil;

/**
 * 默认的引擎组装器
 * @author yancheng11334
 *
 */
public class DefaultScriptEngineBuilder extends AbstractScriptEngineBuilder{

	protected void registerFunction(ScriptEngine engine) throws ScriptException{
		Collection<ScriptFunction> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(ScriptFunction.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<ScriptFunction> it = c.iterator();
		   while(it.hasNext()){
			   engine.addScriptFunction(it.next());
		   }
		}
	}

	protected void registerAssignValueProcessor() throws ScriptException {
		Collection<AssignValueProcessor> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(AssignValueProcessor.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<AssignValueProcessor> it = c.iterator();
		   while(it.hasNext()){
			   AssignValueUtil.addAssignValueProcessor(it.next());
		   }
		}
	}

	protected void registerCollectionModel() throws ScriptException {
		Collection<ScriptCollectionModel> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(ScriptCollectionModel.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<ScriptCollectionModel> it = c.iterator();
		   while(it.hasNext()){
			   CollectionModelUtil.addCollectionModel(it.next());
		   }
		}
	}

	protected void registerOperator() throws ScriptException {
		Collection<Operator> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(Operator.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<Operator> it = c.iterator();
		   while(it.hasNext()){
			   ExpressionUtil.addOperator(it.next());
		   }
		}
	}

	protected void registerOperatorWithContext() throws ScriptException {
		Collection<OperatorWithContext> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(OperatorWithContext.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<OperatorWithContext> it = c.iterator();
		   while(it.hasNext()){
			   ExpressionUtil.addOperator(it.next());
		   }
		}
	}

	protected void registerBooleanConverter() throws ScriptException {
		Collection<BooleanConverter> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(BooleanConverter.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<BooleanConverter> it = c.iterator();
		   while(it.hasNext()){
			   ExpressionUtil.addBooleanConverter(it.next());
		   }
		}
	}

	protected void registerIteratorConverter() throws ScriptException {
		Collection<IteratorConverter> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(IteratorConverter.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<IteratorConverter> it = c.iterator();
		   while(it.hasNext()){
			   ExpressionUtil.addIteratorConverter(it.next());
		   }
		}
	}

	protected void registerNumberCalculator() throws ScriptException {
		Collection<NumberCalculator> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(NumberCalculator.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<NumberCalculator> it = c.iterator();
		   while(it.hasNext()){
			   ExpressionUtil.addNumberCalculator(it.next());
		   }
		}
	}

	protected void registerAttributeProcessor() throws ScriptException {
		Collection<AttributeProcessor> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(AttributeProcessor.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<AttributeProcessor> it = c.iterator();
		   while(it.hasNext()){
			   AttributeUtil.addAttributeProcessor(it.next(),0);
		   }
		}
	}


	protected void registerFunctionCallProcessor() throws ScriptException {
		Collection<FunctionCallProcessor> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(FunctionCallProcessor.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<FunctionCallProcessor> it = c.iterator();
		   while(it.hasNext()){
			   FunctionCallUtil.addFunctionCallProcessor(it.next());
		   }
		}
	}


	@SuppressWarnings("rawtypes")
	protected void registerSingleMethodProcessor() throws ScriptException {
		Collection<SingleMethodProcessor> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(SingleMethodProcessor.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<SingleMethodProcessor> it = c.iterator();
		   while(it.hasNext()){
			   ClassInstanceUtil.addSingleMethodProxy(it.next());
		   }
		}
	}

	@SuppressWarnings("rawtypes")
	protected void registerNewInstanceProcessor() throws ScriptException {
		Collection<NewInstanceProcessor> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(NewInstanceProcessor.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<NewInstanceProcessor> it = c.iterator();
		   while(it.hasNext()){
			   ClassInstanceUtil.addNewInstanceProcessor(it.next());
		   }
		}
	}

	protected void registerInstanceOfProcessor() throws ScriptException {
		Collection<InstanceOfProcessor> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(InstanceOfProcessor.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<InstanceOfProcessor> it = c.iterator();
		   while(it.hasNext()){
			   ClassInstanceUtil.addInstanceOfProcessor(it.next());
		   }
		}
	}

	protected void registerMethodParameterRule() throws ScriptException {
		Collection<MethodParameterRule> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(MethodParameterRule.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<MethodParameterRule> it = c.iterator();
		   while(it.hasNext()){
			   JavaMethodUtil.addMethodParameterRule(it.next());
		   }
		}
	}

	protected void registerConstructorParameterRule() throws ScriptException {
		Collection<ConstructorParameterRule> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(ConstructorParameterRule.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<ConstructorParameterRule> it = c.iterator();
		   while(it.hasNext()){
			   JavaConstructorUtil.addConstructorParameterRule(it.next());
		   }
		}
	}

	protected void registerObjectItemProcessor() throws ScriptException {
		Collection<ObjectItemProcessor> c = BeanContainerFactory.getBeanContainer(getClass().getClassLoader()).getBeans(ObjectItemProcessor.class);
		if(c!=null && !c.isEmpty()){
		   Iterator<ObjectItemProcessor> it = c.iterator();
		   while(it.hasNext()){
			   ObjectItemUtil.addObjectItemProcessor(it.next());
		   }
		}
	}
}
