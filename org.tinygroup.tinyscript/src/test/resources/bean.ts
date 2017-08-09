//ds = dynamicDataSource.query("select * from test1");
//println(ds.getRows());
defaultDB="H2";
id = 3;
newds = dynamicDataSource[[ select * from test1 where id > @id ]];
println(newds);