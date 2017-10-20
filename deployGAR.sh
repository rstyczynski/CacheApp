java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -remote -upload -deploy CacheGARApp/target/CacheGARApp-1.0-SNAPSHOT.ear  -targets Server-0

java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -remote -upload -deploy CacheGARAppAlt/target/CacheGARAppAlt-1.0-SNAPSHOT.ear  -targets Server-0

