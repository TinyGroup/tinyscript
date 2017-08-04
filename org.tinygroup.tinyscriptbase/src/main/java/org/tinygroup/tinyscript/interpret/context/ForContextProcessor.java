package org.tinygroup.tinyscript.interpret.context;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.exception.BreakException;
import org.tinygroup.tinyscript.interpret.exception.ContinueException;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ForControlContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ForInitContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.VariableDeclaratorContext;

/**
 * Created by luoguo on 16/8/10.
 */
public class ForContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ForContext> {
    public Class<TinyScriptParser.ForContext> getType() {
        return TinyScriptParser.ForContext.class;
    }

    public ScriptResult process(TinyScriptParser.ForContext parseTree, ScriptInterpret interpret, ScriptSegment segment, ScriptContext context) throws Exception {

        //处理for循环
        ForControlContext forControlContext = parseTree.forControl();
        ScriptContext  forScriptContext = new DefaultScriptContext(); //构造for循环上下文
        Set<String> paramNames = new HashSet<String>();//记录临时变量
        
        if (forControlContext.enhancedForControl() != null) {
            //冒号格式的for循环
            String name = forControlContext.enhancedForControl().Identifier().getText();
            paramNames.add(name);
            forScriptContext.setParent(context);
            Iterator<Object> it = ExpressionUtil.getIterator(interpret.interpretParseTreeValue(forControlContext.enhancedForControl().expression(), segment, forScriptContext));
            while (it.hasNext()) {
            	forScriptContext.put(name, it.next());
                try {
                    interpret.interpretParseTree(parseTree.statement(), segment, forScriptContext);
                } catch (ContinueException e) {
                    continue;
                } catch (BreakException e) {
                    break;
                }
            }
            
        } else {

            //分号格式的for循环
            List<String> names = null;
            forScriptContext.setParent(context);
            if (forControlContext.forInit() != null) {
                interpret.interpretParseTree(forControlContext.forInit(), segment, forScriptContext);
                names = getForInitParamNames(forControlContext.forInit());
                paramNames.addAll(names);
            }
            
            boolean runtag = true;
            while (runtag) {

                try {
                    interpret.interpretParseTree(parseTree.statement(), segment, forScriptContext);
                    //执行更新
                    if (forControlContext.forUpdate() != null) {
                        interpret.interpretParseTree(forControlContext.forUpdate(), segment, forScriptContext);
                    }
                    if (forControlContext.expression() != null) {
                        runtag = ExpressionUtil.getBooleanValue(interpret.interpretParseTreeValue(forControlContext.expression(), segment, forScriptContext));
                    }
                } catch (ContinueException e) {
                    //执行更新
                    if (forControlContext.forUpdate() != null) {
                        interpret.interpretParseTree(forControlContext.forUpdate(), segment, forScriptContext);
                    }
                    if (forControlContext.expression() != null) {
                        runtag = ExpressionUtil.getBooleanValue(interpret.interpretParseTreeValue(forControlContext.expression(), segment, forScriptContext));
                    }
                    continue;
                } catch (BreakException e) {
                    break;
                }

            }
          
        }
        
        //更新原始上下文
        for(Entry<String,Object> entry:forScriptContext.getItemMap().entrySet()){
        	//非临时变量的结果保存到原始上下文
        	if(!paramNames.contains(entry.getKey())){
        	   context.put(entry.getKey(), entry.getValue());
        	}
        }

        return ScriptResult.VOID_RESULT;
    }

    private List<String> getForInitParamNames(ForInitContext forInitContext) {
        List<String> names = new ArrayList<String>();
        if (forInitContext.localVariableDeclaration() != null) {
            for (VariableDeclaratorContext variableDeclaratorContext : forInitContext.localVariableDeclaration().variableDeclarators().variableDeclarator()) {
                names.add(variableDeclaratorContext.Identifier().getText());
            }
        }
        return names;
    }

}
