package org.af.gMCP.gui.power;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.DesktopPaneBG;
import org.af.commons.widgets.buttons.HorizontalButtonPane;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.dialogs.PowerOptionsPanel;
import org.af.gMCP.gui.dialogs.TextFileViewer;
import org.jdesktop.swingworker.SwingWorker;

public class SampleSizeDialog extends PDialog implements ActionListener {

    RandomizationPanel gPanel;
    PowerReqPanel prPanel;
    
	JButton jbHelp;

    //  Theta hat: θ\u0302

	/**
	 * Constructor
	 * @param parent Parent CreateGraphGUI
	 */
	public SampleSizeDialog(CreateGraphGUI parent) {
		super(parent, "Sample Size Calculations", true);
		
		gPanel = new RandomizationPanel(this);
		tPanel.addTab("Randomization", gPanel);
		pNCP = new ScenarioPanel2(this);
		tPanel.addTab(/*"Standardized "+*/"Effect Size", (Component) pNCP);		
		prPanel = new PowerReqPanel(this);
		tPanel.addTab("Power Requirements", prPanel);
		cvPanel = new CVPanel(this);
		tPanel.addTab("Correlation Matrix", cvPanel);
		tPanel.addTab("Options", new PowerOptionsPanel(parent));
		
		//TODO: Do we want scrollable tabs? 
		//tPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		Set<String> variables = parent.getGraphView().getNL().getAllVariables();
		if (!Configuration.getInstance().getGeneralConfig().useEpsApprox())	{
			variables.remove("ε");
		}
		
		getContentPane().add(tPanel, c);
		
		c.weighty=0; c.gridy++; c.weightx=0; c.fill=GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		//HorizontalButtonPane bp = new OkCancelButtonPane();
		
		try {
			jbHelp = new JButton(
					new ImageIcon(ImageIO.read(DesktopPaneBG.class
							.getResource("/org/af/gMCP/gui/graph/images/questionmark32.png"))));
		} catch (IOException e) {
			ErrorHandler.getInstance().makeErrDialog(e.getMessage(), e);
			jbHelp = new JButton("Help!");
		}
		jbHelp.addActionListener(this);
		
		HorizontalButtonPane bp = new HorizontalButtonPane(new String[] {"Help", "Ok", "Cancel"}, new String[] {"help", HorizontalButtonPane.OK_CMD, HorizontalButtonPane.CANCEL_CMD});
		getContentPane().add(bp, c);
		bp.addActionListener(this);		
		
        pack();
        // Adding space for further arms or scenarios:
        Dimension d = this.getSize();
        this.setSize(d.width, d.height+100);
        setLocationRelativeTo(parent);
        setVisible(true);
	} 
	
	public void actionPerformed(ActionEvent e) {

		String weights = parent.getGraphView().getNL().getGraphName() + "@weights";
		double alpha;
		try {
			alpha = parent.getPView().getTotalAlpha();
		} catch (Exception e1) {
			return;
		}
		String graph = parent.getGraphView().getNL().getGraphName();

		// TODO: Do we still need sometimes something as parse2numeric? I guess yes.
		//RControl.getR().eval(parent.getGraphView().getNL().getGraphName()+"<-gMCP:::parse2numeric("+parent.getGraphView().getNL().getGraphName()+")");

		if (e.getActionCommand().equals(HorizontalButtonPane.OK_CMD)) {

			rCommand = "sampSize(graph=" + graph 
					+", ratio=" + gPanel.getRatio()
					+", effSize=" + pNCP.getEffSizeString()
					+", powerReqFunc=" + prPanel.getPowerFunctions()
					+", target="+prPanel.getPowerTargets()					 
					+ ", corr.sim = " + cvPanel.getSigma() //diag(length(mean)),corr = NULL,"+
					+", alpha=" + alpha
					+ cvPanel.getMatrixForParametricTest()
					+ ", type = \""+Configuration.getInstance().getGeneralConfig().getTypeOfRandom()+"\""
					+ ", upscale = "+(Configuration.getInstance().getGeneralConfig().getUpscale()?"TRUE":"FALSE")
					+ ")";				

			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {				
					new TextFileViewer(parent, "R Objects", "The following R will be executed:\n\n" + rCommand, true);		
				}
			});
			
			
			if (true) return;
			
			parent.glassPane.start();
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {					
					try {
						String result = RControl.getR().eval(rCommand).asRChar().getData()[0];
						new TextFileViewer(parent, "Power results", result, true);
					} catch (Exception e) {
						String message = e.getMessage();
						JOptionPane.showMessageDialog(parent, "R call produced an error:\n\n"+message+"\nWe will open a window with R code to reproduce this error for investigation.", "Error in R Call", JOptionPane.ERROR_MESSAGE);
						JDialog d = new JDialog(parent, "R Error", true);
						d.add(
								new TextFileViewer(parent, "R Objects", "The following R code produced the following error:\n\n" +message+
										rCommand, true)
								);
						d.pack();
						d.setSize(800, 600);
						d.setVisible(true);
						e.printStackTrace();						
					} finally {
						parent.glassPane.stop();
					}
					return null;
				}					 
			};
			worker.execute();				
		}
		if (e.getActionCommand().equals("help")) {
			//TODO Open Help.
		} else {		
			dispose();
		}
	}

}
