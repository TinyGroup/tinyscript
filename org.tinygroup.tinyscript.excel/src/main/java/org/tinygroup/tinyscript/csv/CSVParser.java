package org.tinygroup.tinyscript.csv;

import static org.tinygroup.tinyscript.csv.Token.Type.TOKEN;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.tinyscript.dataset.Field;
import org.tinygroup.tinyscript.dataset.FieldType;
import org.tinygroup.tinyscript.dataset.impl.SimpleDataSet;

public final class CSVParser {
	
	public CSVParser(final Reader reader,final CSVFormat csvFormat){
		format = csvFormat;
		lexer = new Lexer(format, new ExtendedBufferedReader(reader));
	}
	
	private final Token reusableToken = new Token();
	 
	private final Lexer lexer;
	
	private final CSVFormat format;
	
	private List<String> current = null;
    
	private void addRecordValue(List<String> record) {
        final String input = this.reusableToken.content.toString();
        final String nullString = this.format.getNullString();
        if (nullString == null) {
            record.add(input);
        } else {
            record.add(input.equalsIgnoreCase(nullString) ? null : input);
        }
	}
	
	public long getCurrentLineNumber() {
        return this.lexer.getCurrentLineNumber();
    }
	
	public SimpleDataSet extractDataSet() throws Exception{
		 boolean head = true;
		   List<Field> fields = null;
		   List<List<String>> lstData = new ArrayList<List<String>>();
		   while(hasNext()){
			   List<String> result = next();
			   if(head){
				   head = false;
				   fields = getFields(result);
			   }else{
				   lstData.add(result);
			   }
		   }
		   return new SimpleDataSet(fields,lstToArray(lstData));
	}
	
	/**
	 * 根据csv第一行的目录获取field值
	 * @param titles
	 * @return
	 */
	private List<Field> getFields(List<String> titles){
		if(titles == null || titles.size() == 0){
			throw new RuntimeException("CSV文件没有title信息");
		}
		List<Field> fields = new ArrayList<Field>(); 
		for(String title : titles){
			Field field = new Field();
			field.setName(title);
			field.setTitle("");
			field.setType(FieldType.STRING.toString());
			fields.add(field);
		}
		return fields;
	}
	
	
	/**
	 * 数组型集合转二维数组
	 */
	private Object[][] lstToArray(List<List<String>> lstData){
		Object[][] arrayData = new Object[lstData.size()][];
		for(int i=0;i<lstData.size();i++){
			arrayData[i] = CollectionUtil.toNoNullStringArray(lstData.get(i));
		}
		return arrayData;
	}
	
	private boolean hasNext() throws IOException{
		if(this.lexer.isClosed()){
			return false;
		}
		
		if (this.current == null) {
            this.current = this.next();
        }
		
		return this.current != null;
	}
	
	private List<String> next() throws IOException{
		
		List<String> next = this.current;
        this.current = null;

        if (next == null) {
            // hasNext() wasn't called before
            next = this.nextRecord();
//            if (next == null) {
//                throw new NoSuchElementException("No more CSV records available");
//            }
        }
		return next;
	}
	
	private List<String> nextRecord() throws IOException{
		 StringBuilder sb = null;
		 List<String> record = new ArrayList<String>();
		 do {
	            this.reusableToken.reset();
	            this.lexer.nextToken(this.reusableToken);
	            switch (this.reusableToken.type) {
	            case TOKEN:
	                this.addRecordValue(record);
	                break;
	            case EORECORD:
	                this.addRecordValue(record);
	                break;
	            case EOF:
	                if (this.reusableToken.isReady) {
	                    this.addRecordValue(record);
	                }
	                break;
	            case INVALID:
	                throw new IOException("(line " + this.getCurrentLineNumber() + ") invalid parse sequence");
	            case COMMENT: // Ignored currently
	                if (sb == null) { // first comment for this record
	                    sb = new StringBuilder();
	                } else {
	                    sb.append(Constants.LF);
	                }
	                sb.append(this.reusableToken.content);
	                this.reusableToken.type = TOKEN; // Read another token
	                break;
	            default:
	                throw new IllegalStateException("Unexpected Token type: " + this.reusableToken.type);
	            }
	        } while (this.reusableToken.type == TOKEN);
		 
		 return record.isEmpty()?null:record;
	}
	
}
