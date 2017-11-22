package org.tinygroup.tinyscript.naming;

import java.util.ArrayList;
import java.util.List;

/**
 * 命名字符串工具类
 * @author yancheng11334
 *
 */
public final class NamingStringUtil {
   
	private NamingStringUtil(){
		
	}
	
	/**
	 * 根据名称解析命名字符串
	 * @param name
	 * @return
	 */
	public static NamingString parse(String name){
		char[] cs = name.toCharArray();
		int[] types = new int[cs.length];
		boolean unknown = false;
		int upperNum =0;
		int lowerNum =0;
		int enDishNum=0;
		int underlineNUm=0;
		for(int i=0;i<cs.length;i++){
		   if(isUpper(cs[i])){
			  types[i] = 1;
			  upperNum++;
		   }else if(isLower(cs[i])){
			  types[i] = 2; 
			  lowerNum++;
		   }else if(isNumber(cs[i])){
			  types[i] = 3; 
		   }else if(cs[i]=='-'){
			  types[i] = 4; 
			  enDishNum++;
		   }else if(cs[i]=='_'){
			  types[i] = 5;  
			  underlineNUm++;
		   }else{
			  unknown = true;
			  break;
		   }
		}
		
		if(!unknown){
		   if(lowerNum>0 && types[0]==2 && enDishNum==0 && underlineNUm==0){
			   //以大写字母为分隔
			   List<String> tokens = splitTokens(cs,types,1,true);
			   return new LowerCamelNamingString(name,tokens);
		   }else if(lowerNum>0 && enDishNum>0 && underlineNUm==0){
			   //以连字符为分隔
			   List<String> tokens = splitTokens(cs,types,4,false);
			   return new LowerHyphenNamingString(name,tokens);
		   }else if(lowerNum>0 && enDishNum==0 && underlineNUm>0){
			   //以下划线为分隔
			   List<String> tokens = splitTokens(cs,types,5,false);
			   return new LowerUnderScoreNamingString(name,tokens);
		   }else if(upperNum>0 && types[0]==1 && enDishNum==0 && underlineNUm==0){
			   //以大写字母为分隔
			   if(lowerNum==0){
				  //整个单词大写
				   List<String> tokens = new ArrayList<String>();
				   tokens.add(name.toLowerCase());
				   return new UpperCamelNamingString(name,tokens);
			   }else{
				   List<String> tokens = splitTokens(cs,types,1,true);
				   return new UpperCamelNamingString(name,tokens);
			   }
		   }else if(upperNum>0 && enDishNum==0 && underlineNUm>0){
			   //以下划线为分隔
			   List<String> tokens = splitTokens(cs,types,5,false);
			   return new UpperUnderScoreNamingString(name,tokens);
		   }
		}
		
		return new UnKnownNamingString(name);
	}
	
	/**
	 * 拆分字符串
	 * @param cs  字符序列
	 * @param types  字符种类序列
	 * @param splitTag  分隔类型
	 * @param append  是否追加分隔符
	 * @return
	 */
	private static List<String> splitTokens(char[] cs,int[] types,int splitTag,boolean append){
		StringBuilder builder = new StringBuilder();
		List<String> tokens = new ArrayList<String>();
		for(int i=0;i<types.length;i++){
		   if(types[i]==splitTag){
			  if(builder.length()>0){
				 tokens.add(builder.toString().toLowerCase()); 
				 builder = new StringBuilder();  
			  }
			  if(append){
				 builder.append(cs[i]);
			  }
		   }else{
			  builder.append(cs[i]);
		   }
		   
		}
		if(builder.length()>0){
		   tokens.add(builder.toString().toLowerCase()); 
		}
		return tokens;
	}
	
	public static boolean isUpper(char c){
		return c>='A' && c<='Z';
	}
	
	public static boolean isLower(char c){
		return c>='a' && c<='z';
	}
	
	public static boolean isNumber(char c){
		return c>='0' && c<='9';
	}
	
    static class LowerCamelNamingString extends AbstractNamingString{
        
		public LowerCamelNamingString(String name, List<String> tokens) {
			super(name, tokens);
		}

		public NamingStringEnum getType() {
			return NamingStringEnum.LOWER_CAMEL;
		}
		
	}
    
    
    static class LowerHyphenNamingString extends AbstractNamingString{

		public LowerHyphenNamingString(String name, List<String> tokens) {
			super(name, tokens);
		}

		public NamingStringEnum getType() {
			return NamingStringEnum.LOWER_HYPHEN;
		}
    	
    }
    
    static class LowerUnderScoreNamingString extends AbstractNamingString{

		public LowerUnderScoreNamingString(String name, List<String> tokens) {
			super(name, tokens);
		}

		public NamingStringEnum getType() {
			return NamingStringEnum.LOWER_UNDERSCORE;
		}
    	
    }
    
    static class UpperCamelNamingString extends AbstractNamingString{
        
		public UpperCamelNamingString(String name, List<String> tokens) {
			super(name, tokens);
		}

		public NamingStringEnum getType() {
			return NamingStringEnum.UPPER_CAMEL;
		}
		
	}
    
    static class UpperUnderScoreNamingString extends AbstractNamingString{

		public UpperUnderScoreNamingString(String name, List<String> tokens) {
			super(name, tokens);
		}

		public NamingStringEnum getType() {
			return NamingStringEnum.UPPER_UNDERSCORE;
		}
    	
    }
    
    static class UnKnownNamingString extends  AbstractNamingString{
       
		public UnKnownNamingString(String name) {
			super(name);
			tokens.add(name);
		}

		public NamingStringEnum getType() {
			return NamingStringEnum.UNKNOWN;
		}
    	
    }
}
