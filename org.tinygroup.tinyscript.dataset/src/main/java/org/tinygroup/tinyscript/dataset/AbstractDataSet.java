package org.tinygroup.tinyscript.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 抽象数据集 Created by luoguo on 2014/7/4.
 */
public abstract class AbstractDataSet implements DataSet {
	/**
	 * 字段
	 */
	protected List<Field> fields = new ArrayList<Field>();
	private transient Map<String, Integer> columnIndex = new HashMap<String, Integer>();
	private String name;
	protected Logger logger = LoggerFactory.getLogger(AbstractDataSet.class);
	private boolean indexFromOne;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	protected void throwNotSupportMethod() throws Exception {
		throw new Exception(ResourceBundleUtil.getResourceMessage("dataset.onlyread.error", "dataset.fields.notfound"));
	}

	protected Integer getColumn(String fieldName) {
		return columnIndex.get(fieldName.toUpperCase());
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
		columnIndex.clear();
		for (int i = 0; i < fields.size(); i++) {
			columnIndex.put(fields.get(i).getName().toUpperCase(), i);
		}
	}

	public <T> T getData(String fieldName) throws Exception {
		Integer index = getColumn(fieldName);
		if (index == null) {
			throw new Exception(ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.notfound", fieldName));
		}
		return getData(getShowIndex(index));
	}

	public <T> void setData(String fieldName, T data) throws Exception {
		Integer index = getColumn(fieldName);
		if (index == null) {
			throw new Exception(ResourceBundleUtil.getResourceMessage("dataset", "dataset.fields.notfound", fieldName));
		}
		setData(getShowIndex(index), data);
	}

	public void clean() {
		if (fields != null) {
			fields.clear();
			columnIndex.clear();
			fields = null;
			columnIndex = null;
		}
	}

	public DataSet cloneDataSet() throws CloneNotSupportedException {
		throw new CloneNotSupportedException(
				ResourceBundleUtil.getResourceMessage("dataset", "dataset.operate.nosupport", "clone"));
	}

	public boolean isIndexFromOne() {
		return indexFromOne;
	}

	public void setIndexFromOne(boolean tag) {
		this.indexFromOne = tag;
	}

	/**
	 * 根据实际索引获取显示下标(假设实际索引从0开始)
	 * 
	 * @param index
	 * @return
	 */
	public int getShowIndex(int index) {
		return isIndexFromOne() ? index + 1 : index;
	}

	/**
	 * 根据显示下标获取实际索引(假设实际索引从0开始)
	 * 
	 * @param index
	 * @return
	 */
	public int getActualIndex(int index) {
		return isIndexFromOne() ? index - 1 : index;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		try{
			for(Field f:getFields()){
				sb.append(f.getName()).append(" ");
			}
			sb.append("\n");
			for(int i=0;i<getRows();i++){
			   for(int j=0;j<getColumns();j++){
				   sb.append(getData(getShowIndex(i), getShowIndex(j))).append(" ");
			   }
			   sb.append("\n");
			}
			sb.append("\n");
		}catch(Exception e){
			//可能会抛出异常
			throw new RuntimeException(e);
		}
		return sb.toString();
	}
}
