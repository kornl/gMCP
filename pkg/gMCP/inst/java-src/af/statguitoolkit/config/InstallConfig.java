package af.statguitoolkit.config;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InstallConfig extends SpecificConfig {

	/**
	 * Constructor - use Configuration.getInstance().getWebstartConfig() to access it.
	 * @param conf WebstartConfig object
	 */
    InstallConfig(Configuration conf) {
        super(conf);
    }

    public String getURL() {
        return getProperty("webstart.url");
    }
    
    public int getZipFileEntries() {
    	try {
    		return Integer.parseInt(getProperty("zip.file.entries"));
    	} catch(RuntimeException e) {
    		return 5354;
    	}
    }

    public String getBranchDir() {
        if (getProperty("deploy.or.test").equals("deploy")) {
        	return "deploy";
        } else {
        	return "testing";
        }
    }

    public String getProjectDir() {
        return getConf().getProjectName();
    }

    public String getProjectJar() {
        return getConf().getProjectName() + "_bc_complete.jar";
    }

    public URL getProjectDirURL() {
        String urlString = getURL();
        urlString += "/" + getBranchDir();
        urlString += "/" + getProjectDir();
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL constructed!");
        }
    }

    public URL getProjectJarURL() {
        String urlString = getURL();
        urlString += "/" + getBranchDir();
        urlString += "/" + getProjectDir();
        urlString += "/" + getProjectJar();
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL constructed!");
        }
    }

    public URL getProjectVersionPageURL() {
        String urlString = getURL();
        urlString += "/deploy";
        urlString += "/" + getProjectDir();
        urlString += "/version.html";
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL constructed!");
        }
    }

    public String getBase() {
    	String urlString = getURL();
    	urlString += "/" + getBranchDir();
    	urlString += "/" + getProjectDir();
    	return urlString; 
    }    

    public String getLibsDirURLString() {
        String urlString = getURL();
        urlString += "/libs";
        return urlString; 
    }

    public List<URL> getRunToolFiles() {
        List<URL> result = new ArrayList<URL>();        
        String urlString = getURL()+"/local_install";
        try {
            result.add(new URL(urlString + "/run.exe"));
            result.add(new URL(urlString + "/run.ini"));
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL constructed!");
        }
        return  result;
    }
    
    public URL getLibsListerURL() {
        String urlString = getURL()+"/libs/list_libs.php";
        try {
            return new URL(urlString);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed URL constructed!");
        }
    }

    String repos = null;
    
    public void setRepos(String repos) {
    	this.repos = repos;
    }
    
	public String getRepos() {
		if (repos==null) return "";
		return repos+",";
	}

	public boolean isLocal() {		
		logger.info("Is local? "+System.getProperty("install.local", Configuration.NOTFOUND));
		return !System.getProperty("install.local", Configuration.NOTFOUND).equals("false");
	}
}
