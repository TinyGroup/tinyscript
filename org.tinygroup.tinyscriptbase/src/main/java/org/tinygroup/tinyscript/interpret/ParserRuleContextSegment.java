package org.tinygroup.tinyscript.interpret;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.v4.runtime.tree.ParseTree;
import org.springframework.util.CollectionUtils;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.*;
import org.tinygroup.tinyscript.ScriptClass;
import org.tinygroup.tinyscript.ScriptClassConstructor;
import org.tinygroup.tinyscript.ScriptClassInstance;
import org.tinygroup.tinyscript.ScriptClassField;
import org.tinygroup.tinyscript.ScriptClassMethod;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.AbstractScriptEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.exception.ReturnException;

/**
 * 通用解析器脚本片段
 * @author yancheng11334
 *
 */
public class ParserRuleContextSegment implements ScriptSegment{

	private String segmentId;
	private String script;
	private CompilationUnitContext parserRuleContext;
	private ScriptEngine scriptEngine;
	private String packageName;
	private InnerScriptClass scriptClass;
	private List<String> importList;
	
	public ParserRuleContextSegment(ScriptEngine engine,String sourceName,String scriptText,TinyScriptParser.CompilationUnitContext compilationUnitContext) throws ScriptException {
		this.scriptEngine = engine;
		this.segmentId = sourceName;
		this.script = scriptText;
		this.parserRuleContext = compilationUnitContext;
		initContext();
	}
	
	public String getSegmentId() {
		return segmentId==null?getName():segmentId;
	}

	public String getScript(){
		return script;
	}
	
	public String getName(){
		if(StringUtil.isEmpty(packageName)){
		   return scriptClass==null?null:scriptClass.getClassName();
		}else{
		   return scriptClass==null?packageName:packageName+"."+scriptClass.getClassName();
		}
	}
	
	private void initContext() throws ScriptException {
		ScriptContext context = new DefaultScriptContext();
		//初始化包路径
		if(parserRuleContext.packageDeclaration()!=null){
		   packageName = (String) execute(parserRuleContext.packageDeclaration(),context);
		}
		//初始化引入路径
		if(!CollectionUtils.isEmpty(parserRuleContext.importDeclaration())){
			importList = new ArrayList<String>();
			for(ImportDeclarationContext importDeclarationContext:parserRuleContext.importDeclaration()){
				importList.add((String) execute(importDeclarationContext,context));
			}
		}
		//初始化类信息
		if(parserRuleContext.classDeclaration()!=null){
		   scriptClass = new InnerScriptClass(parserRuleContext.classDeclaration());
		}
		
	}

	public Object execute(ScriptContext context) throws ScriptException {
		return execute(parserRuleContext,context);
	}
	
