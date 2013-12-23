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

/* [INSERT COPYRIGHT INFO]
		 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  unleashed
 *   
 *   Handles Courses solr call
 *  
 */
@Path("/all")
public class All {
	private static final String TERM_FQ = "term:";
	private static final String CURRENT_FQ = "end_date:[NOW TO *]";
	
	public All() {
		// TODO Auto-generated constructor stub
	}
	@GET
	@Path("terms/{tid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getAllByTerm(@Context  UriInfo req, 
			@PathParam("tid") String tid,
			@QueryParam("q") String q, @QueryParam("rows") String rows, 
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
		
		return getCombinedResults(req, q,rows,start,fqs,fl, facetMap, sort);
		
	}
	@GET
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getAll(@Context UriInfo req, 
			@QueryParam("q") String q, @QueryParam("rows") String rows, 
			@QueryParam("start") String start,
			@QueryParam("fq") List<String> fq,
			@QueryParam("fl") String fl,
			@QueryParam("sort") String sort)
			throws Exception {
		HashMap<String,List<String>> facetMap = ResourceUtils.getFacetMap(req);
		return getCombinedResults(req, q,rows,start,fq,fl, facetMap,sort);
		
	}
	private ResultsList getCombinedResults( UriInfo req,
			String q, String rows, String start, 
			List<String> fq, String fl, HashMap<String,List<String>> facetMap,
			String sort) throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		ResourceUtils.addFacetFields(facetMap, new String[] {"course.division", "reading.library"});
		ResultsList rl =  ResourceUtils.queryForResultsList(req, q, fqs, fl, start, 
				rows, facetMap, sort);
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
