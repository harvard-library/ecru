package edu.harvard.liblab.ecru.data;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name="results")
@XmlSeeAlso({Course.class, Reading.class})
@XmlType(propOrder={"summary", "items", "faceting"})
public class ResultsList {
	private Summary summary;
	private List<EcruBase> items = new ArrayList<EcruBase> ();
	private Faceting faceting;
	public ResultsList() {
		super();
	}
	public ResultsList(Summary summary, List<EcruBase> docs, Faceting faceting) {
		this();
		this.setSummary(summary);
		this.setItems(docs);
		this.setFaceting(faceting);
	}

	@XmlElementWrapper(name="items")
	@XmlAnyElement
	public List<EcruBase> getItems() {
		return this.items;
	}
	public void setItems(List<EcruBase> items) {
		this.items = items;
	}
	@XmlElement
	public Faceting getFaceting() {
		return this.faceting;
	}
	public void setFaceting(Faceting faceting) {
		this.faceting= faceting;
	}
	
	@XmlElement
	public Summary getSummary() {
		return this.summary;
	}
	public void setSummary(Summary summary) {
		this.summary = summary;
	}

}
