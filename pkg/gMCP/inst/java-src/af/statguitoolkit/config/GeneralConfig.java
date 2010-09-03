package af.statguitoolkit.config;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GeneralConfig extends SpecificConfig {

	/**
	 * Constructor - use Configuration.getInstance().getGeneralConfig() to access it.
	 * @param conf GeneralConfig object
	 */
    GeneralConfig(Configuration conf) {
        super(conf);        
    }

    public final static String DISABLE = "disable";


    /**
     * Is is test or deploy version.
     *
     * @return true iff test version
     */
    public boolean isTestVersion() {
        return getProperty("deploy.or.test").equals("test");
    }

    public void setTestVersion(boolean isTest) {
        setProperty("deploy.or.test", isTest ? "test" : "deploy");
    }

    private String getHash() {
        return getProperty("hash", Configuration.NOTFOUND);
    }

    public void setPassPhrase(String s) {
    	setProperty("pass.phrase", s);
    }

    public String getPassPhrase() {
    	return getProperty("pass.phrase", "");
    }

    public boolean isPasswordProtected() {
    	return !(getHash().equals(Configuration.NOTFOUND) || getHash().equals(""));
    }


    /**
     * Calculates the md5 hash for a String.
     * @param s
     * @return md5 hash for String s or null if an error occured.
     * @throws NoSuchAlgorithmException
     */
    public static String getMD5(String s) throws NoSuchAlgorithmException {
    	MessageDigest md5;
    	md5 = MessageDigest.getInstance("MD5");
    	md5.reset();
    	md5.update(s.getBytes());
    	byte[] result = md5.digest();
    	StringBuffer hexString = new StringBuffer();
    	for (int i=0; i<result.length; i++) {
    		hexString.append(Integer.toHexString(0xFF & result[i]));
    	}
    	return hexString.toString();
    }

    /**
     * Checks whether a given String is a valid password.
     * Therefore the hash value is calculated and we look whether we can find it in the known valid hash value list,
     * returned by getHash().
     * @param s Password String
     * @return is this String a valid password?
     * @throws NoSuchAlgorithmException
     */
	public boolean isValidPassword(String s) throws NoSuchAlgorithmException {
		String h = getMD5(s);
		s = getHash();
		logger.info("Hashvalue: "+h);
    	logger.info("Passphrases: ");
    	while (s.indexOf(";") != -1) {
    		String p = s.substring(0, s.indexOf(";"));
    		logger.info(p);
    		if (p.equals(h)) {
    			return true;
    		}
    		s = s.substring(s.indexOf(";")+1, s.length());
    	}
    	logger.info(s);
    	if (s.equals(h)) {
			return true;
		}
    	return false;
	}

    public void setCheckForUpdates(boolean check) {
    	setProperty("check.for.updates", ""+check);
    }

    public String getCheckForUpdates() {
    	return getProperty("check.for.updates", ""+true);
    }

    public void setOldSVNVersion(String s) {
    	setProperty("old.svn.version", s);
    }

    public String getOldSVNVersion() {
    	return getProperty("old.svn.version", "0");
    }

    public String getSVNVersion() {
        return getProperty("svn.version");
    }

    public void setTempDir(String tempDir) {
        setProperty("tempdir", tempDir);
    }

    public String getTempDir() {
        String tmpDir = getProperty("tempdir", "");
        if (tmpDir.trim().equals("")) {
            tmpDir = System.getProperty("java.io.tmpdir");
        }
        return tmpDir;
    }

    public File getProjectVariantPath() {
    	logger.debug("Project Variant path. Parent:"+getProjectInstallPath().getAbsolutePath()+", Child:"+getConf().getProjectName());
        return new File(getProjectInstallPath(), getConf().getProjectName());
    }
    
    public File getProjectInstallPath() {
    	File homeInstPath = new File(System.getProperty("user.home"), "RWorkbench");
        String instDir = getProperty("install.dir", homeInstPath.getAbsolutePath());
        return new File(instDir);
    }

    public void setProjectInstallPath(File path) {
        setProperty("install.dir", path.getAbsolutePath());
    }

    public File getProjectLogsPath() {
        return new File(getProjectVariantPath(), "logs");
    }

    public File getProjectRSourcePath() {
        return new File(getProjectVariantPath(), "r_src");
    }

    /**
     * Returns the directory where the temporary csv files (converted from XLS) are saved.
     * This is always the directory "datasets" in getProjectInstallPath().
     */
    public File getProjectDataSetsPath() {
        return new File(getProjectVariantPath(), "datasets");
    }

    public File getProjectPlotsPath() {
        return new File(getProjectVariantPath(), "plots");
    }

    public File getProjectPDFsPath() {
        return new File(getProperty("pdf.output", getProjectVariantPath()+System.getProperty("file.separator")+"pdfs"));
    }

    public void setProjectPDFsPath(String path) {
    	setProperty("pdf.output", path);
    }

    public void setPDFViewerPath(String pdfViewerPath) {
        setProperty("acrobat.path", pdfViewerPath);
    }

    public String getPDFViewerPath() {
        return getProperty("acrobat.path", "");
    }

    public String getDesktopPath() {
        return getProperty("desktop.path", "");
    }

    public void setDesktopPath(String desktopPath) {
        setProperty("desktop.path", desktopPath);
    }

    public void setPDFViewerOptions(String pdfViewerOptions) {
        setProperty("pdfviewer.options", pdfViewerOptions);
    }

    public String getPDFViewerOptions() {
        return getProperty("pdfviewer.options", "");
    }

    public void setFontSize(int i) {
        setIntProperty("font.size", i);
    }

    public int getFontSize() {
        return getIntProperty("font.size", "12");
    }

    public String getSetupTitle() {
        return getProperty("setup.title");
    }

    public String getApplicationTitle() {
        return getProperty("application.title");
    }

    public String getAboutTitle() {
        return getProperty("about.title");
    }

    public String getCopyright() {
        return getProperty("about.copyright");
    }

    public String getServerRSourcePath() {
        return getProperty("server.r.src.path");
    }

    public String getBackgroundImage() {
        return getProperty("background.image", "");
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

	public void setLanguage(String string) {
		setProperty("language", string);		
	}

    public String getLanguage() {
        return getProperty("language", null);
    }

    public void setGridSize(int grid) {
		setProperty("grid", ""+grid);		
	}
    
    public int getGridSize() {
		return Integer.parseInt(getProperty("grid", "10"));		
	}
	
}
