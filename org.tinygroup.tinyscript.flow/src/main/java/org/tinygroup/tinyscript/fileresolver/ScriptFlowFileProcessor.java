package org.tinygroup.tinyscript.fileresolver;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.tinyscript.ScriptFlowManager;
import org.tinygroup.tinyscript.config.ScriptFlowConfigs;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 脚本流程配置扫描器
 * @author yancheng11334
 *
 */
public class ScriptFlowFileProcessor extends AbstractFileProcessor {

	private static final String XSTREAM_NAME = "scriptflow";
	
	private ScriptFlowManager scriptFlowManager;
	
	public ScriptFlowManager getScriptFlowManager() {
		return scriptFlowManager;
	}

	public void setScriptFlowManager(ScriptFlowManager scriptFlowManager) {
		this.scriptFlowManager = scriptFlowManager;
	}

	public void process() {
		XStream stream = XStreamFactory.getXStream(XSTREAM_NAME);
		for (FileObject fileObject : deleteList) {
			LOGGER.logMessage(LogLevel.INFO, "删除脚本流程文件[{0}]开始...",
                    fileObject.getAbsolutePath());
			ScriptFlowConfigs configs = (ScriptFlowConfigs) caches.get(fileObject
                    .getAbsolutePath());
			if(configs!=null){
			   scriptFlowManager.removeScriptFlowConfigs(configs);
			   caches.remove(fileObject.getAbsolutePath());
			}
			LOGGER.logMessage(LogLevel.INFO, "删除脚本流程文件[{0}]结束!",
                    fileObject.getAbsolutePath());
		}
		
		for (FileObject fileObject : changeList) {
			LOGGER.logMessage(LogLevel.INFO, "加载脚本流程文件[{0}]开始...",
                    fileObject.getAbsolutePath());
			ScriptFlowConfigs oldConfigs = (ScriptFlowConfigs) caches.get(fileObject
                    .getAbsolutePath());
			if(oldConfigs!=null){
			   scriptFlowManager.removeScriptFlowConfigs(oldConfigs);
			}
			try{
				ScriptFlowConfigs configs = convertFromXml(stream, fileObject);
				scriptFlowManager.addScriptFlowConfigs(configs);
				caches.put(fileObject.getAbsolutePath(), configs);
			}catch(Exception e){
				LOGGER.errorMessage("加载脚本流程文件[{0}]发生异常:", e, fileObject.getAbsolutePath());
			}finally{
				LOGGER.logMessage(LogLevel.INFO, "加载脚本流程文件[{0}]结束!",
	                    fileObject.getAbsolutePath());
			}
			
		}
	}

	protected boolean checkMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(".tsflow") || fileObject.getFileName().endsWith(".tsflow.xml");
	}

}
