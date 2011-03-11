package org.mutoss.config;

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
    
    public File getProjectPDFsPath() {
        return new File(getProperty("pdf.output", System.getProperty("user.dir")));
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

    public void setDigits(int digit) {
		setProperty("digit", ""+digit);		
	}
    
    public int getDigits() {
		return Integer.parseInt(getProperty("Digits", "3"));		
	}
    
    public void setLineWidth(int lw) {
		setProperty("linewidth", ""+lw);		
	}
    
    public int getLineWidth() {
		return Integer.parseInt(getProperty("linewidth", "2"));		
	}
    
	public boolean showFractions() {		
		return Boolean.parseBoolean(getProperty("showFractions", "true"));
	}
	
	public void setShowFractions(boolean showFractions) {		
		setProperty("showFractions", ""+showFractions);
	}
    
	public boolean getColoredImages() {		
		return Boolean.parseBoolean(getProperty("coloredImages", "true"));
	}
	
	public void setColoredImages(boolean colored) {		
		setProperty("coloredImages", ""+colored);
	}

	public int getDigitsInTables() {
		return Integer.parseInt(getProperty("digits.in.tables", "6"));	
	}

	public boolean showRejected() {
		return Boolean.parseBoolean(getProperty("showRejected", "true"));
	}
	
	public void setShowRejected(boolean showRejected) {		
		setProperty("showRejected", ""+showRejected);
	}
	
}
