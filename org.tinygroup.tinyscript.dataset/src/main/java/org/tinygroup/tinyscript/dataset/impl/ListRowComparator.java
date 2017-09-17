package org.tinygroup.tinyscript.dataset.impl;

import java.util.List;

import org.tinygroup.tinyscript.dataset.DataSetRow;
import org.tinygroup.tinyscript.dataset.RowComparator;

public class ListRowComparator implements RowComparator {

	private List<Integer> pks;

	public ListRowComparator(List<Integer> pks) {
		this.pks = pks;
	}

	@Override
	public boolean isEqual(DataSetRow o1, DataSetRow o2) {
		for (Integer i : pks) {
			try {
				if (!(o1.getData(i).equals(o2.getData(i)))) {
					return false;
				}
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int countHash(DataSetRow row){
		int hash = 0;
		for (Integer i : pks) {
			try {
				hash += row.getData(i).toString().hashCode();
			} catch (Exception e) {
				//发生异常跳过当前hash值
			}
		}
		return hash;
	}

}
