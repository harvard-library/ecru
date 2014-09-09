#Installation Instructions

**ecru** is a java-based application that relies on a *solr 4* instance.

## Install and Go Instructions
### Solr
**etc/solr** contains the files needed for the solr configuration instance.

#### Already using "core discovery"?
If you are already running a **solr** instance (4.4 or higher) that is using using **core discovery** as described in [Solr.xml 4.4 and beyond](https://wiki.apache.org/solr/Solr.xml%204.4%20and%20beyond "link to the discussion") :

1. copy **etc/solr/ecru_v2** to your "solr home"
2. **etc/solr/ecru_v2/core.properties**, as released in the repo, assumes that the solr data will "live" in **{solr home}/ecru_v2/data** . If you want the data to be "live" somewhere else, edit **core.properties** by removing the comment '#' from the dataDir property, setting it to the full path, e.g.:
  `dataDir=/usr/local/mystuff/prettygood/data`

#### Need To Run a New Solr instance?

Move or copy the etc/solr subdirectory tree to the place of your choosing (e.g.`cp -R etc/solr /usr/local/mystuff/solr`)  This becomes your *"solr home"*  If you want the solr data to be someplace other than the default, see #2, above.

*Note: this assumes deployment under* **tomcat**; *your mileage may differ somwhat with a different server, such as* **jetty** 


1.  copy **etc/solr-4.6.0.war** 
2.   extract and modify the **WEB-INF/web.xml** in the copy to point to your "solr home",
3.   re-insert the file into your copy
4.  deploy the copied war to your tomcat/webapps 
```
$ cd etc
$ cp solr-4.6.0.war solr.war
$ unzip solr.war WEB-INF/web.xml
#edit WEB-INF/web.xml, replacing /put/your/solr/home/here with the full path to your solr home (.e.g. /usr/local/mystuff/solr)
$ jar -uf solr.war WEB-INF/web.xml
$ mv solr.war /usr/whereveryourtomcatis/webapps
```
####Logging

 Thanks to the wisdom of the **solr** developers, the **.war** file of any version of solr above 4.2 no longer contains the logging jars. ( [see SolrLoggin: Solr 4.3 and above](http://wiki.apache.org/solr/SolrLogging#Solr_4.3_and_above "link to the justification")). 

If you do not already have the files  in **etc/logging** in **${catalina.base}/lib**, or some other server or common library directory that Tomcat accesses on startup, you must copy them over to such a directory.

## The ecru.war file

The **ecru.war** file at etc/ecru.war is distributed with the default URL for the solr instance assumed to be `http:localhost:8080/solr/ecru_v2`.

*Note:* If your solr instance has a different URL, you need to:
1. extract **WEB-INF/classes/ecru.properties** file from **ecru.war**
2. modify the value of the **solr.url** property
3. update **ecru.war**
```
$ cd etc
$ unzip ecru.war WEB-INF/classes/ecru.properties
#edit WEB-INF/web.xml, replacing /put/your/solr/home/here with the full path to your solr home (.e.g. /usr/local/mystuff/solr)
$ jar -uf  ecru.war WEB-INF/classes/ecru.properties
``` 
Copy **ecru.war** to your server's **webapps/**


## Ecru's Structure

(For "build it yourself types")

The current configuration is built using
ant ( https://ant.apache.org/ ) and deployed as a .war file to a tomcat instance.  The build.xml is at **ecru/build.xml**


**ecru/lib/** contains the libraries needed for *ecru*

**ecru/etc/** contains files needed for "Install and Go"
 
  
**ecru/src/** contains the usual source files

**ecru/test/** contains what is currently a limited number of JUnit tests

**ecru/conf/templates** contains templates of property files used by the source and test classes.  See the **ecru/build-local.properties.template** for what properties are needed. The **ecru.properties** file, which is mentioned above, includes properties for presenting "nice" labels for facet fields.

