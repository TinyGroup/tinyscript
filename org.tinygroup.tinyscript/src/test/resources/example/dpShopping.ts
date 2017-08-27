/*王强今天很开心，公司发给N元的年终奖。王强决定把年终奖用于购物，他把想买的物品可能会有附件，附件是从属于某个物品的，
为了不超出预算，他把每件物品规定了一个重要度，分为 5 等：用整数 1 ~ 5 表示,如果要买归类为附件的物品，必须先买该附件所属的主件（0表示物品）。
在不超过 N 元（可以等于 N 元）的前提下，使每件物品的价格与重要度的乘积的总和最大。*/
class Goods{
	name,price,value,rule;//name：商品名字 price:商品价格 value：商品价值（根据题意由重要度*价格算出） rule：标记物品或者附件（0表示物品，其他数字表示是该数字对应物品的附件）
	Goods(name,price,value,rule){
	}
}
list=[new Goods("电脑",800,2*800,0),new Goods("打印机",400,5*400.0,1),new Goods("扫描仪",300,5*300,1),new Goods("书柜",400,3*400,0),new Goods("书桌",500,2*500,0)];
money = 1000;
result = list.dpKnapsack(money,list.price,1,list.value,()->{
	if(list.get(i-1).rule ==0){
		if(list.get(i-1).price<=money){//如果该物品的价格低于当前钱数表示可以购买
			return true;
		}
	}else{
		if(list.get(i-1).price+list.get(list.get(i-1).rule-1).price<=money){//如果该附件加附件对应的物品一共的价格低于当前钱数则表示可以购买
			return true;
		}
	}
	return false;
});
println("购物单问题:\n"+result);