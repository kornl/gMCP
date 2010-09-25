package org.mutoss.gui.graph;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.DesktopPaneBG;
import org.af.statguitoolkit.graph.DialogConfInt;

public class GraphView extends JPanel implements ActionListener {

	JLabel statusBar;
	public NetzListe nl;
	VS vs = new VS();

	private AbstractGraphControl control;
	public static final String STATUSBAR_DEFAULT = "Place new nodes and edges or start the test procedure";

	public GraphView(AbstractGraphControl abstractGraphControl) {
		//super("Graph");
		this.control = abstractGraphControl;
		statusBar = new JLabel(STATUSBAR_DEFAULT);
		nl = new NetzListe(statusBar, vs, abstractGraphControl);
		setLayout(new BorderLayout());
		add("North", getNorthPanel());		
		JScrollPane sPane = new JScrollPane(nl);
		add("Center", sPane);
    }
	
	JButton buttonNewVertex;
	JButton buttonNewEdge;
	JButton buttonZoomOut;
	JButton buttonZoomIn;
	JButton buttonLatex;
	JButton buttonPhysics;
	JButton buttonSave;
	JTextField jtSaveName;
	
	JButton buttonadjPval;
	JButton buttonConfInt;
	JButton buttonStart;	
	JButton buttonBack;
	
	public JPanel getNorthPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add("North", getToolBar());
		panel.add("Center", getSaveBar());		
		panel.add("South", statusBar);
		return panel;
	}

    private JPanel getSaveBar() {
    	JPanel panel = new JPanel();
    	jtSaveName = new JTextField(control.getName(), 24);
    	panel.setLayout(new FlowLayout());
		((FlowLayout) (panel.getLayout()))
				.setAlignment(FlowLayout.LEFT);
		panel.add(new JLabel("Variablename to save to: "));
		panel.add(jtSaveName);
		jtSaveName.addActionListener(this);
		return panel;
	}

	public JPanel getToolBar() {
		JPanel toolPanel = new JPanel();
		try {
			toolPanel.setLayout(new FlowLayout());
			((FlowLayout) (toolPanel.getLayout()))
					.setAlignment(FlowLayout.LEFT);
			buttonNewVertex = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/vertex.png"))));
			toolPanel.add(buttonNewVertex);
			buttonNewVertex.addActionListener(this);
			buttonNewVertex.setToolTipText("new vertex");
			buttonNewEdge = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/edge.png"))));
			toolPanel.add(buttonNewEdge);
			buttonNewEdge.addActionListener(this);
			buttonNewEdge.setToolTipText("new edge");
			buttonZoomOut = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/zoom_out.png"))));
			toolPanel.add(buttonZoomOut);
			buttonZoomOut.addActionListener(this);
			buttonZoomOut.setToolTipText("zoom out");
			buttonZoomIn = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/zoom_in.png"))));
			toolPanel.add(buttonZoomIn);
			buttonZoomIn.addActionListener(this);
			buttonZoomIn.setToolTipText("zoom in");
			buttonLatex = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/latex.png"))));
			toolPanel.add(buttonLatex);
			buttonLatex.addActionListener(new ActionExportToLatex(this, nl, vs));
			buttonLatex.setToolTipText("export to LaTeX");
			
			buttonadjPval = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/adjPval.png"))));
			toolPanel.add(buttonadjPval);
			buttonadjPval.addActionListener(this);
			buttonadjPval.setToolTipText("calculate adjusted p-values");
			buttonConfInt = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/confint2.png"))));
			toolPanel.add(buttonConfInt);
			buttonConfInt.addActionListener(this);
			buttonConfInt.setToolTipText("calculate confidence intervals");
			
			buttonBack = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/back.png"))));
			toolPanel.add(buttonBack);
			buttonBack.setEnabled(false);
			buttonBack.addActionListener(this);
			buttonBack.setToolTipText("go back one step");
			
			buttonStart = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/StartTesting.png"))));
			toolPanel.add(buttonStart);
			buttonStart.addActionListener(this);
			buttonStart.setToolTipText("start testing");
			
			buttonSave = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/save.png"))));
			toolPanel.add(buttonSave);
			buttonSave.addActionListener(this);
			buttonSave.setToolTipText("save");			
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
		} else if (e.getSource().equals(buttonSave) || e.getSource().equals(jtSaveName)) {			
			getNL().saveGraph(jtSaveName.getText(), true);
		} else if (e.getSource().equals(buttonConfInt)) {
			new DialogConfInt(control.getMainFrame(), nl);
		}
	}

	public VS getVS() {		
		return vs;
	}

}
