package af.statguitoolkit.graph;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;


public class ActionExportToLatex implements java.awt.event.ActionListener {
	VS vs;
	NetzListe nl;
	Component parent;

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
			System.out.println("Export to: " + file.getAbsolutePath() + ".");
		} else {
			return;
		}
		String filename = file.getAbsolutePath();
		//	
		Latex.openFile(filename);
		Latex.writeFHeader2();
		Latex.printlnF("\\setlength{\\unitlength}{0.2mm}");
		Latex.printlnF("\\begin{picture}(500,500)");
		Latex.printlnF("\\psset{unit=0.2mm,linewidth=1pt}");
		for (int i = 0; i < nl.knoten.size(); i++) {
			Latex.printF("\\rput(" + nl.knoten.get(i).x + "," + nl.knoten.get(i).y
					+ "){");
			Latex.printlnF("\\cnode(0,0){"
					+ Node.r
					+ "}{Node"
					+ i
					+ "}{"
					+ ((vs.shownr) ? ("" + nl.knoten.get(i).nr)
							: (nl.knoten.get(i).name)) + "}}");
		}
		for (int i = 0; i < nl.edges.size(); i++) {
			int node1 = nl.knoten.indexOf(nl.edges.get(i).von);
			int node2 = nl.knoten.indexOf(nl.edges.get(i).nach);
			if (vs.directed) {
				Latex.printlnF("\\ncline{->}{Node" + node1 + "}{Node" + node2
						+ "}");
			} else {
				Latex.printlnF("\\ncline{-}{Node" + node1 + "}{Node" + node2
						+ "}");
			}
		}
		Latex.printlnF("\\end{picture}");
		Latex.writeFEnd();
		Latex.closeFile();
	}
}
