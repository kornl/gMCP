package org.mutoss.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.af.commons.Localizer;
import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.io.FileTransfer;
import org.af.commons.logging.LoggingSystem;
import org.af.commons.logging.widgets.DetailsDialog;
import org.af.commons.tools.OSTools;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;
import org.mutoss.config.Configuration;
import org.mutoss.gui.dialogs.NumberOfHypotheses;
import org.mutoss.gui.dialogs.RObjectLoadingDialog;
import org.mutoss.gui.dialogs.TextFileViewer;
import org.mutoss.gui.dialogs.VariableNameDialog;
import org.mutoss.gui.graph.GraphView;
import org.mutoss.gui.graph.NetList;
import org.mutoss.gui.options.OptionsDialog;

public class MenuBarMGraph extends JMenuBar implements ActionListener {
	
	GraphView control;
    protected Localizer localizer = Localizer.getInstance();
    private static final Log logger = LogFactory.getLog(MenuBarMGraph.class);
    JMenu fmenu = new JMenu("File");

	public MenuBarMGraph(GraphView control) {
		
		this.control = control;		

		fmenu.add(makeMenuItem("New Graph", "new graph", KeyEvent.VK_N));
		fmenu.add(makeMenuItem("Load Graph from R", "load graph from R", KeyEvent.VK_L));
		fmenu.add(makeMenuItem("Load Graph from RData file", "load graph"));
		fmenu.addSeparator();
		fmenu.add(makeMenuItem("Load p-Values from R", "load p-values from R"));
		fmenu.addSeparator();
		fmenu.add(makeMenuItem("Save Graph to R", "save graph to R", KeyEvent.VK_S));	
		fmenu.add(makeMenuItem("Save Graph to RData file", "save graph"));		
		fmenu.addSeparator();
		fmenu.add(makeMenuItem("Export Graph to PNG Image", "export graph image", KeyEvent.VK_P));
		fmenu.add(makeMenuItem("Export Graph to LaTeX File", "export graph latex", KeyEvent.VK_L));
		fmenu.addSeparator();
		fmenu.add(makeMenuItem("Save LaTeX Report", "save latex report", KeyEvent.VK_R));
		JMenuItem item = makeMenuItem("Save PDF Report", "save pdf");
		item.setEnabled(false);
		fmenu.add(item);
		//fmenu.add(makeMenuItem("Save PDF Report", "save pdf"));
		fmenu.addSeparator();
		createLastUsed();
		fmenu.setMnemonic(KeyEvent.VK_F);
		add(fmenu);

		JMenu menu = new JMenu("Example graphs");
		menu.setMnemonic(KeyEvent.VK_X);

		menu.add(makeMenuItem("Bonferroni-Holm Test", "bht"));
		menu.addSeparator();
		menu.add(makeMenuItem("Parallel Gatekeeping with 4 Hypotheses", "pg"));
		menu.add(makeMenuItem("Improved Parallel Gatekeeping with 4 Hypotheses", "pgi"));
		menu.addSeparator();
		menu.add(makeMenuItem("Example graph from Bretz et al. (2009)", "bretzEtAl"));
		menu.add(makeMenuItem("Example graph from Hommel et al. (2007)", "hommelEtAl"));

		add(menu);

		menu = new JMenu("Analysis");
		menu.setMnemonic(KeyEvent.VK_A);
		
		menu.add(makeMenuItem("Graph analysis", "graphAnalysis"));
		menu.addSeparator();
		menu.add(makeMenuItem("Power analysis", "powerAnalysis"));		

		add(menu);
		
        addExtrasMenu();
        addHelpMenu();
	}
	
	private void createLastUsed() {
		List<String> graphs = Configuration.getInstance().getGeneralConfig().getLatestGraphs();
		
		for(int i=fmenu.getItemCount()-1; i>14; i--) {
			fmenu.remove(i);
		}
		
		if (graphs.size()>0) {	
			int i = 0;
			for (String graph : graphs) {
				i++;
				String s = graph;
				logger.info("Process last used graph: '"+s+"'.");			
				File f = new File(s);				
				if (f.exists()) {
					String path = f.getParent();
					if (path.length()>20) {
						path = path.substring(0, 17)+"...";
					}
					s = f.getName()+" ["+path+"]";
					fmenu.add(makeMenuItem(i+" "+s, "LOAD_GRAPH"+graph, (i+"").charAt(0)));
				} else {					
					if (s.startsWith("R Object: ")) {
						s = s.substring(10);
						if (RControl.getR().eval("exists(\""+s+"\")").asRLogical().getData()[0]) {
							fmenu.add(makeMenuItem(i+" "+s, "LOAD_GRAPH"+graphs, (i+"").charAt(0)));
						}
					}
				}				
			}
			fmenu.addSeparator();
		}		
		fmenu.add(makeMenuItem("Exit", "exit", KeyEvent.VK_X));
	}

