package org.mutoss.config;

import java.io.File;
import java.text.DecimalFormat;

public class GeneralConfig extends SpecificConfig {

	/**
	 * Constructor - use Configuration.getInstance().getGeneralConfig() to access it.
	 * @param conf GeneralConfig object
	 */
    GeneralConfig(Configuration conf) {
        super(conf);        
    }

    public final static String DISABLE = "disable";

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
    
    public void setEps(double eps) {
		setProperty("epsilon", ""+eps);		
	}
    
    public double getEpsilon() {
		return Double.parseDouble(getProperty("epsilon", "0.0001"));		
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

	public boolean useEpsApprox() {
		return Boolean.parseBoolean(getProperty("useEpsApprox", "false"));
	}
	
	public void setUseEpsApprox(boolean useEpsApprox) {
		setProperty("useEpsApprox", ""+useEpsApprox);
	}
	
	public DecimalFormat getDecFormat() {
		String s = "#.";
		for (int i=0; i < getDigits(); i++) {
			s = s + "#";
		}
		return new DecimalFormat(s);
	}
	
}
