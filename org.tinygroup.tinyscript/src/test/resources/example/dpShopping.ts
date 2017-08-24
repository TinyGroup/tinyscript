/*王强今天很开心，公司发给N元的年终奖。王强决定把年终奖用于购物，他把想买的物品可能会有附件，附件是从属于某个物品的，
为了不超出预算，他把每件物品规定了一个重要度，分为 5 等：用整数 1 ~ 5 表示,如果要买归类为附件的物品，必须先买该附件所属的主件（0表示物品）。
在不超过 N 元（可以等于 N 元）的前提下，使每件物品的价格与重要度的乘积的总和最大。*/
class Goods{
	name,price,value,rule;
	Goods(name,price,value,rule){
	}
}
list=[new Goods("电脑",800,1600.0,0),new Goods("打印机",400,2000.0,1),new Goods("扫描仪",300,1500.0,1),new Goods("书柜",400,1200.0,0),new Goods("书桌",500,1000.0,0)];
money = 1000;
result = list.dpKnapsack(money,list.price,1,list.value,(ele,i,money)->{
	if(ele.get(i).rule ==0){
		if(ele.get(i).price<=money){
			return true;
		}
	}else{
		if(ele.get(i).price+ele.get(ele.get(i).rule-1).price<=money){
			return true;
		}
	}
	return false;
});
println("购物单问题:\n"+result);