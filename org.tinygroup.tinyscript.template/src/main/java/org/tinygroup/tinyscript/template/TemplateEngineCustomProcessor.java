package org.tinygroup.tinyscript.template;

import org.tinygroup.template.TemplateContext;
import org.tinygroup.template.TemplateEngine;
import org.tinygroup.template.TemplateException;
import org.tinygroup.template.TemplateRender;
import org.tinygroup.template.impl.TemplateContextDefault;
import org.tinygroup.template.impl.TemplateRenderDefault;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.custom.CustomProcessor;

/**
 * 模板引擎规则处理器
 * @author yancheng11334
 *
 */
public class TemplateEngineCustomProcessor implements CustomProcessor{

	public boolean isMatch(Object obj) {
		return obj instanceof TemplateEngine;
	}

	public Object executeRule(Object customObj, String customRule,
			ScriptContext context) throws ScriptException {
		TemplateEngine engine = (TemplateEngine) customObj;
		TemplateRender render = new TemplateRenderDefault();
		render.setTemplateEngine(engine);
		
		TemplateContext templateContext = new TemplateContextDefault();
		templateContext.setParent(context);
		try {
			return render.renderTemplateContent(customRule, templateContext);
		} catch (TemplateException e) {
		    throw new ScriptException(String.format("模板引擎渲染[%s]发生异常:", customRule),e);
		}
	}

}
