package br.com.marteleto.project.analysis.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static transient Logger log = Logger.getLogger(PropertyUtil.class.getName());
	private static Properties properties = new Properties();

    public static void addProperty(String propsName) throws IOException {
    	if (propsName == null || propsName.trim().equals("")) {
			throw new IOException("propsName is null");
		}
		InputStream is = null;
		File file = new File(propsName);
		if (file.exists()) {
			is = new FileInputStream(file);
		} else {
			is = PropertyUtil.class.getResourceAsStream(propsName);
			if (is == null) {
				is = PropertyUtil.class.getClassLoader().getResourceAsStream(propsName);
				if (is == null) {
					is = loadPropertyInApplicationConfDir(propsName);
				}
			}
		}
		if (is == null) {
			throw new FileNotFoundException("File not found: " + propsName);
		}
		properties.load(is);
		is.close();
		log.log(Level.INFO, "File: {0} loaded successfully.", propsName);
    }
    
    private static InputStream loadPropertyInApplicationConfDir(String propsName) throws FileNotFoundException {
    	String applicationConfDir = PropertyUtil.getApplicationConfDir();
		if (propsName != null && !propsName.trim().equals("") && applicationConfDir != null && !applicationConfDir.trim().equals("")) {
			return new FileInputStream(new File(applicationConfDir + propsName));
		}
		return null;
    }
    
    public static String getProperty(String key) {
    	return PropertyUtil.properties.getProperty(key);
    }
    
    public static void setProperty(String key, String value) {
    	PropertyUtil.properties.setProperty(key, value);
	}
    
    public static void removeProperty(String key) {
    	PropertyUtil.properties.remove(key);
	}
    
    public static String getApplicationConfDir() {
    	return System.getProperty("application.conf.dir");
    }
}