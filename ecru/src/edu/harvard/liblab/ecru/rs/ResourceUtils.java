package edu.harvard.liblab.ecru.rs;

import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrQuery.SortClause;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import edu.harvard.liblab.ecru.Config;
import edu.harvard.liblab.ecru.JsonpFilter;
import edu.harvard.liblab.ecru.data.Course;
import edu.harvard.liblab.ecru.data.EcruBase;
import edu.harvard.liblab.ecru.data.Facet;
import edu.harvard.liblab.ecru.data.Faceting;
import edu.harvard.liblab.ecru.data.ListFacets;
import edu.harvard.liblab.ecru.data.Reading;
import edu.harvard.liblab.ecru.data.ResultsList;
import edu.harvard.liblab.ecru.data.Summary;
import edu.harvard.liblab.ecru.utils.SolrServer;

/**********************************************************************
 * [INSERT COPYRIGHT INFO]
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  unleashed
 *   
 *   provides static methods used by other resources
 *  
 */
public class ResourceUtils {

	private static final String QUERY_ALL = "*:*";
	private static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");//1976-03-06T23:59:59.999Z
	private static HttpSolrServer solrSrvr;
	private static final String SINGLE_COURSE_PATH = "/courses/";
	private static final String READINGS_FOR_COURSE_PATH = "/readings/courses/";
	private static final String AUTHOR_SORT = "?sort=reading.first_author+asc";

	public static String getSolrDate(Date date) throws Exception {
		String retDate = df.format(date) + "T00:00:00.000Z";
		return retDate;
	}

