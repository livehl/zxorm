package cn.livehl.zxorm

import java.text.SimpleDateFormat
import java.util.Date

import com.fasterxml.jackson.databind.{SerializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

import scala.collection.mutable

/**
 * Created by 林 on 14-3-26.
 */

class BaseDBEntity[+Self <: BaseDBEntity[Self]](tableName: String) extends DBEntity(tableName) {
  def toJson: String = {
    BaseDBEntity.map.writeValueAsString(this)
  }

  def toMap:Map[String,Any] = {
    BaseDBEntity.map.readValue(toJson, Map[String,Any]().getClass).asInstanceOf[Map[String,Any]]
  }

  def toHashMap:mutable.HashMap[String,Any] = {
    BaseDBEntity.map.readValue(toJson, mutable.HashMap[String,Any]().getClass).asInstanceOf[mutable.HashMap[String,Any]]
  }

  def fromJson(json: String): Self = {
    BaseDBEntity.map.readValue(json, this.getClass).asInstanceOf[Self]
  }

  //将对应的更新类转为实体类
  def changeUpdateBean(): Self = {
    fromJson(toJson)
  }

  override def queryById(id: Long): Option[Self] = {
    super.queryById(id) map (_.asInstanceOf[Self])
  }

  override def queryByIds(ids: List[Int]): List[Self] = {
    super.queryByIds(ids) map (_.asInstanceOf[Self])
  }


  override def queryOne(sql: String, param: String*): Option[Self] = {
    super.queryOne(s"select * from $tableName where " + sql, param: _*) map (_.asInstanceOf[Self])
  }

  override def queryAll(): List[Self] = {
    super.queryAll map (_.asInstanceOf[Self])
  }

  override def query(where: String, param: String*): List[Self] = {
    super.query(where, param: _*) map (_.asInstanceOf[Self])
  }

  //这个接口需要传条件、排序
  override def queryPage(where: String, pageNum: Int, pageSize: Int, order: String, param: String*): (Int, List[Self]) = {
    val (count, list) = super.queryPage(where, pageNum, pageSize, order, param: _*)
    (count, list map (_.asInstanceOf[Self]))
  }

}

object BaseDBEntity {
  private val map = new ObjectMapper() with ScalaObjectMapper
  map.registerModule(DefaultScalaModule)
  map.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
  map.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))


  //自动检查数据的查询结果是否存在
  implicit class DBOptionAdd[T <: DBEntity](o: Option[T]) {
    def dbCheck: T = if (o.isEmpty) throw new DataNoFindExcepiton else o.get
  }
}

