package org.af.gMCP.gui.power;

import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.af.commons.widgets.validate.RealTextField;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;

public class Group {
	List<JTextField> ncp = new Vector<JTextField>();
	JTextField scname;
	SampleSizeDialog sd;
	

	public Group(SampleSizeDialog sd, String name) {
		this.sd = sd;
		scname = new JTextField(name);
		for (Node n : sd.getNodes()) {
			RealTextField rt = new RealTextField("0.0");
			rt.setText("0.0");
			ncp.add(rt);
		}
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
		String s = RControl.getR().eval("make.names(\""+scname.getText()+"\")").asRChar().getData()[0]+"=c(";
		for (JTextField jt : ncp) {		
			s += jt.getText()+", ";
		}
		return s.substring(0, s.length()-2)+")";
	}

}
