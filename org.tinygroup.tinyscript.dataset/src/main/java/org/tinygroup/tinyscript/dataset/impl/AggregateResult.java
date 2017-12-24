package org.tinygroup.tinyscript.dataset.impl;

import org.tinygroup.tinyscript.dataset.Field;

public class AggregateResult {
	String aggregateName; // 聚合名
	Object[] params;// 聚合的额外参数（当更新分组信息时同步更新聚合结果需要用到
	Object[] array; // 聚合结果
	Field field;//聚合结果对应的字段
	String functionName;//聚合结果对应的方法名

	public String getName() {
		return aggregateName;
	}

	public Object[] getParams() {
		return params;
	}

	public AggregateResult(String name, int size, Field field, String functionName, Object... params) {
		this.params = params;
		this.aggregateName = name;
		this.array = new Object[size];
		this.field = field;
		this.functionName = functionName;
	}

	public Object getData(int row) {
		return array[row];
	}

	public void setData(int row, Object value) {
		array[row] = value;
	}
	
	public Field getField() {
		return field;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setName(String aggregateName) {
		this.aggregateName = aggregateName;
	}
}
