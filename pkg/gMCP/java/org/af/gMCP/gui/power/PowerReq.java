package org.af.gMCP.gui.power;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.af.commons.widgets.validate.RealTextField;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;

public class PowerReq implements ActionListener {
	List<JCheckBox> includeL = new Vector<JCheckBox>();
	JTextField scname;
	
	String[] selection = new String[] {"All of the selected", "Any of the selected", "User defined"};
	
	JComboBox jcbType = new JComboBox(selection);
	
	String userDefined;
	
	PDialog pd;
	
	public PowerReq(PDialog pd, String name) {
		this.pd = pd;
		scname = new JTextField(name);
		for (Node n : pd.getNodes()) {
			JCheckBox jc = new JCheckBox();
			includeL.add(jc);
		}
		jcbType.setPreferredSize(new Dimension(jcbType.getPreferredSize().width, includeL.get(0).getPreferredSize().height));
		jcbType.addActionListener(this);
	}
	
	public void addComponents(JPanel panel, CellConstraints cc, int row) {
		int col = 2;
		panel.add(scname, cc.xy(col, row));
		col += 2;
		panel.add(jcbType, cc.xy(col, row));
		for (JCheckBox jc : includeL) {
			col += 2;
			panel.add(jc, cc.xy(col, row));
		}
		row +=2;
	}
	
	public String getNCPString() {		
		String s = RControl.getR().eval("make.names(\""+scname.getText()+"\")").asRChar().getData()[0]+"=c(";
		for (JCheckBox jc : includeL) {			
			s += jc.getText()+", ";
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
		if (e.getSource()==jcbType && (jcbType.getSelectedItem().equals(selection[2]) || jcbType.getSelectedItem().equals("Edit user defined"))) {
			UserDefinedDialog udd = new UserDefinedDialog(pd);
			userDefined = udd.getUserDefined();			
			if (jcbType.getItemCount()==3) {
				//DefaultComboBoxModel model = (DefaultComboBoxModel) jcbType.getModel();
				jcbType.removeItemAt(2);
				jcbType.addItem(userDefined);
				jcbType.addItem("Edit user defined");
			} else {							
				jcbType.removeItemAt(2);
				jcbType.removeItemAt(2);
				jcbType.addItem(userDefined);
				jcbType.addItem("Edit user defined");
			}
			jcbType.setSelectedIndex(2);
			for (JCheckBox jc : includeL) {			
				jc.setSelected(true);				
			}
		}
		if (jcbType.getSelectedIndex()<2) {
			for (JCheckBox jc : includeL) {			
				jc.setEnabled(true);
			}
		} else {
			for (JCheckBox jc : includeL) {			
				jc.setEnabled(false);
			}
		}
	}
}
