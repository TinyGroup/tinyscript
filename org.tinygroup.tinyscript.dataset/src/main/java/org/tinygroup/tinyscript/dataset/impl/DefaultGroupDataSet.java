package org.tinygroup.tinyscript.dataset.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.DynamicDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;

/**
 * 默认的分组数据集实现
 * @author yancheng11334
 *
 */
public class DefaultGroupDataSet extends GroupDataSet implements Cloneable {

	private List<DynamicDataSet> subDataSetList = new ArrayList<DynamicDataSet>();
	private int current = 0;  //当前子结果集
	private List<AggregateResult> aggregateResultList = new ArrayList<AggregateResult>(); //聚合结果
	
	public DefaultGroupDataSet(List<Field> fields){
		setFields(fields);
	}
	
	public DefaultGroupDataSet(List<Field> fields,boolean tag){
		setFields(fields);
		setIndexFromOne(tag);
	}
	
	public DefaultGroupDataSet(List<Field> fields,List<DynamicDataSet> groups){
		setFields(fields);
		subDataSetList = groups;
	}
	
	public DefaultGroupDataSet(List<Field> fields,List<DynamicDataSet> groups,boolean tag){
		setFields(fields);
		subDataSetList = groups;
		setIndexFromOne(tag);
	}
	
	public DefaultGroupDataSet(List<Field> fields,List<DynamicDataSet> groups,List<AggregateResult> results){
		setFields(fields);
		subDataSetList = groups;
		aggregateResultList = results;
	}
	
	public DefaultGroupDataSet(List<Field> fields,List<DynamicDataSet> groups,List<AggregateResult> results,boolean tag){
		setFields(fields);
		subDataSetList = groups;
		aggregateResultList = results;
		setIndexFromOne(tag);
	}
	
	public boolean isReadOnly() {
		return false;
	}

	public void first() throws Exception {
		current = 0;
		for(DynamicDataSet subDs:subDataSetList){
		    subDs.first();
		}
	}
	
	/**
	 * 本方法参数为实际下标
	 * @param i
	 * @return
	 */
	private DynamicDataSet getSubDataSet(int i){
		return subDataSetList.get(i);
	}

	public boolean previous() throws Exception {
		if(current>0){
		   current--;
		   return true;
		}else{
		   return false;
		}
		
	}

	public void beforeFirst() throws Exception {
		throw new Exception("本数据集实现不支持beforeFirst操作");
	}

	public void afterLast() throws Exception {
		throw new Exception("本数据集实现不支持afterLast操作");
	}

	public boolean next() throws Exception {
		if(current<subDataSetList.size()-1){
		   current++;
		   return  true;
		}else{
		   return  false;
		}
	}

	public boolean absolute(int row) throws Exception {
		if(isIndexFromOne()){
		   if(row>=1 && row<=subDataSetList.size()){
			  current = row-1;
			  return true;
		   }
		}else{
		   if(row>=0 && row<=subDataSetList.size()-1){
		      current = row;
			  return true;
		   }
		}
		return false;
	}

	public int getRows() throws Exception {
		return subDataSetList.size();
	}

