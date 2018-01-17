package org.tinygroup.tinyscript.fileresolver;

import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.tinyscript.ScriptFlowManager;
import org.tinygroup.tinyscript.config.ScriptComponentConfigs;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.xstream.XStreamFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 脚本组件配置扫描器
 * @author yancheng11334
 *
 */
public class ScriptComponentFileProcessor extends AbstractFileProcessor {

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
			LOGGER.logMessage(LogLevel.INFO, "删除脚本组件文件[{0}]开始...",
                    fileObject.getAbsolutePath());
			ScriptComponentConfigs configs = (ScriptComponentConfigs) caches.get(fileObject
                    .getAbsolutePath());
			if(configs!=null){
			   scriptFlowManager.removeScriptComponentConfigs(configs);
			   caches.remove(fileObject.getAbsolutePath());
			}
			LOGGER.logMessage(LogLevel.INFO, "删除脚本组件文件[{0}]结束!",
                    fileObject.getAbsolutePath());
		}
		
		for (FileObject fileObject : changeList) {
			LOGGER.logMessage(LogLevel.INFO, "加载脚本组件文件[{0}]开始...",
                    fileObject.getAbsolutePath());
			ScriptComponentConfigs oldConfigs = (ScriptComponentConfigs) caches.get(fileObject
                    .getAbsolutePath());
			if(oldConfigs!=null){
			   scriptFlowManager.removeScriptComponentConfigs(oldConfigs);
			}
			try{
				ScriptComponentConfigs configs = convertFromXml(stream, fileObject);
				scriptFlowManager.addScriptComponentConfigs(configs);
				caches.put(fileObject.getAbsolutePath(), configs);
			}catch(Exception e){
				LOGGER.errorMessage("加载脚本组件文件[{0}]发生异常:", e, fileObject.getAbsolutePath());
			}finally{
				LOGGER.logMessage(LogLevel.INFO, "加载脚本组件文件[{0}]结束!",
	                    fileObject.getAbsolutePath());
			}
			
		}
	}

	protected boolean checkMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(".tsc") || fileObject.getFileName().endsWith(".tsc.xml");
	}

}
