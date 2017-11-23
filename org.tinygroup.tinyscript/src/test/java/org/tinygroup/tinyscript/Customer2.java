package org.tinygroup.tinyscript;

/**
 * 业务值对象，属性字段和表字段基本不匹配
 * @author yancheng11334
 *
 */
public class Customer2 {

	private String name;
	private String password;
	private int sex;
	private String mobile;
	private String address;
	private int age;
	private boolean onlineTag;
	
	private int level=1; //无关系字段

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isOnlineTag() {
		return onlineTag;
	}

	public void setOnlineTag(boolean onlineTag) {
		this.onlineTag = onlineTag;
	}

	public String toString() {
		return "Customer2 [name=" + name + ", password=" + password + ", sex="
				+ sex + ", mobile=" + mobile + ", address=" + address
				+ ", age=" + age + ", onlineTag=" + onlineTag + ", level="
				+ level + "]";
	}
	
	
}
