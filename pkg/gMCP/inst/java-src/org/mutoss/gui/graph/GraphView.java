package org.mutoss.gui.graph;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.DesktopPaneBG;
import org.mutoss.gui.RControl;

public class GraphView extends JPanel implements ActionListener {

	JLabel statusBar;
	public NetList nl;
	VS vs = new VS();

	private ControlMGraph control;
	public static final String STATUSBAR_DEFAULT = "Place new nodes and edges or start the test procedure";

	public GraphView(ControlMGraph abstractGraphControl) {
		//super("Graph");
		this.control = abstractGraphControl;
		statusBar = new JLabel(STATUSBAR_DEFAULT);
		nl = new NetList(statusBar, vs, abstractGraphControl);
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
	public JTextField jtSaveName;
	
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
		panel.add(new JLabel("Object name in R for saving/loading: "));
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
			buttonLatex.addActionListener(this);
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
			
			/*buttonBack = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/back.png"))));
			toolPanel.add(buttonBack);
			buttonBack.setEnabled(false);
			buttonBack.addActionListener(this);
			buttonBack.setToolTipText("go back one step");*/
			
			buttonStart = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/StartTesting.png"))));
			toolPanel.add(buttonStart);
			buttonStart.addActionListener(this);
			buttonStart.setEnabled(false);
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

	public NetList getNL() {
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
		} else if (e.getSource().equals(buttonSave) || e.getSource().equals(jtSaveName)) {			
			getNL().saveGraph(jtSaveName.getText(), true);
		} else if (e.getSource().equals(buttonConfInt)) {
			if (getNL().getKnoten().size()==0) {
				JOptionPane.showMessageDialog(control.parent, "Please create first a graph.", "Please create first a graph.", JOptionPane.ERROR_MESSAGE);
			} else {
				new DialogConfIntEstVar(control.getMainFrame(), control, nl);
			}
		} else if (e.getSource().equals(buttonStart)) {
			if (!getNL().isTesting()) {
				startTesting();
			} else {
				stopTesting();
			}
		} else if (e.getSource().equals(buttonadjPval)) {
			if (getNL().getKnoten().size()==0) {
				JOptionPane.showMessageDialog(control.parent, "Please create first a graph.", "Please create first a graph.", JOptionPane.ERROR_MESSAGE);
			} else {
				if (!getNL().isTesting()) {
					getNL().saveGraph();
					control.getPView().savePValues();
				}
				String pValues = control.getPView().getPValuesString();
				double[] adjPValues = RControl.getR().eval("gMCP:::adjPValues("+NetList.initialGraph+","+pValues+")@adjPValues").asRNumeric().getData();
				new AdjustedPValueDialog(control.getMainFrame(), control.getPView().pValues, adjPValues, getNL().getKnoten());
			}
		} else if (e.getSource().equals(buttonLatex)) {
			exportLaTeXGraph();
		}
	}

	DecimalFormat format = new DecimalFormat("#.###");
	
	public VS getVS() {		
		return vs;
	}
	
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
		writeLaTeX(getNL().getLaTeX());
	}
	
	public String LATEX_BEGIN_DOCUMENT = "\\documentclass[11pt]{article}\n"+
	 "\\usepackage{tikz}\n"+
	 "\\usetikzlibrary{snakes,arrows,shapes}\n"+
	 "\\begin{document}\n";

	public String LATEX_END_DOCUMENT = "\\end{document}";

	public void stopTesting() {
		if (!getNL().testingStarted) return;
		getNL().stopTesting();
		getNL().reset();
		//control.getPView().removeAllPanels();
		getNL().loadGraph();				
		control.getPView().restorePValues();
		control.getPView().setTesting(false);
		control.getPView().revalidate();
		control.getPView().repaint();
		buttonNewVertex.setEnabled(true);
		buttonNewEdge.setEnabled(true);
		try {
			buttonStart.setIcon(new ImageIcon(ImageIO.read(DesktopPaneBG.class
					.getResource("/org/mutoss/gui/graph/images/StartTesting.png"))));
		} catch (IOException ex) {
			ErrorHandler.getInstance().makeErrDialog(ex.getMessage(), ex);
		}
	}

	public void startTesting() {	
		if (getNL().testingStarted) return;
		control.getPView().savePValues();
		try {
			getNL().startTesting();
			getNL().saveGraph();
			control.getPView().setTesting(true);			
			buttonNewVertex.setEnabled(false);
			buttonNewEdge.setEnabled(false);				
			buttonStart.setIcon(new ImageIcon(ImageIO.read(DesktopPaneBG.class
					.getResource("/org/mutoss/gui/graph/images/Reset.png"))));
		} catch (Exception ex) {
			ErrorHandler.getInstance().makeErrDialog(ex.getMessage(), ex);
		} 
	}
	
	
	public void WriteLaTeXwithR() {
		JFileChooser fc = new JFileChooser();
		File file;
		int returnVal = fc.showSaveDialog(control.getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();			
		} else {
			return;
		}
		String filename = file.getAbsolutePath();
		nl.saveGraph(".exportGraphToLaTeX", false);
		RControl.getR().eval("gMCPReport(.exportGraphToLaTeX, file=\""+filename+"\")");
	}
	
}
