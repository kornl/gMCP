package org.mutoss.config;


import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Through this class we set all persistent configuration settings.
 * You get an instance of this class via the static method getInstance().
 */
public class Configuration {
	protected static Log logger = LogFactory.getLog(Configuration.class);
    protected Preferences prefs;
    protected static Configuration instance = null;
    private String projectName;
    private final String keyPrefix;
    public final static String NOTFOUND = "___NOT_FOUND___";

    /**
     * The default constructor.
     * If you want to access a Configuration object, please use the static method getInstance().
     */
    protected Configuration() {
        prefs = Preferences.userRoot();
        keyPrefix = projectName + ".";
    }

    public final static String TOXICOLOGY = "toxicology";
    public final static String DOSERESPONSE = "doseresponse";

    /**
     * Configuration is a Singleton.
     *
     * @return Configuration Object
     */
    public static Configuration getInstance() {
        if (instance == null)
            instance = new Configuration();
        
        return instance;
    }
    
    /**
     * Sets for one Class a key to some String value.
     * @param c Class
     * @param key Key
     * @param value Value
     */
    public void setClassProperty(Class c, String key, String value) {
    	String cn = c.getName().substring(c.getName().lastIndexOf('.'));
    	setProperty(cn+"."+key, value);
    }

    /**
     * Returns for one Class the associated value to a key.
     * @param c Class
     * @param key Key
     */
    public String getClassProperty(Class c, String key) {
    	String cn = c.getName().substring(c.getName().lastIndexOf('.'));
    	return getProperty(cn+"."+key, Configuration.NOTFOUND);
    }

    protected String getProperty(String prop, String def) {
        return prefs.get(keyPrefix + prop, def);
    }

    protected String getProperty(String prop) {
        String s = getProperty(prop, NOTFOUND);
        if (s.equals(NOTFOUND)) {
            throw new RuntimeException("Property required but not set: " + prop);
        } else {
            return s;
        }
    }

    /**
     * Sets a string value for a property key string.
     * This value is stored via Preferences.userRoot().put(key, val).
     * @param key Property key to set.
     * @param val String value of this property.
     */
    public void setProperty(String key, String val) {
        prefs.put(keyPrefix + key, val);
    }    

    /**
     * Returns the name of the project (e.g. toxicology, doserespones, bioassay, etc.).
     * This is stored in props.getProperty("project.name") and 
     * used as name for several directories, prefixes, etc. 
     * @return name of the project
     */
    public String getProjectName() {
        return projectName;
    }

    public final static String LOCALINSTALL = "local-install";
	public static final String UNIFIEDCLIENT = "unified-client";

	public GeneralConfig getGeneralConfig() {
        return new GeneralConfig(this);
    }

    public PlotConfig getPlotConfig() {
        return new PlotConfig(this);
    }

    public JavaConfig getJavaConfig() {
        return new JavaConfig(this);
    }

    /**
     * Returns a String which lists all Preferences in Preferences.userRoot() which start with the keyPrefix.
     * @return String which lists all Preferences in Preferences.userRoot() which start with the keyPrefix.
     * @throws BackingStoreException
     */
    public String getConfigurationForDebugPurposes() {
    	String s = "";
    	s += keyPrefix + "\n";
    	try {
    		for (String key : prefs.keys()) {
    			if (key.startsWith(keyPrefix)) {
    				String val = prefs.get(key, "__NOT FOUND__");
    				key = key.substring(keyPrefix.length());
    				s += key + " : " + val + "\n";
    			}
    		}
    	} catch (BackingStoreException e) {
    		// We really don't want to throw an error here...
    		logger.error("Error printing configuration:\n"+e.getMessage(), e);
    	}
    	return s;
    }
    
    public void clearConfiguration() {
    	try {
			for (String key : prefs.keys()) {
				if (key.startsWith(keyPrefix)) {				
			    	prefs.remove(key);
				}
			}
		} catch (BackingStoreException e) {
			// We really don't want to throw an error here...
    		logger.error("Error clearing configuration:\n"+e.getMessage(), e);
		}
    }

}