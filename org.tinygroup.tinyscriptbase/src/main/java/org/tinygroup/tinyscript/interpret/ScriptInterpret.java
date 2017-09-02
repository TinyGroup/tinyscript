package org.tinygroup.tinyscript.interpret;

import java.util.HashMap;
import java.util.Map;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import org.tinygroup.tinyscript.parser.grammer.TinyScriptLexer;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.exception.ReturnException;
import org.tinygroup.tinyscript.interpret.terminal.DefaultTerminalNodeProcessor;

/**
 * tiny脚本的解释器
 * 
 * @author yancheng11334
 * 
 */
@SuppressWarnings({ "unchecked","rawtypes" })
public final class ScriptInterpret {
	protected static final Logger LOGGER = LoggerFactory
			.getLogger(ScriptInterpret.class);
	
	private static final int TERMINALNODE_MAX = 200;

	private TerminalNodeProcessor[] terminalNodeProcessors;
	private Map<Class<ParserRuleContext>, ParserRuleContextProcessor> contextProcessorMap;
	private DefaultTerminalNodeProcessor defaultTerminalNodeProcessor;

	public ScriptInterpret() {
		this(TERMINALNODE_MAX);
	}

	public ScriptInterpret(int limit) {
		int num = limit <= 0 ? TERMINALNODE_MAX : limit;
		terminalNodeProcessors = new TerminalNodeProcessor[num];
		contextProcessorMap = new HashMap<Class<ParserRuleContext>, ParserRuleContextProcessor>();
		defaultTerminalNodeProcessor = new DefaultTerminalNodeProcessor();
	}
	
	public void addTerminalNodeProcessor(TerminalNodeProcessor processor) {
        terminalNodeProcessors[processor.getType()] = processor;
    }

    public void addContextProcessor(ParserRuleContextProcessor contextProcessor) {
        contextProcessorMap.put(contextProcessor.getType(), contextProcessor);
    }


    /**
     * 创建tiny脚本段
     * @param engine
     * @param sourceName
     * @param script
     * @return
     * @throws Exception
     */
    public ScriptSegment createScriptSegment(ScriptEngine engine,String sourceName,String script) throws Exception{
    	TinyScriptParser.CompilationUnitContext context = this.parserScriptSegmentContext(sourceName, script);
		ParserRuleContextSegment scriptSegment = new ParserRuleContextSegment(engine,sourceName, script,context);
		return scriptSegment;
    }

    
	/**
	 * 根据脚本段得到解释器上下文
	 * 
	 * @param sourceName
	 * @param script
	 * @return
	 * @throws ScriptException
	 */
	public TinyScriptParser.CompilationUnitContext parserScriptSegmentContext(
			String sourceName, String script) throws ScriptException {
		ANTLRInputStream is = new ANTLRInputStream(script);
		is.name = sourceName;

		Lexer lexer = new TinyScriptLexer(is);
		TinyScriptParser parser = new TinyScriptParser(
				new CommonTokenStream(lexer));
		ScriptParserErrorListener listener = new ScriptParserErrorListener(
				sourceName);
		lexer.removeErrorListeners();
		lexer.addErrorListener(listener);
		parser.removeErrorListeners();
		parser.addErrorListener(listener);

		TinyScriptParser.CompilationUnitContext context = parser.compilationUnit();
		if (!listener.getExceptionList().isEmpty()) {
			throw listener.getExceptionList().get(0);
		}
		return context;
	}
	
	/**
	 * 解析节点对应的值
	 * @param tree
	 * @param segment
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public Object interpretParseTreeValue(ParseTree tree,
			ScriptSegment segment,
			ScriptContext context)
			throws Exception {
		ScriptResult result = interpretParseTree(tree, segment, context);
		if(result!=null && !result.isVoid()){
		   return result.getResult();
		}
		return null;
	}

	/**
	 * 解析节点
	 * @param tree
	 * @param segment
	 * @param context
	 * @return
	 * @throws Exception
	 */
	public ScriptResult interpretParseTree(ParseTree tree,
			ScriptSegment segment,
			ScriptContext context)
			throws Exception {
		ScriptResult returnValue = ScriptResult.VOID_RESULT;
		if(tree==null){
		   return returnValue;
		}
		if(tree instanceof TerminalNode){
			//终端处理
			TerminalNode terminalNode = (TerminalNode) tree;
			//LOGGER.logMessage(LogLevel.INFO, String.format("TerminalNode:%s type:%s", tree.getText(),terminalNode.getSymbol().getType()));
            TerminalNodeProcessor<ParseTree> processor = terminalNodeProcessors[terminalNode.getSymbol().getType()];
            if (processor != null) {
                return processor.process(tree,segment, context);
            } else {
                return defaultTerminalNodeProcessor.process(terminalNode, segment, context);
            }
		}else if(tree instanceof ParserRuleContext){
			//上下文处理
			try{
				//LOGGER.logMessage(LogLevel.INFO, String.format("ParserRuleContext:%s type:%s", tree.getText(),tree.getClass().getName()));
				ParserRuleContextProcessor<ParserRuleContext> processor = contextProcessorMap.get(tree.getClass());
                if (processor != null) {
                    returnValue = processor.process((ParserRuleContext)tree, this, segment, context);
                    if(returnValue!=null && !returnValue.isContinue()){
                       return returnValue;
                    }
                }
                
                //如果没有合适的处理器则遍历子节点
                //遍历子节点根据值判断能处理的场景非常有限,更复杂的场景如指令，必须增加专门的上下文处理器控制。
                for (int i = 0; i < tree.getChildCount(); i++) {
                	ScriptResult value = interpretParseTree(tree.getChild(i), segment, context);
                    if (value.getResult()!=null) {
                    	returnValue = value;
                    }
                }
                return returnValue;
			}catch(ScriptException e){
				//@TODO
				throw e;
			}catch(Exception e){
				//@TODO
				throw e;
			}
		}else{
			throw new ScriptException(String.format("未知的ParseTree节点类型:%s", tree.getClass().getName()));
		}
	}
	
	/**
	 * 判断是否包含return节点
	 * @param tree
	 * @return
	 * @throws Exception
	 */
	public boolean containsReturn(ParserRuleContext tree) throws Exception{
		try{
			 findReturn(tree);
		}catch(ReturnException e){
			//存在返回指令
			return true;
		}
		return false;
	}
	
	private void findReturn(TerminalNode terminalNode) throws ReturnException{
		if(terminalNode.getSymbol().getType()==TinyScriptParser.RETURN){
		   throw new ReturnException(null);
		}
	}
	
	private void findReturn(ParserRuleContext parserRuleContext) throws ReturnException{
		for (int i = 0; i < parserRuleContext.getChildCount(); i++) {
			ParseTree  parseTree = parserRuleContext.getChild(i);
			if(parseTree instanceof TerminalNode){
			   findReturn((TerminalNode)parseTree);
			}else if(parseTree instanceof ParserRuleContext){
			   findReturn((ParserRuleContext)parseTree);
			}
		}
	}
	
}