	private JMenuItem makeMenuItem(String text, String action, int key) {
		JMenuItem item = makeMenuItem(text, action);
		item.setMnemonic(key);
		return item;
	}
	
	private JMenuItem makeMenuItem(String text, String action, char key) {
		JMenuItem item = makeMenuItem(text, action);
		item.setMnemonic(key);
		return item;
	}


	public void loadGraph(String string) {
		NetList nl = control.getNL();		
		newGraph();
		boolean matrix = RControl.getR().eval("is.matrix("+string+")").asRLogical().getData()[0];
		RControl.getR().eval(nl.initialGraph + " <- gMCP:::arrangeNodes("+ (matrix?"matrix2graph(":"(")+ string + "))");
		nl.loadGraph();
		control.getMainFrame().validate();
	}

	public void newGraph() {
		control.stopTesting();
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
        if (e.getActionCommand().startsWith("LOAD_GRAPH")) {        	
        	String s = e.getActionCommand().substring(10);
        	logger.info("Trying to load \""+s+"\"");
        	if (s.startsWith("R Object")) {
        		s = s.substring(10);
        		loadGraph(s);
        		Configuration.getInstance().getGeneralConfig().addGraph("R Object: "+s);
            	createLastUsed();
        	} else {
        		File f = new File(s);
        		if (!f.exists()) {
        			JOptionPane.showMessageDialog(control.getMainFrame(), "Could not find file:\n"+s, "Could not find file", JOptionPane.ERROR_MESSAGE);
        			return;
        		}
        		if (s.toLowerCase().endsWith(".rdata")) {
        			loadGraph(f);
        		}
        	}
        } else if (e.getActionCommand().equals("new graph")) {
        	newGraph();			
        } else if (e.getActionCommand().equals("save graph")) {       	
        	saveGraph();
        } else if (e.getActionCommand().equals("save graph to R")) {
        	if (control.getNL().getKnoten().size()==0) {
        		JOptionPane.showMessageDialog(control.getMainFrame(), "Will not save empty graph.", "Saving to R failed.", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        	VariableNameDialog vnd = new VariableNameDialog(control.getGraphGUI(), control.getGraphName());
        	String name = control.getNL().saveGraph(vnd.getName(), true);
        	Configuration.getInstance().getGeneralConfig().addGraph("R Object: "+name);
        	createLastUsed();
        } else if (e.getActionCommand().equals("export graph image")) {       	
        	saveGraphImage();
        } else if (e.getActionCommand().equals("export graph latex")) {       	
        	exportLaTeXGraph();
        } else if (e.getActionCommand().equals("save pdf")) {  
        	notYetSupported();
        	//savePDF();
        } else if (e.getActionCommand().equals("save latex report")) {
        	exportLaTeXReport();
        } else if (e.getActionCommand().equals("load graph")) {       	
        	loadGraph();
        } else if (e.getActionCommand().equals("load graph from R")) {
        	new RObjectLoadingDialog(control.getGraphGUI());
        	//VariableNameDialog vnd = new VariableNameDialog(control.getGraphGUI());
        	createLastUsed();        	
        } else if (e.getActionCommand().equals("bht")) {
        	new NumberOfHypotheses(control.getGraphGUI(), this, "createBonferroniHolmGraph");        	
        } else if (e.getActionCommand().equals("pg")) {       	
        	loadGraph("createGraphForParallelGatekeeping()");
        } else if (e.getActionCommand().equals("pgi")) {       	
        	loadGraph("createGraphForImprovedParallelGatekeeping()");
        } else if (e.getActionCommand().equals("bretzEtAl")) {       	
        	loadGraph("createGraphFromBretzEtAl()");
        } else if (e.getActionCommand().equals("hommelEtAl")) {       	
        	loadGraph("createGraphFromHommelEtAl()");
        } else if (e.getActionCommand().equals("showLog")) {       	
        	showLog();
        } else if (e.getActionCommand().equals("reportError")) {       	
        	 reportError();
        } else if (e.getActionCommand().equals("exit")) {       	
        	 control.getMainFrame().dispose();
        } else if (e.getActionCommand().equals("showAppHelp")) {
        	showFile("doc/gMCP.pdf");       	 	
        } else if (e.getActionCommand().equals("showParametric")) {
        	showFile("doc/correlated.pdf");       	 	
        } else if (e.getActionCommand().equals("showManual")) {
        	try {	
				Method main = Class.forName("java.awt.Desktop").getDeclaredMethod("getDesktop");
				Object obj = main.invoke(new Object[0]);
				Method second = obj.getClass().getDeclaredMethod("browse", new Class[] { URI.class }); 
				second.invoke(obj, new URI("http://cran.at.r-project.org/web/packages/gMCP/gMCP.pdf"));
			} catch (Exception exc) {			
				logger.warn("No Desktop class in Java 5 or URI error.");
				RControl.getR().eval("browseURL(\"http://cran.at.r-project.org/web/packages/gMCP/gMCP.pdf\")");
			}
        } else if (e.getActionCommand().equals("showEpsDoc")) {
        	showFile("doc/EpsilonEdges.pdf");       	 	
        } else if (e.getActionCommand().equals("showNEWS")) {
        	new TextFileViewer(control.getMainFrame(), new File(RControl.getR().eval("system.file(\"NEWS\", package=\"gMCP\")").asRChar().getData()[0]));      	 	
        } else if (e.getActionCommand().equals("showAbout")) {
        	new AboutDialog(control.getMainFrame());
        } else if (e.getActionCommand().equals("showOptions")) {
        	new OptionsDialog(control.getMainFrame());
        } else if (e.getActionCommand().equals("debugConsole")) {
        	RControl.console.setVisible(true);
        } else if (e.getActionCommand().equals("graphAnalysis")) {
        	if (control.getNL().getKnoten().size()==0) {
        		JOptionPane.showMessageDialog(control.getMainFrame(), "Graph is empty!", "Graph is empty!", JOptionPane.ERROR_MESSAGE);
        		return;
        	}
        	control.getNL().saveGraph(".tmpGraph", false);
        	String text = RControl.getR().eval("graphAnalysis(.tmpGraph)").asRChar().getData()[0];
        	new TextFileViewer(control.getMainFrame(), "Graph analysis", text);
        } else if (e.getActionCommand().equals("powerAnalysis")) {
        	notYetSupported();
        } else if (e.getActionCommand().equals("load p-values from R")) {
        	VariableNameDialog vnd = new VariableNameDialog(control.getGraphGUI(), "");     
			try {
				double[] data = RControl.getR().eval(vnd.getName()).asRNumeric().getData();
				if (data.length!=control.getNL().getKnoten().size()) {
					JOptionPane.showMessageDialog(this, "Number of hypotheses and values do not match.", 
							"Number of hypotheses and values do not match", JOptionPane.ERROR_MESSAGE);
					return;
				}
				control.getPView().setPValues(ArrayUtils.toObject(data));					
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error loading values from R:\n"+ex.getMessage(), 
						"Error loading values from R", JOptionPane.ERROR_MESSAGE);
			} 
        }
	}
	
	private void notYetSupported() {
		JOptionPane.showMessageDialog(control.getMainFrame(), "Not yet supported.", "Not yet supported", JOptionPane.INFORMATION_MESSAGE);
	}

	public void showFile(String s) {
		File f = new File(RControl.getR().eval("system.file(\""+s+"\", package=\"gMCP\")").asRChar().getData()[0]);
		if (OSTools.isWindows() && s.indexOf('.') == -1) {
			try {
				f = FileTransfer.copyFile(f, new File(System.getProperty("java.io.tmpdir"), f.getName()+"TXT"));
			} catch (IOException e) {
				JOptionPane.showMessageDialog(control.getMainFrame(), "Please open and read the following file:\n"+f.getAbsolutePath(), "Could not find appropriate viewer", JOptionPane.WARNING_MESSAGE);
			}
		}		
		if (!f.exists()) {
			throw new RuntimeException("This is strange. The file \""+s+"\" could not be found.");
		} else {
			try {	
				Method main = Class.forName("java.awt.Desktop").getDeclaredMethod("getDesktop");
				Object obj = main.invoke(new Object[0]);
				Method second = obj.getClass().getDeclaredMethod("open", new Class[] { File.class }); 
				second.invoke(obj, f);
			} catch (Exception exc) {			
				logger.warn("No Desktop class in Java 5 or URI error.");
				try {
					if (OSTools.isWindows()) {
						Process p;							
						p = Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + f.getAbsolutePath()+"\"");
						/*if (s.indexOf('.') == -1) {
							p = Runtime.getRuntime().exec("wordpad \"" + f.getAbsolutePath()+"\"");
						}*/						
						p.waitFor();
					} else {
						JOptionPane.showMessageDialog(control.getMainFrame(), "Please open and read the following file:\n"+f.getAbsolutePath(), "Could not find appropriate viewer", JOptionPane.WARNING_MESSAGE);
					}
				} catch (Exception e1) {
					logger.error(e1.getMessage());
					e1.printStackTrace();
					JOptionPane.showMessageDialog(control.getMainFrame(), "Please open and read the following file:\n"+f.getAbsolutePath(), "Could not find appropriate viewer", JOptionPane.WARNING_MESSAGE);
				}

			}
		}
	}
	
