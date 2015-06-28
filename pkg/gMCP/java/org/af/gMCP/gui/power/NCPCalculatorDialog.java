package org.af.gMCP.gui.power;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.af.commons.widgets.buttons.HorizontalButtonPane;
import org.af.commons.widgets.buttons.OkCancelButtonPane;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.dialogs.PowerOptionsPanel;
import org.af.gMCP.gui.dialogs.TextFileViewer;
import org.af.gMCP.gui.graph.LaTeXTool;
import org.af.gMCP.gui.graph.Node;
import org.jdesktop.swingworker.SwingWorker;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class NCPCalculatorDialog extends JDialog implements ActionListener {

	JButton jbCalc = new JButton("Calculate");
	JButton jbReset = new JButton("Reset");
	JButton jbSaveClose = new JButton("Save and Close");
	JButton jbCancelClose = new JButton("Cancel and Close");
	
	NCPRequestor ncpR;
	
	/**
	 * Constructor
	 * @param parent Parent JFrame
	 */
	public NCPCalculatorDialog(PDialog pd, NCPRequestor ncpR) {
		super(pd, "NCP Calculator", true);
		setLocationRelativeTo(pd);
		Vector<Node> nodes = pd.parent.getGraphView().getNL().getNodes();
		
		//TODO: config = new File(path, "gMCP-power-settings.xml");
		
		
        pack();
        
        setVisible(true);
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
}
