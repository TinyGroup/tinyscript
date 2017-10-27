dataSet1 = readTxt("/dataSetExample/data1.txt");
dataSet2 = readTxt("/dataSetExample/data2.txt");
result = dataSet1.xor(dataSet2,()->{ return name+"|"+weight; });
println(result);
println("================================================================");
result = dataSet1.xor(dataSet2,["name","weight"]);
println(result);
println("================================================================");
result = dataSet1.xor(dataSet2,"name");
println(result);







