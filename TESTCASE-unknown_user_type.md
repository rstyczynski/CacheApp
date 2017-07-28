# Test case presenting problem with POF "unknown user type:" when replicated cache is used by two WAR modules running on the same WLS
R.Styczynski, July 27, 2017

# Requirements
1. Linux box
2. packages: git, java8, mvn, curl
3. access to internet 
4. OTN account to download WebLogic software

## versions

java -version
java version "1.8.0_60"
Java(TM) SE Runtime Environment (build 1.8.0_60-b27)
Java HotSpot(TM) 64-Bit Server VM (build 25.60-b23, mixed mode)

git --version
git version 2.11.0 (Apple Git-81)

curl --version
curl 7.51.0 (x86_64-apple-darwin16.0) libcurl/7.51.0 SecureTransport zlib/1.2.8
Protocols: dict file ftp ftps gopher http https imap imaps ldap ldaps pop3 pop3s rtsp smb smbs smtp smtps telnet tftp 
Features: AsynchDNS IPv6 Largefile GSS-API Kerberos SPNEGO NTLM NTLM_WB SSL libz UnixSockets 

WebLogic fmw_12.2.1.2.0_wls_Disk1_1of1.zip


# Procedure

## 1. Open three terminal sessions
a) first for Admin
b) second for Server-0
c) third for deployments

Change directory to your working one in each session

## 2. Clone and build CacheApp
    - in Admin session

```bash
git clone -b User-type-unknown-test-case https://github.com/rstyczynski/CacheApp
cd CacheApp
mvn clean package
```

## 3. set env
    - in Admin session

```bash
cat > setAppEnv.sh <<EOF
if [ -f CacheConfig/pom.xml ]; then
  export APP_HOME=$PWD
  export ORACLE_HOME=$PWD/bin/WLS12212
  export CLASSPATH=$PWD/bin/WLS12212/wlserver/server/lib/weblogic.jar
  export DOMAIN_HOME=$PWD/bin/WLS12212/user_projects/domains/base_domain
else
  echo "###"
  echo "### ERROR! You have to be in CacheApp directory. Clone the app first."
  echo "###"
  unset APP_HOME
  unset ORACLE_HOME
  unset CLASSPATH
  unset DOMAIN_HOME
fi
EOF
```

## 4. Download WebLogic 12.2.1.2 from OTN
    - in Admin session

```bash
source setAppEnv.sh
mkdir $APP_HOME/install
```

Write downloaded fmw_12.2.1.2.0_wls_Disk1_1of1.zip to install dir

```bash
cd $APP_HOME/install
unzip fmw_12.2.1.2.0_wls_Disk1_1of1.zip
```

## 5. Install Weblogic
    - in Admin session

```bash
source setAppEnv.sh
mkdir $APP_HOME/bin
mkdir $APP_HOME/tmp
cat > $APP_HOME/tmp/oraInst.loc <<EOF
inventory_loc=$APP_HOME/tmp/oraInventory
inst_group=oinstall
EOF

cat > $APP_HOME/tmp/WLS12212.rsp <<EOF
[ENGINE]
Response File Version=1.0.0.0.0

[GENERIC]
DECLINE_AUTO_UPDATES=true
MOS_USERNAME=
MOS_PASSWORD=<SECURE VALUE>
AUTO_UPDATES_LOCATION=
SOFTWARE_UPDATES_PROXY_SERVER=
SOFTWARE_UPDATES_PROXY_PORT=
SOFTWARE_UPDATES_PROXY_USER=
SOFTWARE_UPDATES_PROXY_PASSWORD=<SECURE VALUE>
ORACLE_HOME=$ORACLE_HOME
INSTALL_TYPE=Complete with Examples
EOF

java -jar $AP_HOME/install/fmw_12.2.1.2.0_wls_Disk1_1of1/fmw_12.2.1.2.0_wls.jar  -silent -responseFile $APP_HOME/tmp/WLS12212.rsp -invPtrLoc $APP_HOME/tmp/oraInst.loc
```

## 6. Prepare base domain
    - in Admin session

```bash
source setAppEnv.sh
$ORACLE_HOME/oracle_common/common/bin/config.sh 
```

It will open GUI. Select options:
a) Create a new domain
b) Select templates:
       (1) Basic
       (2) WebLogic Coherence
