dataSet = readTxt("/example/knapsack1.txt");
dataSet = dataSet.double("value").int("weight").int("count");
//================================================================================
//01背包
println(dataSet.dpKnapsack(10,dataSet.weight,1,dataSet.value));

//完全背包
println(dataSet.dpKnapsack(10,dataSet.weight,dataSet.value));
//================================================================================


dataSet = readTxt("/example/knapsack2.txt");
dataSet = dataSet.double("value").int("weight").int("count");
//================================================================================
//多重背包
println(dataSet.dpKnapsack(15,dataSet.weight,dataSet.count,dataSet.value));


dataSet = readTxt("/example/knapsack3.txt");
dataSet = dataSet.double("value").int("weight").int("count");
//================================================================================
//混合背包
println(dataSet.dpKnapsack(15,dataSet.weight,dataSet.count,dataSet.value));


//基金问题
dataSet = readTxt("/example/knapsack4.txt");
dataSet = dataSet.int("minmoney").int("maxmoney").double("interestRate");
dataSet.insertColumn(1,"value").insertColumn(2,"count").double("value").int("count");
dataSet.update("value",interestRate[0]*minmoney[0]*80);
dataSet.update("count",maxmoney[0]/minmoney[0]);
//================================================================================
println(dataSet.dpKnapsack(5000,dataSet.minmoney,dataSet.count,dataSet.value));


//购物单问题
dataSet = readTxt("/example/knapsack5.txt");
dataSet = dataSet.int("price").int("importance").int("rule");
dataSet.insertColumn(1,"value").double("value");
dataSet.update("value",price[0]*importance[0]);
//================================================================================
println(dataSet.dpKnapsack(1000,dataSet.price,1,dataSet.value,(i,money)->{
	if(rule[i] == 0){
		if( price[i] <= money){
			return true;
		}
	}else{
		k = rule[i];
		if(price[i]+price[k]<=money){
			return true;
		}
	}
	return false;
}));
