package org.mutoss.tests;

import javax.swing.JLabel;

import org.af.jhlir.backends.rengine.RCallServicesREngine;
import org.mutoss.gui.RControl;
import org.mutoss.gui.graph.AbstractGraphControl;
import org.mutoss.gui.graph.GraphSRMTP;
import org.mutoss.gui.graph.NetzListe;
import org.mutoss.gui.graph.VS;

public class TestImportExport {

	public TestImportExport(String graph1, String graph2) {
		RControl.getRControl(true);
		VS vs = new VS();
		vs.nl = new NetzListe(new JLabel(""), vs, new AbstractGraphControl(graph1, null));
		GraphSRMTP jGraph = new GraphSRMTP(graph1, vs);
		jGraph.getNL().saveGraph(graph2, false);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		RCallServicesREngine r = RControl.getRControl(true).getR();
		r.eval("library(gsrmtp)");
		r.eval("graph <- createGraphFromBretzEtAl()");
		new TestImportExport("graph","graphExport");
	}

}
