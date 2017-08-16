//ds = dynamicDataSource.query("select * from test1");
//println(ds.getRows());

//设置数据库类型(parsedSql底层需要)
defaultDB="H2";
id = 3;

//通过 beanName [[ sql ]] 获取ds实例
//newds = dynamicDataSource[[ select * from test1 where id > @id ]];

//通过  [[ sql ]] 获取ds实例
//可以直接在上下文定义bean,也可以在application.xml全局配置定义
//customBean = "dynamicDataSource";
newds = [[ select * from test1 where id > @id ]];

//输出结果记录
while(newds.next()){
  println(newds.getData("id")+" "+newds.getData("fullname"));
}