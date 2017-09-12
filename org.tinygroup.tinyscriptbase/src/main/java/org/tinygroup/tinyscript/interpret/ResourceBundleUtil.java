package org.tinygroup.tinyscript.interpret;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 信息资源工具类
 * @author yancheng11334
 *
 */
public final class ResourceBundleUtil {
    
	private static final String DEFAULT_RESOURCE_NAME = "base";
	
	private ResourceBundleUtil(){
		
	}
	
	/**
	 * 获取ResourceBundle
	 * @param resourceName
	 * @return
	 */
	public static ResourceBundle getBundle(String resourceName){
		try{
			return ResourceBundle.getBundle(resourceName);
		}catch(Exception e){
			//这里打印栈轨迹,方便定位资源工具异常信息
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 获取ResourceBundle
	 * @param resourceName
	 * @param locale
	 * @return
	 */
	public static ResourceBundle getBundle(String resourceName,Locale locale){
		try{
			return ResourceBundle.getBundle(resourceName,locale);
		}catch(Exception e){
			//这里打印栈轨迹,方便定位资源工具异常信息
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 获取ResourceBundle
	 * @param resourceName
	 * @param locale
	 * @param loader
	 * @return
	 */
	public static ResourceBundle getBundle(String resourceName,Locale locale,ClassLoader loader){
        try{
        	return ResourceBundle.getBundle(resourceName,locale,loader);
		}catch(Exception e){
			//这里打印栈轨迹,方便定位资源工具异常信息
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 获取信息
	 * @param bundle
	 * @param key
	 * @return
	 */
	public static String getMessage(ResourceBundle bundle,String key){
		try{
			if(bundle!=null && key!=null){
				return bundle.getString(key);
			}
		}catch(Exception e){
			//这里打印栈轨迹,方便定位资源工具异常信息
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	/**
	 * 获取信息
	 * @param bundle
	 * @param key
	 * @param parameters
	 * @return
	 */
	public static String getMessage(ResourceBundle bundle,String key,Object... parameters){
		try{
			if(bundle!=null && key!=null){
			   String result =  bundle.getString(key);
			   if(result!=null){
				  return String.format(result, parameters);
			   }
			}
		}catch(Exception e){
			//这里打印栈轨迹,方便定位资源工具异常信息
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	/**
	 * 获取默认资源信息
	 * @param key
	 * @return
	 */
	public static String getMessage(String key){
		return getMessage(DEFAULT_RESOURCE_NAME,key);
	}
	
	/**
	 * 获取指定资源信息
	 * @param resourceName
	 * @param key
	 * @return
	 */
	public static String getMessage(String resourceName,String key){
		ResourceBundle bundle = getBundle(resourceName);
		return getMessage(bundle,key);
	}
	
	/**
	 * 获取默认资源的动态信息
	 * @param key
	 * @param parameters
	 * @return
	 */
	public static String getMessage(String key,Object... parameters){
		return getMessage(DEFAULT_RESOURCE_NAME,key,parameters);
	}
	
	/**
	 * 获取指定资源的动态信息
	 * @param resourceName
	 * @param key
	 * @param parameters
	 * @return
	 */
	public static String getMessage(String resourceName,String key,Object... parameters){
		ResourceBundle bundle = getBundle(resourceName);
		return getMessage(bundle,key,parameters);
	}
}
