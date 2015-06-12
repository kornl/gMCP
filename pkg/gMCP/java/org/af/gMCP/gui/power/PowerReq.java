package org.af.gMCP.gui.power;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.af.commons.widgets.validate.RealTextField;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.graph.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jgoodies.forms.layout.CellConstraints;

public class PowerReq implements ActionListener {
	List<JTextField> ncp = new Vector<JTextField>();
	JTextField scname;
	
	JComboBox jcbType = new JComboBox(new String[] {"All", "Any", "User defined"});
	
	PDialog pd;
	
	public PowerReq(PDialog pd, String name) {
		this.pd = pd;
		scname = new JTextField(name);
		for (Node n : pd.getNodes()) {
			RealTextField rt = new RealTextField("0.0");
			rt.setText("0.0");
			ncp.add(rt);
		}
		jcbType.setPreferredSize(new Dimension(jcbType.getPreferredSize().width, ncp.get(0).getPreferredSize().height));
		jcbType.addActionListener(this);
	}
	
	public void addComponents(JPanel panel, CellConstraints cc, int row) {
		int col = 2;
		panel.add(scname, cc.xy(col, row));
		col += 2;
		panel.add(jcbType, cc.xy(col, row));
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

	public void actionPerformed(ActionEvent e) {
		//System.out.println("\""+jcbType.getSelectedItem()+"\"");
		if (jcbType.getSelectedItem().equals("User defined")) {
			new UserDefinedDialog(pd);
		}
	}
}
