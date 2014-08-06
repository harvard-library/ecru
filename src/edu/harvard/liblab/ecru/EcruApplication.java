package edu.harvard.liblab.ecru;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
/**********************************************************************
 *   Please see LICENSE.txt
 **********************************************************************/
/**
 * @author Bobbi Fox
 *
 *   Project:  ecru
 *    Handles the registration of resource classes, etc.
 *    
*/
public class EcruApplication extends ResourceConfig {

	public EcruApplication() {
		packages("edu.harvard.liblab.ecru.rs");
		register(RestObjectMapperProvider.class);
		register(JacksonFeature.class);
	}



}
