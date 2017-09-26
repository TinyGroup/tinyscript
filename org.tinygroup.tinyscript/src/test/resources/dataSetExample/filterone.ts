dataSet = readTxt("/dataSetExample/data1.txt");
row = dataSet.filterOne(()->{return name[0]=="a";});
println(row.getData("name"));