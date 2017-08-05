package org.tinygroup.tinyscript.dataset;

/**
 * 字段类型
 * Created by luoguo on 2014/7/4.
 */
public final class Field {
    private String name;
    private String title;
    private String type;
    
    public Field(){
    	
    }

    public Field(String name, String title, String type) {
		super();
		this.name = name;
		this.title = title;
		this.type = type;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

	public String toString() {
		return "Field [name=" + name + ", title=" + title + ", type=" + type
				+ "]";
	}
    
    
}
