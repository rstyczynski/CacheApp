# CacheApp
Exemplary Coherence Cache App


# Build exemplary application


```bash
CacheAppBase=>>>project directory put here<<<
cd $CacheAppBase

cd CacheConfig
mvn clean 
mvn install
mvn package 
cd ..

cd CacheNode
mvn clean 
mvn install
mvn package 
cd ..

cd CacheWebClient
mvn clean 
mvn package
cd ..

```

# Know your artifacts

```bash
function showAll {
ls ~/.m2/repository/com/oracle/coherence/spring/coherence-spring/2.0.0-SNAPSHOT/coherence-spring-2.0.0-SNAPSHOT.jar 
ls $CacheAppBase/CacheConfig/target/*.jar
ls $CacheAppBase/CacheNode/target/*.gar
ls $CacheAppBase/CacheWebClient/target/*.war
}

showAll
```


# Deploy your artifacts


1. Add Spring do domain directory

cd $DOMAIN
wget http://maven.springframework.org/release/org/springframework/spring/4.3.8.RELEASE/spring-framework-4.3.8.RELEASE-dist.zip  
unzip spring-framework-4.3.8.RELEASE-dist.zip 


1. Copy coherence-spring-2.0.0-SNAPSHOT.jar to WebLogic $DOMAIN directory
2. Copy CacheConfig-1.0.0-SNAPSHOT.jar to WebLogic $DOMAIN directory

3. Modify servers classpath

