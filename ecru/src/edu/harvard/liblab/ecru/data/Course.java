package edu.harvard.liblab.ecru.data;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
/**********************************************************************
 * [INSERT COPYRIGHT INFO]
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  unleashed
 *   
 *   This POJO class reflects the "Course" type in the Solr database
 *   A Course contains information for a single course instance for a Term
 *  
 */
@XmlRootElement(name="course")
public class Course extends EcruBase {
	
	private String readingsUrl;  // set at delivery time
	
	public Course() {
		super();
		this.setType((EcruBase.T_COURSE));
	}


	/**
	 * @param id
	 * @param displayLabel
	 * @param catalogNo
	 * @param endDate
	 * @param faculty
	 * @param instructors
	 * @param name
	 * @param startDate
	 * @param term
	 * @param url
	 */
	public Course(String id,  String displayLabel, String catNo,
			Date endDate, String division, List<String> instructors,
			String name, Date startDate, String term, 
			String url) {
		super(id, EcruBase.T_COURSE, displayLabel, endDate, startDate, term, new Date());
		
		this.setCatNo(catNo);
		this.setDivision(division);
		this.setInstructors(instructors);
		this.setName(name);
		this.setUrl(url);
	}

	/*
	 *  Make transient those fields that aren't part of a Course
	 */
	
	@Override
	@XmlTransient
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}


	@Override
	@XmlTransient
	public List<String> getAuthors() {
		// TODO Auto-generated method stub
		return super.getAuthors();
	}


	@Override
	@XmlTransient
	public String getAvailUrl() {
		// TODO Auto-generated method stub
		return super.getAvailUrl();
	}


	@Override
	@XmlTransient
	public String getChapter() {
		// TODO Auto-generated method stub
		return super.getChapter();
	}


	@Override
	@XmlElement
	public String getCourseId() {
		// TODO Auto-generated method stub
		return super.getCourseId();
	}


	@Override
	@XmlTransient
	public String getDigUrl() {
		// TODO Auto-generated method stub
		return super.getDigUrl();
	}


	@Override
	@XmlElement
	public String getDoiId() {
		// TODO Auto-generated method stub
		return super.getDoiId();
	}


	@Override
	@XmlTransient
	public List<String> getEditors() {
		// TODO Auto-generated method stub
		return super.getEditors();
	}


	@Override
	@XmlTransient
	public String getIsbn() {
		// TODO Auto-generated method stub
		return super.getIsbn();
	}


	@Override
	@XmlTransient
	public String getIssn() {
		// TODO Auto-generated method stub
		return super.getIssn();
	}


	@Override
	@XmlTransient
	public String getJournal() {
		// TODO Auto-generated method stub
		return super.getJournal();
	}


	@Override
	@XmlTransient
	public Date getLectureDate() {
		// TODO Auto-generated method stub
		return super.getLectureDate();
	}


	@Override
	@XmlTransient
	public String getLibrary() {
		// TODO Auto-generated method stub
		return super.getLibrary();
	}


	@Override
	@XmlTransient
	public String getPubmed() {
		// TODO Auto-generated method stub
		return super.getPubmed();
	}
	
	

	

	@XmlElement
	public String getReadingsUrl() {
		return this.readingsUrl;
	}


	public void setReadingsUrl(String readingsUrl) {
		this.readingsUrl = readingsUrl;
	}


	@Override
	@XmlTransient
	public boolean isRequired() {
		// TODO Auto-generated method stub
		return super.isRequired();
	}


	@Override
	@XmlTransient
	public String getStatus() {
		// TODO Auto-generated method stub
		return super.getStatus();
	}


	@Override
	@XmlTransient
	public String getSystem() {
		// TODO Auto-generated method stub
		return super.getSystem();
	}


	@Override
	@XmlTransient
	public String getSysId() {
		// TODO Auto-generated method stub
		return super.getSysId();
	}


	@Override
	@XmlTransient
	public String getTitle() {
		// TODO Auto-generated method stub
		return super.getTitle();
	}

	/*
	 *   Override toString, Object equals
	 */
	
	@Override
	public String toString() {
		String retVal = "Course (" + this.getId() + ") " + this.getDisplayLabel();
		retVal += "Division: " + this.getDivision() + " Catalog Num: " + this.getCatNo();
		retVal += " Last updated: " + this.getUpdateTimestamp();
		return retVal;
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if (super.equals(obj)) {
		Course c = (Course) obj;
			isEqual = c.getTerm().equals(this.getTerm()) && 
					c.getCatNo().equals(this.getCatNo());

		}
		return isEqual;
	}
	

}
