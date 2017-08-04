/**
 * Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 * <p/>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.gnu.org/licenses/gpl.html
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.tinygroup.tinyscript.interpret.terminal;

import org.antlr.v4.runtime.tree.TerminalNode;
import org.tinygroup.tinyscript.parser.grammer.TinyScriptParser;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptSegment;

public class BooleanNodeProcessor extends AbstractTerminalNodeProcessor {


    public int getType() {
        return TinyScriptParser.BooleanLiteral;
    }

	public Object processTerminalNode(TerminalNode terminalNode, ScriptSegment segment,
			ScriptContext context)
			throws Exception {
		return Boolean.valueOf(terminalNode.getText());
	}

}
