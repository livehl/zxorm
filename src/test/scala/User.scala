

import java.sql.Timestamp
import cn.livehl.zxorm.DBEntity

class User(val id:Int,val username:String,var nickname:String,val email:String) extends DBEntity("User")


case class CaseUser(id:Int,username:String,nickname:String,email:String)extends DBEntity("User")
		// Fields
//		private	Integer id ;
//		private	String username ;
//		private	String password ;
//		private	String nickname ;
//		private	String description ;
//		private	String email ;
//		private	Boolean emailValid ;
//		private	String cellphoneNo ;
//		private	String realName ;
//		private	String address ;
//		private	String avatar ;
//		private	Integer gender ;
//		private	Timestamp createDate ;
//		private	Timestamp birthdate ;
//		private	Timestamp lastSigninDate ;
//		private	Integer channelId ;
//		private	String deviceCode ;
//		private	String appVersion ;
//		private	String lastIp ;
//		private	Integer point ;
//		private	Integer exp ;
//		private	Integer badgeCount ;
//		private	Integer tipCount ;
//		private	Integer followCount ;
//		private	Integer beFollowCount ;
//		private	Integer checkinCount ;
//		private	Integer mayorshipCount ;
//		private	Integer photoCount ;
//		private	Integer status ;
//		private	Integer stageCode ;
//		private	Boolean isSensored ;
//		private	Timestamp lastUpdate ;
//		private	Integer replyCount ;
//		private	Integer inviter ;
//		private	Boolean isSendSinaInviteMsg ;
//		private	Boolean isSendTencentInviteMsg ;
//		private	Integer deviceType ;
//		private	String background ;
//		private	Integer favCount ;
//		private	Integer feedCount ;
//		private	Boolean isVerify ;
//		private	Integer bagLimit ;
//		private	Integer bagUsed ;
//		private	Integer continuousConnectDays ;
//		private	String avatarDecoration ;
//		private	String additionReaction ;
//		private	Integer praiseCount ;
//		private	Integer notificationSetting ;
//		private Integer bePraiseCount;
//		private Boolean cellphoneValid;
