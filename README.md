#zxorm
	授权协议： Apache

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
		class User(val id:Int,val name:String,var age:Int) extends DBEntity("Users")

使用

	查询
		DBEntity.queryOne(classOf[User],"select * from User where name=? ",name)
		DBEntity.query(classOf[User], "select * from Users)")

	增加
		new User(0,"tom",12).insert // id=1,name=tom,age=12
		new User(0,"tomcat",18).insert("name") //id=2,name=tomcat,age=null
		new User(2,"tomcat",18).insertUpdate("age") //id=2,name=tomcat,age=18  特别注意，插入默认情况下id是会被忽略的，请修改 !!DBEntity:34行
	修改
		new User(2,"dog",22).update("id","name")//id=2,name=dog,age=18
		new User(2,"tomcat",30).update("id")//id=2,name=tomcat,age=30
	删除
		new User(2,"dog",22).delete("id")
		
		
		
