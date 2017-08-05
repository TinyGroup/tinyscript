package org.tinygroup.tinyscript.excel;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.excel.util.ExcelUtil;

/**
 * excel结果集
 * 
 * @author yancheng11334
 * 
 */
public class SheetDataSet extends AbstractDataSet {

	private Sheet sheet;
	private int currentRow = -1; //对应实际下标，而非显示下标
	private XlsArea xlsArea;

	public SheetDataSet(Sheet sheet,XlsArea xlsArea) throws Exception {
		this.sheet = sheet;
		this.xlsArea = xlsArea;

		// 验证
		checkValidate();

		// 生成field
		List<Field> fields = new ArrayList<Field>();
		
		Row row = sheet.getRow(getFirstRowNum());
		for (int i = getFirstCellNum(row); i < getLastCellNum(row); i++) {
			Cell nameCell = row.getCell(i);
			fields.add(createField(nameCell));
		}

		super.setFields(fields);
		currentRow = sheet.getFirstRowNum();
	}

	private void checkValidate() throws Exception {
		if (sheet == null || sheet.getPhysicalNumberOfRows() < 1) {
			throw new Exception("没有数据存在，无法移动到第一行！");
		}
	}
	
	private int getFirstRowNum(){
		if(xlsArea!=null && xlsArea.getDataY()>0){
		   return xlsArea.getDataY();
		}
		return sheet.getFirstRowNum();
	}
	
	private int getLastRowNum(){
		if(xlsArea!=null && xlsArea.getDataY()>0 && xlsArea.getHeight()>0){
		   return xlsArea.getDataY()+xlsArea.getHeight();
		}
		return sheet.getLastRowNum();
	}
	
	private int getFirstCellNum(Row row){
		if(xlsArea!=null && xlsArea.getDataX()>0){
		  return xlsArea.getDataX();
		}
		return row.getFirstCellNum();
	}
	
	private int getLastCellNum(Row row){
		if(xlsArea!=null && xlsArea.getDataX()>0 && xlsArea.getWidth()>0){
		  return xlsArea.getDataX()+xlsArea.getWidth();
		}
		return row.getLastCellNum();
	}

	/**
	 * 本方法私有，对应的参数指实际下标
	 * @param row
	 * @param col
	 * @return
	 * @throws Exception
	 */
	private Cell getCell(int row, int col) throws Exception {
		try {
			return sheet.getRow(row).getCell(col);
		} catch (Exception e) {
			throw new Exception(String.format("查询第%d行,第%d列数据失败:", row, col),
					e);
		}
	}

	private Field createField(Cell nameCell) {
		Field field = new Field();
		field.setName(nameCell.getStringCellValue());
		field.setType(String.class.getName());
		return field;
	}

	

	public boolean isReadOnly() {
		return false;
	}

	public void first() throws Exception {
		checkValidate();
		currentRow = getFirstRowNum();
	}

	public boolean previous() throws Exception {
		checkValidate();
		if (currentRow == getFirstRowNum()) {
			throw new Exception("已经移动到第一行，无法继续前移！");
		}
		currentRow--;
		return true;
	}

	public void beforeFirst() throws Exception {
		checkValidate();
		currentRow = getFirstRowNum();
	}

	public void afterLast() throws Exception {
		checkValidate();
		currentRow = getLastRowNum();
	}

	public boolean next() throws Exception {
		checkValidate();
		if (currentRow == getLastRowNum()) {
			return false;
		}
		currentRow++;
		return true;
	}

	public boolean absolute(int row) throws Exception {
		checkValidate();
		if(isIndexFromOne()){
			if (row-1 < getFirstRowNum() || row-1 >= getLastRowNum()) {
				throw new Exception(String.format("指定的行数%s不合法或越界!", row));
			}
		}else{
			if (row < getFirstRowNum() || row >= getLastRowNum()) {
				throw new Exception(String.format("指定的行数%s不合法或越界!", row));
			}
		}
		currentRow = getActualIndex(row);
		return true;
	}

	public int getRows() throws Exception {
		checkValidate();
		return sheet.getPhysicalNumberOfRows();
	}

	public int getColumns() throws Exception {
		if (getFields() == null) {
			throw new Exception("当前记录集没有定义字段相关数据！");
		}
		return getFields().size();
	}

	public <T> T getData(int row, int col) throws Exception {
		row = getActualIndex(row);
		col = getActualIndex(col);
		return (T) ExcelUtil.getCellValue(getCell(row, col));
	}

	public <T> void setData(int row, int col, T data) throws Exception {
		row = getActualIndex(row);
		col = getActualIndex(col);
		Cell cell = getCell(row, col);
		if(cell==null){
		   sheet.getRow(row).createCell(col, Cell.CELL_TYPE_STRING);
		   cell = getCell(row, col);
		}
		if(data==null){
		   cell.setCellType(Cell.CELL_TYPE_BLANK);
		}else if(data.getClass().equals(String.class)){
		   cell.setCellType(Cell.CELL_TYPE_STRING);
		   cell.setCellValue(data.toString());
		}else {
		   cell.setCellType(Cell.CELL_TYPE_STRING);
		   cell.setCellValue(String.valueOf(data));
		}
	}

	public <T> T getData(int col) throws Exception {
		return getData(getShowIndex(currentRow), col);
	}

	public <T> void setData(int col, T data) throws Exception {
		setData(getShowIndex(currentRow), col, data);
	}

	public void clean() {
		super.clean();
		sheet = null;
	}

}
