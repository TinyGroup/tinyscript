package org.tinygroup.tinyscript.interpret.context;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.impl.ArrayScriptContext;
import org.tinygroup.tinyscript.impl.ArrayScriptContext.Element;
import org.tinygroup.tinyscript.interpret.ParserRuleContextProcessor;
import org.tinygroup.tinyscript.interpret.ScriptInterpret;
import org.tinygroup.tinyscript.interpret.ScriptResult;
import org.tinygroup.tinyscript.interpret.ScriptUtil;
import org.tinygroup.tinyscript.interpret.exception.RunScriptException;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.ArrayCreatorContext;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser.DimContext;

/**
 * 创建多维数组对象
 * @author yancheng11334
 *
 */
public class ArrayCreatorContextProcessor implements ParserRuleContextProcessor<TinyScriptParser.ArrayCreatorContext>{

	public Class<ArrayCreatorContext> getType() {
		return ArrayCreatorContext.class;
	}

	public ScriptResult process(ArrayCreatorContext parseTree,
			ScriptInterpret interpret, ScriptSegment segment,
			ScriptContext context) throws Exception {
		String className = null;
		try{
			className = parseTree.qualifiedName().getText();
			
			List<DimContext> dimContextList = parseTree.dims().dim();
			int[] dimensions = new int[dimContextList.size()]; //数组的维度
			
			Class<?> componentType = ScriptUtil.findJavaClass(className,segment,null);
			if(componentType==null){
			   throw new ScriptException(String.format("创建[%s]类型的多维数组失败:没有找到类对象.", className));
			}
			Object array = null;
			//处理多维数组结构体
			Arrays.fill(dimensions, -1);
			for(int i=0;i<dimensions.length;i++){
				DimContext dimContext = dimContextList.get(i);
				if(dimContext.expression()!=null){
				   dimensions[i] = (Integer)interpret.interpretParseTreeValue(dimContext.expression(), segment, context);
				   if(dimensions[i]<0){
					  throw new ScriptException(String.format("创建[%s]类型的多维数组失败:第[%s]维度数组的长度不能小于0", className,i));
				   }
				}	
			}
			ArrayScriptContext arrayScriptContext = null;
			if(parseTree.arrayInitializer()!=null){
				//处理多维初始化数据
				arrayScriptContext = (ArrayScriptContext) interpret.interpretParseTreeValue(parseTree.arrayInitializer(), segment, context);
			}
			
			// 检查多维数组结构体是否存在空维度
			boolean emptyTag = false;
			for(int i=0;i<dimensions.length;i++){
			    if(dimensions[i]<0){
			    	emptyTag = true;
			    	break;
			    }
			}
			if(emptyTag && arrayScriptContext==null){
				throw new ScriptException(String.format("创建[%s]类型的多维数组失败:数组结构存在未定义长度的维度,同时又没有定义初始化数据", className));
			}else if(emptyTag && arrayScriptContext!=null){
				//利用初始化数据填充空维度
				Integer[] dataDims = arrayScriptContext.getDimsToLeaf();
				if(dimensions.length!=dataDims.length){
				   throw new ScriptException(String.format("创建[%s]类型的多维数组失败:数组结构维度长度[%s],数组初始化数据维度长度[%s],两者不匹配", className,dimensions.length,dataDims.length));
				}
				for(int i=0;i<dimensions.length;i++){
					if(dimensions[i]<0){
					   dimensions[i] = dataDims[i].intValue();
					}
				}
			}
			
			if(arrayScriptContext!=null){
				//有初始化数据
				array =  Array.newInstance(componentType, dimensions);
				Iterator<Element> iterator= arrayScriptContext.iterator();
				while(iterator.hasNext()){
					Element element = iterator.next();
					Object a = getArray(array,element.getItems()); //取得对应维度的数组
					Array.set(a, element.getIndex(), element.getValue());
				}
			}else{
				//没有初始化数据
				array =  Array.newInstance(componentType, dimensions);
			}
			
			return new ScriptResult(array);
		}catch (Exception e) {
			throw new RunScriptException(e,parseTree,segment,ScriptException.ERROR_TYPE_RUNNING,String.format("创建对象[%s]数组发生异常", className));
		}
	}
	
	private Object getArray(Object array,Integer[] dims){
		Object result = null;
		if(dims.length==0){
		   return array;	
		}
		for(int i=0;i<dims.length;i++){
			if(i==0){
			   result = Array.get(array, dims[i]);
			}else{
			   result = Array.get(result, dims[i]);
			}
		}
		return result ;
	}

}
