package org.tinygroup.tinyscript.dataset.impl;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.dataset.AbstractDataSet;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

public class DataSetBean implements Map<String, Object> {

	private Map<String, Object> map;

	private String type;
	private List<Field> fields;
	private int rowId = -1;

	public DataSetBean(Map<String, Object> map) {
		this(map, false);
	}

	public DataSetBean(Map<String, Object> map, boolean order) {
		if (order) {
			map = new LinkedHashMap<String, Object>();
		} else {
			map = new HashMap<String, Object>();
		}

		fields = new ArrayList<Field>();
		for (String key : map.keySet()) {
			fields.add(new Field(key, key, map.get(key).getClass().getSimpleName()));
		}

		putAll(map);
	}

	public DataSetBean(AbstractDataSet dataSet, int rowId) {
		this(dataSet, rowId, false);
	}

	public DataSetBean(AbstractDataSet dataSet, int rowId, boolean order) {
		if (order) {
			map = new LinkedHashMap<String, Object>();
		} else {
			map = new HashMap<String, Object>();
		}
		this.fields = dataSet.getFields();
		this.rowId = rowId;
		this.type = dataSet.getClass().getSimpleName();
		for (int i = 0; i < fields.size(); i++) {
			try {
				put(fields.get(i).getName(), dataSet.getData(rowId, dataSet.getShowIndex(i)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 将DataSetBean转为实体Object,class需要实现无参的构造方法
	 * 
	 * @param clazz
	 * @return
	 * @throws ScriptException
	 */
	public <T> T convertToObject(Class<T> clazz) throws ScriptException {
		T t = null;
		try {
			Constructor<T> constructor = clazz.getDeclaredConstructor();
			constructor.setAccessible(true);
			t = constructor.newInstance();
		} catch (Exception e) {
			throw new ScriptException(
					ResourceBundleUtil.getResourceMessage("dataset", "dataset.creatbean.error", clazz.getName()), e);
		}

		for (String key : map.keySet()) {
			try {
				java.lang.reflect.Field field = clazz.getDeclaredField(key);
				field.setAccessible(true);
				field.set(t, map.get(key));
			} catch (NoSuchFieldException e) {
				// 如果没找到对应的field，则跳过
			} catch (IllegalAccessException e) {
			}

		}
		return t;
	}

	@SuppressWarnings("unchecked")
	public <T> T getObject(Object key, Class<T> clazz) {
		Object obj = map.get(key);
		if (obj.getClass() == clazz) {
			return (T) obj;
		}

		return castToBaseType(obj, clazz);
	}

	@Override
	public int size() {
		return map.size();
	}

	@Override
	public boolean isEmpty() {
		return map.size() == 0;
	}

	@Override
	public boolean containsKey(Object key) {
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return map.containsValue(value);
	}

	@Override
	public Object get(Object key) {
		return map.get(key);
	}

	@Override
	public Object put(String key, Object value) {
		return map.put(key, value);
	}

	@Override
	public Object remove(Object key) {
		return map.remove(key);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		map.putAll(m);
	}

	@Override
	public void clear() {
		rowId = -1;
		type = null;
		fields.clear();
		map.clear();
	}

	@Override
	public Set<String> keySet() {
		return map.keySet();
	}

	@Override
	public Collection<Object> values() {
		return map.values();
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return map.entrySet();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	public int getRowId() {
		return rowId;
	}

	public void setRowId(int rowId) {
		this.rowId = rowId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (String key : map.keySet()) {
			builder.append(key).append(":").append(map.get(key).toString()).append(" ");
		}
		builder.append("}");
		return builder.toString();
	}

	@SuppressWarnings("unchecked")
	private <T> T castToBaseType(Object obj, Class<T> clazz) {
		if (clazz == int.class || clazz == Integer.class) {
			try {
				return (T) Integer.valueOf(obj.toString());
			} catch (Exception e) {
				return null;
			}
		}
		if (clazz == long.class || clazz == Long.class) {
			try {
				return (T) Long.valueOf(obj.toString());
			} catch (Exception e) {
				return null;
			}
		}
		if (clazz == short.class || clazz == Short.class) {
			try {
				return (T) Short.valueOf(obj.toString());
			} catch (Exception e) {
				return null;
			}
		}
		if (clazz == double.class || clazz == Double.class) {
			try {
				return (T) Double.valueOf(obj.toString());
			} catch (Exception e) {
				return null;
			}
		}
		if (clazz == float.class || clazz == Float.class) {
			try {
				return (T) Float.valueOf(obj.toString());
			} catch (Exception e) {
				return null;
			}
		}
		if (clazz == boolean.class || clazz == Boolean.class) {
			try {
				return (T) Boolean.valueOf(obj.toString());
			} catch (Exception e) {
				return null;
			}
		}

		if (clazz == String.class) {
			return (T) obj.toString();
		}

		return null;

	}

}
