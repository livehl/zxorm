package cn.livehl.zxorm

import java.sql.{ SQLException, Statement }
import java.util.Date

import collection.JavaConversions._

import org.apache.commons.dbcp.BasicDataSource
import org.apache.commons.dbutils.QueryRunner
import org.apache.commons.dbutils.handlers.MapListHandler

import ReflectTool.getConstructorParamNames

class DBEntity(val tableName: String) {
  private lazy val created = getConstructorParamNames(this.getClass())
  private lazy val methods = this.getClass().getMethods() filter { m => !m.getName().contains("_") && m.getParameterTypes().length == 0 }
  private def getFiledValue(key: String) = {
    methods.filter(_.getName() == key)(0).invoke(this)
  }
  def getFieldKeyValues(fields: String*) = {
    (if (fields.size == 0) {
      getConstructorParamNames(this.getClass()).maxBy(_._2.length)._2 map (_._1)
    } else {
      fields.toList
    }) map (f => f -> methods.filter(_.getName() == f)(0).invoke(this)) filter (_._2 != null) map (kv => kv._1 -> (kv._2 match {
      case b: java.lang.Boolean => if (b) 1 else 0
      case s: String => s
      case d: Date => TimeTool.getFormatStringByDate(d)
      case bd: BigDecimal => bd.toDouble
      case o: Any => o
    }))
  }
  private def getInstallSqlAndparams(fields: String*) = {
    val fieldKVs = getFieldKeyValues(fields: _*) filterNot (_._1 == "id")
    ((s"insert INTO $tableName (${fieldKVs map (_._1) mkString (",")}) values (${fieldKVs map (v => if (v._2.isInstanceOf[String]) "?" else v._2) mkString (",")})") ->
      (fieldKVs.filter(_._2.isInstanceOf[String]).map(_._2.asInstanceOf[String])))
  }
  private def getInstallSqlWithUpdate(field: String, fields: String*) = {
    val (sql, params) = getInstallSqlAndparams(fields: _*)
    ((sql + s" ON DUPLICATE KEY UPDATE $field =?") -> (params ::: (getFiledValue(field).toString) :: Nil))
  }
  private def getDeleteIdSql(id: Int) = {
    s"delete $tableName where id=$id"
  }
  def update(where: String, fields: String*) {
    val whereValue = getFiledValue(where).toString
    val kvs = getFieldKeyValues(fields: _*)
    val params = kvs filter (_._2.isInstanceOf[String]) map (_._2.asInstanceOf[String])
    DBEntity.sql(s"update $tableName set ${kvs map (kv => if (kv._2.isInstanceOf[String]) kv._1 + "=?" else kv._1 + "=" + kv._2 + "") mkString (",")} where $where =?", (params ::: whereValue :: Nil): _*)
  }
  def insert(fields: String*) = {
    DBEntity.insert(this, fields: _*)
  }
  def insertUpdate(updateField: String, fields: String*) {
    DBEntity.insertUpdate(this, updateField, fields: _*)
  }
  def delete(where: String) {
    DBEntity.delete(this, where)
  }
  def queryById(id: Long): Option[_ <: DBEntity] = {
    val l = DBEntity.apply(this.getClass(), s"select * from $tableName where id=$id")
    if (l.length == 0) {
      None
    } else {
      Some(l(0))
    }
  }
  def queryOne(sql: String, param: String*): Option[_ <: DBEntity] = {
    val l = DBEntity.apply(this.getClass(), sql, param: _*)
    if (l.length == 0) {
      None
    } else {
      Some(l(0))
    }
  }
  def queryMap(sql: String, param: String*): List[Map[String, Object]] = {
    DBTool.queryDataMap(sql, param: _*) map (_.toMap) toList
  }

}

