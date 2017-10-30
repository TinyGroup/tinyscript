package org.tinygroup.tinyscript.executor;

import java.io.File;
import java.io.PrintStream;

import org.tinygroup.tinyrunner.Runner;
import org.tinygroup.tinyscript.ScriptException;

public class DefaultTinyScriptExecutor {
    private static TinyScriptOperator operator = new DefaultTinyScriptOperator();
    
    /**
     * 固定入口
     * @param args
     * @throws ScriptException
     */
	public static void main(String[] args) throws ScriptException {
		//执行检验逻辑
		if (args.length == 0) {
			System.out
					.println("Usage:\n\tDefaultTinyScriptExecutor tinyScriptFile [relativePath] [absolutePath] [resources] ");
			return;
		}
		//获取重定向文件
		File logfile = getLogFile(args);
		
		//初始化Spring上下文
		initSpring(logfile);
				
		//执行模拟器
		operator.execute(args);
	}
	
	
	private static File getLogFile(String[] args) throws ScriptException {
		String fileName = "spring.log";
		if(args!=null && args.length>=2 && args[1]!=null){
		   //定向文件和脚本文件同级目录
		   String absolutePath = args[1].replaceAll("\\\\", "/");
		   String newPath = absolutePath.substring(0, absolutePath.lastIndexOf("/"))+"/"+fileName;
		   return new File(newPath);
		}else{
		   //没有配置路径
		   return new File(fileName);
		}
	}
	
	private static void initSpring(File logfile) throws ScriptException {
		PrintStream old = System.out;
		PrintStream newlog = null;
		if(logfile.exists()){
		   logfile.delete();
		}
		try{
			//重定向输出流到文件
			newlog = new PrintStream(logfile);
			System.setOut(newlog);
			//加载配置逻辑
			Runner.init("/application.xml", null);
		} catch (Exception e) {
			throw new ScriptException(e);
		}finally{
			//重定向输出流为原始流
			System.setOut(old);
			if(newlog!=null){
			   newlog.close();	
			}
		}

	}
	
}
