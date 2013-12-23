package edu.harvard.liblab.ecru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.harvard.liblab.ecru.data.Course;

public class TestCourse {

	//TODO: when we rename the collection, this needs to be changed
	private static String url ;
	private static HttpSolrServer solrSrvr;
	private static  DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	@BeforeClass
	public static void setUp() throws Exception {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream("conf/test_ecru.properties"));
			//System.out.println("Application config set to " + ClassLoader.getSystemResource(propertyFile));
		} catch (Exception e) {
			throw new RuntimeException("Couldn't load project configuration!", e);
		}
		url = props.getProperty("solr.url");
		solrSrvr = new HttpSolrServer(url);
	}

	@Test
	public void testCourseStringStringStringDateStringListOfStringStringDateStringDateString() {
		// example testing update at http://svn.apache.org/viewvc/lucene/dev/trunk/solr/solrj/src/test/org/apache/solr/client/solrj/SolrExampleTests.java?view=markup

		Date startDate = new Date(); 
		Date endDate = new Date();

		try {
			startDate = dateFormat.parse("09/01/2003");
			endDate  = dateFormat.parse("12/30/2003");

			Course course = new Course("c_x328182", 
					"Diversity in New Testament Interpretation: 1 Peter. (Elisabeth Schssler Fiorenza )",
					"HDS 1883",endDate, "HDS",new ArrayList<String>(), 
					"Diversity in New Testament Interpretation: 1 Peter", startDate,
					"Fall 2003","http://isites.harvard.edu/icb/icb.do?keyword=k96364");

			solrSrvr.addBean(course);
			UpdateResponse usp = solrSrvr.commit();  
			usp.getStatus();
			QueryResponse rsp = getQueryResponse( "id:c_x328182");
			List<Course> beans = rsp.getBeans(Course.class);
			if (beans == null) {
				fail("couldn't find the course id:c_x328182");
			} 
			else if (beans.size() != 1) {
				fail("didn't get only one bean.  Bean size: " + beans.size());
			}
			else {
				assertEquals("Retrieved course should match " + course.toString(),
						course,  beans.get(0));
			}
			// add instructors
			//Fiorenza, Elisabeth Schussler  Lee, Robin
			course.getInstructors().addAll(Arrays.asList("Fiorenza, Elisabeth Schussler", "Lee, Robin"));
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", "c_x328182");
			Map<String, List<String>> operation = new HashMap<String, List<String>>();
			operation.put("set", Arrays.asList("Fiorenza, Elisabeth Schussler", "Lee, Robin"));
			doc.addField("course.instructor", operation);
			solrSrvr.add(doc);
			solrSrvr.commit();
			rsp = getQueryResponse( "id:c_x328182");
			beans = rsp.getBeans(Course.class);
			if (beans == null) {
				fail("couldn't find the course id:c_x328182");
			} 
			else if (beans.size() != 1) {
				fail("didn't get only one bean.  Bean size: " + beans.size());
			}
			else {
				assertEquals("Retrieved course instructors should match ",
						course.getInstructors(),  beans.get(0).getInstructors());
				assertEquals("Type must be 'course'", "course", beans.get(0).getType());
			}

		} catch (ParseException e) {
			fail("got date parse exception! " + e.getMessage());
		} catch (IOException e) {
			fail("got io exception! " + e.getMessage());
		} catch (SolrServerException e) {
			fail("got SolrServer exception! " + e.getMessage());
		}

	}

	
	
	@AfterClass
	public static void tearDown() {

		try {
			solrSrvr.deleteByQuery("id:c_x328182");
			solrSrvr.commit();
		} catch (SolrServerException e) {
			fail("exception: " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("exception: " + e.getMessage());
		}

	}
	// TODO: move this to some central test class
	protected QueryResponse getQueryResponse(String qryStr) throws SolrServerException {
		SolrQuery query = new SolrQuery();
		query.setQuery(qryStr);
		QueryResponse rsp = solrSrvr.query( query );
		return rsp;
	}
}
