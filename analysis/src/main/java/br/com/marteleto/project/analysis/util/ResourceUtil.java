package br.com.marteleto.project.analysis.util;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	private static ResourceUtil instance = null;
	private static final transient Logger log = Logger.getLogger(ResourceUtil.class.getName());
	private Locale locale;
	private transient List<java.util.ResourceBundle> resources;
	
	public ResourceUtil() {
		resources = new ArrayList<>();
		locale = new Locale("pt","BR");
	}
	
	private static ResourceUtil getInstance() {
		if (instance == null) {
			instance = new ResourceUtil();
		}
		return instance;
	}
	
	public static Locale getLocale() {
		return getInstance().locale;
	}

	public static void setLocale(Locale locale) {
		getInstance().locale = locale;
	}

	public static void addResource(String resourceName) {
    	getInstance().resources.add(ResourceBundle.getBundle(resourceName,getInstance().locale));
    	log.info("File: " + resourceName + " loaded successfully.");
    }
    
	public static String getResource(String key) {
		String[] parameters = {};
        return getInstance().getString(key,parameters);
    }

    public static String getResource(String key, String parameter) {
    	String[] parameters = {parameter};
        return getInstance().getString(key,parameters);
    }
    
    public static String getResource(String key, String[] parameters) {
        return getInstance().getString(key,parameters);
    }
    
    private String getString(String key, String[] parameters) {
    	String result = null;
    	if (resources != null && !resources.isEmpty()) {
    		for (java.util.ResourceBundle resource : resources) {
    			try {
    				result = resource.getString(key);
    			} catch (java.util.MissingResourceException e) {
    				log.log(Level.INFO, e.getMessage(), e);
    			}
    			if (result != null && !result.trim().equals("")) {
    				break;
    			}
    		}
    		if (result != null && parameters != null) {
            	result = MessageFormat.format(result, (Object[]) parameters);
            }
    	}
    	if (result == null) {
    		result = key;
    		log.log(Level.WARNING, "[ {0} ] key was not found.", key);
		}
        return result;
    }
}
