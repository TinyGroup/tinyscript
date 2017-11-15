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
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 多级分组序表
 * @author yancheng11334
 *
 */
public class MultiLevelGroupDataSet extends GroupDataSet {

	private static final String AGGREGATE_TYPE="aggregate";
	
	/**
	 * 源序表
	 */
	private DynamicDataSet  source;
	
	/**
	 * 分组序表的序列
	 */
	private List<DynamicDataSet> subDataSetList = new ArrayList<DynamicDataSet>();
	
	private int currentRow = -1;
	
	private boolean groupTag = false;
	
    private MultiLevelGroupDataSet parent;
	
	private List<AggregateResult> aggregateResultList = new ArrayList<AggregateResult>(); // 聚合结果
	
	/**
	 * 未分组的构造函数
	 * @param dataSet
	 */
	public MultiLevelGroupDataSet(DynamicDataSet dataSet){
		currentRow = 0;
		source = dataSet;
		setFields(new ArrayList<Field>(source.getFields()));
		setIndexFromOne(source.isIndexFromOne());
	}
	
	/**
	 * 分组的构造函数
	 * @param dataSet
	 * @param dataSetList
	 */
	public MultiLevelGroupDataSet(DynamicDataSet dataSet,List<DynamicDataSet> dataSetList){
		this(dataSet);
		setGroups(dataSetList);
		groupTag = true;
	}
	
	
	public void setGroups(List<DynamicDataSet> dataSetList){
		subDataSetList.clear();
		if(dataSetList!=null){
		   for(DynamicDataSet dataSet:dataSetList){
			   if(dataSet instanceof MultiLevelGroupDataSet){
				   MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
				   multiLevelGroupDataSet.parent = this;
				   subDataSetList.add(dataSet);
			   }else{
				   MultiLevelGroupDataSet multiLevelGroupDataSet = new MultiLevelGroupDataSet(dataSet);
				   multiLevelGroupDataSet.parent = this;
				   subDataSetList.add(multiLevelGroupDataSet);
			   }
		   }
		}
		groupTag = true;
	}
	
	public MultiLevelGroupDataSet getParent(){
		return parent;
	}
	
	public DynamicDataSet getSource(){
		return source;
	}
	
	/**
	 * 判断当前序表是否进行分组
	 * @return
	 */
	public boolean isGrouped(){
		return groupTag;
	}
	
	public int getLevel() {
		MultiLevelGroupDataSet  p = parent;
		int level = 0;
		while(p!=null){
			p = p.parent;
			level++;
		}
		return level;
	}

	private AggregateResult getAggregateResult(String name){
		for(AggregateResult result:aggregateResultList){
			if(name!=null && name.equals(result.getName())){
			   return result;
			}
		}
		return null;
	}
	
	public boolean isReadOnly() {
		return false;
	}

	public void first() throws Exception {
		if(isGrouped()){
			currentRow = 0;
		}else{
			source.first();
		}
	}

	public boolean previous() throws Exception {
		if(isGrouped()){
			if(currentRow>0){
			   currentRow--;
			   return true;
			}
			return false;
		}else{
			return source.previous();
		}
		
	}

