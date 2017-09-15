package org.tinygroup.tinyscript.csv.function;

import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.csv.CSVFormat;
import org.tinygroup.tinyscript.csv.CSVPrinter;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.function.AbstractScriptFunction;
import org.tinygroup.tinyscript.interpret.FileObjectUtil;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;
import org.tinygroup.vfs.FileObject;

public class WriteCsvFunction extends AbstractScriptFunction {

	private static final String DEFAULT_ENCODE = "utf-8";
	private static final String DEFAULT_TYPE = "DEFAULT";

	@Override
	public String getNames() {
		return "writeCsv";
	}

	@Override
	public String getBindingTypes() {
		return DataSet.class.getName();
	}

	@Override
	public Object execute(ScriptSegment segment, ScriptContext context, Object... parameters) throws ScriptException {
		try {
			if (parameters == null || parameters.length < 2) {
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

			DataSet dataSet = (DataSet) parameters[0];
			String filePath = (String) parameters[1];

			switch (parameters.length) {
			case 2:
				writeCsv(dataSet, filePath, DEFAULT_ENCODE, 1, dataSet.getRows(), DEFAULT_TYPE);
				break;
			case 3:
				if (parameters[2] instanceof String) {
					writeCsv(dataSet, filePath, (String) parameters[2], 1, dataSet.getRows(), DEFAULT_TYPE);
				} else {
					writeCsv(dataSet, filePath, DEFAULT_ENCODE, (Integer) parameters[2], dataSet.getRows(),
							DEFAULT_TYPE);
				}
				break;
			case 4:
				if (parameters[2] instanceof String) {
					writeCsv(dataSet, filePath, (String) parameters[2], (Integer) parameters[3], dataSet.getRows(),
							DEFAULT_TYPE);
				} else {
					writeCsv(dataSet, filePath, DEFAULT_ENCODE, (Integer) parameters[2], (Integer) parameters[3],
							DEFAULT_TYPE);
				}
				break;
			case 5:
				if (parameters[2] instanceof String) {
					writeCsv(dataSet, filePath, (String) parameters[2], (Integer) parameters[3],
							(Integer) parameters[4], DEFAULT_TYPE);
				} else {
					writeCsv(dataSet, filePath, DEFAULT_ENCODE, (Integer) parameters[2], (Integer) parameters[3],
							(String) parameters[4]);
				}
				break;
			case 6:
				writeCsv(dataSet, filePath, (String) parameters[2], (Integer) parameters[3], (Integer) parameters[4],
						(String) parameters[5]);
				break;
			default:
				throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.parameter.error", getNames()));
			}

			return null;
		} catch (Exception e) {
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("function.run.error", getNames()), e);
		}
	}

	private void writeCsv(DataSet dataSet, String filePath, String encode, int start, int end, String type)
			throws Exception {
		FileObject csvFile = FileObjectUtil.getOrCreateFile(filePath, false);
		CSVPrinter printer = null;
		try {
			printer = new CSVPrinter(new OutputStreamWriter(csvFile.getOutputStream(), encode),
					(CSVFormat) (CSVFormat.class.getDeclaredField(type).get(CSVFormat.class)));
		} catch (NoSuchFieldException e) {
			throw new ScriptException(ResourceBundleUtil.getResourceMessage("excel", "csv.type.error", type));
		}

		List<Object> records = new ArrayList<Object>();

		for (Field field : dataSet.getFields()) {
			records.add(field.getName());
		}
		printer.printRecord(records);

		for (int i = start; i <= end; i++) {
			records = new ArrayList<Object>();
			for (int j = 1; j <= dataSet.getColumns(); j++) {
				records.add(dataSet.getData(i, j));
			}
			printer.printRecord(records);
		}
		printer.close();
	}

}
