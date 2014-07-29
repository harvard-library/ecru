package edu.harvard.liblab.ecru;

import javax.ws.rs.ext.ContextResolver;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.SerializationConfig.Feature;

/**
 * @author bobbi
 *
 *   Project:  ecru
 *   
 *   Going from https://jersey.java.net/documentation/2.6/user-guide.html#json.jackson
 *   so I can hide the null values.
 *   Also: from http://wiki.fasterxml.com/JacksonFAQDateHandling:
 *   return dates as ISO timestamps
 *  
 */
public class RestObjectMapperProvider implements ContextResolver<ObjectMapper> {
	

	final ObjectMapper objMapper ;
	
	public RestObjectMapperProvider() {
		objMapper = createMapper();
	}
	@Override
	public ObjectMapper getContext(Class<?> arg0) {
		return objMapper;
	}
	private static ObjectMapper createMapper() {
		
		final ObjectMapper mapper = new ObjectMapper();
		mapper.configure(Feature.INDENT_OUTPUT, true);
		mapper.configure(Feature.WRITE_NULL_MAP_VALUES, false);
		// the next piece is from http://wiki.fasterxml.com/JacksonFAQDateHandling
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		mapper.setSerializationConfig(mapper.getSerializationConfig().withSerializationInclusion(JsonSerialize.Inclusion.NON_NULL));
		return mapper;
		
	}
}
