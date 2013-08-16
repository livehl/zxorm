name := "zxorm"
 
version := "0.0.1"
 
scalaVersion := "2.10.0"
 
libraryDependencies ++= Seq(
"mysql" % "mysql-connector-java" % "5.1.24",
 "commons-dbutils" % "commons-dbutils" % "1.4",
 "org.javassist" % "javassist" % "3.17.1-GA",
 "commons-dbcp" % "commons-dbcp" % "1.4"
)