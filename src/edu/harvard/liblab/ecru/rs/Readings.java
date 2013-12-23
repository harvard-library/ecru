
package edu.harvard.liblab.ecru.rs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

import edu.harvard.liblab.ecru.data.ResultsList;
/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  ecru
 *   
 *  Handles solr calls to Readings
 *  
 */
@Path("/readings")
public class Readings {
	static final String READINGS_FQ = "type:reading";
	static final String COURSE_ID_FQ = "reading.course_id:";
	static final String TERM_FQ = "term:";
	static final String AUTHOR_FQ = "reading.author:";
	static final String CURRENT_FQ = "end_date:[NOW TO *]";
	static final String LIB_FQ = "reading.library:";
	static final String READING_FQ = "id:";
	
	public Readings() {
		
	}
	@GET
	@Path("courses/{cid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getReadingsByCourse( @Context UriInfo req, 
			@PathParam("cid") String cid,
			@QueryParam("q") String q, 
			@QueryParam("rows") String rows, 
			@QueryParam("start") String start,
			@QueryParam("fq") List<String> fq,
			@QueryParam("fl") String fl,
			@QueryParam("sort") String sort) 
			throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		fqs.add(COURSE_ID_FQ + quoteValue(cid));
		HashMap<String,List<String>> facetMap = ResourceUtils.getFacetMap(req);
		ArrayList<String> min = new ArrayList<String>();
		min.add("2");
		facetMap.put("facet.mincount", min);
		return getReadingsResults(req,q,rows,start,fqs,fl, facetMap, sort);			
	}
	@GET
	@Path("terms/{tid}/libs/{lid}/authors/{aid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getReadingsForTermAndLibAndAuthors(@Context  UriInfo req, 
			@PathParam("tid") String tid,
			@PathParam("lid") String lid,
			@PathParam("aid") String aid,
			@QueryParam("q") String q, 
			@QueryParam("rows") String rows, 
			@QueryParam("start") String start,
			@QueryParam("fq") List<String> fq,
			@QueryParam("fl") String fl,
			@QueryParam("sort") String sort) 
			throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		fqs.add(LIB_FQ +  quoteValue(lid)); 
		if (tid.equals("current")) {
			fqs.add(CURRENT_FQ);
		}
		else {
			fqs.add(TERM_FQ + quoteValue(tid));
		}	
		fqs.add(AUTHOR_FQ+ quoteValue(aid));
		HashMap<String,List<String>>facetMap = ResourceUtils.getFacetMap(req);
		ArrayList<String> min = new ArrayList<String>();
		min.add("2");
		facetMap.put("facet.mincount", min);
		return getReadingsResults(req,q,rows,start,fqs,fl, facetMap, sort);				
	}
	@GET
	@Path("terms/{tid}/libs/{lid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getReadingsForTermAndLib(@Context  UriInfo req, 
			@PathParam("tid") String tid,
			@PathParam("lid") String lid,
			@QueryParam("q") String q, 
			@QueryParam("rows") String rows, 
			@QueryParam("start") String start,
			@QueryParam("fq") List<String> fq,
			@QueryParam("fl") String fl,
			@QueryParam("sort") String sort) 
			throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		fqs.add(LIB_FQ +  quoteValue(lid)); 
		if (tid.equals("current")) {
			fqs.add(CURRENT_FQ);
		}
		else {
			fqs.add(TERM_FQ + quoteValue(tid));
		}	
		HashMap<String,List<String>> facetMap = ResourceUtils.getFacetMap(req);
		ArrayList<String> min = new ArrayList<String>();
		min.add("2");
		facetMap.put("facet.mincount", min);
		return getReadingsResults(req,q,rows,start,fqs,fl, facetMap, sort);			
	}

	@GET
	@Path("terms/{tid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getReadingsByTerm(@Context  UriInfo req, 
			@PathParam("tid") String tid,
			@QueryParam("q") String q, 
			@QueryParam("rows") String rows, 
			@QueryParam("start") String start,
			@QueryParam("fq") List<String> fq,
			@QueryParam("fl") String fl,
			@QueryParam("sort") String sort) 
			throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		if (tid.equals("current")) {
			fqs.add(CURRENT_FQ);
		}
		else {
			fqs.add(TERM_FQ + quoteValue(tid));
		}		
		HashMap<String,List<String>> facetMap = ResourceUtils.getFacetMap(req);
		ArrayList<String> min = new ArrayList<String>();
		min.add("2");
		facetMap.put("facet.mincount", min);
		return getReadingsResults(req,q,rows,start,fqs,fl, facetMap, sort);	
	}
	@GET
	@Path("authors/{aid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getReadingsByAuthor(@Context  UriInfo req, 
			@PathParam("aid") String aid,
			@QueryParam("q") String q, 
			@QueryParam("rows") String rows, 
			@QueryParam("start") String start,
			@QueryParam("fq") List<String> fq,
			@QueryParam("fl") String fl,
			@QueryParam("sort") String sort) 
			throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		
		fqs.add(AUTHOR_FQ+ quoteValue(aid));
		HashMap<String,List<String>> facetMap = ResourceUtils.getFacetMap(req);
		ArrayList<String> min = new ArrayList<String>();
		min.add("2");
		facetMap.put("facet.mincount", min);
		return getReadingsResults(req,q,rows,start,fqs,fl, facetMap, sort);

	}
	@GET
	@Path("{rid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getReading(@Context UriInfo req, 
			@PathParam("rid") String rid,
			@QueryParam("q") String q, 
			@QueryParam("rows") String rows, 
			@QueryParam("start") String start,
			@QueryParam("fq") List<String> fq,
			@QueryParam("fl") String fl,
			@QueryParam("sort") String sort) 
			throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		fqs.add(READING_FQ + quoteValue(rid));
		HashMap<String,List<String>>facetMap = ResourceUtils.getFacetMap(req);
		ArrayList<String> min = new ArrayList<String>();
		min.add("2");
		facetMap.put("facet.mincount", min);
		return getReadingsResults(req,q,rows,start,fqs,fl, facetMap, sort);
		
	}
	@GET
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getReadings(@Context UriInfo req, 
			@QueryParam("q") String q, 
			@QueryParam("rows") String rows, 
			@QueryParam("start") String start,
			@QueryParam("fq") List<String> fq,
			@QueryParam("fl") String fl,
			@QueryParam("sort") String sort) 
			throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		HashMap<String,List<String>>facetMap = ResourceUtils.getFacetMap(req);
		ResourceUtils.addFacetFields(facetMap, new String[] {"reading.library", "term", "reading.author_str"});
		return getReadingsResults(req,q,rows,start,fqs,fl, facetMap, sort);
		
	}
	private ResultsList getReadingsResults( UriInfo req,
			String q, String rows, String start, 
			List<String> fq, String fl,
			HashMap<String,List<String>> facetMap, String sort) throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		fqs.add(READINGS_FQ);
		ResultsList rl =  ResourceUtils.queryForResultsList(req, q, fqs, fl, start, rows, facetMap, sort);
		return rl;
	}
	/**
	 * @param input
	 * @return String
	 * 
	 * If the input value does not have a "*" but does have a space, 
	 * put quotes around it.
	 */
	private String quoteValue(String value) {
		return ResourceUtils.quoteValue(value);
	}
	
}
