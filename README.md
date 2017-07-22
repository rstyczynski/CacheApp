# CacheApp

Exemplary Coherence Cache App for WebLogic 12.2.1 with Coherence Spring integration.

This code demonstrates use of Coherence GAR by Web client both running on multiple nodes of WebLogic 12.2.1 together with Coherence spring integration. Spring integration is a new approach shipped with Coherence 12c, and replaces old style CacheAwareCacheFactory. Note that WebLogic 12.2.1 introduced changes in class loading (https://docs.oracle.com/middleware/1221/wls/NOTES/whatsnew.htm#NOTES550), what is visible in both change of the behaviour and unexpected side effects. 

New spring integrarion model is described here: http://coherence.java.net/coherence-spring/1.0.0/index.html Note that it changes both XML syngtax, and XML header where Coherence Namespaces are used in XML schema to automatically load required schema handlers. Project is currently finalizing support for WebLogic 12.2.1. Home page of new version is available here: http://coherence.java.net/coherence-spring/2.0.0-SNAPSHOT/


# Exemplary application

Exemplary appication is build with use of minimal lines of code, however consist of sevar modules, simulating real life enterprise class application. 

The application consisft of:
1. configurartion files
2. data model
3. cache service w/o Spring integrarion
4. cache service with Spring integrarion
5. web client

Each of them is a separated module. In this example, each module is build as a separated physical set of files controlled by Maven. Note that cahce service is prepared in two modes: with Spring integrartion, and without. 

# Build exemplary application

To build application you need maven, git, java 8, and access to internet to download required java libraries. 

```bash
git clone https://github.com/rstyczynski/CacheApp.git
cd CacheApp

cd CacheConfig
mvn clean package install
cd ..

cd CacheModel
mvn clean package install
cd ..


cd CacheNodeSpring
mvn clean package
cd ..

cd CacheNodeSpringSpring
mvn clean package
cd ..

cd CacheWebClient
mvn clean package
cd ..

```

## Know your artifacts

Build of artifacts generates jar/gar/war modules located in module's target directory. Use below bash script to see location of generated modules.


```bash
function showAll {
ls ~/.m2/repository/com/oracle/coherence/spring/coherence-spring/2.0.0-SNAPSHOT/coherence-spring-2.0.0-SNAPSHOT.jar 
ls $CacheAppBase/CacheConfig/target/*.jar
ls $CacheAppBase/CacheModel/target/*.jar
ls $CacheAppBase/CacheNodeSpring/target/*.gar
ls $CacheAppBase/CacheNodeSpringSpring/target/*.gar
ls $CacheAppBase/CacheWebClient/target/*.war
}

showAll
```

# Prepare servers

Some preparations need to be done at servers. Apart from prepareation of Coherence elements, it's manatory to put requirted jars in domain lib directory.

## 1. Configure Weblogic

WebLogic have to be prepared for deployment by definition of appliction, and data tiers, and Coherence cluster. Description of this area is aout of scope of this description. Please refer to Oracle documentation: https://docs.oracle.com/middleware/1221/wls/CLUST/coherence.htm#CLUST629

Target system should consiste of:
1. Storage disabled application server(s)
2. Storage enabled storage server(s)
3. Coherence cluster


## 2. Put required libraries in domain lib

Due to changes in class loader, required classes must be placed directly in $DOMAIN/lib


### Add Spring and commons logging do domain directory at Application and Cache servers

```bash
DOMAIN=>>>domain directory put here<<<

cd /tmp
wget http://maven.springframework.org/release/org/springframework/spring/4.3.8.RELEASE/spring-framework-4.3.8.RELEASE-dist.zip  
unzip spring-framework-4.3.8.RELEASE-dist.zip 
wget http://apache.cu.be//commons/logging/binaries/commons-logging-1.2-bin.tar.gz
tar -xvzf commons-logging-1.2-bin.tar.gz

cd $DOMAIN
cp /tmp/spring-framework-4.3.8.RELEASE/libs/* lib
cp /tmp/commons-logging-1.2/commons-logging-1.2.jar lib

rm -rf /tmp/commons-logging-1.2
rm -f /tmp/commons-logging-1.2-bin.tar.gz
rm -rf /tmp/spring-framework-4.3.8.RELEASE
rm -f /tmp/spring-framework-4.3.8.RELEASE-dist.zip 
```

### Add to WebLogic $DOMAIN/lib directory at Application and Cache servers

```
coherence-spring-2.0.0-SNAPSHOT.jar
CacheConfig-1.0.0-SNAPSHOT.jar 
CacheModel-1.0.0-SNAPSHOT.jar 
```


## 3. Prepare arguments at Application servers

In opposite to cache storge nodes, which are configiured by GAR archives, applictation nodes have to be informed about cache layout. It's achived by using regular JVM argument 'tangosol.coherence.cacheconfig' pointing to cache config file located in $DOMAN/lib/CacheConfig-1.0.0-SNAPSHOT.jar Cache config is a system wide configuration thus this setup sounds reasonable. Using this technique you may specify special cache config file for proxy nodes.


```bash
-Dtangosol.coherence.cacheconfig=META-INF/trivial-cache-config.xml
```

## 4. Restart aApplication and Cache servers

To register $DOMAIN/lib packages and JVM arguments, restart application and storage nodes. 


# Deploy your artifacts

## 1. Deploy CacheNodeSpring-1.0.0-SNAPSHOT.gar 

Use WebLogic tools or console to deploy CacheNodeSpring-1.0.0-SNAPSHOT.gar on Cache servers


After deplyment you should see lines similar to presented below in server's log.

```
####<Jul 21, 2017, 1:03:27,987 PM UTC> <Info> <J2EE> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '6' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001b> <1500642207987> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-160151> <Registered library Extension-Name: CacheNodeSpring-1 (JAR).> 
####<Jul 21, 2017, 1:04:15,253 PM UTC> <Info> <Health> <machine3> <CacheServer-4> <weblogic.GCMonitor> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000016> <1500642255253> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-310002> <50% of the total memory in the server is free.> 
####<Jul 21, 2017, 1:04:33,51 PM UTC> <Info> <CONCURRENCY> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273051> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162601> <Creating ContextService "DefaultContextService" (partition="DOMAIN", module="null", application="CacheNodeSpring-1.0-SNAPSHOT")> 
####<Jul 21, 2017, 1:04:33,51 PM UTC> <Info> <CONCURRENCY> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273051> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162600> <Creating ManagedThreadFactory "DefaultManagedThreadFactory" (partition="DOMAIN", module="null", application="CacheNodeSpring-1.0-SNAPSHOT")> 
####<Jul 21, 2017, 1:04:33,52 PM UTC> <Info> <CONCURRENCY> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273052> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162610> <Creating ManagedExecutorService "DefaultManagedExecutorService" (partition="DOMAIN", module="null", application="CacheNodeSpring-1.0-SNAPSHOT", workmanager="default")> 
####<Jul 21, 2017, 1:04:33,56 PM UTC> <Info> <CONCURRENCY> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273056> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162611> <Creating ManagedScheduledExecutorService "DefaultManagedScheduledExecutorService" (partition="DOMAIN", module="null", application="CacheNodeSpring-1.0-SNAPSHOT", workmanager="default")> 
####<Jul 21, 2017, 1:04:33,57 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273057> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheNodeSpring-1.0-SNAPSHOT of application CacheNodeSpring-1.0-SNAPSHOT is transitioning from STATE_NEW to STATE_PREPARED on server CacheServer-4.> 
####<Jul 21, 2017, 1:04:33,63 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001c> <1500642273063> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheNodeSpring-1.0-SNAPSHOT of application CacheNodeSpring-1.0-SNAPSHOT successfully transitioned from STATE_NEW to STATE_PREPARED on server CacheServer-4.> 
```


## 2. Deploy CacheWebClient-1.0.0-SNAPSHOT on WebLogic Application servers


Use WebLiogic console to deploy CacheWebClient-1.0.0-SNAPSHOT on WebLogic Application servers.


After deplyment you should see lines similar to presented below in server's log.

```
####<Jul 21, 2017, 1:09:02,877 PM UTC> <Info> <CONCURRENCY> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542877> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162601> <Creating ContextService "DefaultContextService" (partition="DOMAIN", module="null", application="CacheWebClient-1.0-SNAPSHOT")> 
####<Jul 21, 2017, 1:09:02,877 PM UTC> <Info> <CONCURRENCY> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542877> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162600> <Creating ManagedThreadFactory "DefaultManagedThreadFactory" (partition="DOMAIN", module="null", application="CacheWebClient-1.0-SNAPSHOT")> 
####<Jul 21, 2017, 1:09:02,894 PM UTC> <Info> <CONCURRENCY> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542894> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162610> <Creating ManagedExecutorService "DefaultManagedExecutorService" (partition="DOMAIN", module="null", application="CacheWebClient-1.0-SNAPSHOT", workmanager="default")> 
####<Jul 21, 2017, 1:09:02,895 PM UTC> <Info> <CONCURRENCY> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542895> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-2162611> <Creating ManagedScheduledExecutorService "DefaultManagedScheduledExecutorService" (partition="DOMAIN", module="null", application="CacheWebClient-1.0-SNAPSHOT", workmanager="default")> 
####<Jul 21, 2017, 1:09:02,896 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642542896> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT is transitioning from STATE_NEW to STATE_PREPARED on server AppServer-1.> 
####<Jul 21, 2017, 1:09:03,157 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '8' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001a> <1500642543157> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT successfully transitioned from STATE_NEW to STATE_PREPARED on server AppServer-1.> 
```

# Start & verify

Start of GAR service results in spawning number of Coherence threads. Note that each set ot threads is separated from another GAR by (a) classloader and (b) prefix added to thread name. It physically separates service from other applicstions running in WebLogic server. 


## 1. Start CacheNodeSpring-1.0.0-SNAPSHOT application in WebLogic console

Use WebLogic console to start CacheNodeSpring-1.0.0-SNAPSHOT application. It will start Coherence service threads. Note that Soherence threads will have in its name :trivial-scope: what is a service communication scope defined in cache config files.


After start you should see lines similar to presented below in server's log.

```
####<Jul 21, 2017, 1:05:01,922 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001d> <1500642301922> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheNodeSpring-1.0-SNAPSHOT of application CacheNodeSpring-1.0-SNAPSHOT is transitioning from STATE_PREPARED to STATE_ADMIN on server CacheServer-4.> 
####<Jul 21, 2017, 1:05:01,991 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642301991> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:01.991/1085.630 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)', member=10): Loaded cache configuration from "jar:file:/u01/oracle/fmw12.2.1/config/domains/cohmt_domain/CacheConfig-1.0-SNAPSHOT.jar!/META-INF/trivial-spring-cache-config.xml"> 
####<Jul 21, 2017, 1:05:02,58 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302058> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:02.058/1085.697 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)', member=10): Created cache factory com.tangosol.net.ExtensibleConfigurableCacheFactory> 
####<Jul 21, 2017, 1:05:02,62 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302062> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:02.062/1085.701 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)', member=10): Restarting Service: XXX:trivialService> 
####<Jul 21, 2017, 1:05:02,73 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302073> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:02.073/1085.712 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=DistributedCache:trivial-service:trivialService, member=10): Loaded POF configuration from "jar:file:/u01/oracle/fmw12.2.1/config/domains/cohmt_domain/CacheConfig-1.0-SNAPSHOT.jar!/META-INF/trivial-pof-config.xml"> 
####<Jul 21, 2017, 1:05:02,87 PM UTC> <Info> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302087> <[severity-value: 64] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <2017-07-21 13:05:02.087/1085.726 Oracle Coherence GE 12.2.1.2.0 <Info> (thread=DistributedCache:trivial-service:trivialService, member=10): Loaded included POF configuration from "jar:file:/u01/oracle/fmw12.2.1/product/oracle_home/coherence/lib/coherence.jar!/coherence-pof-config.xml"> 
####<Jul 21, 2017, 1:05:02,316 PM UTC> <Trace> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302316> <[severity-value: 256] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <[com.tangosol.coherence.component.util.logOutput.Jdk:log] 2017-07-21 13:05:02.316/1085.954 Oracle Coherence GE 12.2.1.2.0 <D5> (thread=DistributedCache:trivial-service:trivialService, member=10): Service XXX:trivialService joined the cluster with senior service member 10> 
####<Jul 21, 2017, 1:05:02,397 PM UTC> <Trace> <com.oracle.coherence> <machine3> <CacheServer-4> <Logger@9236199 12.2.1.2.0> <<anonymous>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-00000002> <1500642302397> <[severity-value: 256] [rid: 0:5] [partition-id: 0] [partition-name: DOMAIN] > <BEA-000000> <[com.tangosol.coherence.component.util.logOutput.Jdk:log] 2017-07-21 13:05:02.397/1086.035 Oracle Coherence GE 12.2.1.2.0 <D5> (thread=DistributedCache:trivial-service:trivialService, member=10): This member has become the distribution coordinator for MemberSet(Size=1
  Member(Id=10, Timestamp=2017-07-21 12:47:10.589, Address=10.0.15.23:7375, MachineId=17612, Location=site:site-1,rack:rack-1,machine:machine3,process:18298,member:CacheServer-4, Role=CacheCluster-1)
  )> 
####<Jul 21, 2017, 1:05:02,411 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001d> <1500642302411> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheNodeSpring-1.0-SNAPSHOT of application CacheNodeSpring-1.0-SNAPSHOT successfully transitioned from STATE_PREPARED to STATE_ADMIN on server CacheServer-4.> 
####<Jul 21, 2017, 1:05:02,412 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001d> <1500642302412> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheNodeSpring-1.0-SNAPSHOT of application CacheNodeSpring-1.0-SNAPSHOT is transitioning from STATE_ADMIN to STATE_ACTIVE on server CacheServer-4.> 
####<Jul 21, 2017, 1:05:02,412 PM UTC> <Info> <Deployer> <machine3> <CacheServer-4> <[STANDBY] ExecuteThread: '3' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <f92275e6-f186-47a4-9b43-31ba00ccf381-0000001d> <1500642302412> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheNodeSpring-1.0-SNAPSHOT of application CacheNodeSpring-1.0-SNAPSHOT successfully transitioned from STATE_ADMIN to STATE_ACTIVE on server CacheServer-4.> 
```

## 2. Start CacheWebClient-1.0.0-SNAPSHOT 

Use WebLogic console to start CacheWebClient-1.0.0-SNAPSHOT application. 


After start you should see lines similar to presented below in server's log.

```
####<Jul 21, 2017, 1:10:06,693 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '7' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001b> <1500642606693> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT is transitioning from STATE_PREPARED to STATE_ADMIN on server AppServer-1.> 
####<Jul 21, 2017, 1:10:06,706 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '7' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001b> <1500642606706> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT successfully transitioned from STATE_PREPARED to STATE_ADMIN on server AppServer-1.> 
####<Jul 21, 2017, 1:10:06,831 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '7' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001b> <1500642606831> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149059> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT is transitioning from STATE_ADMIN to STATE_ACTIVE on server AppServer-1.> 
####<Jul 21, 2017, 1:10:06,831 PM UTC> <Info> <Deployer> <machine1> <AppServer-1> <[STANDBY] ExecuteThread: '7' for queue: 'weblogic.kernel.Default (self-tuning)'> <<WLS Kernel>> <> <a320e87d-850e-4845-b245-7bf9a335f923-0000001b> <1500642606831> <[severity-value: 64] [rid: 0] [partition-id: 0] [partition-name: DOMAIN] > <BEA-149060> <Module CacheWebClient-1.0-SNAPSHOT.war of application CacheWebClient-1.0-SNAPSHOT successfully transitioned from STATE_ADMIN to STATE_ACTIVE on server AppServer-1.> 
```

# Use

## 1. Use browser to warm up cache

Use your browser to open CacheWebClient-1.0.0-SNAPSHOT URL to initialize cache with exemplary data.

http://machine1:8001/CacheWebClient-1.0-SNAPSHOT/CacheWarmer

```
Filling the cache.

Done.
```


### 1.1. Check Application server standard out 

To verify that pof serializer class works

```
>>>>>Serializer initialized. Done.
>>>>>Record.writeExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Serializer initialized. Done.
>>>>>Record.writeExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Serializer initialized. Done.
>>>>>Record.writeExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Serializer initialized. Done.
>>>>>Record.writeExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Serializer initialized. Done.
>>>>>Record.writeExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Serializer initialized. Done.
>>>>>Record.writeExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Serializer initialized. Done.
>>>>>Record.writeExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Serializer initialized. Done.
>>>>>Record.writeExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Serializer initialized. Done.
>>>>>Record.writeExternal. Done.
>>>>>Record.readExternal. Done.
```

### 1.2. Check Cache server standard out 

To verify that listener class works

```
>>>>>TrivialListener. Update:CacheEvent{LocalCache updated: key=Binary(length=10, value=0x0DC0A497C40E154E0131), old value=Binary(length=15, value=0x15A90F00004E07417273656E616C40), new value=Binary(length=15, value=0x15A90F00004E07417273656E616C40)}
>>>>>TrivialListener. Update:CacheEvent{LocalCache updated: key=Binary(length=10, value=0x0D859DA2AB02154E0132), old value=Binary(length=19, value=0x15A90F00004E0B4173746F6E2056696C6C6140), new value=Binary(length=19, value=0x15A90F00004E0B4173746F6E2056696C6C6140)}
>>>>>TrivialListener. Update:CacheEvent{LocalCache updated: key=Binary(length=10, value=0x0D93DF9BDB0C154E0133), old value=Binary(length=15, value=0x15A90F00004E074275726E6C657940), new value=Binary(length=15, value=0x15A90F00004E074275726E6C657940)}
>>>>>TrivialListener. Update:CacheEvent{LocalCache updated: key=Binary(length=9, value=0x0DCFF6C042154E0134), old value=Binary(length=15, value=0x15A90F00004E074368656C73656140), new value=Binary(length=15, value=0x15A90F00004E074368656C73656140)}
>>>>>TrivialListener. Update:CacheEvent{LocalCache updated: key=Binary(length=10, value=0x0DD9B4F9B20E154E0135), old value=Binary(length=22, value=0x15A90F00004E0E4372797374616C2050616C61636540), new value=Binary(length=22, value=0x15A90F00004E0E4372797374616C2050616C61636540)}
>>>>>TrivialListener. Update:CacheEvent{LocalCache updated: key=Binary(length=10, value=0x0D9C8DCCDD02154E0136), old value=Binary(length=15, value=0x15A90F00004E0745766572746F6E40), new value=Binary(length=15, value=0x15A90F00004E0745766572746F6E40)}
>>>>>TrivialListener. Update:CacheEvent{LocalCache updated: key=Binary(length=10, value=0x0D8ACFF5AD0C154E0137), old value=Binary(length=17, value=0x15A90F00004E0948756C6C204369747940), new value=Binary(length=17, value=0x15A90F00004E0948756C6C204369747940)}
>>>>>TrivialListener. Update:CacheEvent{LocalCache updated: key=Binary(length=10, value=0x0DE4C6F2D901154E0138), old value=Binary(length=22, value=0x15A90F00004E0E4C6569636573746572204369747940), new value=Binary(length=22, value=0x15A90F00004E0E4C6569636573746572204369747940)}
>>>>>TrivialListener. Update:CacheEvent{LocalCache updated: key=Binary(length=10, value=0x0DF284CBA90F154E0139), old value=Binary(length=17, value=0x15A90F00004E094C69766572706F6F6C40), new value=Binary(length=17, value=0x15A90F00004E094C69766572706F6F6C40)}
```

## 2. Use browser to read cache

Use your browser to open CacheWebClient-1.0.0-SNAPSHOT URL to display cache data.

http://machine1:8001/CacheWebClient-1.0-SNAPSHOT/CacheDisplay

```
Raz 
Dwa 
Trzy 
Cztery 
Pięć 
Sześć 
Siedem 
```


### 2.1. Check Application server standard out 


```
>>>>>Record.readExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Record.readExternal. Done.
>>>>>Record.readExternal. Done.
```


# Known limitations

## Classloader does not load resources from root

Classloader in WebLogic 12.2.1 does not load resources from root of the class path. 

Known workaround: move your resources to META-INF and prefix all resource load with META-INF e.g. classpath:META-INF/trivial.properties.

## Classloader does not load classes from /lib directory in GAR

WebLogic Classloader does not load classes from jar archives located in /lib directory in GAR package. It brakes documented contract: https://docs.oracle.com/middleware/1221/wls/WLCOH/create-application.htm#WLCOH719

Known workaround: add classes to (a) system classpath, or (b) $DOMAIN/lib. Note that WebLogic automatucally adds to classpatch all jars located in $DOMAIN/lib dorectory.


## Classloader does not load classes from / directory in GAR

WebLogic Classloader does not load classes from classes located in / directory of GAR package. It brakes documented contract: https://docs.oracle.com/middleware/1221/wls/WLCOH/create-application.htm#WLCOH719

Known workaround: add classes to (a) system classpath, or (b) $DOMAIN/lib. Note that WebLogic automatucally adds to classpatch all jars located in $DOMAIN/lib dorectory.


## Classloader does not refresh classes from undeployed applications

Redeploy of application or undeply and eploy in teh same seccion deos not refresh classes. Application will be efectively no redeployed. 

Known workaround: after undeploy Activate Changes in WebLogic Change Center. This step will remove classes from JVM. 

## Maven GAR support does not work

Maven plugins presented in product documentation does not work. It breakes documented contract: https://docs.oracle.com/middleware/1221/core/MAVEN/coherence_project.htm#MAVEN8912


```bash
mvn archetype:generate \
    -DarchetypeGroupId=com.oracle.coherence.archetype \
    -DarchetypeArtifactId=gar-maven-archetype \
    -DarchetypeVersion=12.2.1-0-0 \
    -DgroupId=org.mycompany \
    -DartifactId=my-gar-project \
    -Dversion=1.0-SNAPSHOT 
```

results with error

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-archetype-plugin:2.3:generate (default-cli) on project CacheApp: The desired archetype does not exist (com.oracle.coherence.archetype:gar-maven-archetype:12.2.1-0-0) -> [Help 1]
```

Known workaround: use jar plugin and change file name to gar using maven tools. 

## Coherence spring integration requires external libraries

Coherence spring integration requires apache logging and spring libraries, but those dependencies are not automatically added. 

Known workaround: manually add commons logger and spring to $DOMAIN/lib

```bash
DOMAIN=>>>domain directory put here<<<

cd /tmp
wget http://maven.springframework.org/release/org/springframework/spring/4.3.8.RELEASE/spring-framework-4.3.8.RELEASE-dist.zip  
unzip spring-framework-4.3.8.RELEASE-dist.zip 
wget http://apache.cu.be//commons/logging/binaries/commons-logging-1.2-bin.tar.gz
tar -xvzf commons-logging-1.2-bin.tar.gz

cd $DOMAIN
cp /tmp/spring-framework-4.3.8.RELEASE/libs/* lib
cp /tmp/commons-logging-1.2/commons-logging-1.2.jar lib

rm -rf /tmp/commons-logging-1.2
rm -f /tmp/commons-logging-1.2-bin.tar.gz
rm -rf /tmp/spring-framework-4.3.8.RELEASE
rm -f /tmp/spring-framework-4.3.8.RELEASE-dist.zip 
```

## Coherence spring integration does not support schema-ref

XML Scheme reference known from Coherence config does not work in 12.2.1 with spring inteegration.  

```
            <backing-map-scheme>
                <local-scheme>
                    <listener>
                        <class-scheme>
                            <scheme-ref>trivialListener</scheme-ref>
                        </class-scheme>
                    </listener>
                </local-scheme>
            </backing-map-scheme>
            <autostart>true</autostart>
        </distributed-scheme>
        <class-scheme>
            <scheme-name>trivialListener</scheme-name>
            <class-name>model.TrivialListener</class-name>
        </class-scheme>
```

Known workaround: Instead of refering to class-scheme refer directly to Spring bean definition. 

```
            <backing-map-scheme>
                <local-scheme>
                    <listener>
                        <class-scheme>
                            <spring:bean>
                                <spring:bean-name>trivialListener</spring:bean-name>
                            </spring:bean>
                        </class-scheme>
                    </listener>
                </local-scheme>
            </backing-map-scheme>
```

