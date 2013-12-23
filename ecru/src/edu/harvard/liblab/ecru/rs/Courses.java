
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
 * [INSERT COPYRIGHT INFO]
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  unleashed
 *   
 *   Handles Courses solr call
 *  
 */
@Path("/courses")
public class Courses {
	static final String COURSE_FQ = "type:course";
	static final String COURSE_SCHOOL_FQ = "course.division:";
	static final String TERM_FQ = "term:";
	static final String CURRENT_FQ = "end_date:[NOW TO *]";
	static final String COURSE_ID = "id:";

	static final String QUERY_ALL = "q=*:*";
	/**
	 * 
	 */
	public Courses() {
		// TODO Auto-generated constructor stub
	}
	
	
	@GET
	@Path("terms/{tid}")
	@Consumes("application/json" )
	@Produces("application/json")
	public ResultsList getCoursesByTerm( @Context UriInfo req, 
			@PathParam("tid") String tid, 
			@QueryParam("q") String q, @QueryParam("rows") String rows, 
			@QueryParam("start") String start,
			@QueryParam("fq") List<String> fq,
			@QueryParam("fl") String fl,
			@QueryParam("sort") String sort)
			throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		HashMap<String,List<String>> facetMap = ResourceUtils.getFacetMap(req);
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		if (tid.equals("current")) {
			fqs.add(CURRENT_FQ);
			ResourceUtils.addFacetFields(facetMap, new String[] {"term"});
		}
		else {
			fqs.add(TERM_FQ + quoteValue(tid));
		}

		ResourceUtils.addFacetFields(facetMap, new String[] {"course.division"});
		return getCoursesResults(req,q,rows,start,fqs,fl,facetMap, sort);
		
		
	}
	@GET
	@Path("schools/{sid}/terms/{tid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getCoursesBySchoolByTerm(@Context  UriInfo req, 
			@PathParam("sid") String sid,
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
		fqs.add(COURSE_SCHOOL_FQ + quoteValue(sid));
		if (tid.equals("current")) {
			fqs.add(CURRENT_FQ);
		}
		else {
			fqs.add(TERM_FQ + quoteValue(tid));
		}
		HashMap<String,List<String>> facetMap = ResourceUtils.getFacetMap(req);
		ResourceUtils.addFacetFields(facetMap, new String[] {"term", "course.instructor_str"});
		return getCoursesResults(req,q,rows,start,fqs,fl,facetMap, sort);
		
	}
	@GET
	@Path("schools/{sid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getCoursesBySchool( @Context UriInfo req, 
			@PathParam("sid") String sid,
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
		fqs.add(COURSE_SCHOOL_FQ + quoteValue(sid));
		
		HashMap<String,List<String>>facetMap = ResourceUtils.getFacetMap(req);
		ResourceUtils.addFacetFields(facetMap, new String[] {"term", "course.instructor_str"});
		return getCoursesResults(req,q,rows,start,fqs,fl,facetMap,sort);
		
	}
	
	@GET
	@Path("{cid}")
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getCourse( @Context  UriInfo req, 
			@PathParam("cid") String cid,
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
		fqs.add(COURSE_ID + quoteValue(cid));
		HashMap<String,List<String>>facetMap = ResourceUtils.getFacetMap(req);
		ArrayList<String> min = new ArrayList<String>();
		min.add("2");
		facetMap.put("facet.mincount", min);
		return getCoursesResults(req,q,rows,start,fqs,fl,facetMap, sort);
		
	}
	@GET
	@Consumes("application/json" )
	@Produces({"application/json"})
	public ResultsList getCourses(@Context UriInfo req,
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
		HashMap<String,List<String>> facetMap = ResourceUtils.getFacetMap(req);

		ResourceUtils.addFacetFields(facetMap, new String[] {"course.division", "term", "course.instructor_str"});
		return getCoursesResults(req, q,rows,start,fqs,fl, facetMap, sort);
		
	}
	private ResultsList getCoursesResults(UriInfo req, String q, String rows, String start, 
			List<String> fq, String fl, HashMap<String,List<String>> facetMap,
			String sort) throws Exception {
		ArrayList<String> fqs =new ArrayList<String>();
		if (fq != null) {
			fqs = new ArrayList<String>(fq);
		}
		fqs.add(COURSE_FQ);
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
