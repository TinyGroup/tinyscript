class Obj{
	name,weight,value;
	Obj(name,weight,value){
	}
}

list=[new Obj("a",2,6.0),new Obj("b",2,3.0),new Obj("c",6,5.0),new Obj("d",5,4.0),new Obj("e",4,6.0)];
map = map(list.name,list.weight);
println(map);