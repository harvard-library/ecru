/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/
/**
 * @author Bobbi Fox
 * 
 *         Project: ecru
 * 
 *         Reads a CSV file into the ecru solr4 database.
 * 
 */
package edu.harvard.liblab.ecru;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import edu.harvard.liblab.ecru.data.Course;
import edu.harvard.liblab.ecru.data.EcruBase;
import edu.harvard.liblab.ecru.data.Reading;
import edu.harvard.liblab.ecru.utils.SingletonSolrServer;

/**
 * @author bobbi
 *
 *   Project:  ecru
 *  
 */
/**
 * @author bobbi
 *
 *   Project:  ecru
 *  
 */
/**
 * @author bobbi
 *
 *   Project:  ecru
 *  
 */
public class LoadCsvData {
	public static final String USAGE =
			"java " + LoadCsvData.class + "-f csv_file -u solr_url -i [unique|addprefix] [-v]" ;
	private static final String FIELD_STR = 
			"ID,Type,Division,CatNo,Term,Name,Instructor,URL,Start Date,End Date,Title,Required,Author,Display,Status,Editor,Journal,Chapter,Library,Course,System,SystemID,ISBN,ISSN,DOI,PUBMED,LectureDate,Order,Note";
	private static final String[] FIELDS = FIELD_STR.split(",");
	//private static String DELIMITER
	private static SimpleDateFormat SIMPLE_DATE = new SimpleDateFormat("MM/dd/yyyy");
	private static List<SolrInputDocument> docUpdates = new ArrayList<SolrInputDocument>();
	private static List<EcruBase> beans = new ArrayList<EcruBase>();
	private static String url;
	private static HttpSolrServer solrSrvr;
	private static boolean isVerbose = false;
	private static boolean needsPrefix = false;
	private static int errRecs = 0;
	private static int numRecs = 0;
	/**
	 * 
	 */
	public LoadCsvData() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 7| args.length == 0 || 
				!args[0].equals("-f") || !args[2].equals("-u") 
				|| !args[4].equals("-i")) {
			System.err.println(USAGE);
			System.exit(1);
		}
		String filename = args[1].trim();
		url = args[3].trim();
		needsPrefix = !args[5].equals("unique");
		isVerbose = (args.length == 7 && args[6].equals("-v"));
		System.out.println("Loading data from " + filename + " " + (needsPrefix?"IDs will be prefixed ":" ") );
		long start = System.currentTimeMillis();
		boolean isReading = false;
		CSVPrinter printer = null;

		CSVFormat format = CSVFormat.EXCEL.withHeader().withDelimiter(',').withAllowMissingColumnNames(true);
		CSVParser parser;
		try {
			if (isVerbose) {
				printer = new CSVPrinter(System.err, format.withDelimiter('|'));
			}
			parser = CSVParser.parse(new File(filename), Charset.forName("UTF-8"),  format);

			solrSrvr = SingletonSolrServer.getSolrServer(url);	
			for (CSVRecord record: parser) {
				numRecs++;
				HashMap<String, String> recMap = new HashMap<String, String>();
				for (String field:FIELDS) {
					String value = null;
					try {
						value =record.get(field);
					} catch (IllegalArgumentException e) {
						if (e.getMessage().indexOf("expected one of") == -1) {
							e.printStackTrace();
							System.exit(1);
						}
					}
					value = value== null?"":value.trim();
					recMap.put(field, value);
				}
				String id = recMap.get("ID");
				if (id.isEmpty() ){
					if (isVerbose) {
						System.err.println("Record missing ID: " );
						printer.printRecord(record);
					}
				}
				else {
					String type = recMap.get("Type");
					SolrDocument sdoc = getDocFromSolr(recMap.get("ID"));
					try {
						if (type.toLowerCase().equals("course")) {
							processCourse(recMap, sdoc);
							isReading = false;
						}
						else {
							if (!isReading) {
								addUpdateCommit(); // just in case the preceeding course(s) are related
							}
							processReading(recMap, sdoc);
							isReading = true;
						}
					} catch (Exception e) {
						if (isVerbose) {
							System.err.println("Record # " + numRecs + 
									" not used:\n\t" + e.getMessage());
						}
						errRecs++;
					}
				}
				if (beans.size() > 20) {
					addUpdateCommit();
				}
			}
			parser.close();
			if (beans.size() > 0 || docUpdates.size() > 0) {
				addUpdateCommit();
			}
		} catch (FileNotFoundException e) {
			System.err.println(filename + " not found");
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		long end = System.currentTimeMillis();
		long courseTime = (end - start) / (long)1000;
		System.out.println(numRecs + " records found, of which " + errRecs + 
				" had a problem; time: " + courseTime + " seconds " + ((courseTime > 60)?("("  + (courseTime/(long)60) +" minutes)" ):""));
		System.exit(0);
	}
	/**
	 *  add reading(s)/course(s), plus any updates, to solr db
	 */
	private static void addUpdateCommit() throws Exception{
		// 
		UpdateResponse ur  = null;
		boolean needCommit = false;
		try {
			if (!beans.isEmpty()) {
				ur = solrSrvr.addBeans(beans);
				needCommit = true;
			}
			if (!docUpdates.isEmpty()) {
				ur = solrSrvr.add(docUpdates);
				needCommit = true;
			}
			if (needCommit) {
				ur = solrSrvr.commit();
			}
		} catch (SolrServerException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
		beans.clear();
		docUpdates.clear();
	}
	/**
	 * @param id  The Unique Key in solr
	 * @return  A record, or null
	 */
	private static SolrDocument getDocFromSolr(String id) {
		SolrQuery sq = new SolrQuery("id:" + id);
		SolrDocument sdoc = null;
		try {
			QueryResponse qr=  solrSrvr.query(sq);
			if (qr != null && qr.getResults() != null && qr.getResults().size() == 1) {
				sdoc = qr.getResults().get(0);
			}
		} catch (SolrServerException e) {
			if (isVerbose) {
				System.err.println("SolrServerException for id:" + id);
			}
			e.printStackTrace();
		}
		return sdoc;
	}
	/**
	 * @param cId  The Unique Key for a course record
	 * @return   true if there are Readings with this key for their 
	 *           readings.course_id
	 */
	private static boolean hasReadings(String cId) {
		boolean retVal = false;
		SolrQuery sq = new SolrQuery("reading.course_id:" + cId);
		try {
			QueryResponse qr=  solrSrvr.query(sq);
			retVal =  (qr != null && qr.getResults() != null && qr.getResults().size() > 0);
		}catch (SolrServerException e) {
			if (isVerbose) {
				System.err.println("SolrServerException for reading.course_id:" + cId);
			}
			e.printStackTrace();
		}
		return retVal;

	}
	
	/**
	 * @param rec        A HashMap containing the values for the Course record
	 * @param sdoc       The record from the solr db, if it already exists
	 * @throws Exception
	 * 
	 * A Course has to, minimally, have an id, name, term, start and end date. 
	 * This version of the loader assumes that, if the course already exists in
	 * the database, its data is being completely replaced by the incoming data,
	 * with the exception that the "has_readings" value is preserved.
	 * 
	 * If the Course is new, this method will check for Readings which have the
	 * reading.course_id equal to the Course's id, and will set the has_readings 
	 * accordingly
	 * 
	 * If the Display field is empty, it will be constructed from the name and 
	 * the first instructor in the instructor field (if it exists) as
	 *   {name} ({instructor} )
	 * 
	 */
	private static void processCourse(HashMap<String, String>rec, SolrDocument sdoc) throws Exception {
		String name = rec.get("Name");
		String term = rec.get("Term");
		String startDateStr = rec.get("Start Date");
		String endDateStr = rec.get("End Date");
		String errMsg = "";
		Date startDate = null;
		Date endDate = null;
		try {
			startDate = SIMPLE_DATE.parse(startDateStr);
			endDate = SIMPLE_DATE.parse(endDateStr);
		} catch (ParseException e) {
			errMsg = "bad start and/or end date";
		}
		if (term.isEmpty()) {
			errMsg += " missing Term";
		}
		if (name.isEmpty()) {
			errMsg += " missing name";
		}
		if (errMsg.isEmpty()) {
			String display = rec.get("Display");
			String[] insts = rec.get("Instructor").split(";");
			for (int i = 0; i < insts.length; i++) {
				insts[i] = insts[i].trim();
			}
			if (display.isEmpty()) {
				display = name;
				if (insts.length > 0) {
					display += " (" + insts[0] + ")";
				}
			}
			String id = rec.get("ID");
			if (needsPrefix) {
				id = "c_" + id;
			}
			String div = rec.get("Division");
			String catNo = rec.get("CatNo");
			String url = rec.get("URL");
			Course c = new Course(id, display,catNo, endDate, div, 
					Arrays.asList(insts), name, startDate, term, url);
			c.setUpdateTimestamp(new Date());
			boolean hasReadings = false;
			if (sdoc != null) {
				if (sdoc.get("course.has_readings") != null) {
					hasReadings = ((Boolean)sdoc.get("course.has_readings")).equals(Boolean.TRUE);
				}
				if (!sdoc.get("term").equals(term) || 
						!sdoc.get("start_date").equals(startDate) || 
						!sdoc.get("end_date").equals(endDate)) {
					updateReadingsTermDates(id, term, startDate, endDate);
				}
			} else {
				if (!hasReadings) {
					// make sure that readings weren't added BEFORE this course!!
					hasReadings = hasReadings(id);
				}
				
			}
			c.setReadings(hasReadings);
			beans.add(c);
		}
		else {
			throw new Exception("Course record " + errMsg);
		}

	}
	/**
	 * @param rec        A HashMap containing the values for the Reading record
	 * @param sdoc       The record from the solr db, if it already exists
	 * @throws Exception
 	 * A reading minimally requires a course id, a title,  and a status.
	 * If the referenced course does not exist in the database, 
	 * or this reading does not already exist, the term, 
	 * start and end dates are also required.
	 * 
	 * If the Display field is empty, this method will create one from existing 
	 * component fields.
	 *  
	 * This method will also update the relevant Course's has_readings flag.
	 * 
	 * This version of the loader assumes that, if the reading already exists in
	 * the database, its data is being completely replaced by the incoming data,
	 * 
	 */
	private static void processReading(HashMap<String, String> rec, SolrDocument sdoc) throws Exception {
		String title = rec.get("Title");
		String cId = rec.get("Course");
		String errMsg = "";
		if (needsPrefix) {
			cId = "c_" + cId;
		}
		if (cId.isEmpty()) {
			throw  new Exception("Reading is missing a Course ID");
		}
		String lib = rec.get("Library");
		String term = rec.get("Term");
		String startDateStr = rec.get("Start Date");
		String endDateStr = rec.get("End Date");
		String status = rec.get("Status");
		Date startDate = null;
		Date endDate = null;
		boolean updateHasReadings = false;
		SolrDocument solrCourse = getDocFromSolr(cId);
		if (solrCourse != null) {  // always take term, start/end dates from course!
			String cTerm = (String)solrCourse.get("term");
			Date cstartDate = (Date)solrCourse.get("start_date");
			Date cendDate = (Date)solrCourse.get("end_date");
			term = cTerm;
			startDate = cstartDate;
			endDate =cendDate;
			updateHasReadings = ((Boolean)solrCourse.get("course.has_readings")).equals(Boolean.FALSE);
		}
		else {
			if (sdoc != null  && sdoc.get("reading.course_id").equals(cId)) { 
				// if the reading exists in the db, and it's for the same course, use that stuff
				if (term.isEmpty()) {
					term = (String)sdoc.get("term");
				}
				if (startDateStr.isEmpty()) {
					startDate = (Date)sdoc.get("start_date");
				}
				if (endDateStr.isEmpty()) {
					endDate = (Date)sdoc.get("end_date");
				}
			} else {
				try {
					startDate = SIMPLE_DATE.parse(startDateStr);
					endDate = SIMPLE_DATE.parse(endDateStr);

				} catch (ParseException e) {

				}
			}

		}

		if (startDate == null) {
			errMsg = " bad or missing startDate";
		}
		if (endDate == null) {
			errMsg += " bad or missing endDate";
		}
		if (term.isEmpty()) {
			errMsg += " missing Term";
		}
		if (title.isEmpty()) {
			errMsg += " missing title";
		}

		if (errMsg.isEmpty()) {
			Date lectureDate = null;
			if (!rec.get("LectureDate").isEmpty()) {
				try {
					lectureDate = SIMPLE_DATE.parse(rec.get("LectureDate"));
				} catch (ParseException e) {

				}
			}

			String authorStr = rec.get("Author");
			String[] authors = authorStr.split(";");
			for (int i = 0; i < authors.length; i++) {
				authors[i] = authors[i].trim();
			}

			String editorStr = rec.get("Editor");
			String[] editors = editorStr.split(";");
			for (int i = 0; i < editors.length; i++) {
				editors[i] = editors[i].trim();
			}
			String display = rec.get("Display");
			if (display.isEmpty()) {
				display = authorStr;
				if (!display.isEmpty()) {
					display += ". " ;
				}
				if (!rec.get("Chapter").isEmpty()) {
					display += rec.get("Chapter") + "(chap.). ";
				}
				display += title + ". " ;
				if (!editorStr.isEmpty()){
					display += editorStr + " (ed.). ";
				}
				if (!rec.get("Journal").isEmpty()) {
					display += " in " + rec.get("Journal");
				}

			}
			String id = (needsPrefix)?"r_" + rec.get("ID"): rec.get("ID");
			int order = -1;
			if (!rec.get("Order").isEmpty()) {
				try {
					order = new Integer((String)rec.get("Order")).intValue();
				} catch (Exception e) {
					if (isVerbose) {
						System.err.println("Record #" + numRecs + 
								" has a value for Order (" + 
								rec.get("Order") + 
								") that can not be converted to an integer");
					}
				}
			}
			boolean required = (rec.get("Required").toLowerCase().equals("Y"));
			/*
			 * String id, String displayLabel, String annotation,
			List<String> authors,
			String availUrl, String sysId, String chapter, String courseId,
			String digUrl, String doiId, List<String> editors, Date endDate,
			String isbn, String issn, String journal, Date lectureDate,
			String library, int order,
			String pubmed, boolean required, String status, Date startDate,
			String system, String term, String title
			 */

			Reading r = new Reading(id, display, rec.get("Note"), Arrays.asList(authors),
					null, rec.get("SystemID"), rec.get("Chapter"), cId,
					rec.get("URL"), rec.get("DOI"), Arrays.asList(editors), 
					endDate,  rec.get("ISBN"), rec.get("ISSN"), rec.get("Journal"), lectureDate,
					lib, order, rec.get("PUBMED"), required, status, startDate, rec.get("System"), term, title);
			if (!authors[0].isEmpty()) {
				r.setFirstAuthor(authors[0]);
			}
			beans.add(r);
			if (updateHasReadings) {
				updateCourseHasReadings(cId);
			}
		}
		else {
			throw new Exception(errMsg);
		}

	}

	/**
	 * @param cId    The course ID
	 * @param term   The updated term
	 * @param startDate The updated startDate
	 * @param endDate  The updated endDate
	 * 
	 * If a Course record already existed in the solr db, and the incoming 
	 * Course record has an updated Term, Start Date, and/or End Date, all the 
	 * associated Reading records in the solr db must be updated as well.
	 */
	private static void updateReadingsTermDates(String cId, String term, 
			Date startDate, Date endDate) {
		SolrQuery sq = new SolrQuery("reading.course_id:" + cId);
		try {
			QueryResponse qr=  solrSrvr.query(sq);
			if (qr != null && qr.getResults() != null && qr.getResults().size() > 0) {
				for (SolrDocument sd: qr.getResults()) {
					SolrInputDocument doc = new SolrInputDocument();
					Map<String, String> partialUpdate = new HashMap<String, String>();
					doc.setField("id", sd.get("id"));
					partialUpdate.put("set", term);
					doc.addField("term", partialUpdate);
					Map<String, Date> partialStart = new HashMap<String, Date>();
					partialStart.put("set", startDate);
					doc.addField("start_date", partialStart);
					Map<String, Date> partialEnd = new HashMap<String, Date>();
					partialEnd.put("set", endDate);
					doc.addField("end_date", partialEnd);
					docUpdates.add(doc);
				}
			}
		}catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * @param id   The id of the course
	 * 
	 * This is only called  once the first associated Reading record 
	 * is read in after the Course record was created.
	 */
	private static void updateCourseHasReadings(String id) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.setField("id", id);
		Map<String, Boolean> partialUpdate = new HashMap<String, Boolean>();
		partialUpdate.put("set", new Boolean(true));
		doc.addField("course.has_readings", partialUpdate);
		docUpdates.add(doc);
	}


}
