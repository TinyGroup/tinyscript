package org.tinygroup.tinyscript.interpret;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.tinygroup.beancontainer.BeanContainerFactory;
import org.tinygroup.context.Context;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.context.*;
import org.tinygroup.tinyscript.interpret.terminal.*;

public final class ScriptUtil {

	private static ScriptInterpret defaultInterpret = new ScriptInterpret();
	private static Map<String,Class<?>> simpleClass = new HashMap<String,Class<?>>();
	
	static {
		simpleClass.put("boolean", boolean.class);
		simpleClass.put("byte", byte.class);
		simpleClass.put("char", char.class);
		simpleClass.put("short", short.class);
		simpleClass.put("int", int.class);
		simpleClass.put("long", long.class);
		simpleClass.put("float", float.class);
		simpleClass.put("double", double.class);
	}
	static {
		// 添加终端节点处理器
		defaultInterpret.addTerminalNodeProcessor(new BooleanNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new SemiNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new NullNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new IntegerNodeProcessor());
		defaultInterpret
				.addTerminalNodeProcessor(new FloatingPointNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new CharNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new StringNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new ReturnNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new LParenNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new RParenNodeProcessor());
		defaultInterpret
				.addTerminalNodeProcessor(new IdentifierNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new LBraceNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new RBraceNodeProcessor());
		defaultInterpret.addTerminalNodeProcessor(new ThisNodeProcessor());

		// 添加上下文节点处理器
		defaultInterpret
				.addContextProcessor(new PackageDeclarationContextProcessor());
		defaultInterpret
				.addContextProcessor(new ImportDeclarationContextProcessor());
		defaultInterpret
				.addContextProcessor(new ClassDeclarationContextProcessor());
		
		defaultInterpret
				.addContextProcessor(new MethodDeclarationContextProcessor());
		defaultInterpret.addContextProcessor(new IfContextProcessor());
		defaultInterpret.addContextProcessor(new ForContextProcessor());
		defaultInterpret.addContextProcessor(new WhileContextProcessor());
		defaultInterpret
				.addContextProcessor(new VariableDeclaratorContextProcessor());
		defaultInterpret
				.addContextProcessor(new FunctionCallExpressionContextProcessor());
		defaultInterpret
				.addContextProcessor(new FieldAccessExpressionContextProcessor());
		defaultInterpret.addContextProcessor(new CreatorContextProcessor());
		defaultInterpret
				.addContextProcessor(new StatementDeclarationContextProcessor());
		defaultInterpret.addContextProcessor(new ContinueContextProcessor());
		defaultInterpret.addContextProcessor(new BreakContextProcessor());
		defaultInterpret.addContextProcessor(new DoContextProcessor());
		defaultInterpret.addContextProcessor(new SwitchContextProcessor());
		defaultInterpret
				.addContextProcessor(new BlockStatementContextProcessor());
		defaultInterpret.addContextProcessor(new ArrayListExpressionContextProcessor());
		defaultInterpret.addContextProcessor(new MapExpressionContextProcessor());
		defaultInterpret.addContextProcessor(new ArrayItemExpressionContextProcessor());
		
		defaultInterpret.addContextProcessor(new ArrayCreatorContextProcessor());
		defaultInterpret.addContextProcessor(new ArrayInitializerContextProcessor());
		defaultInterpret.addContextProcessor(new ObjectCreatorContextProcessor());
		defaultInterpret.addContextProcessor(new ArrayExpressionContextProcessor());

		// 添加表达式运算相关处理器
		defaultInterpret.addContextProcessor(new MathUnaryPrefixProcessor());
		defaultInterpret.addContextProcessor(new MathSingleRightProcessor());
		defaultInterpret.addContextProcessor(new MathSingleLeftProcessor());
		defaultInterpret.addContextProcessor(new MathBinaryBasicProcessor());
		defaultInterpret.addContextProcessor(new MathBinaryBitwiseProcessor());
		defaultInterpret
				.addContextProcessor(new MathCompareRelationalProcessor());
		defaultInterpret
				.addContextProcessor(new MathConditionalTernaryProcessor());
		defaultInterpret.addContextProcessor(new MathBinaryShiftProcessor());
		defaultInterpret.addContextProcessor(new MathBinaryRightProcessor());
		defaultInterpret.addContextProcessor(new MathLogicalConnectProcessor());
		defaultInterpret.addContextProcessor(new InstanceofContextProcessor());
		defaultInterpret.addContextProcessor(new LambdaExpressionContextProcessor());
		defaultInterpret.addContextProcessor(new SqlScriptExpressionContextProcessor());

	}

	public static ScriptInterpret getDefault() {
		return defaultInterpret;
	}

	/**
	 * 获得标识变量的值
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static Object getVariableValue(Context context, Object key) {
		Object value = getValueFromContext(context, key);
		if (value == null) {
			value = getValueFromBean(key);
		}
		return value;
	}

	/**
	 * 从上下文获取对应标识的值
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static Object getValueFromContext(Context context, Object key) {
		return context.get(key.toString());
	}

	/**
	 * 从Bean容器获得对应的值
	 * 
	 * @param key
	 * @return
	 */
	public static Object getValueFromBean(Object key) {
		try {
			return BeanContainerFactory.getBeanContainer(BeanContainerFactory.class.getClassLoader()).getBean(key.toString());
		} catch (Exception e) {
			return null;
		}
	}
	
	public  static Class<?> findJavaClass(String className) {
		try{
			if(simpleClass.containsKey(className)){
			   return simpleClass.get(className);
			}
			return Class.forName(className);
		}catch (Exception e){
			//忽视异常
			return null;
		}
	}
	
	public  static Class<?> findJavaClass(String className,List<String> importList) {
		String newClassName = null;
		Class<?> c = null;
		if (!CollectionUtils.isEmpty(importList)) {
			for (String importPath : importList) {
				if (importPath.endsWith("*")) {
					newClassName = importPath.substring(0,
							importPath.length() - 1) + className;
				} else if(importPath.endsWith("."+className)){
					newClassName = importPath;
				} else{
					continue;
				}
				c = ScriptUtil.findJavaClass(newClassName);
				if(c!=null){
					return c;
				}
			}
		}
		return null;
	}
	
	public static Class<?> findJavaClass(String className,ScriptSegment classSegment,List<String> importList) {
		Class<?> c = null;
		//先直接查类名
		c = ScriptUtil.findJavaClass(className);
		if(c!=null){
		   return c;	
		}
		
		//再查importList
		c = ScriptUtil.findJavaClass(className,importList);
		if(c!=null){
		   return c;	
		}
		
		//接着查classSegment
		if(classSegment!=null){
			c = ScriptUtil.findJavaClass(className,classSegment.getImportList());
			if(c!=null){
			   return c;	
			}
		}
		
		//最后查询java默认包
		String newName = "java.lang."+className;
		c = ScriptUtil.findJavaClass(newName);
		if(c!=null){
		   return c;
		}
		
		return null;
	}

}
