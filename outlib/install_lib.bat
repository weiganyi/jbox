set JAVA_HOME=C:\Program Files\Java\jdk1.7.0_80\

call mvn install:install-file -Dfile=.\commons-codec-1.7.jar -DgroupId=com.jbox.outlib -DartifactId=commons-codec -Dversion=1.7 -Dpackaging=jar
call mvn install:install-file -Dfile=.\commons-io-2.2.jar -DgroupId=com.jbox.outlib -DartifactId=commons-io -Dversion=2.2 -Dpackaging=jar
call mvn install:install-file -Dfile=.\commons-logging-1.0.4.jar -DgroupId=com.jbox.outlib -DartifactId=commons-logging -Dversion=1.0.4 -Dpackaging=jar
call mvn install:install-file -Dfile=.\fastjson-1.1.23.jar -DgroupId=com.jbox.outlib -DartifactId=fastjson -Dversion=1.1.23 -Dpackaging=jar
call mvn install:install-file -Dfile=.\httpclient-4.3.6.jar -DgroupId=com.jbox.outlib -DartifactId=httpclient -Dversion=4.3.6 -Dpackaging=jar
call mvn install:install-file -Dfile=.\httpcore-4.3.3.jar -DgroupId=com.jbox.outlib -DartifactId=httpcore -Dversion=4.3.3 -Dpackaging=jar
call mvn install:install-file -Dfile=.\jackson-annotations-2.0.2.jar -DgroupId=com.jbox.outlib -DartifactId=jackson-annotations -Dversion=2.0.2 -Dpackaging=jar
call mvn install:install-file -Dfile=.\jackson-core-2.0.2.jar -DgroupId=com.jbox.outlib -DartifactId=jackson-core -Dversion=2.0.2 -Dpackaging=jar
call mvn install:install-file -Dfile=.\jackson-databind-2.0.2.jar -DgroupId=com.jbox.outlib -DartifactId=jackson-databind -Dversion=2.0.2 -Dpackaging=jar

call mvn install:install-file -Dfile=.\log4j-1.2.12.jar -DgroupId=com.jbox.outlib -DartifactId=log4j -Dversion=1.2.12 -Dpackaging=jar
call mvn install:install-file -Dfile=.\slf4j-api-1.6.6.jar -DgroupId=com.jbox.outlib -DartifactId=slf4j-api -Dversion=1.6.6 -Dpackaging=jar
call mvn install:install-file -Dfile=.\slf4j-log4j12-1.6.6.jar -DgroupId=com.jbox.outlib -DartifactId=slf4j-log4j12 -Dversion=1.6.6 -Dpackaging=jar

call mvn install:install-file -Dfile=.\mysql-connector-java-5.1.27.jar -DgroupId=com.jbox.outlib -DartifactId=mysql-connector-java -Dversion=5.1.27 -Dpackaging=jar

exit