object DBEntity {
  def setDataSource(ds: BasicDataSource) {
    DBTool.dataSource = ds
  }
  def setDataSource(clazz: String, userName: String, pwd: String, url: String) {
    val ds = new BasicDataSource()
    ds.setDriverClassName(clazz);
    ds.setUsername(userName);
    ds.setPassword(pwd);
    ds.setUrl(url);
    DBTool.dataSource = ds
  }
  /*从map 构造一个实例*/
  def apply[T](clazz: Class[_ <: T], map: Map[String, Object]): T = {
    val created = getConstructorParamNames(clazz).maxBy(_._2.length)
    val params = created._2 map { name_type =>
      val value = map.getOrElse(name_type._1, null)
      val t = name_type._2
      if (null != value && (value.getClass().isInstance(t) || value.getClass() == t)) {
        value
      } else {
        t.getName match {
          case "java.sql.Date" => new java.sql.Date(value.asInstanceOf[java.util.Date].getTime())
          case "java.sql.Time" => new java.sql.Time(value.asInstanceOf[java.util.Date].getTime())
          case "java.sql.Timestamp" => new java.sql.Timestamp(value.asInstanceOf[java.util.Date].getTime())
          case "java.lang.String" => value.toString
          case "scala.math.BigDecimal" => BigDecimal(value.toString)
          case "boolean" => Boolean.box(value.asInstanceOf[Int] == 1)
          case _ => value
        }
      }
    }
    created._1.newInstance(params: _*).asInstanceOf[T]
  }
  /*从sql构造一个列表*/
  def apply[T](clazz: Class[_ <: T], sql: String, param: String*): List[T] = {
    DBTool.queryDataMap(sql, param: _*) map { m => apply[T](clazz, m.toMap) } toList
  }
  def query[T](clazz: Class[_ <: T], sql: String, param: String*): List[T] = {
    apply[T](clazz, sql, param: _*)
  }
  def queryOne[T](clazz: Class[_ <: T], sql: String, param: String*): Option[T] = {
    val l = apply[T](clazz, sql, param: _*)
    if (l.length == 0) {
      None
    } else {
      Some(l(0))
    }
  }
  def queryById[T](clazz: Class[_ <: T], tableName: String, id: Long): Option[T] = {
    val l = apply[T](clazz, s"select * from $tableName where id=$id")
    if (l.length == 0) {
      None
    } else {
      Some(l(0))
    }
  }
  /*插入数据，默认以最长的构造函数的参数作为插入字段*/
  def insert(entity: DBEntity, fields: String*) = {
    val (sql, params) = entity.getInstallSqlAndparams(fields: _*)
    DBTool.insert(sql, params: _*)
  }
  /*插入数据重复后更新字段*/
  def insertUpdate(entity: DBEntity, updateField: String, fields: String*) {
    val (sql, params) = entity.getInstallSqlWithUpdate(updateField, fields: _*)
    DBTool.update(sql, params: _*)
  }
  def delete(entity: DBEntity, where: String) {
    val whereValue = entity.getFiledValue(where).toString
    DBTool.update(s"delete from ${entity.tableName} where $where = ?", whereValue)
  }
  def sql(sql: String, params: String*) {
    DBTool.update(sql, params: _*)
  }
}

private object DBTool {
  var dataSource: BasicDataSource = _

  def insert(sql: String, params: Any*) = {
    val conn = dataSource.getConnection()
    val st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
    if (null != params && params.length > 0) {
      for (i <- 0 until params.length) {
        st.setString(i + 1, params(i).toString())
      }
    }
    try {
      st.executeUpdate()
      val rs = st.getGeneratedKeys()
      if (rs.next()) rs.getInt(1) else -1
    } catch {
      case e: SQLException => throw new DetailSQLException(e, sql)
    } finally {
      st.close()
      conn.close()
    }
  }
  def update(sql: String, params: Any*) = {
    val conn = dataSource.getConnection()
    val st = conn.prepareStatement(sql)
    if (null != params && params.length > 0) {
      for (i <- 0 until params.length) {
        st.setString(i + 1, params(i).toString())
      }
    }
    try {
      if (st.execute()) {
        st.getUpdateCount()
      } else {
        0
      }
    } catch {
      case e: SQLException => throw new DetailSQLException(e, sql)
    } finally {
      st.close()
      conn.close()
    }
  }
  def queryDataMap(sql: String, params: AnyRef*) = {
    val conn = dataSource.getConnection()
    try {
      new QueryRunner().query(conn, sql, new MapListHandler(), params: _*)
    } catch {
      case e: SQLException => throw new DetailSQLException(e, sql)
    } finally {
      conn.close()
    }
  }
}