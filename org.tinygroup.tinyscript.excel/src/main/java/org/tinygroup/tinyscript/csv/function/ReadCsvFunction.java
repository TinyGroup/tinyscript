package org.tinygroup.tinyscript.csv.function;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.csv.CSVFormat;
import org.tinygroup.tinyscript.csv.CSVParser;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.FileObjectUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.vfs.FileObject;

/**
 * 读取csv格式文件
 * 
 * @author yancheng11334
 *
 */
public class ReadCsvFunction extends AbstractScriptFunction {

	public String getNames() {
		return "readCsv";
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (checkParameters(parameters, 1)) {
				return readCsv((String) parameters[0], "utf-8", context);
			} else if (checkParameters(parameters, 2)) {
				return readCsv((String) parameters[0], (String) parameters[1], context);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private DataSet readCsv(String file, String encode, ScriptContext context) throws Exception {
		DataSet dataSet = extractDataSet(file, encode);
		dataSet.setIndexFromOne(getScriptEngine().isIndexFromOne());
		return dataSet;
	}

	public DataSet extractDataSet(String file, String encode) throws Exception {
		FileObject fileObject = null;
		BufferedReader reader = null;
		try {
			fileObject = FileObjectUtil.findFileObject(file, false);
			reader = new BufferedReader(new InputStreamReader(fileObject.getInputStream(), encode));
			CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT);
			return parser.extractDataSet();
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("excel", "file.find.error", file), e);
		} finally {
			if (reader != null) {
				reader.close();
			}
			if (fileObject != null) {
				fileObject.clean();
			}
		}
	}

}
