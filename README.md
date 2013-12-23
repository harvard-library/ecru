ecru
====

__E__lectronic __C__ourse __R__eserves __U__nleashed!

A course reserves information management system that provides a RESTful API for accessing course and reserves information.


__ecru__ uses a [Solr](http://lucene.apache.org/solr/index.html) 4 instance that functions as a NoSQL database, taking advantage of [Solrj](https://wiki.apache.org/solr/Solrj) for creating, updating, and modifying the data.  An [Apache Tomcat](http://lucene.apache.org/solr/index.html) instance supports both the RESTful API and the Solr database.

Begun as a Harvard University [Library Lab](https://osc.hul.harvard.edu/liblab)  project (funded with the generous support of the [Arcadia Fund](http://www.arcadiafund.org.uk/) ) .

Background/Goals
----------------

__ecru__ began as a solution to a problem posed by the changing paradigm of Learning Management systems.  As an early adopter of web-based course information systems, Harvard had developed its own system, iSites. that interacted with the Harvard Library Reserves List management tool, a sophisticated application for managing the multi-step processing of reserves requests, from the initial faculty request to the placement of the requested item on the Reserves Shelf (or providing an electronic link).  The interoperability of these two systems was limited to SOAP interactions, making it difficult for the course reserves information to be exposed to students (and public service librarians) outside of the iSites framework.

Local surveys showed that students prefer to "search" for reserves by author and title keywords, rather than browse through course listings.  __ecru__ was designed to provide much greater flexibility in search options.  

Rather than just putting a RESTful API "wrapper" around the Library's Reserves List tool (which not all faculties at Harvard use), we decided to separate out the needed functionality by building a separate database into which data could be loaded not only from the Reserves List tool, but also from other systems in use elsewhere.

The goal is to design a flexible, relatively lightweight system that supports the ability to query the database in a number of ways using the current RESTful API paradigm.  
