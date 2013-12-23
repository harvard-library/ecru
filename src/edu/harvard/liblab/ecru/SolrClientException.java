

package edu.harvard.liblab.ecru;
/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  ecru
 *   Exception thrown by the solr client
 ************************************************/
public class SolrClientException extends java.lang.Exception {
		
	public SolrClientException() {
		super();
	}
    public SolrClientException(String message) {
        super(message);
    }
    public SolrClientException(String message, Exception e) {
        super(message,e);
    }
}
