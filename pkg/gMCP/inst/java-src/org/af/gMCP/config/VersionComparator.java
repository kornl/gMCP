package org.af.gMCP.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import org.af.commons.widgets.vi.SVNVersions;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Compares two version strings. Separators should only be points, e.g. 2.7.1 or 0.1.15
 */
public class VersionComparator {
	
	protected static Log logger = LogFactory.getLog(VersionComparator.class);
	
	static String onlineversionstring;
	static int onlineversion;
	
    /**
     * Compares two version strings. Separators should only be points, e.g. 2.7.1 or 0.1.15
     *
     * @param v1 version string A
     * @param v2 version string B
     * @return < 0 if v1 < v2, = 0 if v1=v2, > 0 if v1>v2
     */
    public static int compare(String v1, String v2) {
        if (v1.equals(v2))
            return 0;

        StringTokenizer st1 = new StringTokenizer(v1, ".");
        StringTokenizer st2 = new StringTokenizer(v2, ".");

        String tok1 = null, tok2 = null;
        while (true) {
            if (st1.hasMoreTokens()) {
                tok1 = st1.nextToken();
            } else {
                return -1;
            }
            if (st2.hasMoreTokens()) {
                tok2 = st2.nextToken();
            } else {
                return +1;
            }
            int v = Integer.parseInt(tok1) - Integer.parseInt(tok2);
            if (v != 0) {
                return v;
            }
        }
    }
    
    //TODO Put into another thread:
	public void getOnlineVersion() {		
		try {
			URL url = new URL("http://www.algorithm-forge.com/gMCP/version");
            logger.info("Get version from "+url.toString());
			URLConnection conn = url.openConnection();
			BufferedReader in = new BufferedReader(
					new InputStreamReader(
							conn.getInputStream()));			
			onlineversionstring = in.readLine();			
			in.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		onlineversion = SVNVersions.parseInt(onlineversionstring);	
	}
	
	
}
