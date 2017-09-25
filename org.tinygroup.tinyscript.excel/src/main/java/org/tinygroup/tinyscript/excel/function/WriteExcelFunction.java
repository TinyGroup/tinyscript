package org.tinygroup.tinyscript.excel.function;

import java.io.IOException;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.FileObjectUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.tinyscript.interpret.exception.NotMatchException;
import org.tinygroup.vfs.FileObject;

public class WriteExcelFunction extends AbstractScriptFunction {

	@Override
	public String getNames() {
		return "writeExcel";
	}

	@Override
	public String getBindingTypes() {
		return DataSet.class.getName() + "," + List.class.getName();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

			switch (parameters.length) {
			case 2:
				writeExcel((String) parameters[1], parameters[0], null, 1, -1);
				break;
			case 3:
				if (parameters[2] instanceof List) {
					writeExcel((String) parameters[1], parameters[0], (List<String>) parameters[2], 1, -1);
				} else {
					writeExcel((String) parameters[1], parameters[0], null, (Integer) parameters[2], -1);
				}
				break;
			case 4:
				if (parameters[2] instanceof List) {
					writeExcel((String) parameters[1], parameters[0], (List<String>) parameters[2],
							(Integer) parameters[3], -1);
				} else {
					writeExcel((String) parameters[1], parameters[0], null, (Integer) parameters[2],
							(Integer) parameters[3]);
				}
				break;
			case 5:
				writeExcel((String) parameters[1], parameters[0], (List<String>) parameters[2], (Integer) parameters[3],
						(Integer) parameters[4]);
				break;
			default:
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

			return null;
		} catch (ClassCastException e) {
			throw new NotMatchException(e);
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private void writeExcel(String fileUrl, Object dataSet, List<String> sheetNames, int start, int end)
			throws Exception {
		Workbook wb = null;
		FileObject excelFile;

		try {
			excelFile = FileObjectUtil.getOrCreateFile(fileUrl, false);
		} catch (IOException e) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("excel", "file.create.error", fileUrl));
		}

		if (fileUrl.endsWith("xls")) {
			wb = new HSSFWorkbook();
		} else if (fileUrl.endsWith("xlsx")) {
			wb = new XSSFWorkbook();
		} else {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("excel", "file.type.nonsupport"));
		}

		if (dataSet instanceof DataSet) {
			writeSheet(wb, (DataSet) dataSet,
					(sheetNames == null || sheetNames.size() == 0) ? "sheet1" : sheetNames.get(0), start,
					checkEnd((DataSet) dataSet, end));
		} else if (dataSet instanceof List) {

			List<?> dataSets = (List<?>) dataSet;

			for (int i = 0; i < dataSets.size(); i++) {

				String sheetName = null;
				if (sheetNames != null && sheetNames.size() > i) {
					sheetName = sheetNames.get(i);
				} else {
					sheetName = "sheet" + (i + 1);
				}

				try {
					writeSheet(wb, (DataSet) dataSets.get(i), sheetName, start,
							checkEnd((DataSet) dataSets.get(i), end));
				} catch (ClassCastException e) {
					throw new NotMatchException(e);
				} catch (ScriptException e1) {
					throw e1;
				}

			}
		}

		wb.write(excelFile.getOutputStream());
		wb.close();

	}

	private void writeSheet(Workbook wb, DataSet dataSet, String sheetName, int start, int end) throws Exception {
		Sheet sheet = (Sheet) wb.createSheet(sheetName);
		CellStyle style = wb.createCellStyle();

		style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);// 垂直
		style.setAlignment(CellStyle.ALIGN_CENTER);// 水平
		style.setWrapText(true);// 指定当单元格内容显示不下时自动换行

		Row row = sheet.createRow(0);
		Cell cell = null;
		for (int i = 0; i < dataSet.getFields().size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(dataSet.getFields().get(i).getName());
			cell.setCellStyle(style);
		}

		for (int i = 1 + start; i <= 1 + end; i++) {
			row = sheet.createRow(i - start);
			for (int j = 1; j <= dataSet.getFields().size(); j++) {
				cell = row.createCell(j - 1);
				try {
					cell.setCellValue(dataSet.getData(i, j).toString());
				} catch (Exception e) {
					throw new ScriptException(ResourceBundleUtil.getResourceMessage("excel", "write.cell.error", i, j));
				}
				cell.setCellStyle(style);
			}
		}
	}

	private int checkEnd(DataSet dataSet, int end) throws Exception {
		// 因为sheetdataset的data包括标题，所以计算rows时需要减一
		return end == -1 ? dataSet.getRows() - 1 : end <= dataSet.getRows() - 1 ? end : dataSet.getRows() - 1;
	}
}
