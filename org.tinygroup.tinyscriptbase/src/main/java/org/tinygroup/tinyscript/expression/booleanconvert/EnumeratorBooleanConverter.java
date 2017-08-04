package org.tinygroup.tinyscript.expression.booleanconvert;

import org.tinygroup.commons.tools.Enumerator;
import org.tinygroup.tinyscript.expression.BooleanConverter;

public class EnumeratorBooleanConverter implements BooleanConverter{

	public boolean isMatch(Object obj) {
		return obj instanceof Enumerator;
	}

	public Boolean convert(Object obj) {
		Enumerator e = (Enumerator) obj;
        return e.hasMoreElements();
	}

}
