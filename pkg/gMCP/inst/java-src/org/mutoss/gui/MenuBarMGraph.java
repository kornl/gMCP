package org.mutoss.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.af.commons.Localizer;
import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.logging.LoggingSystem;
import org.af.commons.logging.widgets.DetailsDialog;
import org.mutoss.gui.graph.ControlMGraph;
import org.mutoss.gui.graph.NetzListe;

public class MenuBarMGraph extends JMenuBar implements ActionListener {
	
	ControlMGraph control;
    protected Localizer localizer = Localizer.getInstance();
    //protected HelpSystem helpSystem;

	public MenuBarMGraph(ControlMGraph control) {
		
		this.control = control;

		JMenu menu = new JMenu("File");

		menu.add(makeMenuItem("New Graph", "new graph"));
		//menu.add(makeMenuItem("Load Graph", "load graph"));
		//menu.add(makeMenuItem("Save Graph", "save graph"));		
		menu.add(makeMenuItem("Save Graph as Image", "save graph image"));
		menu.add(makeMenuItem("Save Graph as LaTeX File", "save graph latex"));
		menu.addSeparator();
		/*menu.add(makeMenuItem("Save LaTeX Report", "save latex report"));
		menu.add(makeMenuItem("Save PDF Report", "save pdf"));
		menu.addSeparator();*/
		menu.add(makeMenuItem("Quit", "exit"));

		add(menu);

		menu = new JMenu("Example graphs");

		menu.add(makeMenuItem("Bonferroni-Holm Test (2 Null Hypotheses)", "bht2"));
		menu.add(makeMenuItem("Bonferroni-Holm Test (3 Null Hypotheses)", "bht3"));
		menu.add(makeMenuItem("Bonferroni-Holm Test (4 Null Hypotheses)", "bht4"));
		menu.add(makeMenuItem("Bonferroni-Holm Test (5 Null Hypotheses)", "bht5"));
		menu.addSeparator();
		menu.add(makeMenuItem("Parallel Gatekeeping with 4 Hypotheses", "pg"));
		menu.add(makeMenuItem("Improved Parallel Gatekeeping with 4 Hypotheses", "pgi"));
		menu.add(makeMenuItem("Example graph from Bretz et al.", "bretzEtAl"));

		add(menu);

        addExtrasMenu();
        //addHelpMenu(true);
	}
	
	private void loadGraph(String string) {
		NetzListe nl = control.getNL();		
		newGraph();
		RControl.getR().eval(NetzListe.initialGraph + " <- " + string);
		nl.loadGraph();
		control.getMainFrame().validate();
	}

	public void newGraph() {
		control.getGraphView().stopTesting();
		control.getNL().reset();		
	}
	
    public void showAbout() {
        new AboutDialog(control.getMainFrame());
    }

    public void showLog() {
        new DetailsDialog(LoggingSystem.getInstance().makeDetailsPanel());
    }
    
    public void reportError() {    	
        ErrorHandler.getInstance().makeErrDialog(localizer.getString("SGTK_MENU_EXTRAS_REPORT_ERROR"));
    }

