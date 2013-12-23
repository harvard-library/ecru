package edu.harvard.liblab.ecru.data;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**********************************************************************
 * [INSERT COPYRIGHT INFO]
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  unleashed
 *   
 *   This POJO class contains a List of facets from  a solr search, 
 *  
 */
@XmlRootElement(name="FacetList")
@XmlSeeAlso(Facet.class)
@XmlType(propOrder={"header", "facets"})
public class ListFacets {
	private List<Facet> facets;
	private String header;
	public ListFacets() {
		super();
	}
	public ListFacets(String header, List<Facet> facets) {
		this();
		this.setFacets(facets);
		this.setHeader(header);
	}
	 @XmlElementWrapper(name="facets")
	 @XmlElement(name="facet")
	public List<Facet> getFacets() {
		return this.facets;
	}
	public void setFacets(List<Facet> facets) {
		this.facets = facets;
	}
	public String getHeader() {
		return this.header;
	}
	public void setHeader(String header) {
		this.header = header;
	}

}
