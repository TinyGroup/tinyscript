package org.tinygroup.tinyscript.function;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.naming.NamingString;
import org.tinygroup.tinyscript.naming.NamingStringUtil;

/**
 * 转换对象bean的抽象实现类
 * @author yancheng11334
 *
 */
public abstract class AbstractToBeanFunction extends AbstractScriptFunction {

	public String getNames() {
		return "toBean";
	}

	/**
	 * 将map按指定类名转换值对象
	 * @param map
	 * @param className
	 * @return
	 * @throws Exception
	 */
	protected Object toBean(Map<String,Object> map,String className) throws Exception{
		try{
			Class<?> clazz = Class.forName(className);
			return toBean(map,clazz);
		}catch(ClassNotFoundException e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.tobean.noclass", className));
		}
	}
    /**
     * 将map按指定类转换值对象
     * @param map
     * @param clazz
     * @return
     * @throws Exception
     */
	protected Object toBean(Map<String,Object> map,Class<?> clazz) throws Exception{
		Object  bean = null;
		try{
			bean = clazz.newInstance();
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.tobean.instance",clazz.getName()));
		}
		Map<NamingString,String> linkMap = new HashMap<NamingString,String>();
		for(String key:map.keySet()){
			NamingString ns = NamingStringUtil.parse(key);
			linkMap.put(ns, key);
		}
		for(java.lang.reflect.Field classField:clazz.getDeclaredFields()){
			NamingString ns = NamingStringUtil.parse(classField.getName());
		    if(linkMap.containsKey(ns)){
		      String key = linkMap.get(ns);
		      try{
				 BeanUtils.setProperty(bean, classField.getName(),map.get(key));
			  }catch(Exception e){
				 throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.tobean.setvalue",clazz.getName(),classField.getName(),map.get(key)),e);
			  }
		    }
		    //忽略未匹配的字段
		}
		return bean;
	}
}
