package edu.harvard.liblab.ecru;

import java.util.Properties;


public class Config {
	
	public String SOLR_URL;

    
	private static Config instance;
	public static Properties PROPS = new Properties();
	
	public static String propertyFile = "ecru.properties";
	
	private Config() {
		
		PROPS = new Properties();
		try {
			PROPS.load(this.getClass().getClassLoader().getResourceAsStream(Config.propertyFile));
		} catch (Exception e) {
			throw new RuntimeException("Couldn't load project configuration!", e);
		} 
		
		SOLR_URL 			= PROPS.getProperty("solr.url");
		System.err.println("Config initialized.  Solr URL is: " + SOLR_URL);
		
	}
	public Properties getProps() {
		return PROPS;
	}
	public static synchronized Config getInstance() {
		if (instance == null)
			instance = new Config();
		
		return instance;
	}	
	
	
}
