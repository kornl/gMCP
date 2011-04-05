package org.mutoss.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.af.commons.Localizer;
import org.apache.commons.lang.ArrayUtils;
import org.mutoss.config.Configuration;
import org.mutoss.gui.datatable.CellEditorE;
import org.mutoss.gui.datatable.DataFramePanel;
import org.mutoss.gui.datatable.DataTable;
import org.mutoss.gui.datatable.RDataFrameRef;
import org.mutoss.gui.graph.EdgeWeight;
import org.mutoss.gui.graph.GraphMCP;
import org.mutoss.gui.graph.GraphView;
import org.mutoss.gui.graph.PView;

public class CreateGraphGUI extends JFrame implements WindowListener {
	
	GraphMCP graph;
	GraphView agc;
	PView pview;
	DataFramePanel dfp;
	
	public CreateGraphGUI(String graph, double[] pvalues, boolean debug, double grid) {
		super("Creating and modifying graphs");	
		Locale.setDefault(Locale.US);
		JComponent.setDefaultLocale(Locale.US); 
		RControl.getRControl(debug);
		Localizer.getInstance().addResourceBundle("org.mutoss.gui.ResourceBundle");
		if (grid>0) {
			Configuration.getInstance().getGeneralConfig().setGridSize((int)grid);
		}
		setIconImage((new ImageIcon(getClass().getResource("/org/mutoss/gui/graph/images/rjavaicon64.png"))).getImage());
		
		// Fenster in der Mitte des Bildschirms platzieren mit inset = 50 Pixeln Rand.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset,
				screenSize.width  - inset*2,
				screenSize.height - inset*2);
		addWindowListener(this);

		pview = new PView();
		dfp = new DataFramePanel(new RDataFrameRef());
		agc = new GraphView(graph, this);
		setJMenuBar(new MenuBarMGraph(agc));
		makeContent();
		this.graph = new GraphMCP(graph, agc.getVS());
		
		if (pvalues.length>0) getPView().setPValues(ArrayUtils.toObject(pvalues));
		
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
		JSplitPane splitPane2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(dfp), pview);
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
}
