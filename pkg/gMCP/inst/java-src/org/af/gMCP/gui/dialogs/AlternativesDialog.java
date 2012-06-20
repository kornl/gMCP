package org.af.gMCP.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.af.gMCP.gui.MenuBarMGraph;
import org.af.gMCP.gui.graph.NetList;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class AlternativesDialog extends JDialog implements ActionListener {

	JButton ok = new JButton("Ok");
	JPanel panel = new JPanel();
	List<JComboBox> altBoxes = new Vector<JComboBox>();
	public static final String[] alternatives = new String[] {"less", "greater", "two.sided"};

	public AlternativesDialog(JFrame parent, NetList nl) {
		super(parent, "Number of Hypotheses", true);
		setLocationRelativeTo(parent);
		
        String cols = "5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu,pref, 5dlu";
        
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();

        getContentPane().add(new JLabel("Number of hypotheses:"), cc.xy(2, 2));       	        

        rows = "5dlu";
        for (int i=0;i<nl.getNodes().size();i++) {
        	rows = rows + ",pref, 5dlu";
        }
        
        layout = new FormLayout(cols, rows);
        panel.setLayout(layout);
        int row = 2;
        
        
    	for (int i=0;i<nl.getNodes().size();i++) {
    		altBoxes.add(new JComboBox(alternatives));
    		panel.add(altBoxes.get(i), cc.xy(2, row));
    		row += 2;
    	}

    	JScrollPane sp = new JScrollPane(panel);
    	getContentPane().add(sp, cc.xy(2, 4));

    	row += 2;
        
        getContentPane().add(ok, cc.xy(2, 6));
        ok.addActionListener(this);        
        
        pack();
        setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {

		dispose();
	}

	public String getAlternatives() {
		String s = "c(";
		for (int i=0; i<altBoxes.size(); i++) {
			s = s+"\""+altBoxes.get(i).getSelectedItem()+"\"";
			if (i != altBoxes.size()-1) s+= ", ";
		}
		return s+")";
	}
}
