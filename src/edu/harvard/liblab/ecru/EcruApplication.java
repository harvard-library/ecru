package edu.harvard.liblab.ecru;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;

public class EcruApplication extends ResourceConfig {

	public EcruApplication() {
		packages("edu.harvard.liblab.ecru.rs");
		register(RestObjectMapperProvider.class);
		register(JacksonFeature.class);
	}



}
