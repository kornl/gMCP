package org.mutoss.config;


public class JavaConfig extends SpecificConfig {

	/**
	 * Constructor - use Configuration.getInstance().getJavaConfig() to access it.
	 * @param conf JavaConfig object
	 */
    JavaConfig(Configuration conf) {
        super(conf);
    }
    
    public String getLooknFeel() {
        return getProperty("looknfeel", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
    }

    public void setLooknFeel(String looknfeel) {
       setProperty("looknfeel", looknfeel);
    }

}
