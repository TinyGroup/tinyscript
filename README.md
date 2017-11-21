最新版本号：
=========================
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.tinygroup/tinyscript/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.tinygroup/tinyscript)

#tinyscript
一个注重提升集合类处理操作的脚本语言。

特点：
1.  无缝与java结合，支持创建、调用java对象和方法。
2.  可以与Spring结合，直接访问bean对象
3.  语法简洁，避免用户编写复杂的java代码或SQL语句
4.  强大的IDE工具，支持用户直接写脚本，运行得到结果
5.  灵活的扩展机制，用户可以通过扩展数据模型、函数和相关处理器，增加脚本语言的功能
6.  友好的语法提示信息，用户可以准确定位语法错误位置及运行异常原因
7.  日志和错误提示支持国际化

更详细完整的介绍和使用，请参见文档：http://www.tinygroup.org/docs/3625676061396790338


#项目子工程简介

	├── org.tinygroup.tinyscriptbase                  //脚本语言核心工程，包含词法语法解析，执行引擎设计等
	├── org.tinygroup.tinyscript.collection           //数据模型扩展，包含Array、List、Set和Map
	├── org.tinygroup.tinyscript.dataset              //数据模型扩展，包含数据集(序表)
	├── org.tinygroup.tinyscript.tree                 //数据模型扩展，包含树
	├── org.tinygroup.tinyscript.database             //功能强化，实现数据集在数据库方面的扩展
	├── org.tinygroup.tinyscript.excel                //功能强化，实现数据集在Excel方面的扩展
	├── org.tinygroup.tinyscript.text                 //功能强化，实现数据集在文本方面的扩展
	├── org.tinygroup.tinyscript.datasetwithtree      //数据模型转换，实现数据集和树间的转换
	├── org.tinygroup.tinyscript.template             //Tiny模板语言扩展
	├── org.tinygroup.tinyscript                      //TinyScript具体实现
	
#升级历史
版本1.2.0：
[新增] 增加序表insert函数,支持lambda语法，允许数据来自别的序表
[新增] 增加序表和json互转函数：toJson和jsonToDataSet
[新增] 增加序表和xml互转函数：toXml和xmlToDataSet
[新增] 增加一系列日期加强函数：dateAdd、dateName、datePart、dateTrunc、day、makeDate、makeDateTime、month、now、today和year

[删除] 删除序表limit函数，可以采用sub函数取代
[删除] 删除序表updateField函数，可以采用update函数取代

[重构] tinyscript脚本后缀由*.tinyscript和*.ts统一调整为*.tsf,包括处理器、配置及测试脚本及测试用例
[重构] 实现多级分组,底层实现使用MultiLevelGroupDataSet取代DefaultGroupDataSet
[重构] 简化日期函数,参数范围仅限于Date类型
[重构] 重构关联函数,函数命名调整:leftjoin改为joinLeft,rightjoin改为joinRight,fulljoin改为joinFull
[重构] 序表集合操作逻辑调整：记录以A表为准，允许B表字段范围包含A表字段，不再强制要求两表字段完全一致
[重构] 修改sub和subGroup函数逻辑
[重构] 修改update函数，优化内部结构

[Bug]  修改序表排序包含特殊字符报错的Bug，同时调整缺省方向为asc
[Bug]  修改下标为0时某些函数发生越界异常的Bug