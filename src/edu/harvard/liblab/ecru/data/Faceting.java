package edu.harvard.liblab.ecru.data;

import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/

/**
 * @author bobbi
 *
 *   Project:  ecru
 *   
 *   Handles Faceting lists
 *  
 */
@XmlRootElement(name="faceting")
@XmlSeeAlso({ListFacets.class})
public class Faceting {
	private List<ListFacets> listFacets;
	public Faceting() {
		super();
	}
	public Faceting(List<ListFacets> facets) {
		this();
		this.setListFacets(facets);
	}
	@XmlElementWrapper
	@XmlAnyElement
	public List<ListFacets> getListFacets() {
		return this.listFacets;
	}
	public void setListFacets(List<ListFacets> listFacets) {
		this.listFacets = listFacets;
	}
	

}
