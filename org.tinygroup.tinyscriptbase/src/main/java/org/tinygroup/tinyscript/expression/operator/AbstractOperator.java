/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.tinygroup.tinyscript.expression.operator;

import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.expression.Operator;

/**
 * Created by luoguo on 2014/6/5.
 */
public abstract class AbstractOperator implements Operator {
	
	/**
	 * 扩展的操作符处理器需要覆盖本方法
	 */
	public boolean isMatch(Object... parameter) {
		return false;
	}
	
    protected boolean isType(Object object, Class<?> type) {
        return type.isInstance(object);
    }
    
    protected void notSupported(Object object) throws ScriptException {
    	throw new ScriptException(String.format("类型[%s]不支持[%s]的操作", object.getClass().getName(),getOperation()));
    }
    
    //默认都支持,特殊几个操作符处理器不支持如==
    public boolean supportCollection(){
    	return true;
    }
}
