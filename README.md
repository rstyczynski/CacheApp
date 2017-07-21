# CacheApp
Exemplary Coherence Cache App


# Build exemplary application


```bash
CacheAppBase=>>>project directory put here<<<
cd $CacheAppBase

cd CacheConfig
mvn clean install
cd ..

cd CacheNode
mvn clean 
mvn install
mvn package 
cd ..

cd CacheWebClient
mvn package
cd ..

```

# Deploy your artifacts

Copy coherence-spring-2.0.0-SNAPSHOT.jar to WebLogic $DOMAIN directory
```bash
ls ~/.m2/repository/com/oracle/coherence/spring/coherence-spring/2.0.0-SNAPSHOT/coherence-spring-2.0.0-SNAPSHOT.jar 
```

Copy CacheConfig-1.0.0-SNAPSHOT.jar to WebLogic $DOMAIN directory
```bash
ls $CacheAppBase/CacheConfig/target/*.jar
```

Deploy CacheNode-1.0.0-SNAPSHOT.gar on WebLogic storage enabled nodes
```bash
ls $CacheAppBase/CacheNode/target/*.gar
```

Deploy CacheWebClient-1.0.0-SNAPSHOT.gar on WebLogic storage disabled nodes
```bash
ls $CacheAppBase/CacheWebClient/target/*.war
```

