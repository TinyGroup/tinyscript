package org.tinygroup.tinyscript;

public class Student {

	public static String country = "CHINA";  //静态属性
	
	private String name;
	
	private int age;
	
	private boolean registered;
	
	public Student(){
		
	}

	public Student(String name, Integer age, Boolean registered) {
		super();
		this.name = name;
		this.age = age;
		this.registered = registered;
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

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (registered ? 1231 : 1237);
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (age != other.age)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (registered != other.registered)
			return false;
		return true;
	}
	
	
}
