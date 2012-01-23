package org.af.gMCP.gui.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.af.commons.widgets.validate.ValidationException;
import org.af.gMCP.config.Configuration;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * OptionsPanel for plot settings.
 */
public class NumericPanel extends OptionsPanel implements ActionListener { 

    private JCheckBox useEpsApprox;
    private JTextField jtfEps;
    private JCheckBox tryToSimplify;
    private JTextField jtfDigits;
    private JCheckBox verbose;
    private Configuration conf;


    public NumericPanel(Configuration conf) {
        this.conf = conf;

        makeComponents();
        doTheLayout();
    }


    private void makeComponents() {
        useEpsApprox = new JCheckBox("Use epsilon approximation");
        useEpsApprox.setSelected(conf.getGeneralConfig().useEpsApprox());
        useEpsApprox.addActionListener(this);
        useEpsApprox.setEnabled(false);
        
        jtfEps = new JTextField(30);
        jtfEps.setText(""+conf.getGeneralConfig().getEpsilon()); 
        jtfEps.setEnabled(conf.getGeneralConfig().useEpsApprox());
        
        tryToSimplify = new JCheckBox("Try to show fractions / rounded numbers");
        tryToSimplify.setSelected(conf.getGeneralConfig().simplify());
        tryToSimplify.addActionListener(this);
        tryToSimplify.setEnabled(false);
        
        jtfDigits = new JTextField(30);
        jtfDigits.setText(""+conf.getGeneralConfig().getDigits2()); 
        jtfDigits.setEnabled(conf.getGeneralConfig().simplify());
        
        verbose = new JCheckBox("Verbose output of algorithms");
        verbose.setSelected(conf.getGeneralConfig().verbose());
    }

    private void doTheLayout() {
        JPanel p1 = new JPanel();

        String cols = "pref, 5dlu, fill:pref:grow";
        String rows = "pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref";
        FormLayout layout = new FormLayout(cols, rows);

        p1.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 1;
        
        p1.add(useEpsApprox, cc.xyw(1, row, 3));
        
        row += 2;
        
        p1.add(new JLabel("Epsilon:"),     cc.xy(1, row));
        p1.add(jtfEps, cc.xy(3, row));        
        
        row += 2;
        
        p1.add(tryToSimplify, cc.xyw(1, row, 3));
        
        row += 2;
        
        p1.add(new JLabel("Number of digits to assure:"),     cc.xy(1, row));
        p1.add(jtfEps, cc.xy(3, row));        
        
        row += 2;
        
        p1.add(verbose, cc.xyw(1, row, 3));  

        add(p1);
    }


    public void setProperties() throws ValidationException {
       	conf.getGeneralConfig().setVerbose(verbose.isSelected());
       	conf.getGeneralConfig().setUseEpsApprox(useEpsApprox.isSelected());
       	try {
        	double eps = Double.parseDouble(jtfEps.getText());
        	conf.getGeneralConfig().setEps(eps);
        } catch (NumberFormatException e) {
        	JOptionPane.showMessageDialog(this, "\""+jtfEps.getText()+"\" is not a valid double for epsilon.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
       	conf.getGeneralConfig().setSimplify(tryToSimplify.isSelected());
       	try {
        	double eps = Double.parseDouble(jtfEps.getText());
        	conf.getGeneralConfig().setDigits2(eps);
        } catch (NumberFormatException e) {
        	JOptionPane.showMessageDialog(this, "\""+jtfEps.getText()+"\" is not a valid double for epsilon.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
    }


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==useEpsApprox) {
			jtfEps.setEnabled(useEpsApprox.isSelected());
		}
	}
}