	public int getColumns() throws Exception {
		return getFields().size()+aggregateResultList.size();
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(int row, int col) throws Exception {
		int fieldNum = getFields().size();
		
		if(isIndexFromOne()){
		   if(col>=fieldNum+1){
			  //取聚合结果
			  return (T) aggregateResultList.get(getActualIndex(col-fieldNum)).getData(getActualIndex(row));
		   }else{
			  return getSubDataSet(getActualIndex(row)).getData(1,col);  
		   }
		}else{
		   if(col>=fieldNum){
			 //取聚合结果
			  return (T) aggregateResultList.get(getActualIndex(col-fieldNum)).getData(getActualIndex(row));
		   }else{
			  return getSubDataSet(getActualIndex(row)).getData(0,col);
		   }
		}
		
	}

	public <T> void setData(int row, int col, T data) throws Exception {
		int fieldNum = getFields().size();
		
		if(isIndexFromOne()){
		   if(col>=fieldNum+1){
			  //设置聚合结果
			  aggregateResultList.get(getActualIndex(col-fieldNum)).setData(getActualIndex(row), data);
		   }else{
			  getSubDataSet(getActualIndex(row)).setData(1, col, data);
		   }
		}else{
		   if(col>=fieldNum){
			  //设置聚合结果
			  aggregateResultList.get(getActualIndex(col-fieldNum)).setData(getActualIndex(row), data);
		   }else{
			  getSubDataSet(getActualIndex(row)).setData(0, col, data); 
		   }
		}
		
	}

	public <T> T getData(int col) throws Exception {
		return getData(getShowIndex(current),col);
	}

	public <T> void setData(int col, T data) throws Exception {
		setData(getShowIndex(current),col,data);
	}

	public <T> void setData(String fieldName, T data) throws Exception {
		try {
			int col = DataSetUtil.getFieldIndex(this, fieldName);
			if(isIndexFromOne()){
			   if(col>=0){
				  getSubDataSet(current).setData(1,col, data);
			   }else{
				  setData(getShowIndex(current),fieldName,data);
			   }
			}else{
			   if(col>=0){
				  getSubDataSet(current).setData(0,col, data);
			   }else{
			      setData(getShowIndex(current),fieldName,data);
			   }
			}
		} catch (Exception e) {
			throw new Exception("setData操作失败",e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getData(int row, String aggregateName) {
		for(AggregateResult result:aggregateResultList){
		    if(result.getName().equals(aggregateName)){
		       return (T) result.getData(getActualIndex(row));
		    }
		}
		return null;
	}

	public <T> void setData(int row, String aggregateName, T value) {
		for(AggregateResult result:aggregateResultList){
			if(result.getName().equals(aggregateName)){
				result.setData(getActualIndex(row), value);
				return;
			}
		}	
	}

	public DynamicDataSet deleteColumn(int col) throws Exception {
		for(DynamicDataSet subDs:subDataSetList){
			subDs.deleteColumn(col);
		}
		getFields().remove(getActualIndex(col));
		return this;
	}

	public DynamicDataSet deleteColumn(String colName) throws Exception {
		try{
			int col = DataSetUtil.getFieldIndex(this, colName);
			return deleteColumn(getShowIndex(col));
		}catch(Exception e){
			throw new Exception("deleteColumn发生异常",e);
		}
	}

	public DynamicDataSet deleteRow(int row) throws Exception {
		throw new Exception("本数据集实现不支持deleteRow操作");
	}

	public DynamicDataSet insertColumn(int col, Field field)
			throws Exception {
		for(DynamicDataSet subDs:subDataSetList){
			subDs.insertColumn(col, field);
		}
		getFields().add(getActualIndex(col), field);
		return this;
	}

	public DynamicDataSet insertRow(int row) throws Exception {
		throw new Exception("本数据集实现不支持insertRow操作");
	}
	
	public GroupDataSet subGroup(int beginIndex, int endIndex) throws Exception{
		List<DynamicDataSet> newDataSetList = new ArrayList<DynamicDataSet>();
		
		for(DynamicDataSet subDs:subDataSetList){
			newDataSetList.add(DataSetUtil.createDynamicDataSet(subDs, beginIndex, endIndex));
		}
		return new DefaultGroupDataSet(getFields(),newDataSetList);
	}
	
	public GroupDataSet subGroup(int beginIndex) throws Exception {
		List<DynamicDataSet> newDataSetList = new ArrayList<DynamicDataSet>();
		for(DynamicDataSet subDs:subDataSetList){
			newDataSetList.add(DataSetUtil.createDynamicDataSet(subDs, beginIndex, subDs.getRows()-1));
		}
		return new DefaultGroupDataSet(getFields(),newDataSetList);
	}

	public List<DynamicDataSet> getGroups() {
		return subDataSetList;
	}

	public int getCurrentRow() throws Exception {
		return getShowIndex(current);
	}
	
	public void setIndexFromOne(boolean tag) {
		super.setIndexFromOne(tag);
		for(DynamicDataSet subDataSet:subDataSetList){
			subDataSet.setIndexFromOne(tag);
		}
	}
	
	public void createAggregateResult(String aggregateName) {
		AggregateResult result = new AggregateResult(aggregateName,subDataSetList.size());
		aggregateResultList.add(result);
	}
	
	public Object clone() throws CloneNotSupportedException{
		DefaultGroupDataSet groupDataSet = (DefaultGroupDataSet) super.clone();
		List<DynamicDataSet> dataSetList = new ArrayList<DynamicDataSet>();
		for(DynamicDataSet subDataSet:subDataSetList){
			DynamicDataSet newDataSet = (DynamicDataSet)subDataSet.cloneDataSet();
			dataSetList.add(newDataSet);
		}
		List<AggregateResult>  newResultList = new ArrayList<AggregateResult>();
		for(AggregateResult result:aggregateResultList){
			newResultList.add(result);
		}
		groupDataSet.subDataSetList = dataSetList;
		groupDataSet.aggregateResultList = newResultList;
		return groupDataSet;
	}

	public List<AggregateResult> getAggregateResultList() throws Exception {
		return aggregateResultList;
	}

	public DataSet merge() throws Exception {
		List<Field> newFields = new ArrayList<Field>();
		for(Field field:getFields()){
			newFields.add(field);
		}
		Object[][] dataArray = new Object[getRows()][];
		for(int row=0;row<getRows();row++){
			dataArray[row] = new Object[newFields.size()];
			for(int col=0;col<newFields.size();col++){
				dataArray[row][col] = getData(getShowIndex(row), getShowIndex(col));
			}
		}
		return DataSetUtil.createDynamicDataSet(newFields, dataArray,isIndexFromOne());
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DynamicDataSet sort(Comparator c) throws Exception{
		List<Object[]> sortList = new ArrayList<Object[]>(); //排序中间对象
		for(int i=0;i<subDataSetList.size();i++){
			Object[] rowInfo = new Object[getColumns()+1]; //多一位记录原始顺序
			for(int j=0;j<getColumns();j++){
				rowInfo[j] = getData(getShowIndex(i), getShowIndex(j));
			}
			rowInfo[getColumns()] = i;
			sortList.add(rowInfo);
		}
		//执行排序
		Collections.sort(sortList, c);
		List<DynamicDataSet> newSubDataSetList = new ArrayList<DynamicDataSet>();
		List<AggregateResult> newAggregateResultList = new ArrayList<AggregateResult>(); 
		for(int i=0;i<aggregateResultList.size();i++){
			AggregateResult result = aggregateResultList.get(i);
			AggregateResult newResult = new AggregateResult(result.getName(),subDataSetList.size());
			newAggregateResultList.add(newResult);
		}
		for(int i=0;i<sortList.size();i++){
			int oldOrder = (Integer)sortList.get(i)[getColumns()];
			//设置普通字段数据
			newSubDataSetList.add(subDataSetList.get(oldOrder));
			//设置聚合字段数据
			for(int j=0;j<aggregateResultList.size();j++){
				AggregateResult result = aggregateResultList.get(j);
				AggregateResult newResult = newAggregateResultList.get(j);
				newResult.setData(i, result.getData(oldOrder));
			}
		}
		//替换结果
		subDataSetList = newSubDataSetList;
		aggregateResultList = newAggregateResultList;
		return this;
	}
	
}
