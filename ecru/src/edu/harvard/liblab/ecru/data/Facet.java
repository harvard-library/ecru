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
 *   This POJO class contains the information for a single facet from a solr search, 
 *  
 */

public class Facet {
	private long count;
	private String title;
	private String url;
	public Facet() {
		// TODO Auto-generated constructor stub
	}
	@XmlElement
	public long getCount() {
		return this.count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	@XmlElement
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@XmlElement
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

}
