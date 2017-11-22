package org.tinygroup.tinyscript;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.tinygroup.tinyscript.naming.NamingString;
import org.tinygroup.tinyscript.naming.NamingStringEnum;
import org.tinygroup.tinyscript.naming.NamingStringUtil;

/**
 * 命名字符串测试用例
 * @author yancheng11334
 *
 */
public class NamingStringTest extends TestCase {

	public void  testNamingString() throws Exception {
		
		//验证解析类型
		assertEquals(NamingStringEnum.LOWER_CAMEL, NamingStringUtil.parse("id").getType());
		assertEquals(NamingStringEnum.LOWER_CAMEL, NamingStringUtil.parse("userInfo").getType());
		assertEquals(NamingStringEnum.LOWER_HYPHEN, NamingStringUtil.parse("dog-and-cat").getType());
		assertEquals(NamingStringEnum.LOWER_UNDERSCORE, NamingStringUtil.parse("user_tel2").getType());
		assertEquals(NamingStringEnum.UPPER_CAMEL, NamingStringUtil.parse("JavaCode").getType());
		assertEquals(NamingStringEnum.UPPER_UNDERSCORE, NamingStringUtil.parse("STATIC_PARAM").getType());
		
		assertEquals(NamingStringEnum.UNKNOWN, NamingStringUtil.parse("s中文").getType());  //包含字母、数字、连字符、下划线以外字符
		assertEquals(NamingStringEnum.UNKNOWN, NamingStringUtil.parse("yyy-ddd_888").getType()); //同时包含连字符、下划线
		
		//验证转换  
		NamingString s1 = NamingStringUtil.parse("id");
		assertEquals("id",s1.convertTo(NamingStringEnum.LOWER_CAMEL));
		assertEquals("id",s1.convertTo(NamingStringEnum.LOWER_HYPHEN));
		assertEquals("id",s1.convertTo(NamingStringEnum.LOWER_UNDERSCORE));
		assertEquals("Id",s1.convertTo(NamingStringEnum.UPPER_CAMEL));
		assertEquals("ID",s1.convertTo(NamingStringEnum.UPPER_UNDERSCORE));
		
		NamingString s2 = NamingStringUtil.parse("NewUserInfo");
		assertEquals("newUserInfo",s2.convertTo(NamingStringEnum.LOWER_CAMEL));
		assertEquals("new-user-info",s2.convertTo(NamingStringEnum.LOWER_HYPHEN));
		assertEquals("new_user_info",s2.convertTo(NamingStringEnum.LOWER_UNDERSCORE));
		assertEquals("NewUserInfo",s2.convertTo(NamingStringEnum.UPPER_CAMEL));
		assertEquals("NEW_USER_INFO",s2.convertTo(NamingStringEnum.UPPER_UNDERSCORE));
		
		//验证不同类型的关联
		Map<NamingString,String> map = new HashMap<NamingString,String>();
		map.put(s1, s1.toString());
		map.put(s2, s2.toString());
		assertEquals(true,map.containsKey(NamingStringUtil.parse("new_user_info")));
		assertEquals(true,map.containsKey(NamingStringUtil.parse("ID")));
	}
}
