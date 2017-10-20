java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -name CacheGARApp-1.0-SNAPSHOT -undeploy -targets Server-0
java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -name CacheGARAppAlt-1.0-SNAPSHOT -undeploy -targets Server-0

. deployGAR.sh
