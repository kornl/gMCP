package org.mutoss.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

import org.mutoss.gui.RControl;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class CorrelatedTest extends JDialog implements ActionListener {

	JButton ok = new JButton("Ok");
	
    JRadioButton jrbNoCorrelation = new JRadioButton("No Information about correlations");
    JRadioButton jrbStandardCorrelation = new JRadioButton("Select a standard correlation");
    JRadioButton jrbRCorrelation = new JRadioButton("Select an R correlation matrix");

    JComboBox jcbCorString;
    JComboBox jcbCorObject;
    
	public CorrelatedTest(JFrame parent) {
		super(parent, "Correlated test statistics?", true);
		setLocationRelativeTo(parent);
		
		String[] matrices = RControl.getR().eval("gMCP:::getAllMatrices()").asRChar().getData();
		
		String[] correlations = new String[] {"Dunnett"};
		//"Dunnett", "Tukey", "Sequen", "AVE", "Changepoint", "Williams", "Marcus", "McDermott", "UmbrellaWilliams", "GrandMean"
		
	    jcbCorString = new JComboBox(correlations);
	    jcbCorObject = new JComboBox(matrices);
		
		if (matrices.length==1 && matrices[0].equals("No matrices found.")) {
			jcbCorObject.setEnabled(false);
			jrbRCorrelation.setEnabled(false);
		}

	    jrbNoCorrelation.setSelected(true);

	    ButtonGroup group = new ButtonGroup();
	    group.add(jrbNoCorrelation);
	    group.add(jrbStandardCorrelation);
	    group.add(jrbRCorrelation);

	    jrbNoCorrelation.addActionListener(this);
	    jrbStandardCorrelation.addActionListener(this);
	    jrbRCorrelation.addActionListener(this);
		
        String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        getContentPane().add(jrbNoCorrelation,     cc.xy(2, row));
        //getContentPane().add(new JLabel(), cc.xy(4, row));        
        
        row += 2;
        
        getContentPane().add(jrbStandardCorrelation,     cc.xy(2, row));
        getContentPane().add(jcbCorString, cc.xy(4, row));        
        
        row += 2;
        
        getContentPane().add(jrbRCorrelation,     cc.xy(2, row));
        getContentPane().add(jcbCorObject, cc.xy(4, row));        
        
        row += 2;
                
        getContentPane().add(ok, cc.xy(4, row));
        ok.addActionListener(this);        
        
        pack();
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jrbNoCorrelation) {
			
		} else if (e.getSource() == ok) {
			dispose();
		}
	}	
}
