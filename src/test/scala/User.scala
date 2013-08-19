

import java.sql.Timestamp
import cn.livehl.zxorm.DBEntity

class User(val id:Int,val username:String,var nickname:String,val email:String) extends DBEntity("User")


case class CaseUser(id:Int,username:String,nickname:String,email:String)extends DBEntity("User")