	/**
	 * @param req
	 * @return the value part of a query string, with incoming "%20" returned as 
	 * "+"; if no query string, then return the universal query value (*:*)
	 */
	public static String initQueryString(@Context HttpServletRequest req) {
		String queryString = req.getQueryString();
		if (queryString == null ) {
			queryString = "";
		}
		else {
			queryString = queryString.trim().replaceAll("\\%20"," ");
		}
		if (queryString.isEmpty()) {
			queryString = QUERY_ALL;
		}
		return queryString;
	}
	public static HashMap<String,List<String>> addFacetFields(HashMap<String,List<String>>  facetMap, 
			String[] fields) {
		List<String> values = facetMap.get("facet.field");
		if (values == null) {
			values = new ArrayList<String>();
		}
		for (String field: fields) {
			if (!values.contains(field)) {
				values.add(field);
			}
		}

		facetMap.put("facet.field", values);
		return facetMap;
	}
	public static ResultsList queryForResultsList(UriInfo req, String q, 
			List<String> fqs, String fl, String start, String rows, 
			HashMap<String,List<String>> facetMap, String sort) throws Exception {
		SolrQuery query = createSolrQuery(q, fqs, fl, start, rows, facetMap , sort);
		QueryResponse qr = getQueryResponse(query);

		ResultsList rl =  createResultsList(qr, rows, req);
		return rl;
	}
	public static String fullQueryString(String q, List<String> fqs, String fl, 
			String start, String rows) {
		String retVal = "q=";
		if (q!=null&& !q.trim().isEmpty()) {
			retVal += q;
		}
		else {
			retVal += "*:*";
		}
		if (fqs != null && !fqs.isEmpty()) {
			for (String fq:fqs) {
				retVal += "&fq=" + fq;
			}
		}
		if (fl!= null && !fl.trim().isEmpty()) {
			retVal += "&fl=" + fl;
		}
		if (start != null && !start.isEmpty()) {
			retVal += "&start=" + start;
		}
		if (rows != null && !rows.isEmpty()) {
			retVal += "&rows=" + rows;
		}
		return retVal;
	}
	public static SolrQuery createSolrQuery(String q, List<String> fqs, String fl, 
			String start, String rows, HashMap<String,List<String>> facetMap, String sort) {
		SolrQuery query = new SolrQuery(); 
		if (q == null || q.trim().isEmpty()) {
			q = QUERY_ALL;
		}
		query.setQuery(q);
		if (fqs != null && !fqs.isEmpty()) {
			query.setFilterQueries(fqs.toArray(new String[fqs.size()]));
		}
		try {
			Integer s = new Integer(start);
			query.setStart(s);

		} catch (NumberFormatException e) {
			if (start != null)
				System.out.println("start not integer: " + start);
		}
		try {
			Integer r = new Integer(rows);
			query.setRows(r);

		} catch (NumberFormatException e) {
			if (rows != null)
				System.out.println("rows not integer: " + rows);
		}
		if (fl != null && !fl.isEmpty()) {
			if (fl.indexOf("type") < 0) {
				fl += ",type";
			}
			String[] fields = fl.split(",");
			for (int i = 0; i < fields.length; i++) {
				fields[i] = fields[i].trim();
			}

			query.setFields(fields);

		}
		if ( sort != null && !sort.trim().isEmpty()) {
			String[] sortArr = sort.split(",");
			ArrayList<SortClause> clauses = new ArrayList<SortClause>();
			for (String s: sortArr) {
				if (!s.isEmpty()) {
					String[] sc = s.split(" ");
					String v = sc[0];
					String order = "asc";
					if (sc.length == 2) {
						order = sc[1];
					}
					clauses.add(new SortClause(v, 
							(order.equals("asc")? ORDER.asc: ORDER.desc)));
				}
			}
			query.setSorts(clauses);
		}
		query = addFacetsToQuery(query, facetMap);
		return query;	
	}
	public static SolrQuery addFacetsToQuery(SolrQuery query, HashMap<String,List<String>> facetMap) {
		if (!facetMap.isEmpty()) {
			List<String> doFacet = facetMap.get("facet");
			if (doFacet == null ||( !doFacet.isEmpty() && 
					!doFacet.get(0).toLowerCase().equals("false")) ){
				query.setFacet(true);
				List<String> limits = facetMap.get("facet.limit");
				try {
					query.setFacetLimit(Integer.valueOf(limits.get(0)));

				} catch (Exception e) {
					// we don't care about this
				}
				query.setFacetMinCount(1);
				limits = facetMap.get("facet.mincout");
				try {
					query.setFacetLimit(Integer.valueOf(limits.get(0)));

				} catch (Exception e) {
					// we don't care about this
				}			
				List<String> values = facetMap.get("facet.field");
				if (values != null && !values.isEmpty()) {
					query.addFacetField(values.toArray(new String[values.size()]));
				}
			}
		}
		return query;
	}
	public static HashMap<String,List<String>> getFacetMap(UriInfo req) {
		MultivaluedMap<String, String> mmap = req.getQueryParameters();
		HashMap<String,List<String>> facetMap = new HashMap<String,List<String>>();
		Iterator<String> iter = mmap.keySet().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			if (key.startsWith("facet")) {
				facetMap.put(key, mmap.get(key));
			}
		}
		return facetMap;
	}

	//TODO: deal with facets later
	public static ResultsList createResultsList(QueryResponse qr,  String inputRows, UriInfo req) 
			throws Exception {
		ResultsList rl = new ResultsList();
		URI uri = req.getRequestUri();
		String query = uri.getQuery();

		UriBuilder uriBuild = req.getRequestUriBuilder();
		SolrDocumentList docList = qr.getResults();
		long numFound = docList.getNumFound();
		long start = docList.getStart();
		long rows = 10;
		try {
			if (inputRows != null && !inputRows.trim().isEmpty()) {
				Long r = new Long(inputRows);
				rows = r.longValue();
			}
		} catch (NumberFormatException e) {
			System.err.println("Couldn't convert rows: " + inputRows);
		}

		rl.setItems(convertEcruBeans(qr.getBeans(EcruBase.class), start, uriBuild));

		Summary sum = new Summary(query, numFound,rows, start);


		// Don't bother with faceting if there are no more found than the rows
		if (numFound > rows) {
			Faceting facets = getFaceting(qr.getFacetFields());
			facets = decorateFacets( uriBuild, facets);
			rl.setFaceting(facets);
			// get prev and next, if relevant
			if (start + rows < numFound) {
				sum.setNextUrl(createUrlWithNewParam(uriBuild, "start", (start+rows) ));
			}
			if (start - rows > -1) {
				sum.setPrevUrl(createUrlWithNewParam(uriBuild, "start", (start-rows)));				
			}
		}
		rl.setSummary(sum);
		return rl;
	}
	protected static ArrayList<EcruBase> convertEcruBeans(List<EcruBase> beans, 
			long start, UriBuilder uriBuild) 
					throws Exception {
		ArrayList<EcruBase> converted = new ArrayList<EcruBase>(beans.size());
		long index = start + 1;
		String rootUri = determineRootURI(uriBuild.build().toString());
		String courseUri = rootUri + SINGLE_COURSE_PATH;
		String readUri = rootUri + READINGS_FOR_COURSE_PATH;
		for (EcruBase b: beans) {
			b.setIndex(index++);
			if (b.isReading()) {
				Reading r = b.toReading();
				r.setCourseUrl(courseUri + r.getCourseId());
				converted.add(r);
			}
			else if (b.isCourse()) {
				Course c = b.toCourse();
				c.setReadingsUrl(readUri + c.getId()+ AUTHOR_SORT);
				converted.add(c);
			}
			else {
				throw new Exception("Uncategorizable bean " + b);
			}
		}
		return converted;

	}
	public static QueryResponse getQueryResponse(SolrQuery query) throws SolrServerException {
		HttpSolrServer solrS = getSolrServer();
		QueryResponse rsp = solrS.query( query ); 
		return rsp;
	}

	public static HttpSolrServer getSolrServer() {
		if (solrSrvr == null) {
			Config cfg = Config.getInstance();
			solrSrvr = SolrServer.getSolrServer(cfg.SOLR_URL);
		}
		return solrSrvr;
	}

	private static Faceting getFaceting( List<FacetField> facetFields) {
		Faceting faceting = new Faceting();
		faceting.setListFacets(new ArrayList<ListFacets>());
		if (facetFields != null) {
			for (int i = 0; i < facetFields.size(); i++) {
				if (facetFields.get(i).getValueCount() > 1) {
					ListFacets list = new ListFacets();
					list.setFacets(new ArrayList<Facet>());
					list.setHeader(facetFields.get(i).getName());
					for (Count c : facetFields.get(i).getValues()) {
						Facet f = new Facet();
						f.setTitle(c.getName());
						f.setCount(c.getCount());
						list.getFacets().add(f);
					}
					faceting.getListFacets().add(list);
				}
			}
		}
		return faceting;
	}
	private static Faceting decorateFacets(UriBuilder uribld, Faceting faceting) {
		Config cfg = Config.getInstance();
		Properties props = cfg.getProps();
		if (props == null) {
			props = new Properties();
		}
		if (faceting != null && faceting.getListFacets() != null) {
			for (ListFacets list : faceting.getListFacets()) {
				if (list.getFacets() != null) {
					String name = list.getHeader();
					String repl = (String)props.get(name);
					if (repl != null && !repl.isEmpty()) {
						list.setHeader(repl);
					}
					for (Facet f: list.getFacets()) {
						String value = quoteValue(f.getTitle());
						f.setUrl(createUrlWithNewParam(uribld, "fq", (name + ":" + value)));
					}
				}
			}

		}
		return faceting;
	}
	/**
	 * @param input
	 * @return String
	 * 
	 * If the input value does not have a "*" but does have a space, 
	 * put quotes around it.
	 */
	public static String quoteValue(String value) {
		String retVal = value.trim();
		if (retVal.indexOf("*") < 0 && retVal.indexOf(" ") > -1) {
			retVal = "\"" + retVal + "\"";
		}
		return retVal;
	}
	/**
	 * @param uriBuild
	 * @return
	 * 
	 * A clunky method of determining that part of the path that goes to the root of the webapp.
	 * 
	 */
	private static String determineRootURI(String url) {

		int inxs[] = new int[3];;
		inxs[0] = url.indexOf("/courses");
		inxs[1]= url.indexOf("/readings");
		inxs[2] = url.indexOf("/all");
		int inx = 0;
		for (int i = 0;  i < 3; i++) {
			if (inxs[i] < 0)
				inxs[i] = 100000000;
		}
		Arrays.sort(inxs);
		inx = inxs[0];
		if (inx != 100000000) {
			return url.substring(0,inx);
		}
		else {
			return "";
		}



	}
	private static String createUrlWithNewParam(UriBuilder uriBuild, String param, Object... val) {
		UriBuilder ub = UriBuilder.fromUri(uriBuild.build());
		// remove the jsonP parameter
		ub = ub.replaceQueryParam(JsonpFilter.CALLBACK_PARAM, (Object[])null);
		ub = ub.replaceQueryParam(param, val);
		URI uri =  ub.build(); 
//		String retPath = uri.getPath() + "?" + uri.getRawQuery();
//		return retPath;
		return uri.toString();
	}
	private static String cleanString(String input) {
		String retVal = "";
		if (input != null) {
			retVal = input.trim().replaceAll("\\%20"," ");
		}
		return retVal;

	}
}
