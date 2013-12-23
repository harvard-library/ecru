package edu.harvard.liblab.ecru.utils;

import org.apache.solr.client.solrj.impl.HttpSolrServer;

import edu.harvard.liblab.ecru.SolrClientException;

/**********************************************************************
 * [INSERT COPYRIGHT INFO]
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  unleashed
 *   
 *  This singleton class contains the methods needed for talking to Solr.
 *  
 *  At this point, I don't see a need to do double-checked locking per
 *  http://www.ibm.com/developerworks/java/library/j-dcl/index.html
 *  
 */
public class SolrServer {
	
	private static HttpSolrServer solrSrvr;
	
	private  SolrServer() {
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