	public void beforeFirst() throws Exception {
		if(isGrouped()){
			throw new Exception(
					ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "beforeFirst"));
		}else{
			source.beforeFirst();
		}
		
	}

	public void afterLast() throws Exception {
		if(isGrouped()){
			throw new Exception(ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "afterLast"));
		}else{
			source.afterLast();
		}
	}

	public boolean next() throws Exception {
		if(isGrouped()){
		   if(currentRow<subDataSetList.size()-1){
			  currentRow++;
			  return true;
		   }
           return false;
		}else{
		   return source.next();
		}
		
	}

	public boolean absolute(int row) throws Exception {
		if(isGrouped()){
		   int temp = getActualIndex(row);
		   if(temp>=0 && temp<=subDataSetList.size() - 1){
			  currentRow = temp;
			  return true;
		   }
		   return false;
		}else{
		   return source.absolute(row);
		}
		
	}

	public int getRows() throws Exception {
		if(isGrouped()){
		   return subDataSetList.size();
		}else{
		   return source.getRows();
		}
		
	}

	public int getColumns() throws Exception {
		if(isGrouped()){
		   return getFields().size();
		}else{
		   return source.getColumns();
		}
		
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(int row, int col) throws Exception {
		if(isGrouped()){
		   int actualCol = getActualIndex(col);
		   int actualRow = getActualIndex(row);
		   Field field = getFields().get(actualCol);
		   if(AGGREGATE_TYPE.equals(field.getType())){
			  //聚合字段
			  return (T) getAggregateResult(field.getName()).getData(actualRow);
		   }else{
			  //非聚合字段 
			  DynamicDataSet subDataSet = subDataSetList.get(actualRow);
			  return subDataSet.getData(subDataSet.getShowIndex(0), col);
		   }
		}else{
		   return source.getData(row, col);
		}
	}

	public <T> void setData(int row, int col, T data) throws Exception {
		if(isGrouped()){
		   int actualCol = getActualIndex(col);
		   int actualRow = getActualIndex(row);
		   Field field = getFields().get(actualCol);
		   if(AGGREGATE_TYPE.equals(field.getType())){
			  //聚合字段   
			  getAggregateResult(field.getName()).setData(actualRow, data);
		   }else{
			  //非聚合字段   
			  DynamicDataSet subDataSet = subDataSetList.get(actualRow);
			  subDataSet.setData(subDataSet.getShowIndex(0), col, data);
		   }
		}else{
		   source.setData(row, col, data);
		}
		
	}

	public <T> T getData(int col) throws Exception {
		return getData(getShowIndex(currentRow), col);
	}

	public <T> void setData(int col, T data) throws Exception {
		setData(getShowIndex(currentRow), col, data);
	}

	public List<DynamicDataSet> getGroups() {
		return  subDataSetList;
	}
	
	/**
	 * 获得指定级别的序表子集
	 * @param level
	 * @return
	 */
	public List<MultiLevelGroupDataSet> getGroups(int level){
		List<MultiLevelGroupDataSet> list = new ArrayList<MultiLevelGroupDataSet>();
		getGroups(this,level,list);
		return list;
	}

		
	
	/**
	 * 获得未排序的序表子集
	 * @param ds
	 * @return
	 */
	public List<MultiLevelGroupDataSet> getUnGroups(){
		List<MultiLevelGroupDataSet> list = new ArrayList<MultiLevelGroupDataSet>();
		getGroups(this,list);
		return list;
	}
	
	private void getGroups(MultiLevelGroupDataSet ds,int level,List<MultiLevelGroupDataSet> list){
		if(ds.getLevel()==level){
		   list.add(ds);
		}else if( ds.getLevel()<level){
		   for(DynamicDataSet dataSet:ds.getGroups()){
			   MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
			   getGroups(multiLevelGroupDataSet,level,list);
		   }
		}
	}
	
	private void getGroups(MultiLevelGroupDataSet ds,List<MultiLevelGroupDataSet> list){
		if(ds.isGrouped()){
		  for(DynamicDataSet dataSet:ds.getGroups()){
			  MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
			  getGroups(multiLevelGroupDataSet,list);
		  }
		}else{
		   list.add(ds);
		}
	}

	public void createAggregateResult(String aggregateName) {
		AggregateResult result = new AggregateResult(aggregateName, subDataSetList.size());
		aggregateResultList.add(result);
		Field aggregateField = new Field(aggregateName,aggregateName,AGGREGATE_TYPE);
		getFields().add(aggregateField);
	}

	@SuppressWarnings("unchecked")
	public <T> T getData(int row, String aggregateName) {
		return (T) getAggregateResult(aggregateName).getData(getActualIndex(row));
	}

	public <T> void setData(int row, String aggregateName, T value) {
		getAggregateResult(aggregateName).setData(getActualIndex(row), value);
	}

	public GroupDataSet subGroup(int beginIndex, int endIndex) throws Exception {
		List<DynamicDataSet> newDataSetList = new ArrayList<DynamicDataSet>();
		for(DynamicDataSet dataSet:subDataSetList){
		   MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
		   newDataSetList.add(DataSetUtil.createDynamicDataSet(multiLevelGroupDataSet.getSource(), beginIndex, endIndex));
		}
		MultiLevelGroupDataSet multiLevelGroupDataSet = new MultiLevelGroupDataSet(source,newDataSetList);
		return multiLevelGroupDataSet;
	}

	public GroupDataSet subGroup(int beginIndex) throws Exception {
		List<DynamicDataSet> newDataSetList = new ArrayList<DynamicDataSet>();
		for(DynamicDataSet dataSet:subDataSetList){
		   MultiLevelGroupDataSet multiLevelGroupDataSet = (MultiLevelGroupDataSet) dataSet;
		   newDataSetList.add(DataSetUtil.createDynamicDataSet(multiLevelGroupDataSet.getSource(), beginIndex, multiLevelGroupDataSet.getRows()-1));
		}
		MultiLevelGroupDataSet multiLevelGroupDataSet = new MultiLevelGroupDataSet(source,newDataSetList);
		return multiLevelGroupDataSet;
	}

	public List<AggregateResult> getAggregateResultList() throws Exception {
		return aggregateResultList;
	}
	
	/**
	 * 底层API已经实现分组和未分组两种场景，所以可以直接使用统一接口创建新的序表
	 * @return
	 * @throws Exception
	 */
	private  DataSet createDataSet() throws Exception {
		List<Field> newFields = new ArrayList<Field>();
		for (Field field : getFields()) {
			newFields.add(field);
		}
		int row = getRows();
		int col = getColumns();
		Object[][] dataArray = new Object[row][];
		for(int i=0;i<row;i++){
			dataArray[row] = new Object[col];
			for(int j=0;j<col;j++){
				dataArray[i][j] = getData(getShowIndex(i), getShowIndex(j));
			}
		}
		return DataSetUtil.createDynamicDataSet(newFields, dataArray, isIndexFromOne());
	}

	public DataSet merge() throws Exception {
		return createDataSet();
	}

	public DynamicDataSet deleteColumn(int col) throws Exception {
		int actualCol = getActualIndex(col);
		if(isGrouped()){
		   Field field = getFields().get(actualCol);
		   if(AGGREGATE_TYPE.equals(field.getType())){
			  //聚合列
			  AggregateResult result = getAggregateResult(field.getName());
			  aggregateResultList.remove(result);
		   }else{
			  //非聚合列   
			  for(DynamicDataSet dataSet:subDataSetList){
				  dataSet.deleteColumn(col);
			  }
		   }
		}else{
		   source.deleteColumn(col);
		}
		getFields().remove(actualCol);
		return this;
	}

	public DynamicDataSet deleteColumn(String colName) throws Exception {
		int actualCol = getColumn(colName);
		return deleteColumn(getShowIndex(actualCol));
	}

	public DynamicDataSet deleteRow(int row) throws Exception {
		throw new Exception(ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "deleteRow"));
	}

	public DynamicDataSet insertColumn(int col, Field field) throws Exception {
		int actualCol = getActualIndex(col);
		if(isGrouped()){
			for (DynamicDataSet subDs : subDataSetList) {
			   subDs.insertColumn(col, field);
			}
		}else{
			source.insertColumn(col, field);
		}
		getFields().add(actualCol, field);
		return this;
	}

	public DynamicDataSet insertRow(int row) throws Exception {
		throw new Exception(ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "insertRow"));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DynamicDataSet sort(Comparator c) throws Exception {
		if(isGrouped()){
		   List<Object[]> sortList = new ArrayList<Object[]>(); // 排序中间对象
		   for (int i = 0; i < subDataSetList.size(); i++) {
			   Object[] rowInfo = new Object[getColumns() + 1]; // 多一位记录原始顺序
			   for (int j = 0; j < getColumns(); j++) {
					rowInfo[j] = getData(getShowIndex(i), getShowIndex(j));
			   }
			   rowInfo[getColumns()] = i;
			   sortList.add(rowInfo);
		   }
		   // 执行排序
		   Collections.sort(sortList, c);
		   List<DynamicDataSet> newSubDataSetList = new ArrayList<DynamicDataSet>();
		   List<AggregateResult> newAggregateResultList = new ArrayList<AggregateResult>();
		   for (int i = 0; i < aggregateResultList.size(); i++) {
				AggregateResult result = aggregateResultList.get(i);
				AggregateResult newResult = new AggregateResult(result.getName(), subDataSetList.size());
				newAggregateResultList.add(newResult);
		   }
		   for (int i = 0; i < sortList.size(); i++) {
				int oldOrder = (Integer) sortList.get(i)[getColumns()];
				// 设置普通字段数据
				newSubDataSetList.add(subDataSetList.get(oldOrder));
				// 设置聚合字段数据
				for (int j = 0; j < aggregateResultList.size(); j++) {
					AggregateResult result = aggregateResultList.get(j);
					AggregateResult newResult = newAggregateResultList.get(j);
					newResult.setData(i, result.getData(oldOrder));
				}
			}
			// 替换结果
			subDataSetList = newSubDataSetList;
			aggregateResultList = newAggregateResultList;
		}else{
		   source.sort(c);
		}
		return this;
	}

	public int getCurrentRow() throws Exception {
		if(isGrouped()){
			return getShowIndex(currentRow);
		}else{
		    return source.getCurrentRow();
		}
	}

}
