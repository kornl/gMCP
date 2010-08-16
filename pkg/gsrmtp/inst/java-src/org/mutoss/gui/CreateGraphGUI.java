package org.mutoss.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.af.statguitoolkit.graph.GraphSRMTP;

public class CreateGraphGUI extends JFrame implements WindowListener {
	
	GraphSRMTP graph;
	
	public CreateGraphGUI(String graph) {
		super("Creating and modifying graphs");
		RControl.getR();
		this.graph = new GraphSRMTP(graph);
		setIconImage((new ImageIcon(getClass().getResource("/org/mutoss/gui/graph/images/rjavaicon64.png"))).getImage());
		// Fenster in der Mitte des Bildschirms platzieren mit inset = 50 Pixeln Rand.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset,
				screenSize.width  - inset*2,
				screenSize.height - inset*2);
		addWindowListener(this);
		
		setVisible(true);
	}
	
	public static void main(String[] args) {
		new CreateGraphGUI("graph");
	}

	@Override
	public void windowActivated(WindowEvent e) {}
	@Override
	public void windowClosed(WindowEvent e) {}
	@Override
	public void windowClosing(WindowEvent e) {}
	@Override
	public void windowDeactivated(WindowEvent e) {}
	@Override
	public void windowDeiconified(WindowEvent e) {}
	@Override
	public void windowIconified(WindowEvent e) {}
	@Override
	public void windowOpened(WindowEvent e) {}
}
