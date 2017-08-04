package org.tinygroup.tinyscript;

import junit.framework.TestCase;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;

import java.util.List;
import java.util.Map;

/**
 * 测试脚本引擎的集合功能
 *
 * @author yancheng11334
 */
public class MiscTest extends TestCase {

    private ScriptEngine scriptEngine;

    protected void setUp() throws Exception {
        scriptEngine = new DefaultScriptEngine();
        scriptEngine.start();
    }

    public void testInstanceof() throws Exception {
        assertEquals(true, scriptEngine.execute("return \"abc\" instanceof String;"));
        ScriptContext context=new DefaultScriptContext();
        context.put("abc",String.class);
        assertEquals(true, scriptEngine.execute("return \"abc\" instanceof abc;",context));
    }

}
