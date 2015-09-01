name := "zxorm"
 
version := "2.0.0"
 
scalaVersion := "2.10.4"
 
libraryDependencies ++= Seq(
"mysql" % "mysql-connector-java" % "5.1.24" % "test",
 "commons-dbutils" % "commons-dbutils" % "1.5",
 "org.javassist" % "javassist" % "3.17.1-GA",
 "commons-dbcp" % "commons-dbcp" % "1.4",
  "org.xerial" % "sqlite-jdbc" % "3.8.11" % "test",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.5.2"
)