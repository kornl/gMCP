package org.mutoss.tests;

import org.af.statguitoolkit.graph.GraphSRMTP;
import org.mutoss.gui.RControl;
import org.mutoss.gui.graph.VS;

public class TestImportExport {

	public TestImportExport(String graph1, String graph2) {
		RControl.getRControl(true);
		GraphSRMTP jGraph = new GraphSRMTP(graph1, new VS());
		jGraph.getNL().saveGraph(graph2, true);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
