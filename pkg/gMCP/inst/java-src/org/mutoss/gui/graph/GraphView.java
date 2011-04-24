package org.mutoss.gui.graph;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.DesktopPaneBG;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;
import org.mutoss.config.Configuration;
import org.mutoss.gui.CreateGraphGUI;
import org.mutoss.gui.RControl;
import org.mutoss.gui.datatable.DataTable;
import org.mutoss.gui.dialogs.AdjustedPValueDialog;
import org.mutoss.gui.dialogs.DialogConfIntEstVar;
import org.mutoss.gui.dialogs.RejectedDialog;

public class GraphView extends JPanel implements ActionListener {

	String name;
	CreateGraphGUI parent;

	public static final String STATUSBAR_DEFAULT = "Place new nodes and edges or start the test procedure";
	JLabel statusBar;
	public NetList nl;
	
	JButton buttonNewVertex;
	JButton buttonNewEdge;
	JButton buttonZoomOut;
	JButton buttonZoomIn;
	JButton buttonadjPval;
	JButton buttonConfInt;
	JButton buttonStart;	
	JButton buttonBack;
	
	String correlation;
	public String result = ".gMCPResult_" + (new Date()).getTime();
	public boolean resultUpToDate = false;
	
	private static final Log logger = LogFactory.getLog(GraphView.class);
	
	public String getGraphName() {		
		return name;
	}

	public CreateGraphGUI getMainFrame() {		
		return parent;
	}

	public PView getPView() {		
		return parent.getPView();
	}

	public void updateEdge(int from, int to, Double w) {
		updateEdge(from, to, new EdgeWeight(w));
	}

	public DataTable getDataTable() {		
		return parent.getDataTable();
	}

	public CreateGraphGUI getGraphGUI() {
		return parent;
	}
	
	public GraphView(String graph, CreateGraphGUI createGraphGUI) {
		this.name = graph;
		this.parent = createGraphGUI;
		statusBar = new JLabel(STATUSBAR_DEFAULT);
		nl = new NetList(statusBar, this);
		setLayout(new BorderLayout());
		add("North", getNorthPanel());		
		JScrollPane sPane = new JScrollPane(nl);
		add("Center", sPane);
    }
	
	public void setGraphName(String name) {
		this.name = name;
	}

	public JPanel getNorthPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add("North", getToolBar());		
		panel.add("South", statusBar);
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
			
			buttonStart = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/StartTesting.png"))));
			toolPanel.add(buttonStart);
			buttonStart.addActionListener(this);
			buttonStart.setEnabled(false);
			buttonStart.setToolTipText("start testing");		
			
			buttonadjPval = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/adjPval.png"))));
			toolPanel.add(buttonadjPval);			
			buttonadjPval.addActionListener(this);
			buttonadjPval.setEnabled(false);
			buttonadjPval.setToolTipText("calculate adjusted p-values");
			
