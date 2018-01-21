package org.tinygroup.tinyscript.fileresolver;

import java.io.InputStream;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.config.util.ConfigurationUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptEngineFactory;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptUtil;
import org.tinygroup.vfs.FileObject;

/**
 * 扫描指定模块的tiny脚本信息
 * @author yancheng11334
 *
 */
public class ScriptSegmentModuleProcessor extends AbstractFileProcessor {

	private ScriptEngine scriptEngine;
	
	private String[] moduleNames;
	
	private static final String[] DEFALUT_MODULE_NAMES = {"service","dao"}; //默认的模块名

	public ScriptEngine getScriptEngine() {
		if(scriptEngine==null){
		   try{
			   scriptEngine = ScriptEngineFactory.createByBean();
		   }catch(Exception e){
			   throw new RuntimeException(e);
		   }
		}
		return scriptEngine;
	}

	public void setScriptEngine(ScriptEngine scriptEngine) {
		this.scriptEngine = scriptEngine;
	}

	public String[] getModules(){
		if(moduleNames==null){
		   String scriptModuleName = ConfigurationUtil.getConfigurationManager().getConfiguration("scriptModuleName");
		   if(StringUtil.isEmpty(scriptModuleName)){
			   moduleNames = DEFALUT_MODULE_NAMES;
		   }else{
			   moduleNames = scriptModuleName.split(",");
		   }
		}
		return moduleNames;
	}

	public void process() {
		for (FileObject fileObject : changeList) {
			LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("moduleprocessor.load.start", fileObject.getAbsolutePath()));
			ScriptSegment segment = loadScriptSegment(fileObject);
			if(segment!=null){
				getScriptEngine().addScriptSegment(segment);
			   LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("moduleprocessor.loading", segment.getSegmentId()));
			}
			LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("moduleprocessor.load.end", fileObject.getAbsolutePath()));
		}
		for (FileObject fileObject : deleteList) {
			LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("moduleprocessor.delete.start", fileObject.getAbsolutePath()));
			ScriptSegment segment = loadScriptSegment(fileObject);
			if(segment!=null){
				getScriptEngine().removeScriptSegment(segment);
				LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("moduleprocessor.deleting", segment.getSegmentId()));
			}
			LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("moduleprocessor.delete.end", fileObject.getAbsolutePath()));
		}
	}
	
	private ScriptSegment loadScriptSegment(FileObject fileObject){
		InputStream inputStream = null;
		try{
			inputStream = fileObject.getInputStream();
			String text = FileUtil.readStreamContent(inputStream, getScriptEngine().getEncode());
			return ScriptUtil.getDefault().createScriptSegment(getScriptEngine(), null, text);
		}catch(Exception e){
			LOGGER.errorMessage(ResourceBundleUtil.getDefaultMessage("moduleprocessor.load.error", fileObject.getAbsolutePath()),e);
		} finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (Exception e) {
					LOGGER.errorMessage(ResourceBundleUtil.getDefaultMessage("close.file.error", fileObject.getAbsolutePath()),e);
				}
			}
		}
		return null;
	}

	protected boolean checkMatch(FileObject fileObject) {
		String name = fileObject.getFileName();
		//文件是脚本文件
		if(name.endsWith(".tsf")){
		   String[] modules = getModules();
		   for(String module:modules){
			  //文件遵守脚本模块文件命名规范
			  if(!StringUtil.isEmpty(module) && name.endsWith("."+module+".tsf")){  
				 return true;
			  }
		   }
		}
		return false ;
	}


}