c) user: weblogic, password: welcome1
d) Development mode
e) Configure Administration Server
f) Name: AdminServer, Listen address: localhost, Listen port 7001 
g) Press: Create, Next, and Finish


## 7. start Admin
    - in Admin session

source setAppEnv.sh
$DOMAIN_HOME/bin/startWebLogic.sh 


## 8. Configure WebLogic domain
    - in Server-0 session

```bash
source setAppEnv.sh

cat > $APP_HOME/tmp/prepareDomain.wlst <<EOF

connect('weblogic','welcome1','t3://localhost:7001') 

edit()
startEdit()

cd('/')
cmo.createCluster('AppTier')
cd('/Clusters/AppTier')
cmo.setClusterMessagingMode('unicast')

cd('/')
cmo.createCoherenceClusterSystemResource('Coherence-0')
cd('/CoherenceClusterSystemResources/Coherence-0/CoherenceClusterResource/Coherence-0/CoherenceClusterParams/Coherence-0')
cmo.setClusteringMode('multicast')
cmo.setClusterListenPort(7574)
cmo.setMulticastListenAddress('231.1.1.1')
cmo.setTransport('udp')
cmo.setTimeToLive(0)

cd('/')
cmo.createServer('Server-0')
cd('/Servers/Server-0')
cmo.setListenAddress('')
cmo.setListenPort(9001)
cmo.setListenAddress('localhost')
cmo.setListenPortEnabled(true)
cmo.setAdministrationPort(9002)

cmo.setCluster(getMBean('/Clusters/AppTier'))
cmo.setCoherenceClusterSystemResource(getMBean('/CoherenceClusterSystemResources/Coherence-0'))
cd('/CoherenceClusterSystemResources/Coherence-0')
cmo.addTarget(getMBean('/Servers/Server-0'))

activate()
EOF

java weblogic.WLST tmp/prepareDomain.wlst 
```

## 9. start Server-0
    - in Server-0 session


```bash
source setAppEnv.sh 
export JAVA_OPTIONS="-Dtangosol.coherence.cacheconfig=$APP_HOME/CacheConfig/etc/META-INF/trivial-cache-config.xml"

mkdir $DOMAIN_HOME/servers/Server-0
mkdir $DOMAIN_HOME/servers/Server-0/security
echo "username=weblogic 
password=welcome1" > $DOMAIN_HOME/servers/Server-0/security/boot.properties

function restartWLS {
kill %1
rm $DOMAIN_HOME/servers/Server-0/logs/Server-0.*
mkdir $DOMAIN_HOME/servers/Server-0/logs
$DOMAIN_HOME/bin/startManagedWebLogic.sh Server-0  http://localhost:7001 2>&1 >$DOMAIN_HOME/servers/Server-0/logs/Server-0.out &
}
restartWLS
tail -f $DOMAIN_HOME/servers/Server-0/logs/Server-0.out
```

## 10. deploy first app
    - in deployments session

This application will initialise POF structures

```bash
source setAppEnv.sh 
cd $APP_HOME
java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -remote -upload -deploy CacheWebApp/target/CacheWebApp-1.0-SNAPSHOT.ear  -targets Server-0
```

### 10.1. check result
    - in deployments session

```bash
curl http://localhost:9001/CacheWebClient/CacheDisplay
```

```
<html>
<head><title>Cache reader</title></head>
<body>
<p/>
0 - Zero - Zero->weblogic.utils.classloaders.ChangeAwareClassLoader@50099fc5 finder: weblogic.utils.classloaders.CodeGenClassFinder@121c7409 annotation: CacheWebApp-1.0-SNAPSHOT@CacheWebClient
</br>
</body>
</html>
```

### 10.2. Check std out of Server-0

```
>>>>>Serializer initialized. Done.
>>>>>TrivialListener. Insert:CacheEvent{LocalCache inserted: key=0, value=0 - Zero - Zero}
```

### 11. deploy second app
###    - in deployments session
###
###     This application will try to use POF structures

```bash
source setAppEnv.sh 
cd $APP_HOME
java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -remote -upload -deploy CacheWebAppAlt/target/CacheWebAppAlt-1.0-SNAPSHOT.ear  -targets Server-0
```

## 11.1. check result
    - in deployments session

```bash
curl http://localhost:9001/CacheWebClientAlt/CacheDisplay
```

