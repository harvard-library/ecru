This subdirectory contains the files  needed to support a single solr collection


The setup reflects the setup available with Solr 4.6.0's 
example/solr/collection1/conf, with two notable adjustments:

  schema.xml contains the schema for version 1 of ecru.  It is identical to
  ecru_v1.xml
  
  My reading of the documentation indicates that it is possible to
  configure core.properties to use "ecru_v1.xml" as the schema name, but
  I haven't done that yet.
  
