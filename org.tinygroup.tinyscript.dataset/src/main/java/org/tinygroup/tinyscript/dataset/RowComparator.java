package org.tinygroup.tinyscript.dataset;

public interface RowComparator {
	
	public boolean isEqual(DataSetRow o1, DataSetRow o2);

	public int countHash(DataSetRow row);

}
