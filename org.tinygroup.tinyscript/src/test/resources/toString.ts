/*
   演示不同的toString机制
*/
class User{
    name,age;
    User(name,age){
        this.name=name;
        this.age=age;
    }
    toString(){
        return "姓名：${name},年龄:${age}";
    }
}
user = new User("悠然",18);
println(user);              //机制一：java内部调用脚本实例的toString方法
println(user.toString());   //机制二：脚本引擎直接调用脚本类的toString方法
