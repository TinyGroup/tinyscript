package org.tinygroup.tinyscript.dataset.function;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;

public class DataSetReplaceFunction extends AbstractScriptFunction {

	public String getNames() {
		return "replace";
	}
	
	public String getBindingTypes() {
		return DataSet.class.getName();
	}
	
	public boolean  enableExpressionParameter(){
		return true;
	}

	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException("replace函数的参数为空!");
			} else if (checkParameters(parameters, 3)) {
				DataSet left = (DataSet) getValue(parameters[0]);
				DataSet right = (DataSet) getValue(parameters[1]);
				String  name = (String) getValue(parameters[2]);
				int leftColumn = DataSetUtil.getFieldIndex(left, name);
				if(leftColumn<0){
				   throw new ScriptException(String.format("左数据集没有找到字段%s", name));
				}
				int rightColumn = DataSetUtil.getFieldIndex(right, name);
				if(leftColumn<0){
				   throw new ScriptException(String.format("右数据集没有找到字段%s", name));
				}
                return replace((AbstractDataSet)left,(AbstractDataSet)right,leftColumn,rightColumn);
			} else if (checkParameters(parameters, 4)) {
				DataSet left = (DataSet) getValue(parameters[0]);
				DataSet right = (DataSet) getValue(parameters[1]);
				String  leftName = (String) getValue(parameters[2]);
				String  rightName = (String) getValue(parameters[3]);
				int leftColumn = DataSetUtil.getFieldIndex(left, leftName);
				if(leftColumn<0){
				   throw new ScriptException(String.format("左数据集没有找到字段%s", leftName));
				}
				int rightColumn = DataSetUtil.getFieldIndex(right, rightName);
				if(leftColumn<0){
				   throw new ScriptException(String.format("右数据集没有找到字段%s", rightName));
				}
                return replace((AbstractDataSet)left,(AbstractDataSet)right,leftColumn,rightColumn);
			} else {
				throw new ScriptException("replace函数的参数格式不正确!");
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("replace函数的参数格式不正确!", e);
		}
	}
	
	private DataSet replace(AbstractDataSet left,AbstractDataSet right,int leftColumn,int rightColumn) throws Exception{
		if(left.getRows()!=right.getRows()){
		   throw new ScriptException(String.format("左数据集行数%s,右数据集行数%s,行数不匹配!", left.getRows(),right.getRows()));
		}
		for(int i=0;i<left.getRows();i++){
			Object value = right.getData(right.getShowIndex(i), right.getShowIndex(rightColumn));
			left.setData(left.getShowIndex(i), left.getShowIndex(leftColumn), value);
		}
		return left;
	}

}
