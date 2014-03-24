package org.af.gMCP.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.af.commons.widgets.buttons.OkCancelButtonPane;
import org.af.gMCP.config.Configuration;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ErrorDialogChooseLevel extends JDialog implements ActionListener {

	String[] reportLevels = new String[] {
			"Send no error report.",
			"Minimal: Just send the error + version + OS type",
			"Default: Send the most important information",
			"Maximal: Includes more information about the system"
	};
	
	protected JComboBox jcbReportLevel;
	protected JCheckBox jcbScreenshot = new JCheckBox("Send screenshot of gMCP GUI window");	
	protected JLabel reviewInfo = new JLabel("You will be able to review and change the collected data.");
	
	public ErrorDialogChooseLevel(JFrame parent) {
		super(parent, "Select information", true);
		setLocationRelativeTo(parent);

		String cols = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
		String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";

		FormLayout layout = new FormLayout(cols, rows);
		getContentPane().setLayout(layout);
		CellConstraints cc = new CellConstraints();
		
		jcbReportLevel = new JComboBox(reportLevels);

	    int rLevel;
	    try {
	    	rLevel = Integer.parseInt(Configuration.getInstance().getClassProperty(this.getClass(), "reportLevel", "2"));
	    } catch (Exception e) {
	    	rLevel = 2;
	    }
	    jcbReportLevel.setSelectedIndex(rLevel);
	    
		int row = 2;
		
		JTextArea jlabel = new JTextArea("We are sorry that an error occurred.\n" +
				"Please give us details about this error so that we can fix it.");
		jlabel.setOpaque(false);
		jlabel.setEditable(false);
		
		getContentPane().add(jlabel, cc.xyw(2, row, 3));
		
		row += 2;		
		
		getContentPane().add(jcbReportLevel, cc.xyw(2, row, 3));

		row += 2;
		
		jlabel = new JTextArea("The following information will be send to us:");
		jlabel.setOpaque(false);
		jlabel.setEditable(false);
		
		getContentPane().add(jlabel, cc.xyw(2, row, 3));
		
		row += 2;		

		getContentPane().add(reviewInfo, cc.xyw(2, row, 3));
		
		row += 2; 
		
		getContentPane().add(jcbScreenshot, cc.xyw(2, row, 3));

		row += 2;
		
		OkCancelButtonPane ocPane = new OkCancelButtonPane();
		getContentPane().add(ocPane, cc.xy(4, row));
		ocPane.addActionListener(this);
		
		pack();
		
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Configuration.getInstance().setClassProperty(this.getClass(), "reportLevel", ""+jcbReportLevel.getSelectedIndex());
	
	}
	
	public static void main(String[] args) {
		new ErrorDialogChooseLevel(new JFrame());
	}
}
