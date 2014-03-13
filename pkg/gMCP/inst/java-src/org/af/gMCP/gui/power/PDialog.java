package org.af.gMCP.gui.power;

import java.util.Vector;

import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.graph.Node;

public interface PDialog {

    public CreateGraphGUI getParent();
    public Vector<Node> getNodes();

}