			buttonConfInt = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/mutoss/gui/graph/images/confint2.png"))));
			toolPanel.add(buttonConfInt);
			buttonConfInt.addActionListener(this);
			buttonConfInt.setEnabled(false);
			buttonConfInt.setToolTipText("calculate confidence intervals");
			
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
			nl.setZoom(nl.getZoom() * 1.25);
			getNL().refresh();
		} else if (e.getSource().equals(buttonZoomOut)) { 
			nl.setZoom(nl.getZoom() / 1.25);
			getNL().refresh();
		} else if (e.getSource().equals(buttonNewEdge)) {
			nl.newVertex = false;
			nl.newEdge = true;
			getNL().statusBar.setText("Select a node from which this edge should start.");
		} else if (e.getSource().equals(buttonNewVertex)) {
			nl.newVertex = true;
			nl.newEdge = false;
			getNL().statusBar.setText("Click on the graph panel to place the node.");
		} else if (e.getSource().equals(buttonConfInt)) {
			if (!getNL().isTesting()) {
				getNL().saveGraph();
				getPView().savePValues();
			}
			if (getNL().getKnoten().size()==0) {
				JOptionPane.showMessageDialog(parent, "Please create first a graph.", "Please create first a graph.", JOptionPane.ERROR_MESSAGE);
			} else {
				parent.glassPane.start();
				//startTesting();
				correlation = parent.getPView().getCorrelation();
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						if (!resultUpToDate) {
							RControl.getR().evalVoid(result+" <- gMCP("+getNL().initialGraph+getGMCPOptions()+")");
							resultUpToDate = true;
						}
						double[] alpha = RControl.getR().eval(""+getPView().getTotalAlpha()+"*getWeights("+result+")").asRNumeric().getData();
						boolean[] rejected = RControl.getR().eval("getRejected("+result+")").asRLogical().getData();						
						new DialogConfIntEstVar(parent, nl, rejected, alpha);						
						parent.glassPane.stop();
						return null;
					}  
				};
				worker.execute();				
			}
		} else if (e.getSource().equals(buttonStart)) {
			if (!getNL().isTesting()) {
				parent.glassPane.start();
				startTesting();
				correlation = parent.getPView().getCorrelation();
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						if (!resultUpToDate) {
							RControl.getR().evalVoid(result+" <- gMCP("+getNL().initialGraph+getGMCPOptions()+")");
							resultUpToDate = true;
						}
						boolean[] rejected = RControl.getR().eval(result+"@rejected").asRLogical().getData();				
						new RejectedDialog(parent, rejected, parent.getGraphView().getNL().getKnoten());
						parent.glassPane.stop();
						return null;
					}  
				};
				worker.execute();				
			} else {
				stopTesting();
			}
		} else if (e.getSource().equals(buttonadjPval)) {
			if (getNL().getKnoten().size()==0) {
				JOptionPane.showMessageDialog(parent, "Please create first a graph.", "Please create first a graph.", JOptionPane.ERROR_MESSAGE);				
			} else {
				if (!getNL().isTesting()) {
					getNL().saveGraph();
					getPView().savePValues();
				}
				parent.glassPane.start();
				//startTesting();
				correlation = parent.getPView().getCorrelation();
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {						
						if (!resultUpToDate) {
							RControl.getR().evalVoid(result+" <- gMCP("+getNL().initialGraph+getGMCPOptions()+")");
							resultUpToDate = true;
						}
						double[] adjPValues = RControl.getR().eval(result+"@adjPValues").asRNumeric().getData();
						new AdjustedPValueDialog(parent, getPView().pValues, adjPValues, getNL().getKnoten());
						parent.glassPane.stop();
						return null;
					}  
				};
				worker.execute();
			}
		}
	}

	public void stopTesting() {
		if (!getNL().testingStarted) return;
		getNL().stopTesting();
		getNL().reset();
		getNL().loadGraph();				
		getPView().restorePValues();
		getPView().setTesting(false);
		getPView().revalidate();
		getPView().repaint();
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
		getPView().savePValues();
		try {
			getNL().startTesting();
			getNL().saveGraph();
			getPView().setTesting(true);			
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
		int returnVal = fc.showSaveDialog(getMainFrame());
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();			
		} else {
			return;
		}
		String filename = file.getAbsolutePath();
		nl.saveGraph(".exportGraphToLaTeX", false);
		RControl.getR().eval("gMCPReport(.exportGraphToLaTeX, file=\""+filename+"\")");
	}

	public void updateEdge(int from, int to, EdgeWeight weight) {
		logger.info("Adding Edge from "+from+" to "+to+" with weight "+weight.toString()+".");
		Edge e = getNL().findEdge(getNL().getKnoten().get(from), getNL().getKnoten().get(to));
		if (e!=null) {
			int x = e.getK1();
			int y = e.getK2();			
			if (!weight.toString().equals("0")) {
				getNL().addEdge(new Edge(getNL().getKnoten().get(from), getNL().getKnoten().get(to), weight, getNL(), x, y));
			} else {
				getNL().removeEdge(e);
			}
		} else {
			getNL().addEdge(getNL().getKnoten().get(from), getNL().getKnoten().get(to), weight);
		}
		getNL().repaint();		
	}

	public void enableButtons(Boolean enabled) {
		buttonadjPval.setEnabled(enabled);
		buttonConfInt.setEnabled(enabled);
		buttonStart.setEnabled(enabled);
	}

	public String getGMCPOptions() {
		return ","+getPView().getPValuesString()+ correlation+", alpha="+getPView().getTotalAlpha()+", eps="+Configuration.getInstance().getGeneralConfig().getEpsilon();
	}

	public DView getDView() {
		return 	parent.getDView();	
	}
	
}
