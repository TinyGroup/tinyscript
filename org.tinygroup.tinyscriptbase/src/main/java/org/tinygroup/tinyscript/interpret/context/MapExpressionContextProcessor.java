package org.tinygroup.tinyscript.interpret.context;

import java.util.HashMap;
import java.util.Map;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.EntryPairContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.MapExpressionContext;

public class MapExpressionContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.MapExpressionContext>{

	public Class<MapExpressionContext> getType() {
		return MapExpressionContext.class;
	}

	public ScriptResult process(MapExpressionContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		
		if(parseTree.mapEntryList()!=null){
		   //处理map的逻辑
		   Map<Object,Object> map = new HashMap<Object,Object>();
		   for(EntryPairContext entryPairContext:parseTree.mapEntryList().entryPair()){
			   Object key = interpret.interpretParseTreeValue(entryPairContext.expression(0), segment, context);
			   Object value = interpret.interpretParseTreeValue(entryPairContext.expression(1), segment, context);
			   map.put(key, value);
		   }
		   return new ScriptResult(map);
		}else{
			//throw new ScriptException(String.format("[%s]类型的ParserRuleContext没有找到对应的处理元素.", getType()));
			//对应{}，默认返回map
			return new ScriptResult(new HashMap<Object,Object>());
		}
		
	}

}
