package edu.harvard.liblab.ecru.data;

import javax.xml.bind.annotation.XmlElement;

/**********************************************************************
 * [INSERT COPYRIGHT INFO]
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  unleashed
 *   
 *   This POJO class containing the summary information for a solr search, 
 *   corresponding to the responseHeader portion.
 *  
 */
/**
 * @author bobbi
 *
 *   Project:  unleashed
 *  
 */
public class Summary {
	
	private long numFound = 0;
	private String nextUrl = "";
	private String prevUrl = "";

	private String query;
	private long rows = 10;
	private long start = 0;


	public Summary() {
		super();
	}
	public Summary(String query, long numFound, long rows, long start) {
		this.setQuery(query);
		this.setNumFound(numFound);
		this.setRows(rows);
		this.setStart(start);
	}

	public long getNumFound() {
		return this.numFound;
	}


	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}
	@XmlElement
	public String getNextUrl() {
		return this.nextUrl;
	}
	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}
	@XmlElement
	public String getPrevUrl() {
		return this.prevUrl;
	}
	public void setPrevUrl(String prevUrl) {
		this.prevUrl = prevUrl;
	}

	public String getQuery() {
		return this.query;
	}


	public void setQuery(String query) {
		this.query = query;
	}


	public long getRows() {
		return this.rows;
	}


	public void setRows(long rows) {
		this.rows = rows;
	}


	public long getStart() {
		return this.start;
	}


	public void setStart(long start) {
		this.start = start;
	}

}
