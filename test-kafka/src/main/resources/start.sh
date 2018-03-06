## start shell
java -Denv=./ -jar Kafak2Mysql.jar 

## copy data from hadoop to local
hadoop fs -copyToLocal /tmp/Kafka2Mysql.jar ./

