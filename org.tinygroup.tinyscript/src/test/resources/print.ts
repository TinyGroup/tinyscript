
/*
     示例脚本类,演示动态调用
*/
class Model{
    
    printDataSet(path){
       dataSet = readTxt(path);
       //测试方法内输出
       fields = dataSet.getFields();
       for(field:fields){
          System.out.println("field="+field.getName());
       }
    }
    
    printList(){
      list1 = ["aaa","bbb","ccc","ddd"];
      list2 = ["bbb","ddd","eee","fff"];
      System.out.println(list1+list2);  //并集
      System.out.println(list1-list2);  //差集
      System.out.println(list1&list2);  //交集
      System.out.println(list1^list2);  //相对差(异或)
    }
    
    //测试返回值
    createArray(){
       ss = new String[]{"人法地","地法天","天法道","道法自然"};
       return ss;
    }
}

//具体执行逻辑
model = new Model();
model.printDataSet("/Orders.txt");
//model.printDataSet("D:/workspace/tinyscript/org.tinygroup.tinyscript/src/test/resources/Orders.txt");
model.printList();
array = model.createArray();
for(s:array){
  System.out.println(s);
}