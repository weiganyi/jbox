set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_80\

rem okcoin依赖包
call mvn install:install-file -Dfile=.\commons-codec-1.7.jar -DgroupId=com.jbox.openlib -DartifactId=commons-codec -Dversion=1.7 -Dpackaging=jar
call mvn install:install-file -Dfile=.\commons-io-2.2.jar -DgroupId=com.jbox.openlib -DartifactId=commons-io -Dversion=2.2 -Dpackaging=jar
call mvn install:install-file -Dfile=.\commons-logging-1.0.4.jar -DgroupId=com.jbox.openlib -DartifactId=commons-logging -Dversion=1.0.4 -Dpackaging=jar
call mvn install:install-file -Dfile=.\httpclient-4.3.6.jar -DgroupId=com.jbox.openlib -DartifactId=httpclient -Dversion=4.3.6 -Dpackaging=jar
call mvn install:install-file -Dfile=.\httpcore-4.3.3.jar -DgroupId=com.jbox.openlib -DartifactId=httpcore -Dversion=4.3.3 -Dpackaging=jar
call mvn install:install-file -Dfile=.\fastjson-1.1.23.jar -DgroupId=com.jbox.openlib -DartifactId=fastjson -Dversion=1.1.23 -Dpackaging=jar
call mvn install:install-file -Dfile=.\jackson-annotations-2.0.2.jar -DgroupId=com.jbox.openlib -DartifactId=jackson-annotations -Dversion=2.0.2 -Dpackaging=jar
call mvn install:install-file -Dfile=.\jackson-core-2.0.2.jar -DgroupId=com.jbox.openlib -DartifactId=jackson-core -Dversion=2.0.2 -Dpackaging=jar
call mvn install:install-file -Dfile=.\jackson-databind-2.0.2.jar -DgroupId=com.jbox.openlib -DartifactId=jackson-databind -Dversion=2.0.2 -Dpackaging=jar

rem log4j相关包
call mvn install:install-file -Dfile=.\log4j-1.2.12.jar -DgroupId=com.jbox.openlib -DartifactId=log4j -Dversion=1.2.12 -Dpackaging=jar
call mvn install:install-file -Dfile=.\slf4j-api-1.6.6.jar -DgroupId=com.jbox.openlib -DartifactId=slf4j-api -Dversion=1.6.6 -Dpackaging=jar
call mvn install:install-file -Dfile=.\slf4j-log4j12-1.6.6.jar -DgroupId=com.jbox.openlib -DartifactId=slf4j-log4j12 -Dversion=1.6.6 -Dpackaging=jar

rem mysql客户端包
call mvn install:install-file -Dfile=.\mysql-connector-java-5.1.27.jar -DgroupId=com.jbox.openlib -DartifactId=mysql-connector-java -Dversion=5.1.27 -Dpackaging=jar

rem xml解析相关包
call mvn install:install-file -Dfile=.\jaxen-1.1.6.jar -DgroupId=com.jbox.openlib -DartifactId=jaxen -Dversion=1.1.6 -Dpackaging=jar
call mvn install:install-file -Dfile=.\dom4j-1.6.1.jar -DgroupId=com.jbox.openlib -DartifactId=dom4j -Dversion=1.6.1 -Dpackaging=jar

exit
