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
public class PackageDeclarationContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.PackageDeclarationContext>{
    public Class<TinyScriptParser.PackageDeclarationContext> getType() {
        return TinyScriptParser.PackageDeclarationContext.class;
    }

    public ScriptResult process(TinyScriptParser.PackageDeclarationContext parseTree, ScriptInterpret interpret, ScriptSegment segment, ScriptContext context) throws Exception {
        return new ScriptResult(parseTree.qualifiedName().getText());
    }
}
