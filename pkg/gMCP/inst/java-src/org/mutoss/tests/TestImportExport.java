package org.mutoss.tests;

import org.af.jhlir.backends.rengine.RCallServicesREngine;
import org.mutoss.gui.RControl;
import org.mutoss.gui.graph.VS;

public class TestImportExport {

	public TestImportExport(String graph1, String graph2) {
		RControl.getRControl(true);		
		VS vs = new VS();
		double[] weight = RControl.getR().eval("gMCP:::parseEpsPolynom(\"1-e\")").asRNumeric().getData();		
		for (int i=0; i<weight.length; i++) {
			System.out.println(weight[i]);
		}
		/*vs.nl = new NetList(new JLabel(""), vs, new GraphView(graph1, null));
		GraphMCP jGraph = new GraphMCP(graph1, vs);
		vs.nl.saveGraph(graph1, false);*/
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RCallServicesREngine r = RControl.getRControl(true).getR();
		r.eval("library(gMCP)");
		r.eval("graph <- createGraphFromBretzEtAl()");
		r.eval("graph <- createGraphForImprovedParallelGatekeeping()");
		new TestImportExport("graph","graphExport");
	}

}
