//参数说明：list.DPknapsack(容量，重量，[每件物品的件数]，价值，[规则]）

//01背包
w = [2,2,6,5,4];
count = [1,1,1,1,1];
size = 10;
list = ["a","b","c","d","e"];
result = list.DPknapsack(size,w,count,()->{
	return [6,3,5,4,6];
});
println("01背包问题:\n"+result);

//完全背包
w = [2,2,6,5,4];
size = 10;
list = ["a","b","c","d","e"];
result = list.DPknapsack(size,w,()->{
	return [6,3,5,4,6];
});
println("完全背包问题:\n"+result);

//多重背包
w = [12,2,1,4,1];
size = 15;
count = [1,7,12,3,1];
list = ["a","b","c","d","e"];
result = list.DPknapsack(size,w,count,()->{
	return [4,2,1,10,2];
});
println("多重背包问题:\n"+result);

//混合背包
w = [12,2,1,4,1];
size = 15;
count = [1,7,12,3,-1];//-1表示该物品无件数限制
list = ["a","b","c","d","e"];
result = list.DPknapsack(size,w,count,()->{
	return [4,2,1,10,2];
});
println("混合背包问题:\n"+result);

/*小王有一笔钱m(5000)，在t(80)时间内，想做理财投资，有如下几只理财产品，请给出最佳方案*/
w = [100,100,100,100,100];//基金的最小购买额度
count = [10,20,20,10,5];//购买基金的份数
list = ["鹏华国防","鹏华中证","国投瑞银","华商主题精选","金鹰智慧"];//基金的名字
result = list.DPknapsack(5000,w,count,()->{
	lilv = [0.00045,0.00035,0.00055,0.00040,0.00030];//基金的利率
	value = [];
	for(i = 0;i<lilv.size();i++){
		value.add(lilv.get(i)*8000);//根据利率算出每个基金的总的收益（80天）
	}
	return value;
});
println("选择基金问题:\n"+result);

/*王强今天很开心，公司发给N元的年终奖。王强决定把年终奖用于购物，他把想买的物品分为两类：主件与附件，附件是从属于某个主件的，下表就是一些主件与附件的例子：
主件	附件
电脑	打印机，扫描仪
书柜	图书
书桌	台灯，文具
工作椅	无
如果要买归类为附件的物品，必须先买该附件所属的主件。*/
w = [800,400,300,400,500];
count = [1,1,1,1,1];
size = 1000;
list = ["a","b","c","d","e"];
rule = [0,1,1,0,0];
result = list.DPknapsack(size,w,count,rule,()->{
	v = [1600,2000,1500,1200,1000];
	return v;
});
println("购物单问题:\n"+result);


