package org.tinygroup.tinyscript.impl;

import java.util.Map;

import org.tinygroup.tinyscript.collection.expression.*;
import org.tinygroup.tinyscript.collection.function.list.*;
import org.tinygroup.tinyscript.collection.function.map.*;
import org.tinygroup.tinyscript.collection.function.math.*;
import org.tinygroup.tinyscript.collection.function.set.*;
import org.tinygroup.tinyscript.collection.objectitem.*;
import org.tinygroup.tinyscript.database.function.*;
import org.tinygroup.tinyscript.dataset.attribute.*;
import org.tinygroup.tinyscript.dataset.function.*;
import org.tinygroup.tinyscript.dataset.objectitem.*;
import org.tinygroup.tinyscript.datasetwithtree.function.*;
import org.tinygroup.tinyscript.text.function.*;
import org.tinygroup.tinyscript.tree.function.*;
import org.tinygroup.tinyscript.excel.function.*;

import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.assignvalue.AssignValueProcessor;
import org.tinygroup.tinyscript.assignvalue.AssignValueUtil;
import org.tinygroup.tinyscript.expression.ExpressionUtil;
import org.tinygroup.tinyscript.expression.Operator;
import org.tinygroup.tinyscript.impl.DefaultScriptEngine;
import org.tinygroup.tinyscript.interpret.AttributeProcessor;
import org.tinygroup.tinyscript.interpret.AttributeUtil;
import org.tinygroup.tinyscript.interpret.ScriptContextUtil;
import org.tinygroup.tinyscript.objectitem.ObjectItemProcessor;
import org.tinygroup.tinyscript.objectitem.ObjectItemUtil;

/**
 * 默认的集算器实现
 * @author yancheng11334
 *
 */
public class DefaultComputeEngine extends DefaultScriptEngine implements ComputeEngine{
	
	private static ObjectItemProcessor listToListProcessor = new ListToListProcessor();
	private static ObjectItemProcessor dataSetItemProcessor = new DataSetItemProcessor();
	private static ObjectItemProcessor dataSetColumnItemProcessor = new DataSetColumnItemProcessor();
	private static ObjectItemProcessor dataSetRowItemProcessor = new DataSetRowItemProcessor();
	private static ObjectItemProcessor mapItemProcessor = new MapItemProcessor();
	
	private static AttributeProcessor excelAttributeProcessor = new ExcelAttributeProcessor();
	private static AttributeProcessor dataSetColumnAttributeProcessor = new DataSetColumnAttributeProcessor();
	
	private static AssignValueProcessor excelAssignValueProcessor = new ExcelAssignValueProcessor();
	
	private static Operator unionOperator = new UnionOperator();
	private static Operator subtractOperator = new SubtractOperator();
	private static Operator intersectionOperator = new IntersectionOperator();
	private static Operator xorOperator = new XorOperator();
	
	public DefaultComputeEngine() throws ScriptException {
		super();
		initComputeEngine();
	}
	
	public DefaultComputeEngine(Map<?,?> map) throws ScriptException {
		super(map);
		initComputeEngine();
	}

	private void initComputeEngine() throws ScriptException {
		// 设置属性
		setIndexFromOne(true);
		// 注册引擎到上下文
		ScriptContextUtil.setScriptEngine(getScriptContext(), this);

		// 注册函数
		addScriptFunction(new FillFunction());
		addScriptFunction(new IntersectionFunction());
		addScriptFunction(new UnionFunction());
		addScriptFunction(new SubtractFunction());
		addScriptFunction(new FilterFunction());
		addScriptFunction(new SortFunction());
		addScriptFunction(new CopyFunction());
		addScriptFunction(new XorFunction());
		addScriptFunction(new RemoveFunction());

		addScriptFunction(new MapUnionFunction());
		addScriptFunction(new MapIntersectionFunction());
		addScriptFunction(new MapSubtractFunction());
		addScriptFunction(new MapXorFunction());

		addScriptFunction(new SetUnionFunction());
		addScriptFunction(new SetIntersectionFunction());
		addScriptFunction(new SetSubtractFunction());
		addScriptFunction(new SetXorFunction());

		addScriptFunction(new CreateDataTreeFunction());
		addScriptFunction(new DataSetToTreeFunction());

		addScriptFunction(new ExecuteSqlFunction());
		addScriptFunction(new QuerySqlFunction());

		addScriptFunction(new CombinationFunction());
		addScriptFunction(new PermutationFunction());
		addScriptFunction(new AllPermutationFunction());

		addScriptFunction(new DataSetCopyFunction());
		addScriptFunction(new DataSetRemoveFunction());
		addScriptFunction(new DataSetFillFunction());
		addScriptFunction(new DataSetFilterFunction());
		addScriptFunction(new DataSetSortFunction());
		addScriptFunction(new DataSetGroupFunction());
		addScriptFunction(new DataSetJoinFunction());
		addScriptFunction(new DataSetSelectFunction());
		addScriptFunction(new DataSetMatchFunction());
		addScriptFunction(new DataSetUpdateFunction());
		addScriptFunction(new DataSetRenameFunction());
		addScriptFunction(new DataSetFieldFunction());
		addScriptFunction(new DataSetSubFunction());
		addScriptFunction(new DataSetConvertFunction());
		addScriptFunction(new DataSetForEachFunction());
		addScriptFunction(new DataSetReplaceFunction());
		addScriptFunction(new DataSetGroupStagedFunction());
		addScriptFunction(new DataSetGroupDynamicFunction());

		addScriptFunction(new GroupDataSetFilterFunction());
		addScriptFunction(new GroupDataSetSortFunction());

		addScriptFunction(new GroupDataSetAggregateFunction());
		addScriptFunction(new DataSetAggregateFunction());

		addScriptFunction(new CursorRowFunction());
		addScriptFunction(new FirstRowFunction());
		addScriptFunction(new LastRowFunction());
		addScriptFunction(new NextRowFunction());
		addScriptFunction(new PreviewRowFunction());

		addScriptFunction(new ReadExcelFunction());

		addScriptFunction(new ReadTxtFunction());

		// 注册处理器
		ObjectItemUtil.addObjectItemProcessor(listToListProcessor, 0);
		ObjectItemUtil.addObjectItemProcessor(dataSetItemProcessor);
		ObjectItemUtil.addObjectItemProcessor(dataSetColumnItemProcessor);
		ObjectItemUtil.addObjectItemProcessor(dataSetRowItemProcessor);
		ObjectItemUtil.addObjectItemProcessor(mapItemProcessor);

		AttributeUtil.addAttributeProcessor(excelAttributeProcessor, 0);
		AttributeUtil.addAttributeProcessor(dataSetColumnAttributeProcessor, 1);
		AssignValueUtil.addAssignValueProcessor(excelAssignValueProcessor);

		// 注册操作符处理器
		ExpressionUtil.addOperator(unionOperator);
		ExpressionUtil.addOperator(subtractOperator);
		ExpressionUtil.addOperator(intersectionOperator);
		ExpressionUtil.addOperator(xorOperator);
	}
	
}