/u01/oracle/fmw12.2.1/config/domains/cohmt_domain/*:/u01/oracle/fmw12.2.1/config/domains/cohmt_domain/spring-framework-4.3.8.RELEASE/libs/*

3. Restart app and cache nodes


3. Deploy CacheNode-1.0.0-SNAPSHOT.gar on WebLogic storage enabled nodes

####<Jul 21, 2017, 1:03:27,987 PM UTC> <Info> <J2EE> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '6' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001b> <1500642207987> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-160151> <Registered library Extension-Name: CacheNode-1 (JAR).> 
####<Jul 21, 2017, 1:04:15,253 PM UTC> <Info> <Health> <machine3> <CacheServer-4> <weblogic.GCMonitor> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000016> <1500642255253> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-310002> <50% of the total memory in the server is free.> 
####<Jul 21, 2017, 1:04:33,51 PM UTC> <Info> <CONCURRENCY> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273051> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162601> <Creating ContextService "DefaultContextService" (partition="DOMAIN", module="null", application="CacheNode-1.0-SNAPSHOT")> 
####<Jul 21, 2017, 1:04:33,51 PM UTC> <Info> <CONCURRENCY> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273051> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162600> <Creating ManagedThreadFactory "DefaultManagedThreadFactory" (partition="DOMAIN", module="null", application="CacheNode-1.0-SNAPSHOT")> 
####<Jul 21, 2017, 1:04:33,52 PM UTC> <Info> <CONCURRENCY> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273052> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162610> <Creating ManagedExecutorService "DefaultManagedExecutorService" (partition="DOMAIN", module="null", application="CacheNode-1.0-SNAPSHOT", workmanager="default")> 
####<Jul 21, 2017, 1:04:33,56 PM UTC> <Info> <CONCURRENCY> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273056> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162611> <Creating ManagedScheduledExecutorService "DefaultManagedScheduledExecutorService" (partition="DOMAIN", module="null", application="CacheNode-1.0-SNAPSHOT", workmanager="default")> 
####<Jul 21, 2017, 1:04:33,57 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273057> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheNode-1.0-SNAPSHOT of application CacheNode-1.0-SNAPSHOT is transitioning from STATE_NEW to STATE_PREPARED on server CacheServer-4.> 
####<Jul 21, 2017, 1:04:33,63 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273063> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheNode-1.0-SNAPSHOT of application CacheNode-1.0-SNAPSHOT successfully transitioned from STATE_NEW to STATE_PREPARED on server CacheServer-4.> 



4. Deploy CacheWebClient-1.0.0-SNAPSHOT on WebLogic storage disabled nodes


####<Jul 21, 2017, 1:09:02,877 PM UTC> <Info> <CONCURRENCY> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542877> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162601> <Creating ContextService "DefaultContextService" (partition="DOMAIN", module="null", application="CacheWebClient-1.0-SNAPSHOT")> 
####<Jul 21, 2017, 1:09:02,877 PM UTC> <Info> <CONCURRENCY> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542877> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162600> <Creating ManagedThreadFactory "DefaultManagedThreadFactory" (partition="DOMAIN", module="null", application="CacheWebClient-1.0-SNAPSHOT")> 
####<Jul 21, 2017, 1:09:02,894 PM UTC> <Info> <CONCURRENCY> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542894> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162610> <Creating ManagedExecutorService "DefaultManagedExecutorService" (partition="DOMAIN", module="null", application="CacheWebClient-1.0-SNAPSHOT", workmanager="default")> 
####<Jul 21, 2017, 1:09:02,895 PM UTC> <Info> <CONCURRENCY> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542895> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162611> <Creating ManagedScheduledExecutorService "DefaultManagedScheduledExecutorService" (partition="DOMAIN", module="null", application="CacheWebClient-1.0-SNAPSHOT", workmanager="default")> 
####<Jul 21, 2017, 1:09:02,896 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542896> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT is transitioning from STATE_NEW to STATE_PREPARED on server AppServer-1.> 
####<Jul 21, 2017, 1:09:03,157 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642543157> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT successfully transitioned from STATE_NEW to STATE_PREPARED on server AppServer-1.> 





# Start & verify

1. Start CacheNode-1.0.0-SNAPSHOT application in WebLogic console

####<Jul 21, 2017, 1:05:01,922 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001d> <1500642301922> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheNode-1.0-SNAPSHOT of application CacheNode-1.0-SNAPSHOT is transitioning from STATE_PREPARED to STATE_ADMIN on server CacheServer-4.> 
####<Jul 21, 2017, 1:05:01,991 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642301991> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:01.991/1085.630 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)', member=10): Loaded cache configuration from "jar:file:/u01/oracle/fmw12.2.1/config/domains/cohmt_domain/CacheConfig-1.0-SNAPSHOT.jar!/META-INF/trivial-spring-cache-config.xml"> 
####<Jul 21, 2017, 1:05:02,58 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302058> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:02.058/1085.697 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)', member=10): Created cache factory com.tangosol.net.ExtensibleConfigurableCacheFactory> 
####<Jul 21, 2017, 1:05:02,62 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302062> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:02.062/1085.701 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)', member=10): Restarting Service: XXX:trivialService> 
####<Jul 21, 2017, 1:05:02,73 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302073> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:02.073/1085.712 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=DistributedCache:XXX:trivialService, member=10): Loaded POF configuration from "jar:file:/u01/oracle/fmw12.2.1/config/domains/cohmt_domain/CacheConfig-1.0-SNAPSHOT.jar!/META-INF/trivial-pof-config.xml"> 
####<Jul 21, 2017, 1:05:02,87 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302087> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:02.087/1085.726 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=DistributedCache:XXX:trivialService, member=10): Loaded included POF configuration from "jar:file:/u01/oracle/fmw12.2.1/product/oracle_home/coherence/lib/coherence.jar!/coherence-pof-config.xml"> 
####<Jul 21, 2017, 1:05:02,316 PM UTC> <Trace> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302316> <[severity-value: 256] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <[com.tangosol.coherence.component.util.logOutput.Jdk:log] 2017-07-21 13:05:02.316/1085.954 Oracle Coherence GE 12.2.1.2.0 <D5> (thread=DistributedCache:XXX:trivialService, member=10): Service XXX:trivialService joined the cluster with senior service member 10> 
####<Jul 21, 2017, 1:05:02,397 PM UTC> <Trace> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302397> <[severity-value: 256] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <[com.tangosol.coherence.component.util.logOutput.Jdk:log] 2017-07-21 13:05:02.397/1086.035 Oracle Coherence GE 12.2.1.2.0 <D5> (thread=DistributedCache:XXX:trivialService, member=10): This member has become the distribution coordinator for MemberSet(Size=1
  Member(Id=10, Timestamp=2017-07-21 12:47:10.589, Address=10.0.15.23:7375, MachineId=17612, Location=site:site-1,rack:rack-1,machine:machine3,process:18298,member:CacheServer-4, Role=CacheCluster-1)
  )> 
####<Jul 21, 2017, 1:05:02,411 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001d> <1500642302411> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheNode-1.0-SNAPSHOT of application CacheNode-1.0-SNAPSHOT successfully transitioned from STATE_PREPARED to STATE_ADMIN on server CacheServer-4.> 
####<Jul 21, 2017, 1:05:02,412 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001d> <1500642302412> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheNode-1.0-SNAPSHOT of application CacheNode-1.0-SNAPSHOT is transitioning from STATE_ADMIN to STATE_ACTIVE on server CacheServer-4.> 
####<Jul 21, 2017, 1:05:02,412 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001d> <1500642302412> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheNode-1.0-SNAPSHOT of application CacheNode-1.0-SNAPSHOT successfully transitioned from STATE_ADMIN to STATE_ACTIVE on server CacheServer-4.> 


2. Prepare App nodes

a) Class Path:
/u01/oracle/fmw12.2.1/config/domains/cohmt_domain/*:/u01/oracle/fmw12.2.1/config/domains/cohmt_domain/spring-framework-4.3.8.RELEASE/libs/*

b) Arguments:
-Dtangosol.coherence.cacheconfig=META-INF/trivial-cache-config.xml

3. Restart App nodes


2. Start CacheWebClient-1.0.0-SNAPSHOT 

####<Jul 21, 2017, 1:10:06,693 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '7' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001b> <1500642606693> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT is transitioning from STATE_PREPARED to STATE_ADMIN on server AppServer-1.> 
####<Jul 21, 2017, 1:10:06,706 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '7' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001b> <1500642606706> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT successfully transitioned from STATE_PREPARED to STATE_ADMIN on server AppServer-1.> 
####<Jul 21, 2017, 1:10:06,831 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '7' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001b> <1500642606831> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT is transitioning from STATE_ADMIN to STATE_ACTIVE on server AppServer-1.> 
####<Jul 21, 2017, 1:10:06,831 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '7' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001b> <1500642606831> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT successfully transitioned from STATE_ADMIN to STATE_ACTIVE on server AppServer-1.> 


# Use

http://machine1:8001/CacheWebClient-1.0-SNAPSHOT/CacheWarmer


http://localhost:8001/CacheWebClient-1.0-SNAPSHOT/CacheWarmer



