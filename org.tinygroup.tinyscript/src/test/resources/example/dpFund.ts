class Product{
	name,amountPerServing ,maxCount,rate;
	Product(name,amountPerServing,maxCount,rate){
	}
}
//============================================================================

days=80;
list=[new Product("鹏华国防",100,10,0.00045),new Product("鹏华中证",100,20,0.00035),new Product("国投瑞银",100,20,0.00055),new Product("华商主题精选",100,10,0.0004),new Product("金鹰智慧",100,5,0.0003)];
println(list.dpKnapsack(5000,list.amountPerServing,list.maxCount,()->{
	return [0.00045,0.00035,0.00055,0.00040,0.00030]*days*100;
}));