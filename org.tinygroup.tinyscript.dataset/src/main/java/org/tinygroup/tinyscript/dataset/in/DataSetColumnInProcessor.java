package org.tinygroup.tinyscript.dataset.in;

import java.util.HashSet;
import java.util.Set;

import org.tinygroup.tinyscript.dataset.DataSetColumn;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.expression.InExpressionProcessor;

public class DataSetColumnInProcessor implements InExpressionProcessor {

	private DataSetColumnCache colCache;

	@Override
	public boolean isMatch(Object collection) throws Exception {
		return collection instanceof DataSetColumn;
	}

	@Override
	public boolean checkIn(Object collection, Object item) throws Exception {
		DataSetColumn colData = (DataSetColumn) collection;
		if (colCache == null || !colCache.getField().equals(colCache.getField())) {
			colCache = new DataSetColumnCache(colData);
		}
		return colCache.checkData(item);
	}

	class DataSetColumnCache {
		private Field field;
		private Set<Object> colCache;

		DataSetColumnCache(DataSetColumn colData) throws Exception {
			colCache = new HashSet<Object>();
			for (int i = 0; i < colData.getRows(); i++) {
				colCache.add(colData.getData(colData.isIndexFromOne() ? i + 1 : i));
			}
			this.field = colData.getField();
		}

		public boolean checkData(Object item) {
			return colCache.contains(item);
		}

		public Field getField() {
			return field;
		}

		public void setField(Field field) {
			this.field = field;
		}

		public Set<Object> getColCache() {
			return colCache;
		}

		public void setColCache(Set<Object> colCache) {
			this.colCache = colCache;
		}
	}
}
