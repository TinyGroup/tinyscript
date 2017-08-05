package org.tinygroup.tinyscript.example;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.tinygroup.commons.tools.FileUtil;
import org.tinygroup.tinyscript.ComputeEngine;
import org.tinygroup.tinyscript.ScriptContext;
import org.tinygroup.tinyscript.ScriptException;
import org.tinygroup.tinyscript.ScriptSegment;
import org.tinygroup.tinyscript.dataset.DataSet;
import org.tinygroup.tinyscript.dataset.GroupDataSet;
import org.tinygroup.tinyscript.impl.DefaultComputeEngine;
import org.tinygroup.tinyscript.impl.DefaultScriptContext;
import org.tinygroup.tinyscript.interpret.ScriptUtil;

/**
 * 一个月内连续三天涨停的股票
 * @author yancheng11334
 *
 */
public class Example6Test extends TestCase{

	private ComputeEngine engine ;
	private ScriptContext context ;
	
	protected void setUp() throws Exception {
		engine = new DefaultComputeEngine();
		context = new DefaultScriptContext();
		
		String content = FileUtil.readFileContent(new File("src/test/resources/stock.tinyscript"), "utf-8");
		ScriptSegment scriptSegment = ScriptUtil.getDefault().createScriptSegment(engine, null, content);
		engine.addScriptSegment(scriptSegment);
	}
	/**
	 * java为主
	 * @throws Exception
	 */
	public void test6WithJava() throws Exception{
		System.out.println("test6WithJava is start!");
        
		//读取文件
		List<Stock> values = readTxt("src/test/resources/StockRecords.txt");
		
		//按股票分组
		Map<String,List<Stock>> maps = new HashMap<String,List<Stock>>();
		for(Stock stock:values){
			List<Stock> stockList = maps.get(stock.getCode());
			if(stockList==null){
			   stockList = new ArrayList<Stock>();
			   maps.put(stock.getCode(), stockList);
			}
			stockList.add(stock);
		}
		
		//遍历每只股票
		Comparator c = new StockComparator();
		for(List<Stock> stockList:maps.values()){
			Collections.sort(stockList, c);
			
			//计算涨幅
			stockList.get(0).setUp(0d);
			for(int i=1;i<stockList.size();i++){
				Stock oldStock = stockList.get(i-1);
				Stock newStock = stockList.get(i);
				double up = (newStock.getCl()-oldStock.getCl())/oldStock.getCl();
				newStock.setUp(up);
			}
		}
		
		//统计连续涨三天的股票
		double radio =  0.095d;
		List<String> goodStocks = new ArrayList<String>();
		for(List<Stock> stockList:maps.values()){
			for(int i=2;i<stockList.size();i++){
				Stock s0 = stockList.get(i-2);
				Stock s1 = stockList.get(i-1);
				Stock s2 = stockList.get(i);
				if(s0.getUp()>radio && s1.getUp()>radio && s2.getUp()>radio){
					goodStocks.add(s0.getCode());
					break;
				}
			}
		}
		
		//输出结果股票
		for(int i=0;i<goodStocks.size();i++){
			System.out.println("code="+goodStocks.get(i));
		}
		
	    System.out.println("test6WithJava is end!");
	}
	
	private List<Stock> readTxt(String path) throws Exception{
		BufferedReader reader = null;
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(path);
			reader = new BufferedReader(new InputStreamReader(fis,"utf-8"));
			List<Stock> values = new ArrayList<Stock>();
			//解析标题
            String line = reader.readLine();
            String[] fields = line.split("\t");
			
            //解析字段
			while((line=reader.readLine())!=null){
				String[] ss = line.split("\t");
				if(fields.length!=ss.length){
				   throw new ScriptException(String.format("解析行记录[%s]失败:值个数与标题列个数不匹配", line));
				}
				values.add(new Stock(ss));
			}
			return values;
			
		}finally{
			if(reader!=null){
			   reader.close();
			}
			if(fis!=null){
			   fis.close();
			}
		}
		
	}
	
	class StockComparator implements Comparator<Stock>{

		public int compare(Stock o1, Stock o2) {
			return o1.getDate().compareTo(o2.getDate());
		}
		
	}
	
	class Stock {
		private String code;
		private double cl;
		private Date date;
		private double up;
		
		public Stock(String[] ss) throws Exception{
			code = ss[0];
			date = convert(ss[1]);
			cl = Double.parseDouble(ss[2]);
		}
		
		private Date convert(String str) throws Exception{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return format.parse(str);
		}
		public String getCode() {
			return code;
		}
		public void setCode(String code) {
			this.code = code;
		}
		public double getCl() {
			return cl;
		}
		public void setCl(double cl) {
			this.cl = cl;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public double getUp() {
			return up;
		}
		public void setUp(double up) {
			this.up = up;
		}
		
	}
	
	/**
	 * tiny脚本为主,进行运算
	 * @throws Exception
	 */
	public void test6WithScript() throws Exception{
		
		System.out.println("test6WithScript is start!");
		GroupDataSet groupDs = (GroupDataSet) engine.execute("m = new Example6(); return m.countStock(\"src/test/resources/StockRecords.txt\");", context);
		//输出结果股票
		for(int i=0;i<groupDs.getRows();i++){
			System.out.println("code="+groupDs.getData(i+1,1));
		}
		System.out.println("test6WithScript is end!");
	}
	
	public void testMailCharge() throws Exception{
		DataSet resultDs = (DataSet) engine.execute("m = new Example6(); return m.countMailCharge(\"src/test/resources/mailCharge.txt\",\"src/test/resources/testOrder.txt\");", context);
		//输出邮费
		for(int i=0;i<resultDs.getRows();i++){
			resultDs.absolute(i+1);
			System.out.println("POSTAGE="+resultDs.getData("POSTAGE"));
		}
	}
	
	public void testBalance() throws Exception{
		engine.execute("m = new Example6(); return m.countBalance();", context);
	}
	
	public void testVip() throws Exception{
		List list = (List) engine.execute("m = new Example6(); return m.countVip();", context);
		for(Object obj:list){
			System.out.println("name="+obj);
		}
		
	}
	
	//输出水仙花
	public void testNarcissisticNumber() throws Exception{
		List list = (List) engine.execute("m = new Example6(); return m.countNarcissisticNumber();", context);
		for(Object obj:list){
			System.out.println("number="+obj);
		}
		assertEquals(153, list.get(0));
		assertEquals(370, list.get(1));
		assertEquals(371, list.get(2));
		assertEquals(407, list.get(3));
	}
	
	public void testScoreList() throws Exception{
		engine.execute("m = new Example6(); return m.countScoreList();", context);
	}
}
