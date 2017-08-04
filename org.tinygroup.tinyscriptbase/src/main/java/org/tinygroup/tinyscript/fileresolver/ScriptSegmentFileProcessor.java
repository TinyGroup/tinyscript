package org.tinygroup.tinyscript.fileresolver;

import java.io.InputStream;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ScriptUtil;
import org.tinygroup.vfs.FileObject;

/**
 * tiny脚本片段扫描器
 * @author yancheng11334
 *
 */
public class ScriptSegmentFileProcessor extends AbstractFileProcessor {

	private ScriptEngine scriptEngine;

	public ScriptEngine getScriptEngine() {
		return scriptEngine;
	}

	public void setScriptEngine(ScriptEngine scriptEngine) {
		this.scriptEngine = scriptEngine;
	}

	public void process() {
		for (FileObject fileObject : changeList) {
			LOGGER.logMessage(LogLevel.INFO, "tiny脚本文件[{0}]开始加载",fileObject.getAbsolutePath());
			ScriptSegment segment = loadScriptSegment(fileObject);
			if(segment!=null){
			   scriptEngine.addScriptSegment(segment);
			   LOGGER.logMessage(LogLevel.INFO, "加载tiny脚本类[{0}]",segment.getSegmentId());
			}
			LOGGER.logMessage(LogLevel.INFO, "tiny脚本文件[{0}]加载完毕",fileObject.getAbsolutePath());
		}
		for (FileObject fileObject : deleteList) {
			LOGGER.logMessage(LogLevel.INFO, "tiny脚本文件[{0}]开始删除",fileObject.getAbsolutePath());
			ScriptSegment segment = loadScriptSegment(fileObject);
			if(segment!=null){
				scriptEngine.removeScriptSegment(segment);
			   LOGGER.logMessage(LogLevel.INFO, "删除tiny脚本类[{0}]",segment.getSegmentId());
			}
			LOGGER.logMessage(LogLevel.INFO, "tiny脚本文件[{0}]删除完毕",fileObject.getAbsolutePath());
		}
	}
	
	private ScriptSegment loadScriptSegment(FileObject fileObject){
		InputStream inputStream = null;
		try{
			inputStream = fileObject.getInputStream();
			String text = FileUtil.readStreamContent(inputStream, scriptEngine.getEncode());
			return ScriptUtil.getDefault().createScriptSegment(scriptEngine, null, text);
		}catch(Exception e){
			LOGGER.errorMessage("加载tiny脚本文件[{0}]出错", e,fileObject.getAbsolutePath());
		} finally{
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (Exception e) {
					LOGGER.errorMessage("关闭流出错,文件路径:[{0}]", e,fileObject.getAbsolutePath());
				}
			}
		}
		return null;
	}

	protected boolean checkMatch(FileObject fileObject) {
		return fileObject.getFileName().endsWith(".tinyscript") || fileObject.getFileName().endsWith(".ts");
	}

}
