package af.statguitoolkit.config;


public class JavaConfig extends SpecificConfig {

	/**
	 * Constructor - use Configuration.getInstance().getJavaConfig() to access it.
	 * @param conf JavaConfig object
	 */
    JavaConfig(Configuration conf) {
        super(conf);
    }

    public String getMainClassName() {
        return getProperty("main.class");
    }


    public String getErrorHandlerClassName() {
        return getProperty("errorhandler.class", "DefaultExceptionHandler");
    }


    public boolean getWithErrorhandling() {
        return getBoolProperty("with.errorhandling", "true");
    }
    
    public String getLooknFeel() {
        return getProperty("looknfeel", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel");
    }

    public void setLooknFeel(String looknfeel) {
       setProperty("looknfeel", looknfeel);
    }

    public String getJavaHome() {
        return getProperty("java.home", System.getProperty("java.home"));
    }

    public void setJavaHomel(String javaHome) {
       setProperty("java.home", javaHome);
    }

}
