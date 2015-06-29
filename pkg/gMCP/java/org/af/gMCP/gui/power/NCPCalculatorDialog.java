package org.af.gMCP.gui.power;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import org.af.commons.widgets.validate.NumberTextField;
import org.af.commons.widgets.validate.RealTextField;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class NCPCalculatorDialog extends JDialog implements ActionListener {

	JButton jbCalc = new JButton("Calculate");
	JButton jbReset = new JButton("Reset");
	JButton jbSaveClose = new JButton("Save and Close");
	JButton jbCancelClose = new JButton("Cancel and Close");
	JButton checkAll = new JButton("Check/Uncheck all"); 
	List<RealTextField> mlV = new Vector<RealTextField>(); 
	List<RealTextField> mpV = new Vector<RealTextField>();
	List<RealTextField> ncpV = new Vector<RealTextField>();
	List<JCheckBox> saveV = new Vector<JCheckBox>();
	
	NCPRequestor ncpR;
	
	/**
	 * Constructor
	 * @param parent Parent JFrame
	 */
	public NCPCalculatorDialog(PDialog pd, NCPRequestor ncpR) {
		super(pd, "NCP Calculator - Marginal Power", true);
		setLocationRelativeTo(pd);
		Vector<Node> nodes = pd.parent.getGraphView().getNL().getNodes();
		
		String cols = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        
        for (int i=0; i<Math.max(nodes.size(), 2); i++) {
        	cols += ", max(50dlu;pref), 5dlu";
        }
        
        getContentPane().setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;		
				
		for (int i=0; i<nodes.size(); i++) {
			getContentPane().add(new JLabel(nodes.get(i).getName()) , cc.xy(4+2*i, row));
		}
		
		row +=2;
		
		getContentPane().add(new JLabel("Marginal level") , cc.xy(2, row));
		
		for (int i=0; i<nodes.size(); i++) {
			RealTextField ml = new RealTextField("", 0, 1);
			mlV.add(ml);
			getContentPane().add(ml, cc.xy(4+2*i, row));
		}
		
		row +=2;
		
		getContentPane().add(new JLabel("Marginal power") , cc.xy(2, row));
		
		for (int i=0; i<nodes.size(); i++) {
			RealTextField mp = new RealTextField("", 0, 1);
			mpV.add(mp);
			getContentPane().add(mp, cc.xy(4+2*i, row));
		}
		
		row +=2;
		
		getContentPane().add(new JLabel("NCP") , cc.xy(2, row));
		
		for (int i=0; i<nodes.size(); i++) {
			RealTextField ncp = new RealTextField("", 0, 1);
			ncpV.add(ncp);
			getContentPane().add(ncp, cc.xy(4+2*i, row));
		}
		
		row +=2;
		
		getContentPane().add(new JLabel("Save NCP settings") , cc.xy(2, row));
		
		for (int i=0; i<nodes.size(); i++) {
			JCheckBox save = new JCheckBox("");
			saveV.add(save);
			getContentPane().add(save, cc.xy(4+2*i, row));
		}
		getContentPane().add(checkAll, cc.xy(4+2*nodes.size(), row));
		
		row +=2;
		
		jbCalc.addActionListener(this);
		jbReset.addActionListener(this);
		jbSaveClose.addActionListener(this);
		jbCancelClose.addActionListener(this);
		checkAll.addActionListener(this); 
		
		getContentPane().add(jbCalc , cc.xy(2, row));
		getContentPane().add(jbReset , cc.xy(4, row));
		getContentPane().add(jbSaveClose , cc.xy(6, row));
		getContentPane().add(jbCancelClose , cc.xy(8, row));
		
		//TODO: config = new File(path, "gMCP-power-settings.xml");
		
		
        pack();
        
        setVisible(true);
		
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	} 
	
}
