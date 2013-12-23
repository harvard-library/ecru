
package edu.harvard.liblab.ecru;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.harvard.liblab.ecru.data.Reading;
/**********************************************************************
 * [INSERT COPYRIGHT INFO]
 **********************************************************************/
/**
 * @author bobbi
 *
 *   Project:  unleashed
 *  
 */
public class TestReading {
	
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

	/**
	 * Test method for {@link edu.harvard.liblab.ecru.data.EcruBean#createJournal(java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Date, java.lang.String, java.util.Date, java.lang.String, boolean, java.lang.String, java.util.Date, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateJournal() {
		Date startDate = new Date(); 
		Date endDate = new Date();
		try {
			
			solrSrvr.commit();
			startDate = dateFormat.parse("09/01/2003");
			endDate  = dateFormat.parse("12/30/2003");
			
			Reading r = Reading.createJournal("r_x309192",
					"Hochstetler, Kathryn. Crisis and Rapid Reequilibration: The Consequences of Presidential Challenge and Failure in Latin America.. Comparative politics43.2 JAN 2011: 127-146",
					Arrays.asList("Hochstetler, Kathryn"), null, null,"c_314515", null,
					"10.5129/001041511793931834",
					 endDate, "Comparative politics",null,
					 "LAM", null, false, "Available", startDate, null,"Fall 2003",
					"Crisis and Rapid Reequilibration: The Consequences of Presidential Challenge and Failure in Latin America");
			solrSrvr.addBean(r);

			UpdateResponse usp = solrSrvr.commit();  
			usp.getStatus();
			QueryResponse rsp = getQueryResponse( "id:r_x309192");
			List<Reading> beans = rsp.getBeans(Reading.class);
			if (beans == null) {
				fail("couldn't find the reading id:r_x309192");
			} 
			else if (beans.size() != 1) {
				fail("didn't get only one bean.  Bean size: " + beans.size());
			}
			else {
				assertEquals("Retrieved reading should match " + r.toString(),
						r.getDisplayLabel(),  beans.get(0).getDisplayLabel());
				assertTrue("This should register as a journal", beans.get(0).isJournal());
			}
		} catch (ParseException e) {
			fail("got date parse exception! " + e.getMessage());
		} catch (IOException e) {
			fail("got io exception! " + e.getMessage());
		} catch (SolrServerException e) {
			fail("got SolrServer exception! " + e.getMessage());
		}
	}

	/**
	 * Test method for {@link edu.harvard.liblab.ecru.data.EcruBean#createNonJournal(java.lang.String, java.lang.String, java.util.List, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.List, java.util.Date, java.util.List, java.lang.String, java.util.Date, java.lang.String, boolean, java.lang.String, java.util.Date, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testCreateNonJournal() {
		Date startDate = new Date(); 
		Date endDate = new Date();
		
		try {
			
			startDate = dateFormat.parse("09/01/2013");
			endDate  = dateFormat.parse("12/30/2013");
			
			Reading r = Reading.createNonJournal("r_x309482",
					"Schussler Fiorenza, Elisabeth. Rhetoric and ethic: the politics of biblical studies. xi, 220 p. (1999) ",
					Arrays.asList("Schussler Fiorenza, Elizabeth"), "http://webservices.lib.harvard.edu/mobile/catalog/xmlAvailability/008296451",
					"008296451", null, "c_328182", null, null, endDate, 
					"0800627954 (alk. paper)", "1234-5687",
					null, null, "DIV", true, "Available", startDate, "HVD01", "Fall 2003",
					"Rhetoric and ethic: the politics of biblical studies");
			solrSrvr.addBean(r);

			UpdateResponse usp = solrSrvr.commit();  
			usp.getStatus();
			QueryResponse rsp = getQueryResponse( "id:r_x309482");
			List<Reading> beans = rsp.getBeans(Reading.class);
			if (beans == null) {
				fail("couldn't find the course id:r_x309482");
			} 
			else if (beans.size() != 1) {
				fail("didn't get only one bean.  Bean size: " + beans.size());
			}
			else {
				assertEquals("Retrieved course should match " + r.toString(),
						r.getDisplayLabel(),  beans.get(0).getDisplayLabel());
				assertFalse("This should register as a non-journal", beans.get(0).isJournal());
			}
		} catch (ParseException e) {
			fail("got date parse exception! " + e.getMessage());
		} catch (IOException e) {
			fail("got io exception! " + e.getMessage());
		} catch (SolrServerException e) {
			fail("got SolrServer exception! " + e.getMessage());
		}
		
	}

	/**
	 * Test method for {@link edu.harvard.liblab.ecru.data.EcruBean#getType()}.
	 */
	@Test
	public void testGetType() {
		try {
			QueryResponse rsp = getQueryResponse( "id:r_x309482");
			List<Reading> beans = rsp.getBeans(Reading.class);
			if (beans == null) {
				fail("couldn't find the course id:r_x309482");
			} 
			else if (beans.size() != 1) {
				fail("didn't get only one bean.  Bean size: " + beans.size());
			}
			else {
				assertTrue("This should be a 'reading' type", 
						beans.get(0).getType().equals("reading"));
				
			}
			
		} catch (SolrServerException e) {
			{
				fail("got SolrServer exception! " + e.getMessage());
			}
		}
	}

	/**
	 * Test method for {@link edu.harvard.liblab.ecru.data.EcruBean#isJournal()}.
	 */
	@Test
	public void testIsJournal() {
		try {
			QueryResponse rsp = getQueryResponse( "id:r_x309482");
			List<Reading> beans = rsp.getBeans(Reading.class);
			if (beans == null) {
				fail("couldn't find the course id:r_x309482");
			} 
			else if (beans.size() != 1) {
				fail("didn't get only one bean.  Bean size: " + beans.size());
			}
			else {
				assertFalse("Shouldn't be a journal", beans.get(0).isJournal());
				
			}
			rsp = getQueryResponse( "id:r_x309192");

			beans = rsp.getBeans(Reading.class);
			if (beans == null) {
				fail("couldn't find the course id:r_x309192");
			} 
			else if (beans.size() != 1) {
				fail("didn't get only one bean.  Bean size: " + beans.size());
			}
			else {
				assertTrue("Should be a journal", beans.get(0).isJournal());
				
			}
		} catch (SolrServerException e) {
			{
				fail("got SolrServer exception! " + e.getMessage());
			}
		}
	}
	@AfterClass
	public static void tearDown() {
		try {
			solrSrvr.deleteByQuery("id:r_x309482");
			solrSrvr.deleteByQuery("id:r_x309192");
			solrSrvr.commit();
		} catch (SolrServerException e) {
			fail("got SolrServer exception! " + e.getMessage());
		} catch (IOException e) {
			fail("got SolrServer exception! " + e.getMessage());
		}
	}
	// TODO: move this to some central test class
		private QueryResponse getQueryResponse(String qryStr) throws SolrServerException {
			SolrQuery query = new SolrQuery();
		    query.setQuery(qryStr);
			QueryResponse rsp = solrSrvr.query( query );
			return rsp;
		}
}
