java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -name CacheWebApp-1.0-SNAPSHOT -undeploy -targets Server-0
java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -name CacheWebAppAlt-1.0-SNAPSHOT -undeploy -targets Server-0

java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -remote -upload -deploy CacheWebApp/target/CacheWebApp-1.0-SNAPSHOT.ear  -targets Server-0
java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -remote -upload -deploy CacheWebAppAlt/target/CacheWebAppAlt-1.0-SNAPSHOT.ear  -targets Server-0
