package org.tinygroup.tinyscript.interpret;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.tinyscript.ScriptException;

public class InnerScriptReader {

	private List<String> lines = new ArrayList<String>();
	
	public InnerScriptReader(String text) throws ScriptException{
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new StringReader(text));
			String line = null;
			while((line=reader.readLine())!=null){
				lines.add(line);
			}
		}catch(Exception e){
			throw new ScriptException(e);
		}finally{
			if(reader!=null){
			   try {
				 reader.close();
			   } catch (IOException e) {
				  //忽略异常
			   }
			}
		}
	}
	public String getScript(int startLine, int startCharPositionInLine,
			int stopLine, int stopCharPositionInLine) throws ScriptException{
		try{
			StringBuilder sb = new StringBuilder();
			String line = null;
			for(int i=startLine-1;i<stopLine;i++){
				if(i==startLine-1){
					line = lines.get(i);
					sb.append(line.substring(startCharPositionInLine, line.length())).append("\n");
				}else if(i==stopLine-1){
					line = lines.get(i);
					sb.append(line.substring(0, stopCharPositionInLine)).append("\n");
				}else{
					sb.append(lines.get(i)).append("\n");
				}
			}
			return sb.toString();
		}catch(Exception e){
		    throw new ScriptException(ResourceBundleUtil.getDefaultMessage("script.reader.error1", startLine,startCharPositionInLine,
		    		stopLine,stopCharPositionInLine));
		}
	}

	public String getScriptFromStart(int line, int charPositionInLine) throws ScriptException{
        try{
        	StringBuilder sb = new StringBuilder();
			String str = null;
			for(int i=0;i<line;i++){
			    if(i==line-1){
			      str = lines.get(i);
			      sb.append(str.substring(0, charPositionInLine)).append("\n");
			    }else{
			      sb.append(lines.get(i)).append("\n");
			    }
			}
			return sb.toString();
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("script.reader.error2", line,charPositionInLine));
		}
	}

	public String getScriptToStop(int line, int charPositionInLine) throws ScriptException{
        try{
        	StringBuilder sb = new StringBuilder();
			String str = null;
			for(int i=line-1;i<lines.size();i++){
				if(i==line-1){
				   str = lines.get(i);
				   sb.append(str.substring(charPositionInLine,str.length())).append("\n");
				}else{
				   sb.append(lines.get(i)).append("\n");
				}
			}
			return sb.toString();
		}catch(Exception e){
			throw new ScriptException(ResourceBundleUtil.getDefaultMessage("script.reader.error2", line,charPositionInLine));
		}
	}
}
