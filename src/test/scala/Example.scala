import cn.livehl.zxorm.DBEntity

object Example {

  def main(args: Array[String]): Unit = {
    //TODO 为了兼容此测试用例,增加了sqlite的驱动\以及部分兼容
    //不需要请删除以下地方
    //DBEntity.scala  216 行   删除代码段 else if(value.isInstanceOf[String])  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(value.asInstanceOf[String])
    //DBEntity.scala                78行     删除方法   getCreateSqlSqlite
    //DBEntity.scala                92行     修改方法   createTable 删除参数   替换93行   DBEntity.sql(getCreateSqlMysql)


    //初始化连接
//    DBEntity.setDataSource("com.mysql.jdbc.Driver","icdDba","icdDba","jdbc:mysql://192.168.2.4:3303/inChengdu3")
    DBEntity.setDataSource("org.sqlite.JDBC","","","jdbc:sqlite::memory:")
    new User().createTable(false)

    //插入
    val id=new User(0,"livehl","子轩","livehl@126.com").insert() // id=1,name=tom,age=12
    println(id)
    val id1=new User(0,"livehl1","子轩1","livehl@123.com").insert("username") //id=2,name=tomcat,age=null
    println(id1)



    //查询
    val user=new User().query("username=? ","livehl")
    println(user.size)
    //分页查询
    val users=new User().queryPage("",1,10,"")
    println(users)
    //按id查询并且检查数据是否存在
    println(new User().queryById(id).dbCheck)

    //更新 update nickname   email  by id
    val u=new User(id,nickname = "狗蛋",email="goudan@126.com")

      u.update("id","nickname")
    println(new User().queryById(id).dbCheck)

    u.update("id","email")
    println(new User().queryById(id).dbCheck)
    //按id更新所有不为空的字段 update nickname email by id
    new User(id,"","子轩","livehl@126.com").updateNoEmptyById()

    
    //删除
    new User(id).delete("id")
    
    new User(id1).delete("id")

    //事务
    DBEntity.transaction{
        val u1=new User(0,"livehl","子轩","livehl@126.com").insert()
        val u2=new User(0,"livehl1","子轩1","livehl@123.com").insert()
      ///一大啪啦涉及事务的处理过程
      //  特别注意  阿里巴巴的DRDS分库分表的数据库要求执行事务的操作必须在同一台机器上
      //不用看了,下面不需要处理什么东西,只要在方法域内都是事务范围
    }{ex=>
      //事务执行出错了
      println(ex.getMessage)
    }

    new User().cleanTable()
    new User().deleteTable()
  }

}