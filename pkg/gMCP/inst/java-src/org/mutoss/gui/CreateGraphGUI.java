package org.mutoss.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;

import org.af.commons.Localizer;
import org.mutoss.config.Configuration;
import org.mutoss.gui.graph.ControlMGraph;
import org.mutoss.gui.graph.GraphMCP;
import org.mutoss.gui.graph.GraphView;
import org.mutoss.gui.graph.PView;


public class CreateGraphGUI extends JFrame implements WindowListener {
	
	GraphMCP graph;
	
	public CreateGraphGUI(String graph, boolean debug, double grid) {
		super("Creating and modifying graphs");	
		RControl.getRControl(debug);
		Localizer.getInstance().addResourceBundle("org.mutoss.gui.ResourceBundle");
		Configuration.getInstance().getGeneralConfig().setGridSize((int)grid);
		setIconImage((new ImageIcon(getClass().getResource("/org/mutoss/gui/graph/images/rjavaicon64.png"))).getImage());
		agc = new ControlMGraph(graph, this);
		// Fenster in der Mitte des Bildschirms platzieren mit inset = 50 Pixeln Rand.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset,
				screenSize.width  - inset*2,
				screenSize.height - inset*2);
		addWindowListener(this);
		
		setJMenuBar(new MenuBarMGraph(agc));
		makeContent();
		this.graph = new GraphMCP(graph, graphview.getVS());
		
		setVisible(true);
	}
	
	public static void startGUI(String graph, boolean debug, double grid) {
		new CreateGraphGUI(graph, debug, grid);
	}
	
	JLabel statusbar = new JLabel(); 
	GraphView graphview;
	ControlMGraph agc;
	PView pview;
	
	private void makeContent() {
		graphview = new GraphView(agc);
		pview = new PView(agc);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, graphview, pview);
		this.getContentPane().add(splitPane);		
	}

	public static void main(String[] args) {
		new CreateGraphGUI("graph", true, 10);
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
		return graphview;
	}
}
