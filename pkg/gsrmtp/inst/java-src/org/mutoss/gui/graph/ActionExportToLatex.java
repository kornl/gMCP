package org.mutoss.gui.graph;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.gui.RControl;


public class ActionExportToLatex implements java.awt.event.ActionListener {
	VS vs;
	NetzListe nl;
	Component parent;
	
	private static final Log logger = LogFactory.getLog(ActionExportToLatex.class);

	public ActionExportToLatex(Component parent, NetzListe nl, VS vs) {
		this.vs = vs;
		this.nl = nl;
		this.parent = parent;
	}

	public void actionPerformed(java.awt.event.ActionEvent event) {
		JFileChooser fc = new JFileChooser();
		File file;
		int returnVal = fc.showSaveDialog(parent);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			logger.debug("Export to: " + file.getAbsolutePath() + ".");
		} else {
			return;
		}
		String filename = file.getAbsolutePath();
		nl.saveGraph(".exportGraphToLaTeX", false);
		RControl.getR().eval("createGsrmtpReport(.exportGraphToLaTeX, file=\""+filename+"\")");
	}
}
