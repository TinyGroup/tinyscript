package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptResult;

/**
 * Created by luoguo on 16/8/10.
 */
public class ImportDeclarationContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ImportDeclarationContext>{
    public Class<TinyScriptParser.ImportDeclarationContext> getType() {
        return TinyScriptParser.ImportDeclarationContext.class;
    }

    public ScriptResult process(TinyScriptParser.ImportDeclarationContext parseTree, ScriptInterpret interpret, ScriptSegment segment, ScriptContext context) throws Exception {
        String text = parseTree.getText();
        return new ScriptResult(text.substring(6,text.length()-1));
    }
}
