package org.mutoss.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.af.statguitoolkit.graph.GraphSRMTP;
import org.mutoss.gui.graph.NetzListe;
import org.mutoss.gui.graph.VS;

public class CreateGraphGUI extends JFrame implements WindowListener {
	
	GraphSRMTP graph;
	
	public CreateGraphGUI(String graph) {
		super("Creating and modifying graphs");
		RControl.getR();
		setIconImage((new ImageIcon(getClass().getResource("/org/mutoss/gui/graph/images/rjavaicon64.png"))).getImage());
		// Fenster in der Mitte des Bildschirms platzieren mit inset = 50 Pixeln Rand.
		int inset = 50;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset,
				screenSize.width  - inset*2,
				screenSize.height - inset*2);
		addWindowListener(this);
		
		makeContent();
		this.graph = new GraphSRMTP(graph, vs);
		
		setVisible(true);
	}
	
	JLabel statusbar = new JLabel(); 
	VS vs = new VS();
	NetzListe nl;
	
	private void makeContent() {
		nl = new NetzListe(statusbar, vs);		
		this.getContentPane().add(nl);
		
		
		
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
