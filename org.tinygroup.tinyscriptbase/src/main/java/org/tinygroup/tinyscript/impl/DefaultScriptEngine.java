package org.tinygroup.tinyscript.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptFunction;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.*;
import org.tinygroup.tinyscript.function.math.*;
import org.tinygroup.tinyscript.function.random.*;
import org.tinygroup.tinyscript.function.output.*;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;

/**
 * 默认的tiny脚本引擎实现
 * 
 * @author yancheng11334
 * 
 */
public class DefaultScriptEngine extends AbstractScriptEngine {

	//无绑定一般函数
	private Map<String, ScriptFunction> functionMap = new HashMap<String, ScriptFunction>();
	//绑定一般函数
	private Map<Class<?>, Map<String, ScriptFunction>> typeFunctionMap = new HashMap<Class<?>, Map<String, ScriptFunction>>();
	//无绑定动态名称函数
	private List<DynamicNameScriptFunction> dynamicList = new ArrayList<DynamicNameScriptFunction>();
	//绑定动态名称函数
	private Map<Class<?>,List<DynamicNameScriptFunction>> typeDynamicMap = new HashMap<Class<?>,List<DynamicNameScriptFunction>>();

	protected static final Logger LOGGER = LoggerFactory
			.getLogger(DefaultScriptEngine.class);
	
	public DefaultScriptEngine() throws ScriptException{
		super();
		initScriptEngine();
	}
	
	public DefaultScriptEngine(Map<?,?> map) throws ScriptException{
		super(map);
		initScriptEngine();
	}
	
	private void initScriptEngine() throws ScriptException{
		//注册函数
		addScriptFunction(new MathAbsFunction());
	    addScriptFunction(new MathAcosFunction());
	    addScriptFunction(new MathAsinFunction());
		addScriptFunction(new MathAtanFunction());
		addScriptFunction(new MathCosFunction());
		addScriptFunction(new MathSinFunction());
		addScriptFunction(new MathTanFunction());
		addScriptFunction(new MathSqrtFunction());
	    addScriptFunction(new MathPowFunction());
				
		addScriptFunction(new MathAggregateFunction());
				
//		addScriptFunction(new ToIntFunction());
//		addScriptFunction(new ToDoubleFunction());
//		addScriptFunction(new ToLongFunction());
//		addScriptFunction(new ToFloatFunction());
//		addScriptFunction(new ToDateFunction());
		addScriptFunction(new TypeConvertFunction());
		addScriptFunction(new EvalScriptFunction());
				
		addScriptFunction(new DoubleRandFunction());
		addScriptFunction(new FloatRandFunction());
		addScriptFunction(new IntRandFunction());
		addScriptFunction(new LongRandFunction());
		addScriptFunction(new ArrayRandFunction());
				
		addScriptFunction(new ConsolePrintFunction());
		addScriptFunction(new ConsolePrintfFunction());
		addScriptFunction(new ConsolePrintlnFunction());
				
		//注册数学常量
		getScriptContext().put("PI", Math.PI);
		getScriptContext().put("E", Math.E);
				
		//注册引擎到上下文
		ScriptContextUtil.setScriptEngine(getScriptContext(), this);
	}
	
	public void addScriptFunction(ScriptFunction function)
			throws ScriptException {
		function.setScriptEngine(this);
		if(function instanceof DynamicNameScriptFunction){
			//动态名称函数处理
			DynamicNameScriptFunction dynamicNameScriptFunction = (DynamicNameScriptFunction) function;
			if (function.getBindingTypes() == null) {
			    if(!dynamicList.contains(dynamicNameScriptFunction)){
			    	dynamicList.add(dynamicNameScriptFunction);
			    }
			}else{
				String[] types = function.getBindingTypes().split(",");
				for (String type : types) {
					try {
						Class<?> clazz = Class.forName(type);
						List<DynamicNameScriptFunction> dynamicNameList = typeDynamicMap.get(clazz);
						if(dynamicNameList==null){
						   dynamicNameList = new ArrayList<DynamicNameScriptFunction>();
						   typeDynamicMap.put(clazz, dynamicNameList);
						}
						if(!dynamicNameList.contains(dynamicNameScriptFunction)){
							dynamicNameList.add(dynamicNameScriptFunction);
						}
					}catch (ClassNotFoundException e) {
						throw new ScriptException(String.format("添加脚本函数时,没有找到%s类型的类.",
								type), e);
					}
				}
			}
		}else{
			//一般函数处理
			String[] names = function.getNames().split(",");
			if (function.getBindingTypes() == null) {
				for (String name : names) {
					functionMap.put(name, function);
				}
			} else {
				String[] types = function.getBindingTypes().split(",");
				for (String type : types) {
					try {
						Class<?> clazz = Class.forName(type);
						Map<String, ScriptFunction> nameMap = typeFunctionMap
								.get(clazz);
						if (nameMap == null) {
							nameMap = new HashMap<String, ScriptFunction>();
							typeFunctionMap.put(clazz, nameMap);
						}
						for (String name : names) {
							nameMap.put(name, function);
						}
					} catch (ClassNotFoundException e) {
						throw new ScriptException(String.format("添加脚本函数时,没有找到%s类型的类.",
								type), e);
					}
				}
			}
		}
		
	}

