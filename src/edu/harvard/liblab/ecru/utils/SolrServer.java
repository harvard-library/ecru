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
 *  This singleton class contains the methods needed for talking to Solr.
 *  
 *  At this point, I don't see a need to do double-checked locking per
 *  http://www.ibm.com/developerworks/java/library/j-dcl/index.html
 *  
 */
public class SolrServer {
	
	private static String solrUrl = null;;
	
	private  SolrServer() {
	}
	
	public static HttpSolrServer getSolrServer() throws SolrClientException {
		if (solrUrl == null) {
			throw new SolrClientException("Solr Server was never initialized with a URL");
		}
		return getSolrServer(solrUrl);
	}
	
	/**
	 * @param url  The Solr URL
	 * @return
	 */
	public static HttpSolrServer getSolrServer(String url) {
		solrUrl = url;
		return new HttpSolrServer(url);
	}
	

}
