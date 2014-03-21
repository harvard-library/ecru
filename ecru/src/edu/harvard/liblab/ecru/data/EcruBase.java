package edu.harvard.liblab.ecru.data;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.solr.client.solrj.beans.Field;

import edu.harvard.liblab.ecru.utils.DataUtils;

/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  ecru
 *   
 *   This POJO class reflects the entirety of the Ecru schema.  It is subclassed
 *   by Course and Reading
 *  
 */
/**
 * @author bobbi
 *
 *   Project:  ecru
 *  
 */
@XmlSeeAlso({Course.class, Reading.class})
public class EcruBase {
	
	public static final String T_READING = "reading";
	public static final String T_COURSE = "course";

	// The properties are ordered according to subclass needs  

	/*
	 *   Common fields
	 */
	 @Field
	 protected String id;
	 
	 @Field
	 protected String type;
	 
	 // where it is in the list; set by the returning program
	 protected long index;
	 
	 @Field("display_label") 
	 protected String displayLabel;

	 
	 @Field("end_date")
	 protected Date endDate;
	 
	 @Field("start_date")
	 protected Date startDate;

	 @Field
	 protected String term;
 
	 @Field("updated")
	 protected Date updateTimestamp;

	 /*
	  *  Fields needed for Course
	  */
	 @Field("course.cat_no")
	 protected String catNo;

	 @Field("course.division")
	 protected String division;

	 @Field("course.has_readings")
	 protected boolean readings;
	 
	 @Field("course.instructor")
	 protected List<String> instructors;

	 @Field("course.name")
	 protected String name;


	 @Field("course.url")
	 protected String url;
	 
	 /*
	  *  Fields needed for Reading
	  */
	 @Field("reading.author")
	 protected List<String> authors;
	 
	 @Field("reading.avail_url")
	 protected String availUrl;
	 
 
	 @Field("reading.chapter")
	 protected String chapter;
	 
	 @Field("reading.course_id")
	 protected String courseId;
	 
	 @Field("reading.dig_url")
	 protected String digUrl;
	 
	 @Field("reading.doi")
	 protected String doiId;
	 
	 @Field("reading.editor")
	 protected List<String>  editors;
	 
	 @Field("reading.first_author")
	 protected String firstAuthor;
	
	 @Field("reading.isbn")
	 protected String isbn;
	 
	 @Field("reading.issn")
	 protected String issn;
 	 
	 @Field("reading.journal")
	 protected String  journal;
	 
	 @Field("reading.lecture_date")
	 protected Date lectureDate ;
	 
	 @Field("reading.library")
	 protected String library;
	 
	 @Field("reading.pubmed")
	 protected String pubmed;
	 
	 @Field("reading.required")
	 protected boolean required;
	 
	 @Field("reading.status")
	 protected String status;

	 @Field("reading.system")
	 protected String system;
	 
	 @Field("reading.system_id")
	 protected String sysId;
	 
	 @Field("reading.title")
	 protected String title;
	 
	public EcruBase() {
		
	}



/*
 * 
 *   GETTERS AND SETTERS
 */