	public void removeScriptFunction(ScriptFunction function)
			throws ScriptException {
		function.setScriptEngine(null);
		if(function instanceof DynamicNameScriptFunction){
			//动态名称函数处理
			if (function.getBindingTypes() == null) {
				dynamicList.remove(function);
			} else {
				String[] types = function.getBindingTypes().split(",");
				for (String type : types) {
					try {
						Class<?> clazz = Class.forName(type);
						List<DynamicNameScriptFunction> dynamicNameList = typeDynamicMap.get(clazz);
				        if(dynamicNameList!=null){
				           dynamicNameList.remove(function);
				           
				           if(dynamicNameList.isEmpty()){
				        	  typeDynamicMap.remove(clazz);  
				           }
				        }
					}catch (ClassNotFoundException e) {
						throw new ScriptException(String.format("卸载脚本函数时,没有找到%s类型的类.",
								type), e);
					}
				}
			}
		} else {
			//一般函数处理
			String[] names = function.getNames().split(",");
			if (function.getBindingTypes() == null) {
				for (String name : names) {
					functionMap.remove(name);
				}
			} else {
				String[] types = function.getBindingTypes().split(",");
				for (String type : types) {
					try {
						Class<?> clazz = Class.forName(type);
						Map<String, ScriptFunction> nameMap = typeFunctionMap
								.get(clazz);
						if(nameMap!=null){
							for (String name : names) {
								nameMap.remove(name); 
							}
							if(nameMap.isEmpty()){
							   typeFunctionMap.remove(clazz);
							}
						}
						
					} catch (ClassNotFoundException e) {
						throw new ScriptException(String.format("卸载脚本函数时,没有找到%s类型的类.",
								type), e);
					}
				}
			}
		}
		
	}

	public ScriptFunction findScriptFunction(Object object,
			String functionName) throws ScriptException {
		if(object==null){
			//一般函数
			ScriptFunction function = functionMap.get(functionName);
			
			if(function==null){
			   //动态名称函数
			   for(DynamicNameScriptFunction dynamicNameScriptFunction:dynamicList){
				   if(dynamicNameScriptFunction.exsitFunctionName(functionName)){
					  return dynamicNameScriptFunction;
				   }
			   }
			}
			return function;
		}else{
			//一般函数
			ScriptFunction function = findScriptFunctionByClass(object.getClass(),functionName);
			if(function!=null){
			   return function;
			}
			for(Class<?> clazz:typeFunctionMap.keySet()){
			   if(clazz.isInstance(object)){
				  function = findScriptFunctionByClass(clazz,functionName);
				  if(function!=null){
					 return function;
				  }
			   }
			}
			//动态名称函数
			function = findDynamicNameScriptFunction(object.getClass(),functionName);
			if(function!=null){
			   return function;
			}
			for(Class<?> clazz:typeDynamicMap.keySet()){
			   if(clazz.isInstance(object)){
				   function = findDynamicNameScriptFunction(clazz,functionName);
				   if(function!=null){
					  return function;
				   }
			   }
			}
		}
		return null;
	}
	
	private ScriptFunction findDynamicNameScriptFunction(Class<?> clazz,String functionName){
		List<DynamicNameScriptFunction> dynamicNameList = typeDynamicMap.get(clazz);
		if(dynamicNameList!=null){
		   for(DynamicNameScriptFunction dynamicNameScriptFunction:dynamicNameList){
			  if(dynamicNameScriptFunction.exsitFunctionName(functionName)){
				 return dynamicNameScriptFunction;
			  }
		   }
		}
		return null;
	}
	
	private ScriptFunction findScriptFunctionByClass(Class<?> clazz,String functionName){
		Map<String, ScriptFunction> nameMap = typeFunctionMap.get(clazz);
		return nameMap!=null?nameMap.get(functionName):null;
	}

	protected ScriptSegment findScriptSegmentWithoutCache(Object queryRule)
			throws ScriptException {
		ScriptSegment segment = null;
		//执行创建段逻辑
		String sourceName= "S"+System.nanoTime();
		try{
			segment = getScriptInterpret().createScriptSegment(this, sourceName,(String)queryRule);
		}catch(Exception e){
			throw new ScriptException(String.format("根据查询内容[%s]解析脚本段失败,异常信息:%s",
					queryRule,e.getMessage()));
		}
		return segment;		
	}

	public void start() throws ScriptException {
		LOGGER.logMessage(LogLevel.INFO, "tiny脚本引擎启动完成!");
	}
	
	public void stop() throws ScriptException {
		super.stop();
		functionMap.clear();
		typeFunctionMap.clear();
		dynamicList.clear();
		typeDynamicMap.clear();
		LOGGER.logMessage(LogLevel.INFO, "tiny脚本引擎关闭完成!");
	}

}
