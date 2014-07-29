package edu.harvard.liblab.ecru.data;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/
/**
 * @author Bobbi Fox
 * 
 *         Project: unleashed
 * 
 *         This POJO class reflects the "Course" type in the Solr database A
 *         Course contains information for a single course instance for a Term
 * 
 */
@XmlRootElement(name = "course")
public class Course extends EcruBase {

	private String readingsUrl; // set at delivery time

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
	public Course(String id, String displayLabel, String catNo, Date endDate,
			String division, List<String> instructors, String name,
			Date startDate, String term, String url) {
		super(id, EcruBase.T_COURSE, displayLabel, endDate, startDate, term,
				new Date());

		this.setCatNo(catNo);
		this.setDivision(division);
		this.setInstructors(instructors);
		this.setName(name);
		this.setUrl(url);
	}

	/*
	 * Make transient those fields that aren't part of a Course
	 */

	@Override
	@XmlTransient
	public String getName() {
		return super.getName();
	}

	@Override
	@XmlTransient
	public List<String> getAuthors() {
		return super.getAuthors();
	}

	@Override
	@XmlTransient
	public String getAvailUrl() {
		return super.getAvailUrl();
	}

	@Override
	@XmlTransient
	public String getChapter() {
		return super.getChapter();
	}

	@Override
	@XmlElement
	public String getCourseId() {
		return super.getCourseId();
	}

	@Override
	@XmlTransient
	public String getDigUrl() {
		return super.getDigUrl();
	}

	@Override
	@XmlElement
	public String getDoiId() {
		return super.getDoiId();
	}

	@Override
	@XmlTransient
	public List<String> getEditors() {
		return super.getEditors();
	}

	@Override
	@XmlTransient
	public String getIsbn() {
		return super.getIsbn();
	}

	@Override
	@XmlTransient
	public String getIssn() {
		return super.getIssn();
	}

	@Override
	@XmlTransient
	public String getJournal() {
		return super.getJournal();
	}

	@Override
	@XmlTransient
	public Date getLectureDate() {
		return super.getLectureDate();
	}

	@Override
	@XmlTransient
	public String getLibrary() {
		return super.getLibrary();
	}

	@Override
	@XmlTransient
	public int getOrder() {
		// TODO Auto-generated method stub
		return super.getOrder();
	}

	@Override
	@XmlTransient
	public String getPubmed() {
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
		return super.isRequired();
	}

	@Override
	@XmlTransient
	public String getStatus() {
		return super.getStatus();
	}

	@Override
	@XmlTransient
	public String getSystem() {
		return super.getSystem();
	}

	@Override
	@XmlTransient
	public String getSysId() {
		return super.getSysId();
	}

	@Override
	@XmlTransient
	public String getTitle() {
		return super.getTitle();
	}

	/*
	 * Override toString, Object equals
	 */

	@Override
	public String toString() {
		String retVal = "Course (" + this.getId() + ") "
				+ this.getDisplayLabel();
		retVal += "Division: " + this.getDivision() + " Catalog Num: "
				+ this.getCatNo();
		retVal += " Last updated: " + this.getUpdateTimestamp();
		return retVal;
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if (super.equals(obj)) {
			Course c = (Course) obj;
			isEqual = c.getTerm().equals(this.getTerm())
					&& c.getCatNo().equals(this.getCatNo());

		}
		return isEqual;
	}

}
