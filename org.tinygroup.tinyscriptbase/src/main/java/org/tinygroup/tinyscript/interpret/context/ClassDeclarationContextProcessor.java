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
public class ClassDeclarationContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ClassDeclarationContext>{
    public Class<TinyScriptParser.ClassDeclarationContext> getType() {
        return TinyScriptParser.ClassDeclarationContext.class;
    }

    public ScriptResult process(TinyScriptParser.ClassDeclarationContext parseTree, ScriptInterpret interpret, ScriptSegment segment, ScriptContext context) throws Exception {
        return new ScriptResult(parseTree.Identifier().getText());
    }
}
