package edu.harvard.liblab.ecru.data;

import javax.xml.bind.annotation.XmlElement;
/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  ecru
 *   
 *   This POJO class contains the information for a single facet from a solr search, 
 *  
 */

public class Facet {
	private long count;
	private String title;
	private String url;
	public Facet() {
		
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
