package org.mutoss.tests;

import javax.swing.JLabel;

import org.af.jhlir.backends.rengine.RCallServicesREngine;
import org.mutoss.gui.RControl;
import org.mutoss.gui.graph.GraphMCP;
import org.mutoss.gui.graph.GraphView;
import org.mutoss.gui.graph.NetList;
import org.mutoss.gui.graph.VS;

public class TestImportExport {

	public TestImportExport(String graph1, String graph2) {
		RControl.getRControl(true);
		VS vs = new VS();
		vs.nl = new NetList(new JLabel(""), vs, new GraphView(graph1, null));
		GraphMCP jGraph = new GraphMCP(graph1, vs);
		//jGraph.getNL().saveGraph(graph2, false);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RCallServicesREngine r = RControl.getRControl(true).getR();
		r.eval("library(gMCP)");
		r.eval("graph <- createGraphFromBretzEtAl()");
		new TestImportExport("graph","graphExport");
	}

}
