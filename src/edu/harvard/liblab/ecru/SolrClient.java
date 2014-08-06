package edu.harvard.liblab.ecru;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/

/**
 * @author bobbi
 *
 *   Project:  ecru
 *  
 */
public class SolrClient {

	private String baseURL;
	private String userAgent;

	

	private static final int DEFAULT_CONNECTION_TIMEOUT = 60000;
	private static final int DEFAULT_SOCKET_TIMEOUT = 60000; 

	//the default constructor sets the following to the above defaults, use the other constructor if you need different values

	private int connectionTimeout;	
	private int socketTimeout;

	

	


	/**
	 * @param baseURL
	 * @param userAgent
	 * 
	 */
	public SolrClient(String baseURL, String userAgent) {
		init(baseURL, userAgent, DEFAULT_CONNECTION_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
	}


	/**
	 * @param baseURL
	 * @param userAgent
	 * @param keyStorePath
	 * @param keyStorePassword
	 * @param trustStorePath
	 * @param trustStorePassword
	 * 
	 * Takes the default connection and socket timeouts
	 */
	public SolrClient(String baseURL, String userAgent, String keyStorePath, 
			String keyStorePassword, String trustStorePath, String trustStorePassword) {
		init(baseURL, userAgent,  DEFAULT_CONNECTION_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
	}

	/**
	 * @param baseURL
	 * @param userAgent
	 * @param connectionTimeout - timeout until a connection is established in milliseconds - 0 means no timeout
	 * @param socketTimeout - time waiting for data, in milliseconds - 0 means no timeout
	 */
	public SolrClient(String baseURL, String userAgent, 
			int connectionTimeout, int socketTimeout) {
		init(baseURL, userAgent,  connectionTimeout, socketTimeout);
	}
	/**
	 * @param baseURL
	 * @param userAgent
	 *
	 * @param connectionTimeout - timeout until a connection is established in milliseconds - 0 means no timeout
	 * @param socketTimeout - time waiting for data, in milliseconds - 0 means no timeout
	 * 
		 * 
	 */
	private void init(String baseURL, String userAgent,  int connectionTimeout, int socketTimeout){
		if (userAgent == null || userAgent.trim().isEmpty()) {
			throw new RuntimeException("User agent MUST be defined!");
		}
		this.baseURL = baseURL;
		this.userAgent = userAgent;
		this.connectionTimeout = connectionTimeout;
		this.socketTimeout = socketTimeout;

	}		


	
	/**
	 * URL encodes against UTF-8, then changes all '+' to "%20"
	 * 
	 * @param str
	 * @return
	 */
	private String encode(String str) throws UnsupportedEncodingException {
		String retVal = str;
		try {
			retVal = URLEncoder.encode(str, "UTF-8");
			retVal = retVal.replaceAll("\\+", "%20");
		} catch (Exception e) {
			throw new UnsupportedEncodingException(e.getMessage());
		}
		return retVal;
	}



	/**
	 * Invokes the submitted URL with an application/xml accept header
	 * and returns the response body as a string
	 * @param url
	 * @return
	 * @throws SolrClientException
	 */
	public String callURLGet(String url) throws SolrClientException {
		int statusCode = -1;


		HttpClient client = createHttpClient();
		HttpGet request = new HttpGet(url);

		request.addHeader("user-agent", this.userAgent);
		request.addHeader("accept", "application/xml");

		try {
			HttpResponse response = client.execute(request);
			StatusLine status = response.getStatusLine();

			statusCode = status.getStatusCode();
			String responseString = EntityUtils.toString(response.getEntity());
			if(statusCode == HttpStatus.SC_OK) {
				return responseString;
			}
			else {
				throw new Exception(responseString);
			}
		}
		catch(Exception e) {
			String error = e.getMessage();
			if(statusCode == HttpStatus.SC_NOT_FOUND) {
				error = url + " could not be reached";
			}
			throw new SolrClientException("Error calling Solr service. "+error+"Status code="+statusCode,e);
		}
		finally {
			client.getConnectionManager().shutdown();
		}
	}



	/**
	 * Creates a new HttpClient. If keyStorePath and/or trustStorePath are set, a secure client will
	 * be created by reading in the keyStore and/or trustStore. In addition, system keys will also be 
	 * included (i.e. those specified in the JRE).
	 * 
	 * Most of the code below is "borrowed" from edu.harvard.hul.ois.drs2.callservice.ServiceClient
	 * 
	 * @return  an HttpClient ready to roll!
	 */
	protected HttpClient createHttpClient() throws SolrClientException {  			
		// turn on SSL logging, according to Log4j settings

		DefaultHttpClient httpClient = new DefaultHttpClient(); 

		// set parameters
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 
				connectionTimeout);
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, socketTimeout);  
		httpClient.getParams().setParameter(CoreProtocolPNames.HTTP_CONTENT_CHARSET, "UTF-8");       	
		httpClient.getParams().setParameter(ClientPNames.HANDLE_AUTHENTICATION, true);

		return httpClient;
	}
}
