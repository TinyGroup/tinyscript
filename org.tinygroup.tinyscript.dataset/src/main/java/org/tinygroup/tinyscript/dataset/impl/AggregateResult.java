package org.tinygroup.tinyscript.dataset.impl;

public class AggregateResult {

	String  aggregateName;  //聚合名
	Object[] array;  //聚合结果
	
	public String getName(){
		return aggregateName;
	}
	
	public AggregateResult(String name,int size){
		this.aggregateName = name;
		this.array = new Object[size];
	}
	
	public Object getData(int row){
		return array[row];
	}
	
	public void setData(int row,Object value){
		array[row] = value;
	}
}
