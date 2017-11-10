package org.tinygroup.tinyscript.fileresolver;

import java.io.InputStream;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.fileresolver.impl.AbstractFileProcessor;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
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
			LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("fileprocessor.load.start", fileObject.getAbsolutePath()));
			ScriptSegment segment = loadScriptSegment(fileObject);
			if(segment!=null){
			   scriptEngine.addScriptSegment(segment);
			   LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("fileprocessor.loading", segment.getSegmentId()));
			}
			LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("fileprocessor.load.end", fileObject.getAbsolutePath()));
		}
		for (FileObject fileObject : deleteList) {
			LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("fileprocessor.delete.start", fileObject.getAbsolutePath()));
			ScriptSegment segment = loadScriptSegment(fileObject);
			if(segment!=null){
				scriptEngine.removeScriptSegment(segment);
				LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("fileprocessor.deleting", segment.getSegmentId()));
			}
			LOGGER.logMessage(LogLevel.INFO, ResourceBundleUtil.getDefaultMessage("fileprocessor.delete.end", fileObject.getAbsolutePath()));
		}
	}
	
	private ScriptSegment loadScriptSegment(FileObject fileObject){
		InputStream inputStream = null;
		try{
			inputStream = fileObject.getInputStream();
			String text = FileUtil.readStreamContent(inputStream, scriptEngine.getEncode());
			return ScriptUtil.getDefault().createScriptSegment(scriptEngine, null, text);
		}catch(Exception e){
			LOGGER.errorMessage(ResourceBundleUtil.getDefaultMessage("fileprocessor.load.error", fileObject.getAbsolutePath()),e);
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
		return fileObject.getFileName().endsWith(".tsf") ;
	}

}
