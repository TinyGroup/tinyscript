package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractSortFunction;

@SuppressWarnings("rawtypes")
public class DataSetSortFunction extends AbstractSortFunction{
	
	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	protected Object sort(ScriptSegment segment, ScriptContext context,
			Object obj, String rule) throws ScriptException {
		
		try{
			DynamicDataSet dataSet = (DynamicDataSet) obj;
			Comparator c = getComparator(rule,segment,dataSet);
			if(c==null){
			   throw new ScriptException(String.format("解析排序规则[%s]失败", rule));
			}
			dataSet.sort(c);
			return dataSet;
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("序列按规则[%s]排序发生异常:", rule),e);
		}
	}

	@SuppressWarnings({ "unchecked" })
	protected Comparator createComparator(String rule, ScriptSegment segment,
			Object source) throws Exception {
		List<FieldSortRule> rules = new ArrayList<FieldSortRule>();
		try{
			if(matchFieldRule(rule)){
				String[] ss = rule.trim().split(",");
				for(String s:ss){
					FieldSortRule fieldSortRule = parse(s,(DataSet)source);
					if(fieldSortRule!=null){
					   rules.add(fieldSortRule);
					}
				}
				FieldSortComparator comparator = new FieldSortComparator();
				comparator.setFieldSortRuleList(rules);
				return comparator;
			}
		}catch(ScriptException e){
			throw e;
		}catch(Exception e){
			throw new ScriptException(String.format("序列按规则[%s]排序发生异常:", rule),e);
		}
		return null;
	}
	
	/**
	 * 解析字段信息
	 * @param s
	 * @return
	 * @throws Exception 
	 */
	private FieldSortRule parse(String s,DataSet dataSet) throws Exception{
		FieldSortRule rule=null;
		if(!StringUtil.isEmpty(s)){
		   String [] ss = s.trim().split("\\s+");
		   rule = new FieldSortRule();
		   if(ss.length>=2 && ss[1].equalsIgnoreCase("desc")){
			   rule.setAsc(false);
		   }else{
			   rule.setAsc(true); 
		   }
		   rule.setContextName(DEFAULT_CONTEXT_NAME);
		   int index = DataSetUtil.getFieldIndex(dataSet, ss[0]);
		   if(index<0){
			  throw new ScriptException(String.format("根据字段%s匹配数据集列失败", ss[0]));
		   }
		   rule.setIndex(index);
		   rule.setSegment(createDataSetSegment(rule,dataSet));
		   
		}
		
		return rule;
	}
	
	/**
	 * return array[i];
	 * @param rule
	 * @param dataSet
	 * @return
	 */
	private String createDataSetSegment(FieldSortRule rule,DataSet dataSet){
		StringBuilder sb = new StringBuilder();
		sb.append("return ").append(DEFAULT_CONTEXT_NAME);
		sb.append("[").append(rule.getIndex()).append("]");
		sb.append(";");
		return sb.toString();
	}

	protected Object sortByLambda(Object sortObject, Comparator c)
			throws Exception {
		DynamicDataSet dataSet = (DynamicDataSet) sortObject;
		return dataSet.sort(c);
	}

}
