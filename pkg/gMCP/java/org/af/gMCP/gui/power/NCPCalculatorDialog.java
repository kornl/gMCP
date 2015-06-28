package org.af.gMCP.gui.power;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;

import org.af.gMCP.gui.graph.Node;

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
