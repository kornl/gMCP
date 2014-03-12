package org.af.gMCP.gui.dialogs;

import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.layout.CellConstraints;

public class Scenario {
	List<JTextField> ncp;
	JTextField scname;
	
	PowerDialog pd;
	
	public Scenario(PowerDialog pd, String name) {
		this.pd = pd;
		 scname = new JTextField(name);
		
	}
	
	public void addComponents(JPanel panel, CellConstraints cc, int row) {
		int col = 2;
		panel.add(scname, cc.xy(col, row));
		for (JTextField jt : ncp) {
			col += 2;
			panel.add(jt, cc.xy(col, row));
		}
		row +=2;
	}
	
	public String getNCPString() {		
		String s = "c(";
		for (JTextField jt : ncp) {		
			s += jt.getText()+", ";
		}
		return s.substring(0, s.length()-2)+")";
	}
}