	String correlation;
	File f;
	
	public void exportLaTeXReport() {
		if (control.getNL().getKnoten().size()==0) {
    		JOptionPane.showMessageDialog(control.getMainFrame(), "Can not create report for empty graph.", "Can not create report for empty graph.", JOptionPane.ERROR_MESSAGE);
    		return;
    	}
		if (!RControl.getR().eval("exists(\""+control.getNL().initialGraph+"\")").asRLogical().getData()[0]) {
			control.getNL().saveGraph();
		}
		JFileChooser fc = new JFileChooser(Configuration.getInstance().getClassProperty(this.getClass(), "LaTeXReportDirectory"));
		fc.setDialogType(JFileChooser.SAVE_DIALOG);		
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {			
			f = fc.getSelectedFile();
			Configuration.getInstance().setClassProperty(this.getClass(), "LaTeXReportDirectory", f.getParent());
			if (!f.getName().toLowerCase().endsWith(".tex")) {
            	f = new File(f.getAbsolutePath()+".tex");
            }
			logger.info("Export to: " + f.getAbsolutePath() + ".");
		} else {
			return;
		}
		control.getMainFrame().glassPane.start();
		//startTesting();
		correlation = control.getPView().getCorrelation();
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				if (!control.resultUpToDate) {
					RControl.getR().evalVoid(control.result+" <- gMCP("+control.getNL().initialGraph+","+control.getPView().getPValuesString()+ correlation+", alpha="+control.getPView().getTotalAlpha()+")");
					control.resultUpToDate = true;
				}
				RControl.getR().eval("gMCPReport("+control.result+", file=\""+f.getAbsolutePath()+"\")");
				control.getMainFrame().glassPane.stop();
				return null;
			}  
		};
		worker.execute();
	}
	
	public void writeLaTeX(String s) {
		JFileChooser fc = new JFileChooser(Configuration.getInstance().getClassProperty(this.getClass(), "LaTeXDirectory"));
		fc.setDialogType(JFileChooser.SAVE_DIALOG);
		File f;
		int returnVal = fc.showSaveDialog(this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {			
			f = fc.getSelectedFile();
			Configuration.getInstance().setClassProperty(this.getClass(), "LaTeXDirectory", f.getParent());
			if (!f.getName().toLowerCase().endsWith(".tex")) {
            	f = new File(f.getAbsolutePath()+".tex");
            }
			logger.info("Export to: " + f.getAbsolutePath() + ".");
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
		writeLaTeX(control.getNL().getLaTeX());
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
		BufferedImage img = control.getNL().getImage();
		JFileChooser fc = new JFileChooser(Configuration.getInstance().getClassProperty(this.getClass(), "ImageDirectory"));		
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            Configuration.getInstance().setClassProperty(this.getClass(), "ImageDirectory", f.getParent());
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
	
	private void loadGraph() {		
		JFileChooser fc = new JFileChooser(Configuration.getInstance().getClassProperty(this.getClass(), "RObjDirectory"));		
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				return f.getName().toLowerCase().endsWith(".rdata");
			}
			public String getDescription () { return "RData files"; }  
		});

        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
        	File f = fc.getSelectedFile();
        	loadGraph(f);
        }
        control.getMainFrame().validate();
	}
	
	private void loadGraph(File f) {
		control.stopTesting();            
        Configuration.getInstance().setClassProperty(this.getClass(), "RObjDirectory", f.getParent());
        try {            	
        	//((ControlMGraph) control).getNL().loadFromXML(f);
    		String loadedGraph = RControl.getR().eval("load(file=\""+f.getAbsolutePath()+"\")").asRChar().getData()[0];
    		loadGraph(loadedGraph);
    		Configuration.getInstance().getGeneralConfig().addGraph(f.getAbsolutePath());
        	createLastUsed();
		} catch( Exception ex ) {
			JOptionPane.showMessageDialog(this, "Loading graph from '" + f.getAbsolutePath() + "' failed: " + ex.getMessage(), "Saving failed.", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void saveGraph() {
		JFileChooser fc = new JFileChooser(Configuration.getInstance().getClassProperty(this.getClass(), "RObjDirectory"));		
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				return f.getName().toLowerCase().endsWith(".rdata");
			}
			public String getDescription () { return "RData files"; }  
		});
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            Configuration.getInstance().setClassProperty(this.getClass(), "RObjDirectory", f.getParent());
            if (!f.getName().toLowerCase().endsWith(".rdata")) {
            	f = new File(f.getAbsolutePath()+".RData");
            }
            try {
            	VariableNameDialog vnd = new VariableNameDialog(control.getGraphGUI(), control.getGraphName());            	
            	String name = vnd.getName();
            	control.getNL().saveGraph(name, false); 
            	RControl.getR().eval("save("+name+", file=\""+f.getAbsolutePath()+"\")");        		
            	JOptionPane.showMessageDialog(control.getMainFrame(), "Exported graph to R object '"+name+"' and saved this to \n'" + f.getAbsolutePath() + "'.", "Saved graph", JOptionPane.INFORMATION_MESSAGE);
            	Configuration.getInstance().getGeneralConfig().addGraph(f.getAbsolutePath());
            	createLastUsed();
    		} catch( Exception ex ) {
    			JOptionPane.showMessageDialog(control.getMainFrame(), "Saving graph to '" + f.getAbsolutePath() + "' failed: " + ex.getMessage(), "Saving failed", JOptionPane.ERROR_MESSAGE);
    		}
        }	
	}

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
    	menu.add(makeMenuItem(localizer.getString("SGTK_MENU_EXTRAS_OPTIONS"), "showOptions"));
    	menu.addSeparator();
    	menu.add(makeMenuItem(localizer.getString("SGTK_MENU_EXTRAS_LOG"), "showLog"));    	
    	menu.add(makeMenuItem(localizer.getString("SGTK_MENU_EXTRAS_REPORT_ERROR"), "reportError"));
    	if (System.getProperty("eclipse") != null) {		
    		menu.add(makeMenuItem("Debug console", "debugConsole"));
    	}
    	return menu;
    }
    
    public void addHelpMenu() {
    	add(makeHelpMenu());
    }
    
    private JMenu makeHelpMenu() {
    	 JMenu menu = new JMenu("Help");
    	 menu.setMnemonic(KeyEvent.VK_H);
         menu.add(makeMenuItem("About", "showAbout", KeyEvent.VK_B));         
         menu.add(makeMenuItem("Introduction to gMCP", "showAppHelp", KeyEvent.VK_I));
         menu.add(makeMenuItem("Weighted parametric tests defined by graphs", "showParametric", KeyEvent.VK_P));
         menu.add(makeMenuItem("gMCP R Online Reference manual", "showManual", KeyEvent.VK_M));
         //menu.add(makeMenuItem("Theoretical Background", "showAppHelp"));
         /*menu.addSeparator();
         menu.add(makeMenuItem("Description of Edges with Infinitesimal Small Epsilon Weights", "showEpsDoc"));*/
         menu.addSeparator();
         menu.add(makeMenuItem("Version Info / NEWS", "showNEWS", KeyEvent.VK_N));
         return menu;
	}	

}
