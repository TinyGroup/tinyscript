package org.tinygroup.tinyscript.dataset.function;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.impl.DataSetBean;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.LambdaFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.naming.NamingString;
import org.tinygroup.tinyscript.naming.NamingStringUtil;

public class DataSetToBeanListFunction extends AbstractScriptFunction {

	public String getNames() {
		return "toList";
	}

	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	@SuppressWarnings({ "unchecked" })
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				//用户没有指定类，直接返回map的序列
				AbstractDataSet dataSet = (AbstractDataSet) parameters[0];
				List<DataSetBean> list = new ArrayList<DataSetBean>();
				for (int i = 0; i < dataSet.getRows(); i++) {
					list.add(new DataSetBean(dataSet, dataSet.getShowIndex(i)));
				}
				return list;
			} else if (checkParameters(parameters, 2)) {
				AbstractDataSet dataSet = (AbstractDataSet) parameters[0];
				if(parameters[1] instanceof String){
					//用户指定类名，返回类实例的序列
					try{
						Class<?> clazz = Class.forName((String)parameters[1]);
						return toListBean(dataSet,clazz);
					}catch(ClassNotFoundException e){
						throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset","dataset.tolist.noclass", parameters[1]));
					}
				}else if(parameters[1] instanceof Class){
					//用户指定类，返回类实例的序列
					Class<?> clazz = (Class<?>) parameters[1];
					return toListBean(dataSet,clazz);
				}else if(parameters[1] instanceof LambdaFunction){
					//用户自定义对象创建，返回对象的序列
					LambdaFunction function = (LambdaFunction) parameters[1];
					return toListObject(dataSet,function,context);
				}
			} else if (checkParameters(parameters, 3)) {
				AbstractDataSet dataSet = (AbstractDataSet) parameters[0];
				Map<String,String> relation = (Map<String,String>) parameters[2];
				if(parameters[1] instanceof String){
					//用户指定类名，定义序表字段和值对象字段的映射关系，返回类实例的序列
					try{
						Class<?> clazz = Class.forName((String)parameters[1]);
						return toListBean(dataSet,clazz,relation);
					}catch(ClassNotFoundException e){
						throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset","dataset.tolist.noclass", parameters[1]));
					}
				}else if(parameters[1] instanceof Class){
					//用户指定类，定义序表字段和值对象字段的映射关系，返回类实例的序列
					Class<?> clazz = (Class<?>) parameters[1];
					return toListBean(dataSet,clazz,relation);
				}
			}
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));

		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}
	
	/**
	 * 通过DataSet和Class计算字段关联
	 * @param dataSet
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	private List<Object> toListBean(AbstractDataSet dataSet,Class<?> clazz) throws Exception{
		Map<NamingString,String> linkMap = new HashMap<NamingString,String>();
		
		for(org.tinygroup.tinyscript.dataset.Field field:dataSet.getFields()){
			NamingString ns = NamingStringUtil.parse(field.getName());
			linkMap.put(ns, ns.toString());
		}
		
		//字段名和序表列的映射关系
		Map<String,Integer> map = new HashMap<String,Integer>();
		for(java.lang.reflect.Field classField:clazz.getDeclaredFields()){
			NamingString ns = NamingStringUtil.parse(classField.getName());
			if(linkMap.containsKey(ns)){
			   String colName = linkMap.get(ns);
			   int index = DataSetUtil.getFieldIndex(dataSet, colName);
			   if(index<0){
				   throw new ScriptException(
							ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.notfound", colName)); 
			   }
			   map.put(classField.getName(), dataSet.getShowIndex(index));
			}
			//没有匹配的字段自动忽略
		}
		return toListObject(dataSet,clazz,map);
	}
	
	/**
	 * 用户指定DataSet和Class的字段关联
	 * @param dataSet
	 * @param clazz
	 * @param relation
	 * @return
	 * @throws Exception
	 */
	private List<Object> toListBean(AbstractDataSet dataSet,Class<?> clazz,Map<String,String> relation) throws Exception{
		//字段名和序表列的映射关系
		Map<String,Integer> map = new HashMap<String,Integer>();
		
		for(Entry<String, String> entry:relation.entrySet()){
			String colName = entry.getKey();
			int index = DataSetUtil.getFieldIndex(dataSet, colName);
			if(index<0){
				throw new ScriptException(
							ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.notfound", colName)); 
			}
			map.put(entry.getValue(), dataSet.getShowIndex(index));
		}

		return toListObject(dataSet,clazz,map);
	}
	
	private List<Object> toListObject(AbstractDataSet dataSet,Class<?> clazz,Map<String,Integer> map) throws Exception{
		List<Object> list = new ArrayList<Object>();
		//调用BeanUtils设置属性值
		for (int i = 0; i < dataSet.getRows(); i++) {
			Object  bean = null;
			try{
				bean = clazz.newInstance();
			}catch(Exception e){
				throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset","dataset.tolist.instance",clazz.getName()));
			}
			int showRow = dataSet.getShowIndex(i);
			for(Entry<String, Integer> entry:map.entrySet()){
				Object value = dataSet.getData(showRow, entry.getValue());
				try{
					BeanUtils.setProperty(bean, entry.getKey(),value);
				}catch(Exception e){
					throw new ScriptException(ResourceBundleUtil.getResourceMessage("dataset","dataset.tolist.setvalue",clazz.getName(),entry.getKey(),value),e);
				}
			}
			list.add(bean);
		}
		return list;
	}
	
	private List<Object> toListObject(AbstractDataSet dataSet,LambdaFunction function,ScriptContext context) throws Exception{
		List<Object> list = new ArrayList<Object>();
		ScriptContext subContext = new DefaultScriptContext();
		subContext.setParent(context);
		for (int i = 0; i < dataSet.getRows(); i++) {
			//构建lambda上下文环境
			DataSetUtil.setRowContext(dataSet, subContext, i);
			ScriptResult result = function.execute(subContext);
			list.add(result.getResult());
		}
		return list;
	}

}