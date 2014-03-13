package org.af.gMCP.gui.power;

import java.util.List;
import java.util.Vector;

import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.graph.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface PDialog {

    public CreateGraphGUI getParent();
    public List<Node> getNodes();
	public List<Element> getConfigurationNodes(Document document);
	public void loadConfig(Element root);

}
