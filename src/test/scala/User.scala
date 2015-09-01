import java.sql.Timestamp
import java.sql.Date
import cn.livehl.zxorm.{BaseDBEntity}



class User(val id:Int= -1,val username:String="",val nickname:String="",val email:String="", val createDate: Date=new Date(System.currentTimeMillis())) extends BaseDBEntity[User]("User")
