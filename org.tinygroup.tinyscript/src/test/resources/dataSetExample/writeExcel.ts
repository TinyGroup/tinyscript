dataSet1 = readExcel("/dataSetExample/writeExcelTest.xlsx");
dataSet1.get(0).writeExcel("/dataSetExample/testExcel1.xlsx");
dataSet1.writeExcel("/dataSetExample/testExcel2.xlsx",["mysheet"]);
dataSet1.writeExcel("/dataSetExample/testExcel3.xlsx",["mysheet"],2);
dataSet1.writeExcel("/dataSetExample/testExcel4.xlsx",1,2);
dataSet1.writeExcel("C:/Users/hspcadmin/Desktop/testExcel5.xlsx",["mysheet1","mysheet2"],1,2);

dataSet1 = readCsv("/dataSetExample/writeCsvTest.csv","gbk");
dataSet1.writeCsv("/dataSetExample/testCsv1.csv");
dataSet1.writeCsv("/dataSetExample/testCsv2.csv",2);
dataSet1.writeCsv("/dataSetExample/testCsv3.csv","gbk");
dataSet1.writeCsv("/dataSetExample/testCsv4.csv","gbk",2);
dataSet1.writeCsv("/dataSetExample/testCsv5.csv",2,3);
dataSet1.writeCsv("/dataSetExample/testCsv6.csv","gbk",2,3);
dataSet1.writeCsv("/dataSetExample/testCsv7.csv",1,4,"RFC4180");
dataSet1.writeCsv("/dataSetExample/testCsv8.csv","gbk",2,3,"RFC4180");
