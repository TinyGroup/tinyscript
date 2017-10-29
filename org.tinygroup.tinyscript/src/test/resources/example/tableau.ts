println(sign(-1.1));//返回数字的符号
println(radians(30));//角度转弧度
println(log(100,10));//对数（10为底，100的对数）
println(ln(exp(2)));//自然对数
println(exp(2));//自然指数
println(degrees(radians(30)));//弧度转角度
println(cot(15));//余切
println(atan2(5,5));//反正切，弧度表示
list=[1..5];
println(sampStdevp(list));//标准差（样本）
println(sampVarp(list));//方差（样本）
println(percentile(list,0.5));//百分位

dataSet = readTxt("/example/history.txt").group("code2");
println(dataSet.countAll("firstdt"));//统计个数
println(dataSet.sampStdevp("firstdt"));
println(dataSet.sampVarp("firstdt"));
println(dataSet.percentile("firstdt",0.5));

println(dataSet.countAllGroup("firstdt"));//统计个数
println(dataSet.sampStdevpGroup("firstdt"));
println(dataSet.sampVarpGroup("firstdt"));
println(dataSet.percentileGroup("firstdt",0.5));