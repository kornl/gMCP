package org.mutoss.gui.graph;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.DesktopPaneBG;

public class GraphView extends JPanel implements ActionListener {

	JLabel statusBar;
	public NetzListe nl;
	VS vs = new VS();

	private AbstractGraphControl control;
	public static final String STATUSBAR_DEFAULT = "Place new nodes and edges or start the test procedure";

	public GraphView(AbstractGraphControl abstractGraphControl) {
		//super("Graph");
		this.control = abstractGraphControl;
		setLayout(new BorderLayout());
		add("North", getNorthPanel());		
		nl = new MGraphListe(statusBar, vs, abstractGraphControl);
		JScrollPane sPane = new JScrollPane(nl);
		add("Center", sPane);
    }
	
	JButton buttonNewVertex;
	JButton buttonNewEdge;
	JButton buttonZoomOut;
	JButton buttonZoomIn;
	JButton buttonPrint;
	JButton buttonLatex;
	JButton buttonPhysics;
	
	public JPanel getNorthPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add("North", getToolBar());
		statusBar = new JLabel(STATUSBAR_DEFAULT);
		panel.add("South", statusBar);
		return panel;
	}

    public JPanel getToolBar() {
		Insets insets = new Insets(0, 0, 0, 0);
		JPanel toolPanel = new JPanel();
		try {
			toolPanel.setLayout(new FlowLayout());
			((FlowLayout) (toolPanel.getLayout()))
					.setAlignment(FlowLayout.LEFT);
			buttonNewVertex = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/af/statguitoolkit/images/vertex.png"))));
			toolPanel.add(buttonNewVertex);
			buttonNewVertex.addActionListener(this);
			buttonNewVertex.setToolTipText("new vertex");
			buttonNewEdge = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/af/statguitoolkit/images/edge.png"))));
			toolPanel.add(buttonNewEdge);
			buttonNewEdge.addActionListener(this);
			buttonNewEdge.setToolTipText("new edge");
			buttonZoomOut = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/af/statguitoolkit/images/zoom_out.png"))));
			toolPanel.add(buttonZoomOut);
			buttonZoomOut.addActionListener(this);
			buttonZoomOut.setToolTipText("zoom out");
			buttonZoomIn = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/af/statguitoolkit/images/zoom_in.png"))));
			toolPanel.add(buttonZoomIn);
			buttonZoomIn.addActionListener(this);
			buttonZoomIn.setToolTipText("zoom in");
			buttonPrint = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/af/statguitoolkit/images/print.png"))));
			toolPanel.add(buttonPrint);
			buttonPrint.addActionListener(this);
			buttonPrint.setToolTipText("print graph");
			buttonLatex = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/af/statguitoolkit/images/latex.png"))));
			toolPanel.add(buttonLatex);
			buttonLatex.addActionListener(new ActionExportToLatex(this, nl, vs));
			buttonLatex.setToolTipText("export to LaTeX");
			buttonPhysics = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/af/statguitoolkit/images/smiley.png"))));
			toolPanel.add(buttonPhysics);
			buttonPhysics.addActionListener(this);
			buttonPhysics.setToolTipText("physics");
		} catch (IOException e) {
			ErrorHandler.getInstance().makeErrDialog(e.getMessage(), e);
		}
		return toolPanel;
	}

	public NetzListe getNL() {
		return nl;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(buttonZoomIn)) {
			vs.setZoom(vs.getZoom() * 1.25);
			getNL().refresh();
		} else if (e.getSource().equals(buttonZoomOut)) { 
			vs.setZoom(vs.getZoom() / 1.25);
			getNL().refresh();
		} else if (e.getSource().equals(buttonNewEdge)) {
			vs.newVertex = false;
			vs.newEdge = true;
			getNL().statusBar.setText("Select a node from which this edge should start.");
		} else if (e.getSource().equals(buttonNewVertex)) {
			vs.newVertex = true;
			vs.newEdge = false;
			getNL().statusBar.setText("Click on the graph panel to place the node.");
		} else if (e.getSource().equals(buttonPhysics)) {
			getNL().changePhysics();
		} else if (e.getSource().equals(buttonPrint)) {
			PrintGraph.print(getNL());
		}
		
	}

}
