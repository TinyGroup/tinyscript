package org.tinygroup.tinyscript.interpret.context;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.sql.SqlUtil;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.SqlScriptExpressionContext;

public class SqlScriptExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.SqlScriptExpressionContext>{

	public Class<SqlScriptExpressionContext> getType() {
		return SqlScriptExpressionContext.class;
	}

	public ScriptResult process(SqlScriptExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		try{
			 Object sqlObj = interpret.interpretParseTreeValue(parseTree.getChild(0), segment, context);
			 String sql = parseTree.getChild(1).getText();
			 sql = sql.substring(2, sql.length()-2); //去掉[[ ]]
			 Object value = SqlUtil.executeSql(sqlObj, sql, context);
			 if(value!=null && value instanceof ScriptResult){
			    return (ScriptResult) value;
			 }
			 return new ScriptResult(value);
		}catch(Exception e){
			throw new ScriptException(String.format("[%s]类型的ParserRuleContext处理发生异常", getType()),e);
		}
	}
	
}
