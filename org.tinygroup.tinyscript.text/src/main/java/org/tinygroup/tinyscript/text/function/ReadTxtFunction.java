package org.tinygroup.tinyscript.text.function;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.util.DataSetUtil;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.FileObjectUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.vfs.FileObject;

/**
 * 增加读取char(9)分割记录，char(10)/char(13)换行的文本记录
 * 
 * @author yancheng11334
 *
 */
public class ReadTxtFunction extends AbstractScriptFunction {

	public String getNames() {
		return "readTxt";
	}

	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length == 0) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.empty", getNames()));
			} else if (parameters.length == 1 && parameters[0] != null) {
				return readTxt((String) parameters[0], null, context);
			} else if (parameters.length == 2 && parameters[0] != null && parameters[1] != null) {
				return readTxt((String) parameters[0], (String) parameters[1], context);
			} else {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private DataSet readTxt(String path, String encode, ScriptContext context) throws Exception {
		if (StringUtil.isEmpty(encode)) {
			encode = "utf-8";
		}
		BufferedReader reader = null;
		FileObject fileObject = null;
		try {
			fileObject = FileObjectUtil.findFileObject(path, false);
			if (fileObject == null) {
				throw new ScriptException(ResourceBundleUtil.getResourceMessage("text", "text.file.notfound", path));
			}
			reader = new BufferedReader(new InputStreamReader(fileObject.getInputStream(), encode));

			List<Field> fields = new ArrayList<Field>();
			List<String[]> values = new ArrayList<String[]>();
			// 解析标题
			String line = reader.readLine();
			String[] ss = line.split("\t");
			for (String s : ss) {
				fields.add(new Field(s, s, "Object"));
			}

			// 解析字段
			while ((line = reader.readLine()) != null) {
				ss = line.split("\t");
				if (fields.size() != ss.length) {
					throw new ScriptException(ResourceBundleUtil.getResourceMessage("text", "text.row.error", line));
				}
				values.add(ss);
			}

			Object[][] dataArray = new Object[values.size()][];
			for (int i = 0; i < values.size(); i++) {
				// 不能直接使用String[],导致后期无法转换类型
				String[] rows = values.get(i);
				dataArray[i] = new Object[rows.length];
				for (int j = 0; j < rows.length; j++) {
					dataArray[i][j] = rows[j];
				}
			}

			// 返回数据集
			return DataSetUtil.createDynamicDataSet(fields, dataArray, getScriptEngine().isIndexFromOne());
		} finally {
			if (fileObject != null) {
				fileObject.clean();
			}
			if (reader != null) {
				reader.close();
			}
		}
	}

}