```html
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Draft//EN">
<HTML>
<HEAD>
<TITLE>Error 500--Internal Server Error</TITLE>
</HEAD>
<BODY bgcolor="white">
<FONT FACE=Helvetica><BR CLEAR=all>
<TABLE border=0 cellspacing=5><TR><TD><BR CLEAR=all>
<FONT FACE="Helvetica" COLOR="black" SIZE="3"><H2>Error 500--Internal Server Error</H2>
</FONT></TD></TR>
</TABLE>
<TABLE border=0 width=100% cellpadding=10><TR><TD VALIGN=top WIDTH=100% BGCOLOR=white><FONT FACE="Courier New"><pre>&#40;Wrapped: CacheName=trivialCache, Key=0&#41; java.io.IOException: unknown user type: model.TrivialRecord
	at com.tangosol.coherence.component.util.CacheHandler.releaseClassLoader(CacheHandler.CDB:23)
	at com.tangosol.coherence.component.util.CacheHandler.getCachedResource(CacheHandler.CDB:39)
	at com.tangosol.coherence.component.util.CacheHandler.convert(CacheHandler.CDB:1)
	at com.tangosol.coherence.component.util.CacheHandler$EntrySet$Entry.getValue(CacheHandler.CDB:2)
	at view.TrivialCacheDisplay.doGet(TrivialCacheDisplay.java:48)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:687)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:790)
	at weblogic.servlet.internal.StubSecurityHelper$ServletServiceAction.run(StubSecurityHelper.java:286)
	at weblogic.servlet.internal.StubSecurityHelper$ServletServiceAction.run(StubSecurityHelper.java:260)
	at weblogic.servlet.internal.StubSecurityHelper.invokeServlet(StubSecurityHelper.java:137)
	at weblogic.servlet.internal.ServletStubImpl.execute(ServletStubImpl.java:350)
	at weblogic.servlet.internal.ServletStubImpl.execute(ServletStubImpl.java:247)
	at weblogic.servlet.internal.WebAppServletContext$ServletInvocationAction.wrapRun(WebAppServletContext.java:3679)
	at weblogic.servlet.internal.WebAppServletContext$ServletInvocationAction.run(WebAppServletContext.java:3649)
	at weblogic.security.acl.internal.AuthenticatedSubject.doAs(AuthenticatedSubject.java:326)
	at weblogic.security.service.SecurityManager.runAsForUserCode(SecurityManager.java:197)
	at weblogic.servlet.provider.WlsSecurityProvider.runAsForUserCode(WlsSecurityProvider.java:203)
	at weblogic.servlet.provider.WlsSubjectHandle.run(WlsSubjectHandle.java:71)
	at weblogic.servlet.internal.WebAppServletContext.doSecuredExecute(WebAppServletContext.java:2433)
	at weblogic.servlet.internal.WebAppServletContext.securedExecute(WebAppServletContext.java:2281)
	at weblogic.servlet.internal.WebAppServletContext.execute(WebAppServletContext.java:2259)
	at weblogic.servlet.internal.ServletRequestImpl.runInternal(ServletRequestImpl.java:1691)
	at weblogic.servlet.internal.ServletRequestImpl.run(ServletRequestImpl.java:1651)
	at weblogic.servlet.provider.ContainerSupportProviderImpl$WlsRequestExecutor.run(ContainerSupportProviderImpl.java:270)
	at weblogic.invocation.ComponentInvocationContextManager._runAs(ComponentInvocationContextManager.java:348)
	at weblogic.invocation.ComponentInvocationContextManager.runAs(ComponentInvocationContextManager.java:333)
	at weblogic.work.LivePartitionUtility.doRunWorkUnderContext(LivePartitionUtility.java:54)
	at weblogic.work.PartitionUtility.runWorkUnderContext(PartitionUtility.java:41)
	at weblogic.work.SelfTuningWorkManagerImpl.runWorkUnderContext(SelfTuningWorkManagerImpl.java:640)
	at weblogic.work.ExecuteThread.execute(ExecuteThread.java:406)
	at weblogic.work.ExecuteThread.run(ExecuteThread.java:346)
Caused by: java.io.IOException: unknown user type: model.TrivialRecord
	at com.tangosol.io.pof.ConfigurablePofContext.serialize(ConfigurablePofContext.java:360)
	at com.tangosol.util.ExternalizableHelper.serializeInternal(ExternalizableHelper.java:2899)
	at com.tangosol.util.ExternalizableHelper.toBinary(ExternalizableHelper.java:283)
	at com.tangosol.coherence.component.util.daemon.queueProcessor.service.grid.ReplicatedCache$ConverterToInternal.convert(ReplicatedCache.CDB:6)
	at com.tangosol.coherence.component.util.CacheHandler.releaseClassLoader(CacheHandler.CDB:15)
	... 30 more
Caused by: java.lang.IllegalArgumentException: unknown user type: model.TrivialRecord
	at com.tangosol.io.pof.ConfigurablePofContext.getUserTypeIdentifier(ConfigurablePofContext.java:440)
	at com.tangosol.io.pof.ConfigurablePofContext.getUserTypeIdentifier(ConfigurablePofContext.java:429)
	at com.tangosol.io.pof.PofBufferWriter.writeUserType(PofBufferWriter.java:1927)
	at com.tangosol.io.pof.PofBufferWriter.writeObject(PofBufferWriter.java:1865)
	at com.tangosol.io.pof.ConfigurablePofContext.serialize(ConfigurablePofContext.java:354)
	... 34 more
</pre></FONT></TD></TR>
</TABLE>

</BODY>
</HTML>
```

