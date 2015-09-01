#zxorm
	授权协议： Apache

	2015.9.1
	升级2.0版本
	查询更方便,大幅减少sql语句
	增加事务方法域
	增加更多辅助方法

初始化数据库连接

	DBEntity.setDataSource("com.mysql.jdbc.Driver","root","","jdbc:mysql://127.0.0.1:3306/db")
	或者
	val ds = new BasicDataSource()
    ds.setDriverClassName(clazz);
    ds.setUsername(userName);
    ds.setPassword(pwd);
    ds.setUrl(url);
    
    DBEntity.setDataSource(ds)


关联类

	继承并传入表名
		class User(val id:Int= -1,val username:String="",val age:Int= -1) extends BaseDBEntity[User]("User")


使用

    表处理

        创建
            new User().createTable(false)
        清空
            new User().cleanTable
        删除
            new User().deleteTable

	查询
		val user=new User().query("username=? ","livehl")
		val users=new User().queryPage("",1,10,"")
		new User().queryAll()
		new User().queryByIds(1::2::3::Nil)

	增加
		new User(0,"tom",12).insert // id=1,name=tom,age=12
		new User(0,"tomcat",18).insert("name") //id=2,name=tomcat,age=null
		new User(2,"tomcat",18).insertUpdate("age") //id=2,name=tomcat,age=18  特别注意，插入默认情况下id是会被忽略的，请修改 !!DBEntity:34行
	    new User(10,"tom",12).insertWithId // id=10,name=tom,age=12
	修改
		new User(2,"dog",22).update("id","name")//id=2,name=dog,age=18
		new User(2,"tomcat",30).update("id")//id=2,name=tomcat,age=30
		new User(2,"tomcat",30).updateNoEmptyById//id=2,name=tomcat,age=30
	删除
		new User(2,"dog",22).delete("id")
		
		
		
