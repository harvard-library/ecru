#Installation Instructions

**ecru** is a java-based application that relies on a *solr 4* instance.


The current configuration is built using
ant ( https://ant.apache.org/ ) and deployed as a .war file to a tomcat instance.  The build.xml is at **ecru/build.xml**

The underlying **solr 4** instance is currently incorporated into the built .war file.

## Structure

**ecru/lib/** contains the libraries needed for *ecru*

**ecru/etc/** 

  * If you are running **solr 4** under tomcat, see **ecru/etc/conf/Catalina/solr.xml.template**
  
  * **ecru/etc/solr/** contains all the files needed for the *solr 4* instance. See the **ecru/etc/solr/README.txt** for more information on that. 
  
**ecru/src/** contains the usual

**ecru/test/** contains what is currently a limited number of JUnit tests

**ecru/conf/templates** contains templates of property files used by the source and test classes.  See the **ecru/build-local.properties.template** for what properties are needed. The *ecru.properties* file includes properties for presenting "nice" labels for facet fields.
