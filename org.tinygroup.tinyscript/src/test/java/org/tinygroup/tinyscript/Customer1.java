package org.tinygroup.tinyscript;

/**
 * 显示值对象
 * @author yancheng11334
 *
 */
public class Customer1 {

	private String userName;
	private String userPass;
	private String userSex;
	private String mobile;
	private String address;
	private String userAge;
	private String onlineTag;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPass() {
		return userPass;
	}
	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}
	public String getUserSex() {
		return userSex;
	}
	public void setUserSex(String userSex) {
		this.userSex = userSex;
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
	public String getUserAge() {
		return userAge;
	}
	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}
	public String getOnlineTag() {
		return onlineTag;
	}
	public void setOnlineTag(String onlineTag) {
		this.onlineTag = onlineTag;
	}
	
	public String toString() {
		return "Customer1 [userName=" + userName + ", userPass=" + userPass
				+ ", userSex=" + userSex + ", mobile=" + mobile + ", address="
				+ address + ", userAge=" + userAge + ", onlineTag=" + onlineTag
				+ "]";
	}
	
}
