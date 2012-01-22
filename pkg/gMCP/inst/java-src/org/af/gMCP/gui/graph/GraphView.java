package org.af.gMCP.gui.graph;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
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
import javax.swing.filechooser.FileFilter;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.DesktopPaneBG;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.datatable.DataTable;
import org.af.gMCP.gui.dialogs.AdjustedPValueDialog;
import org.af.gMCP.gui.dialogs.DialogConfIntEstVar;
import org.af.gMCP.gui.dialogs.RejectedDialog;
import org.af.gMCP.gui.dialogs.VariableNameDialog;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

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
	
	String correlation = "";
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
											.getResource("/org/af/gMCP/gui/graph/images/vertex.png"))));
			toolPanel.add(buttonNewVertex);
			buttonNewVertex.addActionListener(this);
			buttonNewVertex.setToolTipText("new vertex");
			
			buttonNewEdge = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/af/gMCP/gui/graph/images/edge.png"))));
			toolPanel.add(buttonNewEdge);
			buttonNewEdge.addActionListener(this);
			buttonNewEdge.setToolTipText("new edge");
			
			buttonZoomOut = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/af/gMCP/gui/graph/images/zoom_out.png"))));
			toolPanel.add(buttonZoomOut);
			buttonZoomOut.addActionListener(this);
			buttonZoomOut.setToolTipText("zoom out");
			
			buttonZoomIn = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/af/gMCP/gui/graph/images/zoom_in.png"))));
			toolPanel.add(buttonZoomIn);
			buttonZoomIn.addActionListener(this);
			buttonZoomIn.setToolTipText("zoom in");
			
			buttonStart = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/af/gMCP/gui/graph/images/StartTesting.png"))));
			toolPanel.add(buttonStart);
			buttonStart.addActionListener(this);
			buttonStart.setEnabled(false);
			buttonStart.setToolTipText("start testing");		
			
			buttonadjPval = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/af/gMCP/gui/graph/images/adjPval.png"))));
			toolPanel.add(buttonadjPval);			
			buttonadjPval.addActionListener(this);
			buttonadjPval.setEnabled(false);
			buttonadjPval.setToolTipText("calculate adjusted p-values");
			
			buttonConfInt = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
											.getResource("/org/af/gMCP/gui/graph/images/confint2.png"))));
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
				getPView().savePValues();
				getNL().saveGraphWithoutVariables(getNL().initialGraph, false);
	        	getNL().loadGraph();
	        	getPView().restorePValues();
			}
			if (getNL().getNodes().size()==0) {
				JOptionPane.showMessageDialog(parent, "Please create first a graph.", "Please create first a graph.", JOptionPane.ERROR_MESSAGE);
			} else {
				parent.glassPane.start();
				//startTesting();
				correlation = parent.getPView().getParameters();
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						if (!resultUpToDate) {
							RControl.getR().evalVoid(result+" <- gMCP("+getNL().initialGraph+getGMCPOptions()+")");
							resultUpToDate = true;
						}
						double[] alpha = RControl.getR().eval(""+getPView().getTotalAlpha()+"*getWeights("+result+")").asRNumeric().getData();
						boolean[] rejected = RControl.getR().eval("getRejected("+result+")").asRLogical().getData();
						parent.glassPane.stop();
						new DialogConfIntEstVar(parent, nl, rejected, alpha);
						return null;
					}  
				};
				worker.execute();				
			}
		} else if (e.getSource().equals(buttonStart)) {
			if (!getNL().isTesting()) {
				getPView().savePValues();
				getNL().saveGraphWithoutVariables(getNL().initialGraph, false);
	        	getNL().loadGraph();
	        	getPView().restorePValues();
				parent.glassPane.start();				
				startTesting();
				correlation = parent.getPView().getParameters();
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {
						if (!resultUpToDate) {
							RControl.getR().evalVoid(result+" <- gMCP("+getNL().initialGraph+getGMCPOptions()+")");
							resultUpToDate = true;
						}
						boolean[] rejected = RControl.getR().eval(result+"@rejected").asRLogical().getData();
						String output = null;
						if (Configuration.getInstance().getGeneralConfig().verbose() && RControl.getR().eval("!is.null(attr("+result+", \"output\"))").asRLogical().getData()[0]) {
							output = RControl.getR().eval("attr("+result+", \"output\")").asRChar().getData()[0];
						}
						parent.glassPane.stop();
						new RejectedDialog(parent, rejected, parent.getGraphView().getNL().getNodes(), output);
						return null;
					}  
				};
				worker.execute();				
			} else {
				stopTesting();
			}
		} else if (e.getSource().equals(buttonadjPval)) {
			if (getNL().getNodes().size()==0) {
				JOptionPane.showMessageDialog(parent, "Please create first a graph.", "Please create first a graph.", JOptionPane.ERROR_MESSAGE);				
			} else {
				if (!getNL().isTesting()) {
					getPView().savePValues();
					getNL().saveGraphWithoutVariables(getNL().initialGraph, false);
		        	getNL().loadGraph();					
					getPView().restorePValues();
				}
				parent.glassPane.start();
				//startTesting();
				correlation = parent.getPView().getParameters();
				SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
					@Override
					protected Void doInBackground() throws Exception {						
						if (!resultUpToDate) {
							RControl.getR().evalVoid(result+" <- gMCP("+getNL().initialGraph+getGMCPOptions()+")");
							resultUpToDate = true;
						}
						double[] adjPValues = RControl.getR().eval(result+"@adjPValues").asRNumeric().getData();
						parent.glassPane.stop();
						new AdjustedPValueDialog(parent, getPView().pValues, adjPValues, getNL().getNodes());
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
		getDataTable().setTesting(false);
		getPView().restorePValues();
		getPView().setTesting(false);
		getPView().revalidate();
		getPView().repaint();
		buttonNewVertex.setEnabled(true);
		buttonNewEdge.setEnabled(true);
		try {
			buttonStart.setIcon(new ImageIcon(ImageIO.read(DesktopPaneBG.class
					.getResource("/org/af/gMCP/gui/graph/images/StartTesting.png"))));
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
			getDataTable().setTesting(true);
			getPView().setTesting(true);			
			buttonNewVertex.setEnabled(false);
			buttonNewEdge.setEnabled(false);				
			buttonStart.setIcon(new ImageIcon(ImageIO.read(DesktopPaneBG.class
					.getResource("/org/af/gMCP/gui/graph/images/Reset.png"))));
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
		Edge e = getNL().findEdge(getNL().getNodes().get(from), getNL().getNodes().get(to));
		if (e!=null) {
			int x = e.getK1();
			int y = e.getK2();			
			if (!weight.toString().equals("0")) {
				getNL().setEdge(new Edge(getNL().getNodes().get(from), getNL().getNodes().get(to), weight, getNL(), x, y));
			} else {
				getNL().removeEdge(e);
			}
		} else {
			getNL().setEdge(getNL().getNodes().get(from), getNL().getNodes().get(to), weight);
		}
		getNL().repaint();		
	}

	public void enableButtons(Boolean enabled) {
		buttonadjPval.setEnabled(enabled);
		if (getPView().jrbNoCorrelation.isSelected()) buttonConfInt.setEnabled(enabled);
		buttonStart.setEnabled(enabled);
	}

	public String getGMCPOptions() {
		return ","+getPView().getPValuesString()
				+ correlation
				+", alpha="+getPView().getTotalAlpha()
				+", eps="+Configuration.getInstance().getGeneralConfig().getEpsilon()
				+", verbose="+(Configuration.getInstance().getGeneralConfig().verbose()?"42":"FALSE");
	}

	public DView getDView() {
		return 	parent.getDView();	
	}
	
	public void saveGraphImage(File file) {
		BufferedImage img = getNL().getImage();
		try {
			ImageIO.write( img, "png", file );
		} catch( Exception ex ) {
			JOptionPane.showMessageDialog(this, "Saving image to '" + file.getAbsolutePath() + "' failed: " + ex.getMessage(), "Saving failed.", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void saveGraph() {
		JFileChooser fc = new JFileChooser(Configuration.getInstance().getClassProperty(this.getClass(), "RObjDirectory"));		
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setFileFilter(new FileFilter() {
			public boolean accept(File f) {
				if (f.isDirectory()) return true;
				return f.getName().toLowerCase().endsWith(".rdata");
			}
			public String getDescription () { return "RData files"; }  
		});
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            Configuration.getInstance().setClassProperty(this.getClass(), "RObjDirectory", f.getParent());
            if (!f.getName().toLowerCase().endsWith(".rdata")) {
            	f = new File(f.getAbsolutePath()+".RData");
            }
            try {
            	VariableNameDialog vnd = new VariableNameDialog(getGraphGUI(), getGraphName());            	
            	String name = vnd.getName();
            	name = getNL().saveGraph(name, false); 
            	String filename = f.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\");            	
            	RControl.getR().eval("save("+name+", file=\""+filename+"\")");        		
            	JOptionPane.showMessageDialog(getMainFrame(), "Exported graph to R object '"+name+"' and saved this to \n'" + f.getAbsolutePath() + "'.", "Saved graph", JOptionPane.INFORMATION_MESSAGE);
            	Configuration.getInstance().getGeneralConfig().addGraph(f.getAbsolutePath());
    		} catch( Exception ex ) {
    			JOptionPane.showMessageDialog(getMainFrame(), "Saving graph to '" + f.getAbsolutePath() + "' failed: " + ex.getMessage(), "Saving failed", JOptionPane.ERROR_MESSAGE);
    		}
        }	
	}

	public void loadPValuesFromR() {
		VariableNameDialog vnd = new VariableNameDialog(getGraphGUI());     
		try {
			double[] data = RControl.getR().eval(vnd.getName()).asRNumeric().getData();
			if (data.length!=getNL().getNodes().size()) {
				JOptionPane.showMessageDialog(getMainFrame(), "Number of hypotheses and values do not match.", 
						"Number of hypotheses and values do not match", JOptionPane.ERROR_MESSAGE);
				return;
			}
			getPView().setPValues(ArrayUtils.toObject(data));					
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading values from R:\n"+ex.getMessage(), 
					"Error loading values from R", JOptionPane.ERROR_MESSAGE);
		}		
	}
	
}
