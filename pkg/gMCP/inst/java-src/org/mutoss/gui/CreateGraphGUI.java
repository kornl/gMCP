package org.mutoss.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.af.commons.Localizer;
import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.InfiniteProgressPanel;
import org.af.commons.widgets.InfiniteProgressPanel.AbortListener;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.config.Configuration;
import org.mutoss.gui.datatable.CellEditorE;
import org.mutoss.gui.datatable.DataFramePanel;
import org.mutoss.gui.datatable.DataTable;
import org.mutoss.gui.datatable.RDataFrameRef;
import org.mutoss.gui.graph.EdgeWeight;
import org.mutoss.gui.graph.GraphMCP;
import org.mutoss.gui.graph.GraphView;
import org.mutoss.gui.graph.PView;
import org.rosuda.REngine.JRI.JRIEngine;

public class CreateGraphGUI extends JFrame implements WindowListener, AbortListener {
	
	GraphMCP graph;
	GraphView agc;
	PView pview;
	DataFramePanel dfp;
	public InfiniteProgressPanel glassPane;
	protected static Log logger = LogFactory.getLog(CreateGraphGUI.class);
	
	public CreateGraphGUI(String graph, double[] pvalues, boolean debug, double grid) {
		super("gMCP GUI");
		Locale.setDefault(Locale.US);
		JComponent.setDefaultLocale(Locale.US); 
		RControl.getRControl(debug);
		Localizer.getInstance().addResourceBundle("org.mutoss.gui.ResourceBundle");
		if (grid>0) {
			Configuration.getInstance().getGeneralConfig().setGridSize((int)grid);
		}
		try {		
			Configuration.getInstance().getGeneralConfig().setVersionNumber(RControl.getR().eval("gMCP:::gMCPVersion()").asRChar().getData()[0]);
		} catch (Exception e) {
			// This is no vital information and will fail for e.g. R 2.8.0, so no error handling here...
			logger.warn("Package version could not be set:\n"+e.getMessage());
		}
		setIconImage((new ImageIcon(getClass().getResource("/org/mutoss/gui/graph/images/rjavaicon64.png"))).getImage());
		
		// Fenster in der Mitte des Bildschirms platzieren mit inset = 50 Pixeln Rand.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset,
				screenSize.width  - inset*2,
				screenSize.height - inset*2);
		addWindowListener(this);

		pview = new PView(this);
		dfp = new DataFramePanel(new RDataFrameRef());
		agc = new GraphView(graph, this);
		setJMenuBar(new MenuBarMGraph(agc));
		makeContent();
		this.graph = new GraphMCP(graph, agc.getNL());
		
		if (pvalues.length>0) getPView().setPValues(ArrayUtils.toObject(pvalues));
		glassPane = new InfiniteProgressPanel(this, "Calculating");
	    setGlassPane(glassPane);
	    glassPane.addAbortListener(this);

		setVisible(true);
	}
	
	/**
	 * The following three variables are only need at start time and ignored after that!
	 */
	static String graphStr;
	static boolean debug;
	static double grid;
	
	public static void startGUI(String graphStr, boolean debug, double grid) {
		CreateGraphGUI.graphStr = graphStr;
		CreateGraphGUI.debug = debug;
		CreateGraphGUI.grid = grid;
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CreateGraphGUI(CreateGraphGUI.graphStr, new double[] {}, CreateGraphGUI.debug, CreateGraphGUI.grid);
			}
		});		
	}
	
	private void makeContent() {
		dfp.getTable().setDefaultEditor(EdgeWeight.class, new CellEditorE(agc));
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(dfp), new JScrollPane(pview));
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, agc, splitPane2);
		getContentPane().add(splitPane);		
	}

	public static void main(String[] args) {
		new CreateGraphGUI("graph", new double[] {}, true,  10);
	}

	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowClosing(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	public PView getPView() {		
		return pview;
	}

	public GraphView getGraphView() {		
		return agc;
	}

	public DataTable getDataTable() {		
		return dfp.getTable();
	}

	@Override
	public void abort() {
		if (RControl.getR().getREngine().getClass() == JRIEngine.class) {
			JRIEngine engine = (JRIEngine) RControl.getR().getREngine();
			engine.getRni().rniStop(0);
			// We try to evaluate something:
			try {
				RControl.getR().eval("rmvnorm(n=5, mean=c(1,2), sigma=matrix(c(4,2,2,3), ncol=2))");
			} catch (Exception e) {
				/* There is a chance this first evaluation goes
				 * wrong due to protect / unprotect issues caused
				 * by the interrupt. 
				 */
				logger.warn("There was an error in the test eval after interrupt:\n"+e.getMessage(), e);
			}
			// Now the second evaluation should be fine:
			try {
				RControl.getR().eval("rmvnorm(n=5, mean=c(1,2), sigma=matrix(c(4,2,2,3), ncol=2))");
			} catch (Exception e) {				
				logger.error("There was an error in the 2. test eval after interrupt:\n"+e.getMessage(), e);
				String message = "There was an error interrupting the R calculation.\n"
					+"After you press okay an error dialog will open and please inform us about this.\n"
					+"After that we recommend that you close the GUI (but you can try whether saving of graphs or other things work).";
				JOptionPane.showMessageDialog(this, message, "Error interrupting R calculation", JOptionPane.ERROR_MESSAGE);
				ErrorHandler.getInstance().makeErrDialog("");
			}
		} else {
			logger.error("Could not stop REngine of class '"+RControl.getR().getREngine().getClass()+"'");
		}
	}
}
