package org.af.gMCP.gui;

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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.InfiniteProgressPanel;
import org.af.commons.widgets.InfiniteProgressPanel.AbortListener;
import org.af.commons.widgets.WidgetFactory;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.config.VersionComparator;
import org.af.gMCP.gui.datatable.DataFramePanel;
import org.af.gMCP.gui.datatable.RDataFrameRef;
import org.af.gMCP.gui.dialogs.TellAboutOnlineUpate;
import org.af.gMCP.gui.graph.DView;
import org.af.gMCP.gui.graph.GraphView;
import org.af.gMCP.gui.graph.PView;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rosuda.REngine.JRI.JRIEngine;

public class CreateGraphGUI extends JFrame implements WindowListener, AbortListener {
	
	GraphView control;
	PView pview;
	DView dview;
	public DataFramePanel dfp;
	public InfiniteProgressPanel glassPane;
	protected static Log logger = LogFactory.getLog(CreateGraphGUI.class);
	public boolean isGraphSaved = true;
	public static CreateGraphGUI lastCreatedGUI;
	
	public CreateGraphGUI(String graph, double[] pvalues, boolean debug, double grid, boolean experimentalFeatures) {
		super("gMCP GUI");
		Locale.setDefault(Locale.US);
		JComponent.setDefaultLocale(Locale.US); 
		RControl.getRControl(debug);
		if (grid>0) {
			Configuration.getInstance().getGeneralConfig().setGridSize((int)grid);
		}
		Configuration.getInstance().getGeneralConfig().setExperimental(experimentalFeatures);
		
		/* Get and save R and gMCP version numbers */
		try {		
			Configuration.getInstance().getGeneralConfig().setRVersionNumber(RControl.getR().eval("paste(R.version$major,R.version$minor,sep=\".\")").asRChar().getData()[0]);
			Configuration.getInstance().getGeneralConfig().setVersionNumber(RControl.getR().eval("gMCP:::gMCPVersion()").asRChar().getData()[0]);
			this.setTitle("gMCP GUI "+Configuration.getInstance().getGeneralConfig().getVersionNumber());
		} catch (Exception e) {
			// This is no vital information and will fail for e.g. R 2.8.0, so no error handling here...
			logger.warn("Package version could not be set:\n"+e.getMessage());
		}
		
		/* Count the number of starts */
		int n = Configuration.getInstance().getGeneralConfig().getNumberOfStarts();
		Configuration.getInstance().getGeneralConfig().setNumberOfStarts(n+1);		
		logger.info("gMCP start No. "+n+1);
		
		setIconImage((new ImageIcon(getClass().getResource("/org/af/gMCP/gui/graph/images/rjavaicon64.png"))).getImage());
		try {
			setLooknFeel();
		} catch (Exception e1) {
			JOptionPane.showMessageDialog(this, "Font size and Look'n'Feel could not be restored.", "Error restoring Look'n'Feel", JOptionPane.ERROR_MESSAGE);
		}
				
		/* 
		 * We want to check for unsaved changes and eventually quit the R console as well, 
		 * so we implement the WindowListener interface and let windowClosing() do the work.
		 */
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
		
		dview = new DView(this);
		dfp = new DataFramePanel(new RDataFrameRef());
		control = new GraphView(graph, this);  // NetList object is created here.
		dfp.registerControl(control);
		pview = new PView(this);
		setJMenuBar(new MenuBarMGraph(control));		
		makeContent();
		
		if (RControl.getR().eval("exists(\""+graph+"\")").asRLogical().getData()[0]) {
			control.getNL().loadGraph(graph);
		}
		
		if (pvalues.length>0) getPView().setPValues(ArrayUtils.toObject(pvalues));
		glassPane = new InfiniteProgressPanel(this, "Calculating");
	    setGlassPane(glassPane);
	    glassPane.addAbortListener(this);

	    // Place the frame in the middle of the screen with a border of inset = 50 pixel.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset,
				screenSize.width  - inset*2,
				screenSize.height - inset*2);

		setVisible(true);
		splitPane1.setDividerLocation(0.75);
		splitPane2.setDividerLocation(0.5);
		splitPane1.setResizeWeight(0.75);
		splitPane2.setResizeWeight(0.5);		
		
		// If this causes trouble look again at the following bug work around:
		// http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6409815
		// or perhaps try something like this:
		// http://www.jguru.com/faq/view.jsp?EID=27191

		//TODO Is there really no better way than this kind of strange workaround?!?
		new Thread(new Runnable() {
			public void run() {
				for (int i=0; i<6; i++) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						logger.warn("Interrupted: "+e.getMessage(), e);
					}				
					splitPane1.setDividerLocation(0.75);
					splitPane2.setDividerLocation(0.5);
				}
			}
		}).start();
		
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {				
				if (!Configuration.getInstance().getGeneralConfig().tellAboutCheckOnline()) {
					new TellAboutOnlineUpate(null);
					Configuration.getInstance().getGeneralConfig().setTellAboutCheckOnline(true);
				}
				new Thread(new Runnable() {
					public void run() {
						VersionComparator.getOnlineVersion();
					}
				}).start();				
			}
		});
		
		lastCreatedGUI = this;
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
				new CreateGraphGUI(CreateGraphGUI.graphStr, new double[] {}, CreateGraphGUI.debug, CreateGraphGUI.grid, true);
			}
		});		
	}
	
	JSplitPaneBugWorkAround splitPane;
	JSplitPaneBugWorkAround splitPane1;
	JSplitPaneBugWorkAround splitPane2;
	
	private void makeContent() {		
		splitPane1 = new JSplitPaneBugWorkAround(JSplitPane.VERTICAL_SPLIT, control, dview);		
		splitPane2 = new JSplitPaneBugWorkAround(JSplitPane.VERTICAL_SPLIT, new JScrollPane(dfp), new JScrollPane(pview));		
		splitPane = new JSplitPaneBugWorkAround(JSplitPane.HORIZONTAL_SPLIT, splitPane1, splitPane2);		
		getContentPane().add(splitPane);		
	}

	public static void main(String[] args) {
		new CreateGraphGUI("graph", new double[] {}, true,  10, true);
	}

	/**
	 * Closes the R console if we are in bundled mode and checks for unsaved changes. 
	 */
	public void windowClosing(WindowEvent e) {
		if (!isGraphSaved && Configuration.getInstance().getGeneralConfig().askWhenGraphIsNotSaved()) {
			int answer = JOptionPane.showConfirmDialog(this, "The current graph is not saved yet!\nDo you want to save it?", 
					"Do you want to save the graph?",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
			if (answer==JOptionPane.CANCEL_OPTION) return;
			if (answer==JOptionPane.YES_OPTION) {
				control.saveGraph();
			}
		}
		if (RControl.getR().eval("exists(\".isBundle\")").asRLogical().getData()[0]) {
			RControl.getR().eval("q(save=\"no\")");
		}
		dispose();
	}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	
	public PView getPView() {		
		return pview;
	}

	public GraphView getGraphView() {		
		return control;
	}

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

	public DView getDView() {		
		return dview;
	}
	
	public DataFramePanel getDataFramePanel() {
		return dfp;
	}
	
	private void setLooknFeel() throws ClassNotFoundException, IllegalAccessException,
	InstantiationException, UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel(Configuration.getInstance().getJavaConfig().getLooknFeel());
        WidgetFactory.setFontSizeGlobal(Configuration.getInstance().getGeneralConfig().getFontSize());
		SwingUtilities.updateComponentTreeUI(this);
	}
}
