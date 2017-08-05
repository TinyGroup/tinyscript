package org.tinygroup.tinyscript.database;

import javax.sql.RowSet;

/**
 * Created by luoguo on 2014/7/4.
 */
public class RowSetDataSet extends ResultSetDataSet {
	public RowSetDataSet(RowSet rowSet) {
		super(rowSet);
	}
}
