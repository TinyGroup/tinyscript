dataSet1 = readTxt("/example/own_school_org.txt");
dataSet2 = readTxt("/example/edu_grade.txt");
//==============================================================================

result = dataSet2.match(dataSet1,class_code==class_name).group("outid","grade_level").insertColumn("total_num").countGroup("score");
for(i = 1;i<=result.getRows();i++){
	result[i][16] = result[i][17];
}
passresult = result.filterGroup(()->{return (score[0] in ["中", "通过", "良", "优", "及格", "合格"])||(int(score[0])>=60&&int(score[0])<=100);});
passresult.countGroup("score");


println("grade_level\toutid\tpass_num\ttotal_num");
for(i = 1;i<=result.getRows();i++){
	println(result[i]["grade_level"]+"    "+result[i]["outid"]+"    "+passresult[i][17]+"    "+result[i][16]);	
}