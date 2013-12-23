package edu.harvard.liblab.ecru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
import edu.harvard.liblab.ecru.data.EcruBase;
import edu.harvard.liblab.ecru.data.Reading;

public class TestCourseAndReading {

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
		Date startDate = new Date(); 
		Date endDate = new Date();
		startDate = dateFormat.parse("09/01/2003");
		endDate  = dateFormat.parse("12/30/2003");

		Course course = new Course("c_x328182", 
				"Diversity in New Testament Interpretation: 1 Peter. (Elisabeth Schssler Fiorenza )",
				"HDS 1883",endDate, "HDS",new ArrayList<String>(), 
				"Diversity in New Testament Interpretation: 1 Peter", startDate,
				"Fall 2003","http://isites.harvard.edu/icb/icb.do?keyword=k96364");

		solrSrvr.addBean(course);
		Reading r = Reading.createNonJournal("r_x309482",
				"Schussler Fiorenza, Elisabeth. Rhetoric and ethic: the politics of biblical studies. xi, 220 p. (1999) ",
				Arrays.asList("Schussler Fiorenza, Elizabeth"), "http://webservices.lib.harvard.edu/mobile/catalog/xmlAvailability/008296451",
				"008296451", null, "c_328182", null, null, endDate, 
				"0800627954 (alk. paper)", "1234-5687",
				null, null, "DIV", true, "Available", startDate, "HVD01", "Fall 2003",
				"Rhetoric and ethic: the politics of biblical studies");
		solrSrvr.addBean(r);
		r = Reading.createJournal("r_x309192",
				"Hochstetler, Kathryn. Crisis and Rapid Reequilibration: The Consequences of Presidential Challenge and Failure in Latin America.. Comparative politics43.2 JAN 2011: 127-146",
				Arrays.asList("Hochstetler, Kathryn"), null, null,"c_314515", null,
				"10.5129/001041511793931834",
				 endDate, "Comparative politics",null,
				 "LAM", null, false, "Available", startDate, null,"Fall 2003",
				"Crisis and Rapid Reequilibration: The Consequences of Presidential Challenge and Failure in Latin America");
		solrSrvr.addBean(r);

		UpdateResponse usp = solrSrvr.commit();  

	}

	@Test
	public void testFederatedSearch() {
		// example testing update at http://svn.apache.org/viewvc/lucene/dev/trunk/solr/solrj/src/test/org/apache/solr/client/solrj/SolrExampleTests.java?view=markup

		try {
			

			QueryResponse rsp = getQueryResponse( "(fiorenza AND Fall 2003)");
			List<EcruBase> beans = rsp.getBeans(EcruBase.class);
			if (beans == null || beans.size() == 0) {
				fail("couldn't find anything with fiorenza AND Fall 2003");
			} 
			else {
				Reading r = null;
				Course c = null;
				boolean hasReading = false;
				boolean hasCourse = false;
				ArrayList<EcruBase>  bs = new ArrayList<EcruBase>();
				for (EcruBase bean: beans){
					if (hasReading = bean.isReading()) 
						bs.add(bean.toReading());
					if (hasCourse = bean.isCourse()) {
						bs.add(bean.toCourse());
					}
					if (hasReading && hasCourse) {
						break;
					}
				}
				
				assertTrue("Got at least one course and one reading", isAMatch(bs, "fiorenza"));
			}

		} catch (SolrServerException e) {
			fail("got SolrServer exception! " + e.getMessage());
		}

	}

	private static boolean isAMatch(List<EcruBase> beans, String... fragments) {
		boolean retVal = false;
		boolean hasCourse = false;
		boolean hasReading = false;
		try {
				for (EcruBase eb: beans) {
					if (!hasReading && eb.getType().equals(EcruBase.T_READING)) {
						Reading r = eb.toReading();
						System.out.println(r);
						hasReading = matchFrags(eb.getDisplayLabel().toLowerCase(),fragments);
					}
					else if (!hasCourse && eb .getType().equals(EcruBase.T_COURSE)) {
						Course c = eb.toCourse();
						System.out.println(c);
						hasCourse = matchFrags(eb.getDisplayLabel().toLowerCase(),fragments);
						}
					}


		} catch (Exception e) {
			e.printStackTrace();
		}
		retVal = hasCourse && hasReading;
		return retVal;
	}
	@AfterClass
	public static void tearDown() {
		try {
			solrSrvr.deleteByQuery("id:c_x328182");
			solrSrvr.deleteByQuery("id:r_x309482");
			solrSrvr.deleteByQuery("id:r_x309192");
			solrSrvr.commit();
		} catch (SolrServerException e) {
			fail("exception: " + e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			fail("exception: " + e.getMessage());
		}
	}
	private static boolean matchFrags(String display, String... fragments) {
		boolean retVal = true;
		for (String frag: fragments) {
			if (display.indexOf(frag.toLowerCase()) <0) {
				retVal = false;
				break;
			}
		}
		return retVal;
	}
	// TODO: move this to some central test class
	protected QueryResponse getQueryResponse(String qryStr) throws SolrServerException {
		SolrQuery query = new SolrQuery();
		query.setQuery(qryStr);
		QueryResponse rsp = solrSrvr.query( query );
		return rsp;
	}
}
