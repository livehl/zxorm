import cn.livehl.zxorm.DBEntity

object Example {

  def main(args: Array[String]): Unit = {
    //初始化连接
    DBEntity.setDataSource("com.mysql.jdbc.Driver","icdDba","icdDba","jdbc:mysql://192.168.2.4:3303/inChengdu3")

    //查询
    val user=DBEntity.queryOne(classOf[User],"select * from User where username=? ","livehl")
    println(user)
    val users=DBEntity.query(classOf[CaseUser], "select * from User limit 10")
    println(users)

    //插入
    val id=new User(0,"livehl","子轩","livehl@126.com").insert() // id=1,name=tom,age=12
    println(id)
    val id1=new User(0,"livehl1","子轩1","livehl@123.com").insert("username") //id=2,name=tomcat,age=null
    println(id1)
    //更新
    val newUser=DBEntity.queryById(classOf[User], "User", id).get
    println(DBEntity.queryById(classOf[CaseUser], "User", id).get)
    newUser.nickname="livehl2"
    newUser.update("id", "nickname")
    println(DBEntity.queryById(classOf[CaseUser], "User", id).get)
    
    //删除
    new User(id,"","","").delete("id")
    
    new User(id1,"","","").delete("id")
    
  }

}