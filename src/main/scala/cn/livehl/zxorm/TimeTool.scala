package cn.livehl.zxorm
import java.util.Date
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.TimeZone
object TimeTool {
  def getFormatStringByNow() = {
    getFormatStringByDate(new Date())
  }
  def getFormatStringByDate(date: Date): String = {
    if (null == date) {
      null
    } else {
      new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date)
    }
  }
  def getDayString(date: Date=null,exp:String=null)={
    val firstDate=
      if (date==null){
    	  new Date()
	    }else{
	      date
	    }
    val secDate=if (null!=exp){
      getAfterDate(exp, firstDate)
    }else{
      firstDate
    }
    new SimpleDateFormat("yyyy-MM-dd").format(secDate)
  }
  def getAfterDate(strExp: String, date: Date = new Date()) = {
    val exp = strExp.substring(strExp.length() - 1);
    val add = Integer.valueOf(strExp.substring(0, strExp.length() - 1));
    val cal = Calendar.getInstance();
    cal.setTime(date);
    exp.trim match {
      case "s" => cal.add(Calendar.SECOND, add)
      case "m" => cal.add(Calendar.MINUTE, add)
      case "H" => cal.add(Calendar.HOUR, add)
      case "d" => cal.add(Calendar.DAY_OF_MONTH, add)
      case "M" => cal.add(Calendar.MONTH, add)
      case "y" => cal.add(Calendar.YEAR, add)
    }
    cal.getTime()
  }
}