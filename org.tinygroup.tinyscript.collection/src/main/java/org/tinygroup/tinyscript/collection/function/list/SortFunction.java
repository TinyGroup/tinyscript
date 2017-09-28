package org.tinygroup.tinyscript.collection.function.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.function.AbstractSortFunction;
import org.tinygroup.tinyscript.interpret.ResourceBundleUtil;

/**
 * 序列的排序
 * 
 * @author yancheng11334
 *
 */
@SuppressWarnings("rawtypes")
public class SortFunction extends AbstractSortFunction {

	public String getBindingTypes() {
		return "java.util.List";
	}

	@SuppressWarnings({ "unchecked" })
	protected Object sort(ScriptSegment segment, ScriptContext context, Object obj, String rule)
			throws ScriptException {
		try {
			List<Object> list = (List<Object>) obj;
			Comparator c = getComparator(rule, segment, list);
			if (c == null) {
				throw new ScriptException(
						ResourceBundleUtil.getResourceMessage("collection", "collection.sort.rule.error", rule));
			}
			Collections.sort(list, c);
			return list;
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(
					ResourceBundleUtil.getResourceMessage("collection", "collection.sort.error", rule), e);
		}
	}

	@SuppressWarnings("unchecked")
	protected Comparator createComparator(String rule, ScriptSegment segment, Object source) throws Exception {
		List<FieldSortRule> rules = new ArrayList<FieldSortRule>();
		try {
			if (matchFieldRule(rule)) {
				String[] ss = rule.trim().split(",");
				for (String s : ss) {
					FieldSortRule fieldSortRule = parse(s);
					if (fieldSortRule != null) {
						rules.add(fieldSortRule);
					}
				}
				FieldSortComparator comparator = new FieldSortComparator();
				comparator.setFieldSortRuleList(rules);
				return comparator;
			}
		} catch (ScriptException e) {
			throw e;
		} catch (Exception e) {
			throw new ScriptException(
					ResourceBundleUtil.getResourceMessage("collection", "collection.sort.error", rule), e);
		}
		return null;
	}

	/**
	 * 解析字段信息
	 * 
	 * @param s
	 * @return
	 * @throws Exception
	 */
	private FieldSortRule parse(String s) throws Exception {
		FieldSortRule rule = null;
		if (!StringUtil.isEmpty(s)) {
			String[] ss = s.trim().split("\\s+");
			rule = new FieldSortRule();
			if (ss.length >= 2 && ss[1].equalsIgnoreCase("desc")) {
				rule.setAsc(false);
			} else {
				rule.setAsc(true);
			}
			rule.setContextName(DEFAULT_CONTEXT_NAME);
			rule.setSegment(createSegment(rule, ss[0]));
		}

		return rule;
	}

	/**
	 * return object.id;
	 * 
	 * @param rule
	 * @param text
	 * @return
	 */
	private String createSegment(FieldSortRule rule, String text) {
		StringBuilder sb = new StringBuilder();
		sb.append("return ").append(DEFAULT_CONTEXT_NAME);
		sb.append(".").append(text);
		sb.append(";");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	protected Object sortByLambda(Object sortObject, Comparator c) throws Exception {
		List list = (List) sortObject;
		Collections.sort(list, c);
		return list;
	}

}
