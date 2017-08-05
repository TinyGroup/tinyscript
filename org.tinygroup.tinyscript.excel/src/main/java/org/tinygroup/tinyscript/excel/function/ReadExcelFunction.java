package org.tinygroup.tinyscript.excel.function;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.excel.SheetDataSet;
import org.tinygroup.tinyscript.excel.util.ExcelUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

/**
 * 读取excel的函数
 * @author yancheng11334
 *
 */
public class ReadExcelFunction extends AbstractScriptFunction {

	public String getNames() {
		return "readExcel";
	}


	public Object execute(ScriptSegment segment, ScriptContext context,
			Object... parameters) throws ScriptException {
		try{
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException("readExcel函数的参数为空!");
			}else if(parameters.length == 1 && parameters[0]!=null){
				return readExcel((String)parameters[0],null,context);
			}else if(parameters.length == 2 && parameters[0]!=null && parameters[1]!=null){
				return readExcel((String)parameters[0],(String)parameters[1],context);
			}else{
				throw new ScriptException("readExcel函数的参数格式不正确!");
			}
		}catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException("readExcel函数的参数格式不正确!", e);
		}
	}
	
	private DataSet readExcel(String file,String name,ScriptContext context) throws Exception{
		DataSet dataSet = extractDataSet(file,name);
		dataSet.setIndexFromOne(getScriptEngine().isIndexFromOne());
		return dataSet;
	}
	
	public DataSet extractDataSet(String file,String name)
			throws Exception {
		Workbook wb = null;
		FileObject fileObject = null;
		try {
			fileObject = VFS.resolveFile(file);
            wb = ExcelUtil.readWorkbook(fileObject);
        
            if(name!=null){
            	for (int i = 0; i < wb.getNumberOfSheets(); i++) {
    				Sheet sheet = wb.getSheetAt(i);
    				if(name.equals(sheet.getSheetName())){
    				   DataSet dataSet = createDataSet(sheet);
    				   return dataSet; 
    				}
    			}	
            	return null;
            }else{
            	Sheet sheet = wb.getSheetAt(0);
            	DataSet dataSet = createDataSet(sheet);
				return dataSet; 
            }
			

		} catch (Exception e) {
			throw new Exception(
					String.format("抽取excel文件%s，发生异常:", file ), e);
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
		SheetDataSet dataSet = new SheetDataSet(sheet,null);
		dataSet.setName(sheet.getSheetName());
		return dataSet;
	}
	

}
