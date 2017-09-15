package org.tinygroup.tinyscript.excel.function;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.excel.SheetDataSet;
import org.tinygroup.tinyscript.excel.util.ExcelUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.FileObjectUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.vfs.FileObject;

/**
 * 读取excel的函数
 * 
 * @author yancheng11334
 *
 */
public class ReadExcelFunction extends AbstractScriptFunction {

	public String getNames() {
		return "readExcel";
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				return readExcel((String) parameters[0], null);
			} else if (checkParameters(parameters, 2)) {
				return readExcel((String) parameters[0], (String) parameters[1]);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private Object readExcel(String file, String name) throws Exception {
		Object dataSet = extractDataSet(file, name);
		return dataSet;
	}

	public Object extractDataSet(String file, String name) throws Exception {
		Workbook wb = null;
		FileObject fileObject = null;
		try {
			fileObject = FileObjectUtil.findFileObject(file, false);
			wb = ExcelUtil.readWorkbook(fileObject);

			if (name != null) {
				for (int i = 0; i < wb.getNumberOfSheets(); i++) {
					Sheet sheet = wb.getSheetAt(i);
					if (name.equals(sheet.getSheetName())) {
						DataSet dataSet = createDataSet(sheet);
						dataSet.setIndexFromOne(getScriptEngine().isIndexFromOne());
						return dataSet;
					}
				}
				return null;
			} else {
				List<DataSet> dataSets = new ArrayList<DataSet>();
				for (int i = 0; i < wb.getNumberOfSheets(); i++) {
					Sheet sheet = wb.getSheetAt(i);
					if (sheet.getPhysicalNumberOfRows() != 0) {
						DataSet dataSet = createDataSet(sheet);
						dataSet.setIndexFromOne(getScriptEngine().isIndexFromOne());
						dataSets.add(dataSet);
					}
				}

				return dataSets;
			}

		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("excel", "file.find.error", file), e);
		} finally {
			if (fileObject != null) {
				fileObject.clean();
			}
			if (wb != null) {
				try {
					wb.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	/**
	 * 通过Sheet页创建结果集
	 * 
	 * @param sheet
	 * @return
	 * @throws Exception
	 */
	private DataSet createDataSet(Sheet sheet) throws Exception {
		SheetDataSet dataSet = new SheetDataSet(sheet, null);
		dataSet.setName(sheet.getSheetName());
		return dataSet;
	}

}
