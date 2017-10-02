package org.tinygroup.tinyscript.executor;

import java.io.File;

import org.apache.commons.lang.StringEscapeUtils;
import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.ScriptUtil;

/**
 * 抽象脚本执行器
 * @author yancheng11334
 *
 */
public abstract class AbstractTinyScriptOperator implements TinyScriptOperator{

	@SuppressWarnings("unused")
	public void execute(String[] args) throws ScriptException {
		String relativePath = null;
		String absolutePath = null;
		String methodScript = null;

		// 解析参数
		if (args.length >= 1) {
			relativePath = args[0].replaceAll("\\\\", "/");
		}
		if (args.length >= 2) {
			absolutePath = args[1].replaceAll("\\\\", "/");
		}
		if (args.length >= 3) {
			methodScript = args[2].replaceAll("'", "\"");
		}
		
		//创建引擎实例
		ScriptEngine engine = createScriptEngine();
		
		//处理上下文
		ScriptContext context = new DefaultScriptContext();
		context.setParent(engine.getScriptContext());
		
		try{
			//加载并注册脚本类
			String content = FileUtil.readFileContent(new File(absolutePath), "utf-8");
			//反转义
			content = StringEscapeUtils.unescapeJava(content);
			ScriptSegment scriptSegment = ScriptUtil.getDefault().createScriptSegment(engine, null, content);
			engine.addScriptSegment(scriptSegment);
			
			engine.execute(scriptSegment, context);
		}catch(ScriptException e){
		    throw e;
		}catch(Exception e){
		    throw new ScriptException(ResourceBundleUtil.getDefaultMessage("engine.run.error", absolutePath),e);
		}
	}

}
