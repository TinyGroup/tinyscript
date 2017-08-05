package org.tinygroup.tinyscript.excel.util;

import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tinygroup.vfs.FileObject;

/**
 * excel辅助工具类
 * @author yancheng11334
 *
 */
public class ExcelUtil {

	public static final String XLSX_FILE_NAME = ".xlsx";
	public static final String XLTX_FILE_NAME = ".xltx";

	/**
	 * 根据模板加载excel
	 * @param fileObject
	 * @return
	 * @throws Exception
	 */
	public static  Workbook readWorkbook(FileObject fileObject) throws Exception {
		Workbook wb = null;
		try {
			String fileName = fileObject.getFileName().toLowerCase();
			if(fileName.endsWith(XLSX_FILE_NAME) || fileName.endsWith(XLTX_FILE_NAME)){
				wb = new XSSFWorkbook(fileObject.getInputStream());
			}else{
				wb = new HSSFWorkbook(fileObject.getInputStream());
			}
			
		} catch (IOException e) {
            throw new Exception(String.format("解析excel文件%s，发生异常",fileObject.getFileName()),e);
		}
		return wb;
	}
	
	/**
	 * 创建新的excel
	 * @param fileObject
	 * @return
	 * @throws Exception
	 */
	public static  Workbook createWorkbook(FileObject fileObject) throws Exception {
		Workbook wb = null;
		String fileName = fileObject.getFileName().toLowerCase();
		if (fileName.endsWith(XLSX_FILE_NAME) || fileName.endsWith(XLTX_FILE_NAME)) {
			wb = new XSSFWorkbook();
		} else {
			wb = new HSSFWorkbook();
		}
		return wb;
	}
	
	/**
	 * 复制行
	 * @param srcRow
	 * @param dstRow
	 * @throws Exception
	 */
	public static void copyRow(Row srcRow,Row dstRow) throws Exception {
//		CellStyle srcSylte = srcRow.getRowStyle();
//		CellStyle dstStyle = dstRow.getRowStyle();
//		dstStyle.cloneStyleFrom(srcSylte);
//		dstRow.setRowStyle(dstStyle);
//		dstRow.setHeight(srcRow.getHeight());
//		dstRow.setHeightInPoints(srcRow.getHeightInPoints());
//		dstRow.setRowNum(srcRow.getRowNum());
//		dstRow.setZeroHeight(srcRow.getZeroHeight());
		if(srcRow==null || dstRow==null){
		   return;
		}
		for(int j=srcRow.getFirstCellNum();j<srcRow.getLastCellNum();j++){
			Cell srcCell = srcRow.getCell(j);
			Cell dstCell = dstRow.createCell(j);
			copyCell(srcCell, dstCell);
		}
	}
	
	/**
	 * 复制单元格
	 * @param srcCell
	 * @param dstCell
	 * @throws Exception
	 */
	public static void copyCell(Cell srcCell,Cell dstCell) throws Exception {
		CellStyle srcSylte = srcCell.getCellStyle();
		CellStyle dstStyle = dstCell.getRow().getSheet().getWorkbook().createCellStyle();
		//CellStyle dstStyle = dstCell.getCellStyle();
		dstStyle.cloneStyleFrom(srcSylte);
		dstCell.setCellStyle(dstStyle);
		dstCell.setCellType(Cell.CELL_TYPE_STRING);
		dstCell.setCellValue(ExcelUtil.getCellValue(srcCell));
	}
	
	public static String getCellValue(Cell cell) {
		String value = "";
		if(cell==null){
		   return value;	
		}
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_NUMERIC: // 数值型
			if (DateUtil.isCellDateFormatted(cell)) {
				// 如果是date类型则 ，获取该cell的date值
				value = DateUtil.getJavaDate(cell.getNumericCellValue())
						.toString();
			} else {// 纯数字
				value = String.valueOf(cell.getNumericCellValue());
			}
			break;
		/* 此行表示单元格的内容为string类型 */
		case Cell.CELL_TYPE_STRING: // 字符串型
			value = cell.getRichStringCellValue().toString();
			break;
		case Cell.CELL_TYPE_FORMULA:// 公式型
			// 读公式计算值
			value = String.valueOf(cell.getNumericCellValue());
			if (value.equals("NaN")) {// 如果获取的数据值为非法值,则转换为获取字符串
				value = cell.getRichStringCellValue().toString();
			}
			// cell.getCellFormula();读公式
			break;
		case Cell.CELL_TYPE_BOOLEAN:// 布尔
			value = String.valueOf(cell.getBooleanCellValue());
			break;
		/* 此行表示该单元格值为空 */
		case Cell.CELL_TYPE_BLANK: // 空值
			value = "";
			break;
		case Cell.CELL_TYPE_ERROR: // 故障
			value = "";
			break;
		default:
			value = cell.getRichStringCellValue().toString();
		}
		return value;
	}
}
