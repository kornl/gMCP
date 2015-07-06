package org.af.gMCP.gui.power;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.af.commons.widgets.validate.RealTextField;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.graph.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jgoodies.forms.layout.CellConstraints;

public class Scenario2 implements NCPRequestor, ActionListener {
	List<JTextField> effSizes = new Vector<JTextField>();
	JTextField scname;
	//JButton ncpc = new JButton("Calculate NCP");
	
	PDialog pd;
	
	public Scenario2(PDialog pd, String name) {
		this.pd = pd;
		scname = new JTextField(name);
		for (Node n : pd.getNodes()) {
			RealTextField rt = new RealTextField("0.0", 10, -Double.MAX_VALUE, Double.MAX_VALUE);			
			rt.setText("0.0");
			effSizes.add(rt);
		}
		//ncpc.addActionListener(this);
	}
	
	public void addComponents(JPanel panel, CellConstraints cc, int row) {
		int col = 2;
		panel.add(scname, cc.xy(col, row));
		for (JTextField jt : effSizes) {
			col += 2;
			panel.add(jt, cc.xy(col, row));
		}
		//col +=2;
		//panel.add(ncpc, cc.xy(col, row));
		row +=2;
	}
	
	public String getEffSizeString() {		
		String s = RControl.getR().eval("make.names(\""+scname.getText()+"\")").asRChar().getData()[0]+"=c(";
		for (JTextField jt : effSizes) {		
			s += jt.getText()+", ";
		}
		return s.substring(0, s.length()-2)+")";
	}

	 public void loadConfig(Element e) {
		scname.setText(e.getAttribute("name"));
		NodeList nlist = e.getChildNodes();
		for (int i=0; i<Math.min(nlist.getLength(), effSizes.size()); i++) {
			effSizes.get(i).setText(((Element)nlist.item(i)).getAttribute("ncp"));
		}
	 }
	
	public Element getConfigNode(Document document) {
		Element e = document.createElement("scenario");
		e.setAttribute("name", scname.getText());
		for (JTextField jt : effSizes) {
			Element eNCP = document.createElement("ncp");
			eNCP.setAttribute("ncp", jt.getText());
			e.appendChild(eNCP);
		}
		return e;
	}

	public void setNCP(double[] ncps) {		
		
	}

	NCPCalculatorDialog ncpCD = null;
	
	public void actionPerformed(ActionEvent e) {
		if (ncpCD==null) {
			ncpCD = new NCPCalculatorDialog(pd, this);
		} else {
			ncpCD.setVisible(true);
		}
	}
}
