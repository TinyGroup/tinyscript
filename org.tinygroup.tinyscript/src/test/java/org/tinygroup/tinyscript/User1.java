package org.tinygroup.tinyscript;

public class User1 {

	private String id;
	private String name;
	private int age;
	private int sex;
	
	
	public User1(String id, String name, int age, int sex) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
		this.sex = sex;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}

	public String toString() {
		return "User1 [id=" + id + ", name=" + name + ", age=" + age + ", sex="
				+ sex + "]";
	}
	
	
	
}
