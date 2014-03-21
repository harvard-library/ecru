package edu.harvard.liblab.ecru.utils;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import edu.harvard.liblab.ecru.SolrClientException;

/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  ecru
 *   
 *  This singleton class contains the methods needed for talking to Solr in 
 *  a single threaded way.  It may no longer be needed, as some of the solr bugs
 *  that caused HttpClient threads not to be terminated seem to have been fixed.
 *  
 *  At this point, I don't see a need to do double-checked locking per
 *  http://www.ibm.com/developerworks/java/library/j-dcl/index.html
 *  
 */
public class SingletonSolrServer {
	
	private static HttpSolrServer solrSrvr;
	
	private  SingletonSolrServer() {
	}
	
	public static HttpSolrServer getSolrServer() throws SolrClientException {
		if (solrSrvr == null) {
			throw new SolrClientException("Solr Server was never initialized with a URL");
		}
		return solrSrvr;
	}
	
	/**
	 * @param url  The Solr URL
	 * @return
	 */
	public static HttpSolrServer getSolrServer(String url) {
		if (solrSrvr == null) {
			init(url);
		}
		return solrSrvr;
	}
	
	private static void init(String url) {
		if (solrSrvr == null) {
			solrSrvr = new HttpSolrServer(url);
		}
	}
}
