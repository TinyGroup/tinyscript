#tinyscript
一个注重提升集合类处理操作的脚本语言。

特点：
1.  无缝与java结合，支持创建、调用java对象和方法。
2.  可以与Spring结合，直接访问bean对象
3.  语法简洁，避免用户编写复杂的java代码或SQL语句
4.  强大的IDE工具，支持用户直接写脚本，运行得到结果
5.  灵活的扩展机制，用户可以通过扩展数据模型、函数和相关处理器，增加脚本语言的功能

更详细完整的介绍和使用，请参见文档：http://www.tinygroup.org/docs/6213296363248889992


##项目子工程简介

	├── org.tinygroup.tinyscriptbase                  //脚本语言核心工程，包含词法语法解析，执行引擎设计等
	├── org.tinygroup.tinyscript.collection           //数据模型扩展，包含Array、List、Set和Map
	├── org.tinygroup.tinyscript.dataset              //数据模型扩展，包含数据集(序表)
	├── org.tinygroup.tinyscript.tree                 //数据模型扩展，包含树
	├── org.tinygroup.tinyscript.database             //功能强化，实现数据集在数据库方面的扩展
	├── org.tinygroup.tinyscript.excel                //功能强化，实现数据集在Excel方面的扩展
	├── org.tinygroup.tinyscript.text                 //功能强化，实现数据集在文本方面的扩展
	├── org.tinygroup.tinyscript.datasetwithtree      //数据模型转换，实现数据集和树间的转换
	├── org.tinygroup.tinyscript                      //集合运算语言实现