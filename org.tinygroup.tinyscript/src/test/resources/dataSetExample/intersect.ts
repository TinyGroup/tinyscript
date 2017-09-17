dataSet1 = readTxt("/dataSetExample/data1.txt");
dataSet2 = readTxt("/dataSetExample/data2.txt");
result = dataSet1.intersect(dataSet2,()->{ return name+"|"+weight; });
for(i = 1;i<=result.getRows();i++){
	for(j=1;j<=result.getColumns();j++){
		print(result[i][j]+" ");
	}
	println();
}
println("================================================================");
result = dataSet1.intersect(dataSet2,["name","weight"]);
for(i = 1;i<=result.getRows();i++){
	for(j=1;j<=result.getColumns();j++){
		print(result[i][j]+" ");
	}
	println();
}
println("================================================================");
result = dataSet1.intersect(dataSet2,"name");
for(i = 1;i<=result.getRows();i++){
	for(j=1;j<=result.getColumns();j++){
		print(result[i][j]+" ");
	}
	println();
}