### 11.2. Check std out of Server-0

```
>>>>>Serializer initialized. Done.
>>>>>TrivialListener. Insert:CacheEvent{LocalCache inserted: key=0A, value=0A - Zero A - Zero A}
<Jul 28, 2017, 11:54:24,188 AM CEST> <Error> <HTTP> <BEA-101020> <[ServletContext@1846249541[app:CacheWebAppAlt-1.0-SNAPSHOT module:/CacheWebClientAlt path:null spec-version:3.1]] Servlet failed with an Exception
(Wrapped: CacheName=trivialCache, Key=0) java.io.IOException: unknown user type: model.TrivialRecord
	at com.tangosol.coherence.component.util.CacheHandler.releaseClassLoader(CacheHandler.CDB:23)
	at com.tangosol.coherence.component.util.CacheHandler.getCachedResource(CacheHandler.CDB:39)
	at com.tangosol.coherence.component.util.CacheHandler.convert(CacheHandler.CDB:1)
	at com.tangosol.coherence.component.util.CacheHandler$EntrySet$Entry.getValue(CacheHandler.CDB:2)
	at view.TrivialCacheDisplay.doGet(TrivialCacheDisplay.java:48)
	Truncated. see log file for complete stacktrace
Caused By: java.io.IOException: unknown user type: model.TrivialRecord
	at com.tangosol.io.pof.ConfigurablePofContext.serialize(ConfigurablePofContext.java:360)
	at com.tangosol.util.ExternalizableHelper.serializeInternal(ExternalizableHelper.java:2899)
	at com.tangosol.util.ExternalizableHelper.toBinary(ExternalizableHelper.java:283)
	at com.tangosol.coherence.component.util.daemon.queueProcessor.service.grid.ReplicatedCache$ConverterToInternal.convert(ReplicatedCache.CDB:6)
	at com.tangosol.coherence.component.util.CacheHandler.releaseClassLoader(CacheHandler.CDB:15)
	Truncated. see log file for complete stacktrace
Caused By: java.lang.IllegalArgumentException: unknown user type: model.TrivialRecord
	at com.tangosol.io.pof.ConfigurablePofContext.getUserTypeIdentifier(ConfigurablePofContext.java:440)
	at com.tangosol.io.pof.ConfigurablePofContext.getUserTypeIdentifier(ConfigurablePofContext.java:429)
	at com.tangosol.io.pof.PofBufferWriter.writeUserType(PofBufferWriter.java:1927)
	at com.tangosol.io.pof.PofBufferWriter.writeObject(PofBufferWriter.java:1865)
	at com.tangosol.io.pof.ConfigurablePofContext.serialize(ConfigurablePofContext.java:354)
	Truncated. see log file for complete stacktrace
> 
```


## 12. Undeploy apps
    - in deployments session

```bash
source setAppEnv.sh 
cd $APP_HOME

java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -name CacheWebApp-1.0-SNAPSHOT -undeploy -targets Server-0
java -cp $CLASSPATH weblogic.Deployer  -adminurl t3://localhost:7001 -user weblogic -password welcome1  -name CacheWebAppAlt-1.0-SNAPSHOT -undeploy -targets Server-0
```

## 13. kill Server-0 node
    - in Server-0 session

```bash
kill %1
```