	private Object execute(ParseTree tree,ScriptContext context) throws ScriptException {
		if(!(scriptEngine instanceof AbstractScriptEngine)){
			throw new ScriptException(String.format("类型[%s]的tiny脚本引擎并不是AbstractScriptEngine的子类，不支持获取脚本解释器!", scriptEngine.getClass().getName()));
		}
		AbstractScriptEngine abstractScriptEngine = (AbstractScriptEngine) scriptEngine;
		try{
			ScriptResult result = abstractScriptEngine.getScriptInterpret().interpretParseTree(tree, this, context);
			if(result.isVoid()){
			   return null;
			}else{
			   if(result.getResult() != null){
				  if(result.getResult() instanceof LambdaFunction){
					  LambdaFunction f = (LambdaFunction) result.getResult();
					  if(f.getFunctionName()==null){//匿名表达式执行
						 return f.execute(context).getResult();
					  }
				  }
			   }
			   return result.getResult();
			}
		}catch(ReturnException e){
			//中断异常不用管异常信息,只需要处理返回值
			return e.getValue();
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("scriptId:[%s],script:[%s]的脚本片段渲染发生异常", segmentId,script),e);
		}
	}

	public void setScriptEngine(ScriptEngine engine) {
		this.scriptEngine = engine;
	}

	public ScriptEngine getScriptEngine() {
		return scriptEngine;
	}

	public String getPackage() {
		return packageName;
	}

	public List<String> getImportList() {
		return importList;
	}

	public ScriptClass getScriptClass() {
		return scriptClass;
	}


	class InnerScriptClass implements ScriptClass{
		private ClassDeclarationContext classDeclarationContext;
        private String className;
        private Map<String,ScriptClassMethod> methodMaps = new HashMap<String,ScriptClassMethod>();
        private Map<String,ScriptClassField> fieldMaps = new HashMap<String,ScriptClassField>();
        private ScriptClassConstructor[] constructors = null;
        		
        public InnerScriptClass(ClassDeclarationContext context) throws ScriptException {
        	this.classDeclarationContext = context;
        	initContext();
        }
        
        private void initContext() throws ScriptException {
        	//获得类名
        	className = (String) ParserRuleContextSegment.this.execute(classDeclarationContext,new DefaultScriptContext());
        	//获得方法
        	List<MemberDeclarationContext> members = classDeclarationContext.classBody().memberDeclaration();
        	if(!CollectionUtils.isEmpty(members)){
        		List<ScriptClassConstructor> constructorList = new ArrayList<ScriptClassConstructor>();
        	    for(MemberDeclarationContext memberDeclarationContext:members){
        	       //区分一般方法和构造方法
        	       if(memberDeclarationContext.methodDeclaration()!=null){
        	    	   if(memberDeclarationContext.methodDeclaration().Identifier().getText().equals(className)){
        	    		   //构造方法
        	    		   ScriptClassConstructor  constructor = new InnerScriptClassConstructor(memberDeclarationContext.methodDeclaration(),this);
        	    		   constructorList.add(constructor);
        	    	   }else{
        	    		   //一般方法
        	    		   ScriptClassMethod method = new InnerScriptMethod(memberDeclarationContext.methodDeclaration());
            	    	   methodMaps.put(method.getMethodName(), method); 
        	    	   }
        	    	   
        	       }
        	       //识别字段
        	       if(memberDeclarationContext.fieldDeclaration()!=null){
        	    	   try{
        	    		    for(VariableDeclaratorContext variableDeclaratorContext:memberDeclarationContext.fieldDeclaration().variableDeclarators().variableDeclarator()){
        	    		    	ScriptClassField field = new InnerScriptField(variableDeclaratorContext);
        	        	    	fieldMaps.put(field.getFieldName(), field);
        	    		    }
        	    	   }catch(Exception e){
        	    		  throw new ScriptException(String.format("解析%s类的类属性变量失败", className),e);
        	    	   }
        	    	  
        	       }
        	    }
        	    //处理构造器数组
        	    constructors = new ScriptClassConstructor[constructorList.size()];
        	    constructorList.toArray(constructors);
        	}
        }
        
		public String getClassName() {
			return className;
		}

		public ScriptClassMethod getScriptMethod(String methodName) {
			return methodMaps.get(methodName);
		}

		public ScriptClassField getScriptField(String fieldName) {
			return fieldMaps.get(fieldName);
		}

		public ScriptClassInstance newInstance(ScriptContext context, Object... parameters) throws ScriptException{
			ScriptClassConstructor[] constructors = getScriptClassConstructors();
			if(constructors!=null){
			   for(ScriptClassConstructor constructor:constructors){
				   if(constructor.isMatch(parameters)){ 
					  return constructor.newInstance(context, parameters); 
				   }
			   }
			}
			return new InnerScriptClassInstance(this);
		}

		public ScriptSegment getScriptSegment() {
			return ParserRuleContextSegment.this;
		}

		public ScriptClassMethod[] getScriptMethods() {
			ScriptClassMethod[] methods = new ScriptClassMethod[methodMaps.size()];
			return methodMaps.values().toArray(methods);
		}

		public ScriptClassField[] getScriptFields() {
			ScriptClassField[] fields = new ScriptClassField[fieldMaps.size()];
			return fieldMaps.values().toArray(fields);
		}

		public ScriptClassConstructor[] getScriptClassConstructors() {
			return constructors;
		}
	}
	
	class InnerScriptClassInstance implements ScriptClassInstance {

		private InnerScriptClass innerScriptClass;
		private Map<String,Object> valueMaps = new HashMap<String,Object>(); //不同实例的类属性值不同，不能共用ScriptClass的类属性，那里只是初始值。
		public InnerScriptClassInstance(InnerScriptClass clazz){
			this.innerScriptClass = clazz;
			for(ScriptClassField field:innerScriptClass.fieldMaps.values()){
				valueMaps.put(field.getFieldName(), field.getValue());
			}
		}
		public ScriptClass getScriptClass() {
			return innerScriptClass;
		}

		public Object getField(String fieldName) {
			return valueMaps.get(fieldName);
		}
		
		public boolean existField(String fieldName) {
			return valueMaps.containsKey(fieldName);
		}

		public void setField(String fieldName, Object value) {
			valueMaps.put(fieldName, value);
		}

		public Object execute(ScriptContext context,String methodName, Object... parameters)
				throws ScriptException {
			ScriptClassMethod method = innerScriptClass.getScriptMethod(methodName);
			if(method==null){
			   throw new ScriptException(String.format("类型%s的对象没有找到函数%s", innerScriptClass.getClassName(),methodName));
			}
			return method.execute(context, parameters);
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(innerScriptClass.getClassName()).append("[");
			ScriptClassField[] fields = innerScriptClass.getScriptFields();
			if(fields!=null){
			   for(int i=0;i<fields.length;i++){
				  ScriptClassField field = fields[i];
				  sb.append(field.getFieldName()).append("=").append(valueMaps.get(field.getFieldName()));
			      if(i<fields.length-1){  
			    	 sb.append(",");
			      }
			   }
			}
			sb.append("]");
			return sb.toString();
		}
		
	}
	
	class InnerScriptClassConstructor implements ScriptClassConstructor{
		private MethodDeclarationContext methodDeclarationContext;
		private String[] parameterNames;
		private InnerScriptClass scriptClass;
		
		public InnerScriptClassConstructor(MethodDeclarationContext methodDeclarationContext,InnerScriptClass scriptClass) throws ScriptException{
			this.methodDeclarationContext = methodDeclarationContext;
			this.scriptClass = scriptClass;
			initContext();
		}
		
		private void initContext() throws ScriptException {
        	if(methodDeclarationContext.formalParameters().formalParameterList()!=null){
        		List<FormalParameterContext> formalParameterContextList = methodDeclarationContext.formalParameters().formalParameterList().formalParameter();
        		parameterNames = new String[formalParameterContextList.size()];
        		for(int i=0;i<formalParameterContextList.size();i++){
        			parameterNames[i] = formalParameterContextList.get(i).getText();
        		}
        	}
        }
		
		public String[] getParamterNames() {
			return parameterNames;
		}

		public boolean isMatch(Object... parameters) {
			int m = parameterNames==null?0:parameterNames.length;
			int n = parameters == null?0:parameters.length;
			return m==n;
		}

		public ScriptClassInstance newInstance(ScriptContext context,
				Object... parameters) throws ScriptException {
			ScriptClassInstance instance = new InnerScriptClassInstance(scriptClass);
			try{
				if(parameterNames!=null){
				   for(int i=0;i<parameterNames.length;i++){
					  context.getItemMap().put(parameterNames[i], parameters[i]);
				   }
				}
				ScriptContextUtil.setScriptClassInstance(context, instance);
				execute(methodDeclarationContext.methodBody(),context);
			}finally{
				if(parameterNames!=null){
				   for(int i=0;i<parameterNames.length;i++){
					  context.getItemMap().remove(parameterNames[i]);  
				   }
				}
				ScriptContextUtil.setScriptClassInstance(context, null);
			}
			return instance;
		}
		
	}

	class InnerScriptMethod implements ScriptClassMethod{
		private MethodDeclarationContext methodDeclarationContext;
        private String methodName;
        private String[] parameterNames;
        
        public InnerScriptMethod(MethodDeclarationContext context) throws ScriptException {
        	this.methodDeclarationContext = context;
        	initContext();
        }
        
        private void initContext() throws ScriptException {
        	//获得方法名
        	methodName = methodDeclarationContext.Identifier().getText();
        	if(methodDeclarationContext.formalParameters().formalParameterList()!=null){
        		List<FormalParameterContext> formalParameterContextList = methodDeclarationContext.formalParameters().formalParameterList().formalParameter();
        		parameterNames = new String[formalParameterContextList.size()];
        		for(int i=0;i<formalParameterContextList.size();i++){
        			parameterNames[i] = formalParameterContextList.get(i).getText();
        		}
        	}
        }
        
		public String getMethodName() {
			return methodName;
		}
		

		public String[] getParamterNames() {
			return parameterNames;
		}

		public Object execute(ScriptContext context, Object... parameters)
				throws ScriptException {
			try{
				if((parameterNames==null || parameterNames.length==0) && (parameters==null || parameters.length==0)){
					
				}else if(parameterNames!=null && parameters!=null && parameterNames.length==parameters.length){
					for(int i=0;i<parameterNames.length;i++){
						context.put(parameterNames[i], parameters[i]);
					}
				}else{
					throw new ScriptException("执行脚本函数发生异常:参数列表与定义不一致");
				}
				
				return ParserRuleContextSegment.this.execute(methodDeclarationContext,context);
			}finally{
				if(parameterNames!=null){
				   for(int i=0;i<parameterNames.length;i++){
					   context.remove(parameterNames[i]);
				   }
				}
			}
		}

		
	}
	
	class InnerScriptField implements ScriptClassField{
        private String fieldName;
        private Object value;
        private VariableDeclaratorContext variableDeclaratorContext;
        
        public InnerScriptField(VariableDeclaratorContext context) throws ScriptException {
        	this.variableDeclaratorContext = context;
        	initContext();
        }
        
        private void initContext() throws ScriptException {
        	fieldName =  variableDeclaratorContext.getChild(0).getText();
        	value =  ParserRuleContextSegment.this.execute(variableDeclaratorContext,new DefaultScriptContext());
        }
        
		public String getFieldName() {
			return fieldName;
		}

		public Object getValue() {
			return value;
		}
		
	}
}