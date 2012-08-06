package org.af.gMCP.gui;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import org.af.gMCP.config.Configuration;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ErrorDialogChooseLevel extends JDialog {

	protected JRadioButton jrbNo = new JRadioButton("Send no error report.");
	protected JRadioButton jrbMinimal = new JRadioButton("Minimal: Just send the stack trace + version + OS type"); 
	protected JRadioButton jrbDefault = new JRadioButton("Default: Send the most important information");
	protected JRadioButton jrbMax = new JRadioButton("Maximal: Includes more information about the system");
	protected JCheckBox jcbScreenshot = new JCheckBox("Send screenshot of GUI");	
	
	public ErrorDialogChooseLevel(JFrame parent) {
		super(parent, "Select information", true);
		setLocationRelativeTo(parent);

		String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu";
		String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";

		FormLayout layout = new FormLayout(cols, rows);
		getContentPane().setLayout(layout);
		CellConstraints cc = new CellConstraints();

		ButtonGroup group = new ButtonGroup();
	    group.add(jrbNo);
	    group.add(jrbMinimal);
	    group.add(jrbDefault);
	    group.add(jrbMax);
	    
	    String reportLevel = Configuration.getInstance().getClassProperty(this.getClass(), "report level", "default");
	    
	    if (reportLevel.equals("no")) {
	    	jrbNo.setSelected(true);
	    } else if (reportLevel.equals("minimal")) {
	    	jrbMinimal.setSelected(true);
	    } else if (reportLevel.equals("default")) {
	    	jrbDefault.setSelected(true);
	    } else if (reportLevel.equals("max")) {
	    	jrbMax.setSelected(true);
	    } 
		
		int row = 2;
		
		getContentPane().add(jrbNo, cc.xyw(2, row, 3));
		
		row += 2;
		
		getContentPane().add(jrbMinimal, cc.xyw(2, row, 3));
		
		row += 2;
		
		getContentPane().add(jrbDefault, cc.xyw(2, row, 3));
		
		row += 2;
		
		getContentPane().add(jrbMax, cc.xyw(2, row, 3));
		
		row += 2;
		
		getContentPane().add(jcbScreenshot, cc.xyw(2, row, 3));
		
		setVisible(true);
	}
}