	public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("new graph")) {
        	newGraph();			
        } else if (e.getActionCommand().equals("save graph")) {       	
        	//saveGraph();
        } else if (e.getActionCommand().equals("save pdf")) {       	
        	//savePDF();
        } else if (e.getActionCommand().equals("save graph image")) {       	
        	saveGraphImage();
        } else if (e.getActionCommand().equals("save graph latex")) {       	
        	exportLaTeXGraph();
        } else if (e.getActionCommand().equals("save latex report")) {       	
        	//exportLaTeXReport();
        } else if (e.getActionCommand().equals("load graph")) {       	
        	//loadGraph();
        } else if (e.getActionCommand().equals("bht2")) {       	
        	loadGraph("createBonferroniHolmGraph("+2+")");
        } else if (e.getActionCommand().equals("bht3")) {       	
        	loadGraph("createBonferroniHolmGraph("+3+")");
        } else if (e.getActionCommand().equals("bht4")) {       	
        	loadGraph("createBonferroniHolmGraph("+4+")");
        } else if (e.getActionCommand().equals("bht5")) {       	
        	loadGraph("createBonferroniHolmGraph("+5+")");
        } else if (e.getActionCommand().equals("pg")) {       	
        	loadGraph("createGraphForParallelGatekeeping()");
        } else if (e.getActionCommand().equals("pgi")) {       	
        	loadGraph("createGraphForImprovedParallelGatekeeping()");
        } else if (e.getActionCommand().equals("bretzEtAl")) {       	
        	loadGraph("createGraphFromBretzEtAl()");
        } else if (e.getActionCommand().equals("showLog")) {       	
        	showLog();
        } else if (e.getActionCommand().equals("reportError")) {       	
        	 reportError();
        } else if (e.getActionCommand().equals("exit")) {       	
        	 control.getMainFrame().dispose();
        }         
	}

	/*
	public void exportLaTeXReport() {
		NetzListe nl = ((ControlMGraph) control).getNL();
		String doc = "\\section*{Initial graph}\n";
		for (GraphStep gs : nl.getReport()) {			
			if (!gs.getName().trim().isEmpty()) {
				doc += "\n"+gs.getLatex()+"\n\\section{Rejection of "+gs.getName().replace("_", "\\_")+"}\n";
			} else {
				doc += "\n"+gs.getLatex()+"\n";
			}
		}
		List<CI> ciV = ((ControlMGraph) control).getNL().getCi();
		if (ciV != null) {
			doc += "\n\\section{Confidence intervals}";
			for (CI ci : ciV) {				
				doc += "Confidence interval "+ci.getName().replace("_", "\\_")+":  $]"+((ci.getLb()==Double.NEGATIVE_INFINITY)?"-\\infty":format.format(ci.getLb()))+", "+((ci.getUb()==Double.POSITIVE_INFINITY)?"\\infty":format.format(ci.getUb()))+"[$\\\\\n";					
			}
		}		
		writeLaTeX(doc);
	}
	*/
	
	DecimalFormat format = new DecimalFormat("#.###");
	
	
	public void writeLaTeX(String s) {
		JFileChooser fc = new JFileChooser();		
		File f;
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			f = fc.getSelectedFile();
			if (!f.getName().toLowerCase().endsWith(".tex")) {
            	f = new File(f.getAbsolutePath()+".tex");
            }
			System.out.println("Export to: " + f.getAbsolutePath() + ".");
		} else {
			return;
		}
		try {
			FileWriter out = new FileWriter(f);
			out.write(LATEX_BEGIN_DOCUMENT);
			out.write(s);
			out.write(LATEX_END_DOCUMENT);
			out.close();
		} catch( Exception ex ) {
			JOptionPane.showMessageDialog(null, "Saving LaTeX code to '" + f.getAbsolutePath() + "' failed: " + ex.getMessage(), "Saving failed.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void exportLaTeXGraph() {
		NetzListe nl = ((ControlMGraph) control).getNL();
		writeLaTeX(nl.getLaTeX());
	}
	
	public String LATEX_BEGIN_DOCUMENT = "\\documentclass[11pt]{article}\n"+
										 "\\usepackage{tikz}\n"+
										 "\\usetikzlibrary{snakes,arrows,shapes}\n"+
										 "\\begin{document}\n";
	
	public String LATEX_END_DOCUMENT = "\\end{document}";

	/*
	private void savePDF() {		
		JFileChooser fc = new JFileChooser(Configuration.getInstance().getGeneralConfig().getProjectPDFsPath().getAbsolutePath());		
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".pdf")) {
            	f = new File(f.getAbsolutePath()+".pdf");
            }
            try {
    			PDFReport pr = new PDFReport(((ControlMGraph) control));
    			pr.makePDF(f);
    		} catch( Exception ex ) {
    			JOptionPane.showMessageDialog(this, "Saving pdf report to '" + f.getAbsolutePath() + "' failed: " + ex.getMessage(), "Saving failed.", JOptionPane.ERROR_MESSAGE);
    			ex.printStackTrace();
    		}
        }
	}
	*/

	
	private void saveGraphImage() {
		BufferedImage img = ((ControlMGraph) control).getNL().getImage();
		JFileChooser fc = new JFileChooser();		
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".png")) {
            	f = new File(f.getAbsolutePath()+".png");
            }
            try {
    			ImageIO.write( img, "png", f );
    		} catch( Exception ex ) {
    			JOptionPane.showMessageDialog(this, "Saving image to '" + f.getAbsolutePath() + "' failed: " + ex.getMessage(), "Saving failed.", JOptionPane.ERROR_MESSAGE);
    		}
        }	
		
	}

	/*
	private void loadGraph() {
		((ControlMGraph) control).stopTesting();
		JFileChooser fc = new JFileChooser();		
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	((ControlMGraph) control).getNL().reset();
            File f = fc.getSelectedFile();
            try {
            	((ControlMGraph) control).getNL().loadFromXML(f);
    		} catch( Exception ex ) {
    			JOptionPane.showMessageDialog(this, "Loading graph from '" + f.getAbsolutePath() + "' failed: " + ex.getMessage(), "Saving failed.", JOptionPane.ERROR_MESSAGE);
    		}
        }
        control.getMainFrame().validate();
	}*/

	/*
	private void saveGraph() {
		JFileChooser fc = new JFileChooser();		
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (!f.getName().toLowerCase().endsWith(".xml")) {
            	f = new File(f.getAbsolutePath()+".xml");
            }
            try {
            	((ControlMGraph) control).getNL().saveToXML(f);
    		} catch( Exception ex ) {
    			JOptionPane.showMessageDialog(this, "Saving graph to '" + f.getAbsolutePath() + "' failed: " + ex.getMessage(), "Saving failed.", JOptionPane.ERROR_MESSAGE);
    		}
        }	
	}
	*/

	protected JMenuItem makeMenuItem(String text, String action) {
        return makeMenuItem(text, action, true);
    }

    protected JMenuItem makeMenuItem(String text, String action, boolean enabled) {
        JMenuItem item = new JMenuItem(text);        
        item.setActionCommand(action);
        item.setEnabled(enabled);
        item.addActionListener(this);
        return (item);
    }
    
    public void addExtrasMenu() {
        add(makeExtrasMenu());
    }

    public JMenu makeExtrasMenu() {
    	JMenu menu = new JMenu(localizer.getString("SGTK_MENU_EXTRAS"));
    	menu.add(makeMenuItem(localizer.getString("SGTK_MENU_EXTRAS_LOG"), "showLog"));
    	//menu.add(makeMenuItem(localizer.getString("SGTK_MENU_EXTRAS_OPTIONS"), "showOptions"));
    	menu.add(makeMenuItem(localizer.getString("SGTK_MENU_EXTRAS_REPORT_ERROR"), "reportError"));
    	return menu;
    }

    /*
    public void addHelpMenu(boolean helpAvailable) {
    	add(makeHelpMenu(helpAvailable));
    }
    
    private JMenu makeHelpMenu(boolean helpAvailable) {
    	 this.helpSystem = getControl().getHelpSystem();
    	 JMenu menu = new JMenu(localizer.getString("SGTK_MENU_HELP"));
         menu.add(makeMenuItem(localizer.getString("SGTK_MENU_HELP_ABOUT"), "showAbout"));         
         if (helpAvailable) menu.add(makeMenuItem(localizer.getString("SGTK_MENU_HELP_JAVA_HELP"), "showAppHelp"));
         return menu;
	}
	*/

}
