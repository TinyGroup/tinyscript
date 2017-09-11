package org.tinygroup.tinyscript.interpret;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.antlr.v4.runtime.CommonToken;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RuleContext;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.interpret.exception.InterpretFormatException;
import org.tinygroup.tinyscript.interpret.exception.ParserRuleContextException;
import org.tinygroup.tinyscript.interpret.exception.RecognizerException;


/**
 * tiny脚本解释器的异常侦听器
 * @author yancheng11334
 *
 */
public class ScriptParserErrorListener implements org.antlr.v4.runtime.ANTLRErrorListener {
    
	private String sourceName;
	private List<ScriptException> exceptionList = new ArrayList<ScriptException>();
	
	public ScriptParserErrorListener(String name){
		this.sourceName = name;
	}
	
	public List<ScriptException> getExceptionList() {
		return exceptionList;
	}

	public void syntaxError(Recognizer<?, ?> recognizer,
			Object offendingSymbol, int line, int charPositionInLine,
			String msg, RecognitionException e) {
		ParserRuleContext parserRuleContext = null;
    	if(e!=null){
    		RuleContext ruleContext=e.getCtx();
        	if(ruleContext!=null && ruleContext instanceof ParserRuleContext){
        		parserRuleContext = (ParserRuleContext)ruleContext;
        	}
    	}
    	
    	String text = null;
    	if(offendingSymbol instanceof CommonToken){
    		CommonToken token = (CommonToken) offendingSymbol;
    		text = token.getInputStream().toString();
    	}
    	
    	if(parserRuleContext!=null){
    		ParserRuleContextException pe = new ParserRuleContextException(e,parserRuleContext,text);
    		exceptionList.add(new InterpretFormatException(pe));
    	}else{
    		RecognizerException re = new RecognizerException(recognizer,line,charPositionInLine,msg);
    		exceptionList.add(new InterpretFormatException(re));
    	}
	}

	public void reportAmbiguity(Parser recognizer, DFA dfa, int startIndex,
			int stopIndex, boolean exact, BitSet ambigAlts, ATNConfigSet configs) {
		//ingore
	}

	public void reportAttemptingFullContext(Parser recognizer, DFA dfa,
			int startIndex, int stopIndex, BitSet conflictingAlts,
			ATNConfigSet configs) {
		//ingore
	}

	public void reportContextSensitivity(Parser recognizer, DFA dfa,
			int startIndex, int stopIndex, int prediction, ATNConfigSet configs) {
		//ingore
	}

	public String toString() {
		return "ScriptParserErrorListener [sourceName=" + sourceName
				+ ", exceptionList=" + exceptionList + "]";
	}

	
}
