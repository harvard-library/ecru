package edu.harvard.liblab.ecru.data;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**********************************************************************
 * [INSERT COPYRIGHT INFO]
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  unleashed
 *   
 *   This POJO class reflects the "Reading" type in the Solr database
 *   A Reading contains the bibliographic information for a single item, 
 *   associated with a Course.
 *  
 */
/**
 * @author bobbi
 *
 *   Project:  unleashed
 *  
 */
public class Reading extends EcruBase {
 
	private String courseUrl;
	
	public Reading() {
		super();
		this.setType((EcruBase.T_READING));
	}

	
	/**
	 * @param id
	 * @param displayLabel
	 * @param authors
	 * @param availUrl
	 * @param catId
	 * @param chapter
	 * @param courseId
	 * @param digUrl
	 * @param doiId
	 * @param editors
	 * @param endDate
	 * @param isbn
	 * @param issn
	 * @param journal
	 * @param lectureDate
	 * @param library
	 * @param pubmed
	 * @param required
	 * @param status
	 * @param startDate
	 * @param system
	 * @param term
	 * @param title
	 */
	public Reading(String id, String displayLabel, List<String> authors,
			String availUrl, String sysId, String chapter, String courseId,
			String digUrl, String doiId, List<String> editors, Date endDate,
			String isbn, String issn, String journal, Date lectureDate,
			String library, String pubmed, boolean required, String status, Date startDate,
			String system, String term, String title) {
		super(id, EcruBase.T_READING, displayLabel, endDate, startDate, term, 
				new Date());
		
		this.setAuthors(authors);
		this.setAvailUrl( availUrl);
		this.setSysId(sysId);
		this.setChapter(chapter);
		this.setCourseId(courseId);
		this.setDigUrl(digUrl);
		this.setDoiId(doiId);
		this.setEditors(editors);
		this.setIsbn(isbn);
		this.setIssn(issn);
		this.setJournal( journal);
		this.setLectureDate(lectureDate);
		this.setLibrary(library);
		this.setPubmed(pubmed);
		this.setRequired(required);
		this.setStatus(status);
		this.setSystem(system);
		this.setTitle(title);
	}

	/**
	 * 
	 * Courtesy method for creating a Journal Reading
	 * 
	 * @param id
	 * @param displayLabel
	 * @param authors
	 * @param availUrl
	 * @param catId
	 * @param courseId
	 * @param digUrl
	 * @param doiId
	 * @param endDate
	 * @param journal
	 * @param lectureDate
	 * @param library
	 * @param pubmed
	 * @param required
	 * @param status
	 * @param startDate
	 * @param system
	 * @param term
	 * @param title
	 * @return
	 */
	public static Reading createJournal(String id, String displayLabel, List<String> authors,
			String availUrl, String catId, String courseId, String digUrl, String doiId,Date endDate,
			 String journal, Date lectureDate,
			String library, String pubmed, boolean required, String status, Date startDate,
			String system, String term, String title) {
		return new Reading(id, displayLabel, authors, availUrl, catId, null, 
				courseId, digUrl, doiId, null, endDate, null, null, journal, lectureDate, 
				library, pubmed, required, status, startDate, system, term, title);
	}

	/**
	 * 
	 * Courtesy method for creating a "non-journal" reading
	 * 
	 * @param id
	 * @param displayLabel
	 * @param authors
	 * @param availUrl
	 * @param catId
	 * @param chapter
	 * @param courseId
	 * @param digUrl
	 * @param editors
	 * @param endDate
	 * @param isbns
	 * @param journal
	 * @param lectureDate
	 * @param library
	 * @param required
	 * @param status
	 * @param startDate
	 * @param system
	 * @param term
	 * @param title
	 * @return
	 */
	public static Reading createNonJournal(String id, String displayLabel, List<String> authors,
			String availUrl, String catId, String chapter, String courseId,
			String digUrl, List<String> editors, Date endDate,
			String isbn, String issn, String journal, Date lectureDate,
			String library, boolean required, String status, Date startDate,
			String system, String term, String title) {
		
		return new Reading(id, displayLabel, authors, availUrl, catId, null, 
				courseId, digUrl, null,editors, endDate, isbn, issn, null, lectureDate,
				library, null,  required, status, startDate, system, term, title);
	}
	
	
	public boolean isJournal() {
		return (this.getJournal() != null && !this.getJournal().isEmpty());
	}
	
	/* 
	 *  Override jaxb methods that don't apply to Readings
	 */
	@Override
	@XmlTransient
	public String getCatNo() {
		// TODO Auto-generated method stub
		return super.getCatNo();
	}


	@Override
	@XmlTransient
	public String getDivision() {
		// TODO Auto-generated method stub
		return super.getDivision();
	}


	@Override
	@XmlTransient
	public List<String> getInstructors() {
		// TODO Auto-generated method stub
		return super.getInstructors();
	}

	@XmlElement
	public String getCourseUrl() {
		return this.courseUrl;
	}


	public void setCourseUrl(String courseUrl) {
		this.courseUrl = courseUrl;
	}


	@Override
	@XmlTransient
	public boolean isReadings() {
		// TODO Auto-generated method stub
		return super.isReadings();
	}


	@Override
	@XmlTransient
	public String getUrl() {
		// TODO Auto-generated method stub
		return super.getUrl();
	}
	
	/*
	 * any object-override methods
	 */
	
	@Override
	public String toString() {
		String retVal = "Reading (" + this.getId() + ") " + this.getDisplayLabel();
		retVal += " Library: " + this.getLibrary();
		retVal += " Last updated: " + this.getUpdateTimestamp();
		return retVal;
	}




	
}
