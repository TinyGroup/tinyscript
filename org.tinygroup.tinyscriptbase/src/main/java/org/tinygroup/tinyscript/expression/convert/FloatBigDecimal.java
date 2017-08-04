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
package org.tinygroup.tinyscript.expression.convert;

import org.tinygroup.tinyscript.expression.Converter;

import java.math.BigDecimal;

/**
 * Created by luoguo on 2014/6/5.
 */
public class FloatBigDecimal implements Converter<Float,BigDecimal> {

    public BigDecimal convert(Float object) {
        return new BigDecimal(object);
    }


    public Class<?> getSourceType() {
        return Float.class;
    }


    public Class<?> getDestType() {
        return BigDecimal.class;
    }
}
