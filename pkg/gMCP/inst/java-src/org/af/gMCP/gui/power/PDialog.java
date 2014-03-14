package org.af.gMCP.gui.power;

import java.util.List;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JTabbedPane;

import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.graph.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class PDialog extends JDialog {

	CreateGraphGUI parent;
    Vector<Node> nodes;
    
    ScenarioPanel pNCP;    
    UserDefinedPanel userDefinedFunctions;
    CVPanel cvPanel;
	
	JTabbedPane tPanel = new JTabbedPane();

	public PDialog(CreateGraphGUI parent, String string, boolean b) {
		super(parent, string, b);
	}
	
	public Vector<Node> getNodes() {		
		return nodes;
	}
	
	 public CreateGraphGUI getParent() {
		 return parent;
	 }

	 public void loadConfig(Element root) {
		 pNCP.loadConfig((Element) root.getElementsByTagName("scenarios").item(0));
		 cvPanel.loadConfig((Element) root.getChildNodes().item(1));
		 userDefinedFunctions.loadConfig((Element) root.getChildNodes().item(2));
		 		 
	 }

	public List<Element> getConfigurationNodes(Document document) {
		Vector<Element> v = new Vector<Element>();
		v.add(pNCP.getConfigNode(document));
		v.add(cvPanel.getConfigNode(document));
		v.add(userDefinedFunctions.getConfigNode(document));
		return v;
	}
	 
}