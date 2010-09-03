package org.af.statguitoolkit.gui.datawizard;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import org.af.commons.Localizer;
import org.netbeans.spi.wizard.WizardPage;

public class PanelXLSInfo extends WizardPage implements ActionListener {

	JCheckBox jbNoInfo = new JCheckBox("Do not show me this info again.", false);
	
	public static String getDescription() {
        return  Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_XLSINFO_DESC");
	}
	
    public PanelXLSInfo(DataWizard wizard) {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        JTextPane jtreminder = new JTextPane();
        jtreminder.setStyledDocument(getDocument());
        
        panel.add(jtreminder);
        
        panel.add(jbNoInfo);
        jbNoInfo.addActionListener(this);
       
        add(panel);        
    }

    private DefaultStyledDocument getDocument() {
    	DefaultStyledDocument doc = new DefaultStyledDocument();
		try {
			SimpleAttributeSet attr = new SimpleAttributeSet();
			StyleConstants.setAlignment(attr, StyleConstants.ALIGN_CENTER);			
	        
			String s = "For proper import of Excel data, make sure that imported sheets:\n" +
					" - do NOT contain formulas,\n" +
					" - contain a unique identifier for missing data,\n" +
					" - do NOT contain empty cells,\n" +
					" - are properly formatted (numeric columns are defined as numeric and\n" +
					"   contain only numeric values, etc.)\n";			
			doc.insertString(doc.getLength(), s, attr);			
			doc.insertString(doc.getLength(),
					"Be careful with graphics, NAs and too large data sheets (filled with 0 or NAs).\n", attr);
			doc.insertString(doc.getLength(),
					"If you run into problems, save Excel file as CSV file or visit:\n", attr);			
			doc.insertString(doc.getLength(),
					"http://www.algorithm-forge.com/rjavaclient/faq#excel\n", getLink());
			
			doc.setParagraphAttributes(0, doc.getLength(), attr, true);
		} catch (BadLocationException ble) {
				
		}

    	return doc;
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
    
	public String canGoNext() {
		return null;
	}

	public void actionPerformed(ActionEvent e) {
		String info = jbNoInfo.isSelected()?"no":"yes";		
	}

}
