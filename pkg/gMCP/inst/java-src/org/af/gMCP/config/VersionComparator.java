package org.af.gMCP.config;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.af.commons.widgets.buttons.OKButtonPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Compares two version strings. Separators should only be points, e.g. 2.7.1 or 0.1.15
 */
public class VersionComparator extends JDialog implements ActionListener {
	
	protected static Log logger = LogFactory.getLog(VersionComparator.class);
	
	static String onlineversion;
	
	public VersionComparator(String longMessage, String txt, String news, String version, String onlineVersion) {
		super((Frame)null, "New version available!");
		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=0;
		
		JTextArea jta1 = new JTextArea(txt);
		JTextArea jta2 = new JTextArea(longMessage);
		JTextArea jta3 = new JTextArea(news);
		jta3.setFont(new Font("Monospaced", Font.PLAIN, 10));
		
		if (txt.length()>3) {  
			(getContentPane()).add(jta1, c);
			c.gridy++;
		}
		(getContentPane()).add(jta2, c);
		c.gridy++; c.weighty=1;
		JScrollPane js = new JScrollPane(jta3);
		(getContentPane()).add(js, c);
		c.gridy++; c.weighty=0;
		OKButtonPane ok = new OKButtonPane();
		ok.addActionListener(this);
		(getContentPane()).add(ok, c);
		
		getContentPane().setPreferredSize(new Dimension(800, 600));
		
		pack();	
		
	    setLocationRelativeTo(null);
	    
		setVisible(true);
	}
	
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

        StringTokenizer st1 = new StringTokenizer(v1, ".-");
        StringTokenizer st2 = new StringTokenizer(v2, ".-");

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
            logger.debug("Comparing token "+tok1+" vs. "+tok2+".");
            int v = Integer.parseInt(tok1) - Integer.parseInt(tok2);
            if (v != 0) {
                return v;
            }
        }
    }
    
	public static void getOnlineVersion() {
		try {
			if (Configuration.getInstance().getGeneralConfig().checkOnline()) {
				String version = Configuration.getInstance().getGeneralConfig().getVersionNumber();
				String rversion = Configuration.getInstance().getGeneralConfig().getRVersionNumber();								
				URL url = new URL("http://www.algorithm-forge.com/gMCP/version"+
						(System.getProperty("eclipse") != null?"test":"")
						+".php?R="+rversion
						+"&gMCP="+version
						+"&time="+Configuration.getInstance().getGeneralConfig().getRandomID());
				URL newsURL = new URL("http://cran.r-project.org/web/packages/gMCP/NEWS");
				logger.info("Get version from "+url.toString());
				URLConnection conn = url.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));			
				onlineversion = in.readLine();
				String line;
				String txt = "";
				while ((line = in.readLine()) != null) {
					if (line.equals("END")) break;
					txt += line + "\n"; 
				}
				in.close();
				
				logger.info("Get news from "+url.toString());
				conn = newsURL.openConnection();
				in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String news = "";
				while ((line = in.readLine()) != null) {
					if (line.equals("END")) break;
					news += line + "\n"; 
				}
				in.close();

				if (Configuration.getInstance().getGeneralConfig().reminderNewVersion() && compare(onlineversion, Configuration.getInstance().getGeneralConfig().getVersionNumber())>0) 
				{					
					String message = "The newest version on CRAN is "+onlineversion+". "+
									 "Your version is "+version+".\n"+
									 "If you want to update, please restart R and use install.packages(\"gMCP\").\n"+
									 "Please note that you can not update gMCP while it is loaded.\n" +
									 "" +
									 "NEWS:";
					//JOptionPane.showMessageDialog(null, message, "New version available!", JOptionPane.INFORMATION_MESSAGE);
					new VersionComparator(message, txt, news, version, onlineversion);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void actionPerformed(ActionEvent e) {		
		dispose();		
	}
	
}
