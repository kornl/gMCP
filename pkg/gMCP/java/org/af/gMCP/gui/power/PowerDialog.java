package org.af.gMCP.gui.power;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.af.commons.errorhandling.DefaultExceptionHandler;
import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.buttons.HorizontalButtonPane;
import org.af.commons.widgets.buttons.OkCancelButtonPane;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.dialogs.PowerOptionsPanel;
import org.af.gMCP.gui.dialogs.TextFileViewer;
import org.af.gMCP.gui.graph.LaTeXTool;
import org.af.jhlir.call.RDataFrame;
import org.af.jhlir.call.RList;
import org.jdesktop.swingworker.SwingWorker;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PowerDialog extends PDialog implements ActionListener {

	VariablePanel vp = null;
	
	/**
	 * Constructor
	 * @param parent Parent JFrame
	 */
	public PowerDialog(CreateGraphGUI parent) {
		super(parent, "Power Simulation - specify probability distribution of test statistics", true);
		
		config = new File(path, "gMCP-power-settings.xml");
		
		pNCP = new ScenarioPanel(this);
		tPanel.addTab("Noncentrality Parameter (NCP) Settings", (Component) pNCP);
		cvPanel = new CVPanel(this);
		tPanel.addTab("Correlation Matrix", cvPanel);
		userDefinedFunctions = new UserDefinedPanel(this, nodes);
		tPanel.addTab("User defined power function", userDefinedFunctions);
		oPanel = new PowerOptionsPanel(parent);
		tPanel.addTab("Options", oPanel);
		Set<String> variables = parent.getGraphView().getNL().getAllVariables();
		if (!Configuration.getInstance().getGeneralConfig().useEpsApprox())	{
			variables.remove("ε");
		}
		if (variables.size()>0) {
			vp = new VariablePanel(variables);
			tPanel.addTab("Variables", vp);
		}
		
		getContentPane().add(tPanel, c);
		
		c.weighty=0; c.gridy++; c.weightx=0; c.fill=GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.EAST;
		
		HorizontalButtonPane bp = new HorizontalButtonPane(new String[] {"Help", "Ok", "Cancel"}, new String[] {"help", HorizontalButtonPane.OK_CMD, HorizontalButtonPane.CANCEL_CMD});
		getContentPane().add(bp, c);
		bp.addActionListener(this);		
		
		if (config.exists()) {
			SettingsToXML.loadConfigFromXML(config, this);
		}
		
        pack();
        setLocationRelativeTo(parent);
        
		if (tmp && !Configuration.getInstance().getClassProperty(this.getClass(), "tellAboutFiles", "yes").equals("no")) {
			JCheckBox tellMeAgain = new JCheckBox("Don't show me this info again.");			
			String message = "The settings in this dialog will be saved for further runs.\n" +
					"If you want these settings to be automatically saved not only\n" +
					"temporarily, but even between sessions, please specify a\n" +
					"directory for saving these files in the options and reopen\n" +
					"this dialog.";
			JOptionPane.showMessageDialog(parent, new Object[] {message, tellMeAgain}, "Info", JOptionPane.INFORMATION_MESSAGE);
			if (tellMeAgain.isSelected()) {
				Configuration.getInstance().setClassProperty(this.getClass(), "tellAboutFiles", "no");
			}
		}
        
        setVisible(true);
		
	} 
	
	public void actionPerformed(ActionEvent e) {

		double alpha;
		try {
			alpha = parent.getPView().getTotalAlpha();
		} catch (Exception e1) {
			return;
		}

		// TODO: Do we still need sometimes something as parse2numeric? I guess yes.
		//RControl.getR().eval(parent.getGraphView().getNL().getGraphName()+"<-gMCP:::parse2numeric("+parent.getGraphView().getNL().getGraphName()+")");

		if (e.getActionCommand().equals(HorizontalButtonPane.OK_CMD)) {
			
			// If there is still some user-defined power function in the jtUserDefined JTextField, this will add it to the list: 
			userDefinedFunctions.actionPerformed(null);
			
			if (RControl.getR().eval("any(is.na("+cvPanel.getSigma()+"))").asRLogical().getData()[0]) {
				JOptionPane.showMessageDialog(this, "Correlation matrix for simulation can not contain NAs.", "No NAs allowed", JOptionPane.ERROR_MESSAGE);
				tPanel.setSelectedComponent(cvPanel);
				return;
			}
			
			SettingsToXML.saveSettingsToXML(config, this);

			createLongRCommand(alpha);
			
			rCommand = "gMCP:::calcMultiPower(graph="+parent.getGraphView().getNL().getGraphName()+", alpha="+alpha+", ncpL="+pNCP.getNCPString()
					+ ","+"corr.sim = " + cvPanel.getSigma() //diag(length(mean)),corr = NULL,"+
					+ cvPanel.getMatrixForParametricTest()
					+ ", f = "+userDefinedFunctions.getUserDefined()
					+ ", n.sim = "+Configuration.getInstance().getGeneralConfig().getNumberOfSimulations()
					+ ", type = \""+Configuration.getInstance().getGeneralConfig().getTypeOfRandom()+"\""
					+ getVariables()
					+ ", digits=4"
					+ ")";				

			parent.glassPane.start();
			SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

				@Override
				protected Void doInBackground() throws Exception {					
					try {						
						RControl.setSeed();
						//RDataFrame result = RControl.getR().eval(rCommand).asRDataFrame();
						RList result = RControl.getR().eval(rCommand).asRList();
						new PowerResultDialog(parent, result.get(0).asRDataFrame(), result.get(1).asRChar().getData(), longRCommand+"\n\n"+rCommand);
					} catch (Exception e) {						
						ErrorHandler.getInstance().makeErrDialog(e.getMessage(), e, false);
					} finally {
						parent.glassPane.stop();
					}
					return null;
				}					 
			};
			worker.execute();				
		}
		if (e.getActionCommand().equals("help")) {
			if (tPanel.getSelectedComponent()==pNCP) {
				parent.openHelp("ncps");
			} else if (tPanel.getSelectedComponent()==cvPanel) {
				parent.openHelp("cormat2");
			} else if (tPanel.getSelectedComponent()==oPanel) {
				parent.openHelp("optNumeric");
			} else if (tPanel.getSelectedComponent()==userDefinedFunctions) {
				parent.openHelp("udpf");
			} else {
				parent.openHelp("power");
			}
		} else {		
			dispose();
		}
	}

	private String getVariables() {
		if (vp==null) return "";
		return vp.getVariables();
	}

	private void createLongRCommand(double alpha) {

		longRCommand = "createCalcPowerCall(graph="+parent.getGraphView().getNL().getGraphName()+", alpha="+alpha+", ncpL=\""+pNCP.getNCPString()+"\""
				+ ","+"corr.sim = " + cvPanel.getSigma() //diag(length(mean)),corr = NULL,"+
				+ cvPanel.getMatrixForParametricTest()
				+ ", f = \""+userDefinedFunctions.getUserDefined()+"\""
				+ ", n.sim = "+Configuration.getInstance().getGeneralConfig().getNumberOfSimulations()
				+ ", type = \""+Configuration.getInstance().getGeneralConfig().getTypeOfRandom()+"\""
				+ getVariables()
				+ ", digits=4"
				+ ")";		
		
		longRCommand = RControl.getR().eval(longRCommand).asRChar().getData()[0];
		
	}

}
