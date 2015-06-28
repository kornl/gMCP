package org.af.gMCP.gui.power;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.af.commons.widgets.buttons.HorizontalButtonPane;
import org.af.commons.widgets.buttons.OkCancelButtonPane;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.dialogs.PowerOptionsPanel;
import org.af.gMCP.gui.dialogs.TextFileViewer;
import org.jdesktop.swingworker.SwingWorker;

public class SampleSizeDialog extends PDialog implements ActionListener {

    RandomizationPanel gPanel;
    PowerReqPanel prPanel;

    //  Theta hat: θ\u0302

	/**
	 * Constructor
	 * @param parent Parent CreateGraphGUI
	 */
	public SampleSizeDialog(CreateGraphGUI parent) {
		super(parent, "Sample Size Calculations", true);
		
		pNCP = new ScenarioPanel(this);
		tPanel.addTab("Effect Size", pNCP);
		prPanel = new PowerReqPanel(this);
		tPanel.addTab("Power Requirements", prPanel);
		gPanel = new RandomizationPanel(this);
		tPanel.addTab("Randomization", gPanel);
		cvPanel = new CVPanel(this);
		tPanel.addTab("Correlation Matrix", cvPanel);
		userDefinedFunctions = new UserDefinedPanel(this, nodes);
		//tPanel.addTab("User defined power function", userDefinedFunctions);
		tPanel.addTab("Options", new PowerOptionsPanel(parent));
		
		Set<String> variables = parent.getGraphView().getNL().getAllVariables();
		if (!Configuration.getInstance().getGeneralConfig().useEpsApprox())	{
			variables.remove("ε");
		}
		
		getContentPane().add(tPanel, c);
		
		c.weighty=0; c.gridy++; c.weightx=0; c.fill=GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		//HorizontalButtonPane bp = new OkCancelButtonPane();
		HorizontalButtonPane bp = new HorizontalButtonPane(new String[] {"Help", "Ok", "Cancel"}, new String[] {"help", HorizontalButtonPane.OK_CMD, HorizontalButtonPane.CANCEL_CMD});
		getContentPane().add(bp, c);
		bp.addActionListener(this);		
		
        pack();
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
		String G = parent.getGraphView().getNL().getGraphName() + "@m";

		// TODO: Do we still need sometimes something as parse2numeric? I guess yes.
		//RControl.getR().eval(parent.getGraphView().getNL().getGraphName()+"<-gMCP:::parse2numeric("+parent.getGraphView().getNL().getGraphName()+")");

		if (e.getActionCommand().equals(HorizontalButtonPane.OK_CMD)) {

			rCommand = "gMCP:::calcMultiPower(weights="+weights+", alpha="+alpha+", G="+G+pNCP.getNCPString()
					+ ","+"corr.sim = " + cvPanel.getSigma() //diag(length(mean)),corr = NULL,"+
					+ cvPanel.getMatrixForParametricTest()
					+ userDefinedFunctions.getUserDefined()
					+ ", nSim = "+Configuration.getInstance().getGeneralConfig().getNumberOfSimulations()
					+ ", type = \""+Configuration.getInstance().getGeneralConfig().getTypeOfRandom()+"\""
					+ ")";				

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
