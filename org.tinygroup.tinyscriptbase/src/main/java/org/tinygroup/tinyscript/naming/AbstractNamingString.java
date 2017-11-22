package org.tinygroup.tinyscript.naming;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象命名字符串
 * @author yancheng11334
 *
 */
public abstract class AbstractNamingString implements NamingString{
    /**
     * 原始字符串
     */
	private String source;
	
	/**
	 * 全部采用小写存储单词
	 */
	protected List<String> tokens = new ArrayList<String>();
	
	public AbstractNamingString(String name){
		this.source = name;
	}
	
	public AbstractNamingString(String name,List<String> tokens){
		this.source = name;
		this.tokens = tokens;
	}
	
	public String convertTo(NamingStringEnum type){
		switch(type){
		  case LOWER_CAMEL:{ return convertLowerCamel(); }
		  case LOWER_HYPHEN:{ return convertLowerHyphen(); }
		  case LOWER_UNDERSCORE:{ return convertLowerUnderScore();}
		  case UPPER_CAMEL:{ return convertUpperCamel(); }
		  case UPPER_UNDERSCORE:{ return convertUpperUnderScore();}
		  case UNKNOWN:{ return source;}
		}
		return null;
	}
	
	private String convertLowerCamel(){
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<tokens.size();i++){
		   if(i==0){
			  builder.append(tokens.get(i));
		   }else{
			  builder.append(upperFirst(tokens.get(i)));
		   }
		}
		return builder.toString();
	}
	
	private String convertLowerHyphen(){
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<tokens.size();i++){
		   if(i==0){
			  builder.append(tokens.get(i));
		   }else{
			  builder.append("-").append(tokens.get(i));
		   }
		}
		return builder.toString();
	}
	
	private String  convertLowerUnderScore(){
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<tokens.size();i++){
		   if(i==0){
			  builder.append(tokens.get(i));
		   }else{
			  builder.append("_").append(tokens.get(i));
		   }
		}
		return builder.toString();
	}
	
	private String convertUpperCamel(){
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<tokens.size();i++){
			builder.append(upperFirst(tokens.get(i)));
		}
		return builder.toString();
	}
	
	private String  convertUpperUnderScore(){
		StringBuilder builder = new StringBuilder();
		for(int i=0;i<tokens.size();i++){
		   if(i==0){
			  builder.append(tokens.get(i).toUpperCase());
		   }else{
			  builder.append("_").append(tokens.get(i).toUpperCase());
		   }
		}
		return builder.toString();
	}
	
	private String upperFirst(String str){
		return str.substring(0, 1).toUpperCase()+str.substring(1, str.length());
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tokens == null) ? 0 : tokens.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		AbstractNamingString other = (AbstractNamingString) obj;
		if (tokens == null) {
			if (other.tokens != null)
				return false;
		} else if (!tokens.equals(other.tokens))
			return false;
		return true;
	}
	
	public String toString(){
		return source;
	}
	
}
