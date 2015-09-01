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
            new User().createTable
        清空
            new User().cleanTable
        删除
            new User().deleteTable

	查询
		val user=new User().query("username=? ","livehl")
		val (allCount,users)=new User().queryPage("",1,10,"")
		new User().queryAll()
		new User().queryByIds(1::2::3::Nil)

	增加
		new User(0,"tom",12).insert // id=1,name=tom,age=12
		new User(0,"tomcat",18).insert("name") //id=2,name=tomcat,age=null
		new User(2,"tomcat",28).insertUpdate("age") //id=2,name=tomcat,age=28
	    new User(10,"tom",12).insertWithId // id=10,name=tom,age=12
	修改
		new User(2,"dog",22).update("id","name")//id=2,name=dog,age=18
		new User(2,"tomcat",30).update("id")//id=2,name=tomcat,age=30
		new User(2,"tomcat",30).updateNoEmptyById//id=2,name=tomcat,age=30
	删除
		new User(2,"dog",22).delete("id")

    事务
        DBEntity.transaction{
            val u1=new User(0,"livehl","子轩","livehl@126.com").insert()
            val u2=new User(0,"livehl1","子轩1","livehl@123.com").insert()
          //一大啪啦涉及事务的处理过程
          //  特别注意  阿里巴巴的DRDS分库分表的数据库要求执行事务的操作必须在同一台机器上
          //不用看了,下面不需要处理什么东西,只要在方法域内都是事务范围,执行完毕后自动提交
        }{ex=>
          //事务执行出错了,会自动回滚,这里不用操心,只做逻辑处理
          println(ex.getMessage)
        }
		
		
