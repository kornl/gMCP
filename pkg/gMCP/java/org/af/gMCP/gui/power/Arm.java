package org.af.gMCP.gui.power;

import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.af.commons.widgets.validate.RealTextField;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;

public class Arm {
	List<JCheckBox> includeL = new Vector<JCheckBox>();
	JTextField scname;
	SampleSizeDialog sd;
	

	public Arm(SampleSizeDialog sd, String name) {
		this.sd = sd;
		scname = new JTextField(name, 30);
		for (Node n : sd.getNodes()) {
			JCheckBox jc = new JCheckBox();
			includeL.add(jc);
		}
	}
	
	public void addComponents(JPanel panel, CellConstraints cc, int row) {
		int col = 2;
		panel.add(scname, cc.xy(col, row));
		for (JCheckBox jc : includeL) {
			col += 2;
			panel.add(jc, cc.xy(col, row));
		}
		row +=2;
	}
	

	
	/* public void loadConfig(Element e) {
	scname.setText(e.getAttribute("name"));
	NodeList nlist = e.getChildNodes();
	for (int i=0; i<Math.min(nlist.getLength(), ncp.size()); i++) {
		ncp.get(i).setText(((Element)nlist.item(i)).getAttribute("ncp"));
	}
 }

public Element getConfigNode(Document document) {
	Element e = document.createElement("scenario");
	e.setAttribute("name", scname.getText());
	for (JTextField jt : ncp) {
		Element eNCP = document.createElement("ncp");
		eNCP.setAttribute("ncp", jt.getText());
		e.appendChild(eNCP);
	}
	return e;
} */

}