	/**
	 * @param id
	 * @param type
	 * @param displayLabel
	 * @param endDate
	 * @param startDate
	 * @param term
	 * @param updateTimestamp
	 */
	public EcruBase(String id, String type, String displayLabel, Date endDate,
			Date startDate, String term, Date updateTimestamp) {
		super();
		this.id = id;
		this.type = type;
		this.displayLabel = displayLabel;
		this.endDate = endDate;
		this.startDate = startDate;
		this.term = term;
		this.updateTimestamp = updateTimestamp;
	}
	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		EcruBase eb = (EcruBase) obj;
		isEqual = (eb.getDisplayLabel().equals(displayLabel) && 
				eb.getType().equals(type) &&
				eb.getId().equals(id) && eb.getTerm().equals(term));
		return isEqual;
	}
	@XmlElementWrapper(name="authors")
	@XmlElement(name="author")
	public List<String> getAuthors() {
		return DataUtils.trimList(this.authors);
	}
	@XmlElement
	public String getAvailUrl() {
		return DataUtils.trimStr(this.availUrl);
	}
	@XmlElement
	public String getCatNo() {
		return DataUtils.trimStr(this.catNo);
	}
	@XmlElement
	public String getChapter() {
		return DataUtils.trimStr(this.chapter);
	}
	@XmlElement
	public String getCourseId() {
		return this.courseId;
	}

	@XmlElement
	public String getDigUrl() {
		return DataUtils.trimStr(this.digUrl);
	}


	@XmlElement
	public String getDisplayLabel() {
		return DataUtils.trimStr(this.displayLabel);
	}

	@XmlElement
	public String getDivision() {
		return DataUtils.trimStr(this.division);
	}

	@XmlElement
	public String getDoiId() {
		return this.doiId;
	}

	@XmlElementWrapper(name="editors")
	@XmlElement(name="editor")
	public List<String> getEditors() {
		return DataUtils.trimList(this.editors);
	}



	@XmlElement
	public Date getEndDate() {
		return this.endDate;
	}


	@XmlTransient
	public String getFirstAuthor() {
		return this.firstAuthor;
	}

	@XmlElement
	public String getId() {
		return this.id;
	}

	@XmlElement
	public long getIndex() {
		return this.index;
	}

	@XmlElementWrapper(name="instructors")
	@XmlElement(name="instructor")
	public List<String> getInstructors() {
		return DataUtils.trimList(this.instructors);
	}



	@XmlElement
	public String getIsbn() {
		return DataUtils.trimStr(this.isbn);
	}


	@XmlElement
	public String getIssn() {
		return DataUtils.trimStr(this.issn);
	}



	@XmlElement
	public String getJournal() {
		return DataUtils.trimStr(this.journal);
	}


	@XmlElement
	public Date getLectureDate() {
		return this.lectureDate;
	}



	@XmlElement
	public String getLibrary() {
		return DataUtils.trimStr(this.library);
	}


	@XmlElement
	public String getName() {
		return DataUtils.trimStr(this.name);
	}



	@XmlElement
	public String getPubmed() {
		return this.pubmed;
	}


	@XmlElement
	public Date getStartDate() {
		return this.startDate;
	}



	@XmlElement
	public String getStatus() {
		return DataUtils.trimStr(this.status);
	}


	@XmlElement
	public String getSysId() {
		return this.sysId;
	}



	@XmlElement
	public String getSystem() {
		return DataUtils.trimStr(this.system);
	}

	@XmlElement
	public String getTerm() {
		return DataUtils.trimStr(this.term);
	}


	@XmlElement
	public String getTitle() {
		return DataUtils.trimStr(this.title);
	}

	@XmlElement
	public String getType() {
		return this.type;
	}

	@XmlElement
	public Date getUpdateTimestamp() {
		return this.updateTimestamp;
	}


	@XmlElement
	public String getUrl() {
		return DataUtils.trimStr(this.url);
	}



	@XmlTransient
	public boolean isCourse() {
		return this.getType().equals(T_COURSE);
	}


	@XmlTransient
	public boolean isReading() {
		return this.getType().equals(T_READING);
	}



	public boolean isReadings() {
		return this.readings;
	}


	@XmlElement
	public boolean isRequired() {
		return this.required;
	}

	public void setAuthors(List<String> authors) {
		this.authors = DataUtils.trimList(authors);
	}
	public void setAvailUrl(String availUrl) {
		this.availUrl = DataUtils.trimStr(availUrl);
	}


	public void setCatNo(String catNo) {
		this.catNo = DataUtils.trimStr(catNo);
	}


	public void setChapter(String chapter) {
		this.chapter = DataUtils.trimStr(chapter);
	}



	public void setCourseId(String courseId) {
		this.courseId = courseId;
	}


	public void setDigUrl(String digUrl) {
		this.digUrl = DataUtils.trimStr(digUrl);
	}



	public void setDisplayLabel(String displayLabel) {
		this.displayLabel = DataUtils.trimStr(displayLabel);
	}


	public void setDivision(String division) {
		this.division = DataUtils.trimStr(division);
	}



	public void setDoiId(String doiId) {
		this.doiId = doiId;
	}

	public void setEditors(List<String> editors) {
		this.editors = DataUtils.trimList(editors);
	}



	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}



	public void setFirstAuthor(String firstAuthor) {
		this.firstAuthor = firstAuthor;
	}



	public void setId(String id) {
		this.id = id;
	}


	public void setIndex(long index) {
		this.index = index;
	}

	public void setInstructors(List<String> instructors) {
		this.instructors = DataUtils.trimList(instructors);
	}

	public void setIsbn(String isbn) {
		this.isbn = DataUtils.trimStr(isbn);
	}



	public void setIssn(String issn) {
		this.issn = DataUtils.trimStr(issn);
	}


	public void setJournal(String journal) {
		this.journal = DataUtils.trimStr(journal);
	}



	public void setLectureDate(Date lectureDate) {
		this.lectureDate = lectureDate;
	}


	public void setLibrary(String library) {
		this.library = DataUtils.trimStr(library);
	}



	public void setName(String name) {
		this.name = DataUtils.trimStr(name);
	}


	public void setPubmed(String pubmed) {
		this.pubmed = pubmed;
	}



	public void setReadings(boolean readings) {
		this.readings = readings;
	}

	
	public void setRequired(boolean required) {
		this.required = required;
	}


	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}



	public void setStatus(String status) {
		this.status = DataUtils.trimStr(status);
	}



	public void setSysId(String sysId) {
		this.sysId = sysId;
	}


	public void setSystem(String system) {
		this.system = DataUtils.trimStr(system);
	}



	public void setTerm(String term) {
		this.term = DataUtils.trimStr(term);
	}


	public void setTitle(String title) {
		this.title = DataUtils.trimStr(title);
	}



	public void setType(String type) {
		this.type = type;
	}

	public void setUpdateTimestamp(Date updateTimestamp) {
		this.updateTimestamp = updateTimestamp;
	}


	public void setUrl(String url) {
		this.url = DataUtils.trimStr(url);
	}


	/**
	 * Create a Course object from an EcruBase object; apparently,
	 * solrj annotations aren't transitive enough to keep from getting a 
	 * ClassCastException.
	 * 
	 * 
	 *
	 * @return
	 */
	public  Course toCourse() throws ClassCastException {
		if (!this.getType().equals(T_COURSE)) {
			throw new ClassCastException("Entry is not a Course");
		}
		Course c =  new Course(this.getId(), this.getDisplayLabel(), this.getCatNo(),
				this.getEndDate(), this.getDivision(), this.getInstructors(),
				this.getName(), this.getStartDate(), this.getTerm(),
				this.getUrl());
		c.setIndex(this.getIndex());
		c.setReadings(this.isReadings());
		return c;
	}



	/**
	 * Create a Reading object from an EcruBase object; apparently,
	 * solrj annotations aren't transitive enough to keep from getting a 
	 * ClassCastException.
	 * 
	 * 
	 *
	 * @return
	 */
	public  Reading toReading() throws ClassCastException {
		if (!this.getType().equals(T_READING)) {
			throw new ClassCastException("Entry is not a Reading");
		}
		Reading r = new Reading(this.getId(), this.getDisplayLabel(), this.getAuthors(),
				this.getAvailUrl(), this.getSysId(), this.getChapter(), this.getCourseId(),
				this.getDigUrl(), this.getDoiId(), this.getEditors(), this.getEndDate(), this.getIsbn(),
				this.getIssn(),
				this.getJournal(), this.getLectureDate(), this.getLibrary(),
				this.getPubmed(),
				this.isRequired(), this.getStatus(), this.getStartDate(), this.getSystem(),
				this.getTerm(), this.getTitle());
		r.setIndex(this.getIndex());
		return r;
	}

	@Override
	public String toString() {
		String retVal = this.type + " (" +
				this.getId() + ") " + this.getDisplayLabel();
		retVal += " Last updated: " + this.updateTimestamp;
		return retVal;

	}
}
