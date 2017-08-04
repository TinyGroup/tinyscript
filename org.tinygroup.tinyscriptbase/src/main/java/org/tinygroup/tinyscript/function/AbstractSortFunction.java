package org.tinygroup.tinyscript.function;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;

/**
 * 排序入口函数
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public abstract class AbstractSortFunction extends AbstractScriptFunction {

	private static final Pattern ORDER_SIMPLE = Pattern.compile("\\s*(asc|desc)\\s*", Pattern.CASE_INSENSITIVE);
	
    private static final String  SPACE = "\\s*";
	private static final String  FIELD = "[_a-zA-Z\u4E00-\u9FA5]*";
	private static final String  ORDER = "(asc|desc)?";
	private static final Pattern ORDER_FIELD = Pattern.compile(SPACE+FIELD+SPACE+ORDER+"("+SPACE+","+FIELD+SPACE+ORDER+")*", Pattern.CASE_INSENSITIVE);
		
	private Comparator<Object> asc = new SimpleTypeComparator<Object>(true);
	private Comparator<Object> desc = new SimpleTypeComparator<Object>(false);
	protected static final String  DEFAULT_CONTEXT_NAME = "_tiny_list_ele";
	
	public String getNames() {
		return "sort";
	}
	
	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		if(parameters==null || parameters.length==0){
			throw new ScriptException("sort函数的参数为空!"); 
		}else if(parameters.length==2 && parameters[0]!=null && parameters[1]!=null){
			return sort(segment,context,parameters[0],(String)parameters[1]);
		}else{
			throw new ScriptException("sort函数的参数格式不正确!"); 
		}
	}
	
	protected boolean matchSimpleRule(String rule){
		return ORDER_SIMPLE.matcher(rule).matches();
	}
	
	protected boolean matchFieldRule(String rule){
		return ORDER_FIELD.matcher(rule).matches();
	}
	
	/**
	 * 排序
	 * @param segment
	 * @param context
	 * @param obj
	 * @param rule
	 * @return
	 * @throws ScriptException
	 */
	protected abstract Object sort(ScriptSegment segment, ScriptContext context,Object obj,String rule) throws ScriptException;
	
	/**
	 * 获取比较器
	 * @param rule
	 * @param segment
	 * @param source
	 * @return
	 * @throws Exception
	 */
	public Comparator getComparator(String rule,ScriptSegment segment,Object source) throws Exception{
		//匹配简单规则
		if(matchSimpleRule(rule)){
			String runRule = rule.toLowerCase();
			if(runRule.indexOf("asc")>-1){
			   return asc;
			}else{
			   return desc;
			}
		}
		//进行字段匹配
		return createComparator(rule,segment,source);
	}
	
	/**
	 * 创建比较器
	 * @param rule
	 * @param segment
	 * @param source
	 * @return
	 * @throws Exception
	 */
	protected abstract Comparator createComparator(String rule,ScriptSegment segment,Object source) throws Exception;
	
	class SimpleTypeComparator<Object> implements Comparator<Object>{

		private boolean asc;
		
		public SimpleTypeComparator(boolean ascTag){
			this.asc = ascTag;
		}
		
		public int compare(Object o2, Object o1) {
			if(asc){
				return compareObject(o2,o1);  //升序
			}else{
				return compareObject(o1,o2);  //降序
			}
		}
		
		protected int compareObject(Object o1, Object o2){
			if(o1 instanceof Comparable && o2 instanceof Comparable){
				Comparable c1 = (Comparable) o1;
				Comparable c2 = (Comparable) o2;
				return c1.compareTo(c2);
			}else{
				String s1 = o1.toString();
				String s2 = o2.toString();
				return s1.compareTo(s2);	
			}
		}

	}

	public class FieldSortRule {
		private boolean asc;
		private String contextName;
		private String segment;
		private int index;
		public boolean isAsc() {
			return asc;
		}
		public void setAsc(boolean asc) {
			this.asc = asc;
		}
		public String getContextName() {
			return contextName;
		}
		public void setContextName(String contextName) {
			this.contextName = contextName;
		}
		public String getSegment() {
			return segment;
		}
		public void setSegment(String segment) {
			this.segment = segment;
		}
		public int getIndex() {
			return index;
		}
		public void setIndex(int index) {
			this.index = index;
		}
	}
	
    public class FieldSortComparator <Object> implements Comparator<Object>{

		private List<FieldSortRule> fieldSortRuleList;
		private ScriptContext context = new DefaultScriptContext();
		
		public List<FieldSortRule> getFieldSortRuleList() {
			return fieldSortRuleList;
		}

		public void setFieldSortRuleList(List<FieldSortRule> fieldSortRuleList) {
			this.fieldSortRuleList = fieldSortRuleList;
		}

		public int compare(Object o1, Object o2) {
			int compareValue = 0;
		    try{
		    	for(FieldSortRule rule:fieldSortRuleList){
					Object p1 = eval(rule,o1);
					Object p2 = eval(rule,o2);
					if(rule.isAsc()){
					   //升序
					   compareValue = compareObject(p1,p2);
					}else{
					   //降序
					   compareValue = compareObject(p2,p1);
					}
					//某个字段比较出结果,直接返回;否则继续
					if(compareValue!=0){
					   return compareValue;	
					}
				}
		    }catch(RuntimeException e){
		    	throw e;
		    }catch(Exception e){
		    	throw new RuntimeException("排序发生异常:",e);
		    }
			
			return compareValue;
		}
		
		private Object eval(FieldSortRule rule,Object obj){
			context.put(rule.getContextName(), obj);
			try {
				return (Object)AbstractSortFunction.this.getScriptEngine().execute(rule.getSegment(), context);
			} catch (ScriptException e) {
				throw new RuntimeException(String.format("排序执行脚本[%s]发生异常", rule.getSegment()),e);
			}
		}
		
		protected int compareObject(Object p1, Object p2){
			if(p1 instanceof Comparable && p2 instanceof Comparable){
				Comparable c1 = (Comparable) p1;
				Comparable c2 = (Comparable) p2;
				return c1.compareTo(c2);
			}else{
				String s1 = p1.toString();
				String s2 = p2.toString();
				return s1.compareTo(s2);	
			}
		}
	}
	

}
