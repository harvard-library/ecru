This directory contains the configuration and other files needed to be placed underneath
SOLR_HOME.

Note that we are using the "core discovery" [version 4+] method of defining 
the solr core(s).

The files in *this* directory are placed as follows:


solr.xml
ecru_v2/
	core.properties
	conf/   
     	# contains the supporting files, including ecru_v2.xml, which contains the schema.

If you are already using solr, you need merely to move ecru_v2/ and its contents to your SOLR_HOME; otherwise, move this directory to the parent directory of where you intend SOLR_HOME to be.


