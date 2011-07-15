package org.af.gMCP.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.af.commons.widgets.WidgetFactory;
import org.af.commons.widgets.buttons.OKButtonPane;
import org.af.gMCP.config.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Displays Information about the program, its licenses, used libraries,
 * their licenses, the web site ...
 */
public class AboutDialog extends JDialog implements ActionListener {
	private static Log logger = LogFactory.getLog(AboutDialog.class);
    private static final long serialVersionUID = 1L;
    
    JTextPane jtAbout = new JTextPane();
    
	
    public AboutDialog(JFrame parent) {
    	super(parent, "About gMCP-GUI "+Configuration.getInstance().getGeneralConfig().getVersionNumber());

        jtAbout.setStyledDocument(getDocument());
        jtAbout.setEditable(false);

        Container cp = getContentPane();
		cp.add(new JScrollPane(jtAbout));
        cp = WidgetFactory.makeDialogPanelWithButtons(cp, new OKButtonPane(), this);
		setContentPane(cp);

        pack();
		setLocationRelativeTo(parent);
		setVisible(true);
    }
    
    private DefaultStyledDocument getDocument() {
    	DefaultStyledDocument doc = new DefaultStyledDocument();
        logger.info("Creating About-Text.");
		try {			
			doc.insertString(doc.getLength(),					
					"gMCP-GUI"+"\n\n", getH1());			
			doc.insertString(doc.getLength(),
					"by Kornelius Rohmeyer is distributed under GPL>=2.0."+"\n\n", getT());			
			doc.insertString(doc.getLength(), "This program uses the libraries log4j, JLaTeXMath, iText (2.1.4), jxlayer,\n swingworker, commons logging/lang, JRI and JGoodies Forms.\n", getT());
			doc.setParagraphAttributes(0, doc.getLength(), getC(), true);
        } catch (BadLocationException ble) {

        }
    	return doc;
    }
    
    /**
     * Return the SimpleAttributeSet for a level 1 headline.
     * @return The SimpleAttributeSet for a level 1 headline.
     */
    public static SimpleAttributeSet getH1() {
    	SimpleAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attr, "SansSerif");
        StyleConstants.setFontSize(attr, 14);
        StyleConstants.setBold(attr, true);
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);        
        return attr;
    }
    
    /**
     * Return the SimpleAttributeSet for a level 1 headline.
     * @return The SimpleAttributeSet for a level 1 headline.
     */
    public static SimpleAttributeSet getC() {
    	SimpleAttributeSet attr = new SimpleAttributeSet();
        StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);        
        return attr;
    }
    
    /**
     * Return the SimpleAttributeSet for normal text.
     * @return The SimpleAttributeSet for normal text.
     */
    public static SimpleAttributeSet getT() {
    	SimpleAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attr, "SansSerif");
        StyleConstants.setFontSize(attr, 12);
        return attr;
    }
    
    /**
     * Return the SimpleAttributeSet for hyperlinks.
     * @return The SimpleAttributeSet for hyperlinks.
     */
    public static SimpleAttributeSet getLink() {
    	SimpleAttributeSet attr = new SimpleAttributeSet();
		StyleConstants.setFontFamily(attr, "SansSerif");
        StyleConstants.setFontSize(attr, 12);
        StyleConstants.setForeground(attr, new Color(0,0,160));
        StyleConstants.setUnderline(attr, true);
        return attr;
    }
    
	/**
	 * Evaluates ActionEvents.
	 * @param e ActionEvent
	 */
	public void actionPerformed(ActionEvent e) {		
		if (e.getActionCommand().equals(OKButtonPane.OK_CMD)) {
			dispose();
		}
		/* else if (jbVersion.getText().equals(e.getActionCommand())) {
			URL helpURL = null;
			try {		
				//TODO what happens if the user is offline like in the completely offline variant?
                helpURL = control.getConf().getWebstartConfig().getProjectVersionPageURL();
				jtAbout.setPage(helpURL);
			} catch (IOException ioe) {
		        logger.warn("Exception while displaying "+helpURL+".", ioe);
			}			
		}*/
	}
}
