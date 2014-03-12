package org.af.gMCP.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PowerOptionsPanel extends JPanel implements DocumentListener, ActionListener {

	CreateGraphGUI parent;
	
	public PowerOptionsPanel(CreateGraphGUI parent) {
		this.parent = parent;
		this.conf = Configuration.getInstance();

        makeComponents();
        doTheLayout();
	}

    private Configuration conf;
    private JTextField numberOfSimulations;
    private JComboBox randomNumbers;
    private JComboBox upscale;

    private void makeComponents() {
        numberOfSimulations = new JTextField(30);
        numberOfSimulations.setText(""+conf.getGeneralConfig().getNumberOfSimulations());
        numberOfSimulations.setToolTipText("<html>" +
        		"The Monte Carlo sample size for power calculations.<br>" +
        		"Default is 10000.</html>");
        numberOfSimulations.getDocument().addDocumentListener(this);
        
        randomNumbers = new JComboBox(new String[] {"quasirandom", "pseudorandom"});
        randomNumbers.setSelectedIndex(conf.getGeneralConfig().getTypeOfRandom().equals("quasirandom")?0:1);
        randomNumbers.setToolTipText("<html>" +
        		"You can select quasirandom or pseudorandom numbers for<br>" +
        		"power calculations. The quasirandom option uses a randomized<br>" +
        		"Lattice rule, and should be more efficient than the<br>" +
        		"pseudorandom option that uses ordinary (pseudo) random numbers.</html>");
        randomNumbers.addActionListener(this);
        
        upscale = new JComboBox(new String[] {"Yes", "No"});
        upscale.setSelectedIndex(conf.getGeneralConfig().getUpscale()?0:1);
        upscale.setToolTipText("<html>" +
        		"If 'No' is selected then for each intersection of hypotheses (i.e. each subgraph)<br>" +
        		"a weighted test is performed at the possibly reduced level alpha of sum(w)*alpha,<br>" + 
        		"where sum(w) is the sum of all node weights in this subset.<br>" +
        		"If 'Yes' is selected all weights are upscaled, so that sum(w)=1.<br>" +
        		"Please see the manual for a longer explanation and examples.</html>");
        upscale.addActionListener(this);
    }

    private void doTheLayout() {
        JPanel p1 = new JPanel();

        String cols = "pref, 5dlu, fill:pref:grow";
        String rows = "pref, 5dlu, pref, 5dlu, pref";
        FormLayout layout = new FormLayout(cols, rows);

        p1.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 1;
            
        p1.add(new JLabel("Monte Carlo sample size for power:"),     cc.xy(1, row));
        p1.add(numberOfSimulations, cc.xy(3, row));        
        
        row += 2;
        
        p1.add( new JLabel("Type of random numbers:"),     cc.xy(1, row));
        p1.add(randomNumbers, cc.xy(3, row));        
        
        row += 2;
        
        p1.add( new JLabel("Weights of subgraphs are upscaled to 1:"),     cc.xy(1, row));
        p1.add(upscale, cc.xy(3, row));        
        
        add(p1);
    }
    
    public void setProperties() {       	
       	try {
        	conf.getGeneralConfig().setNumberOfSimulations(Integer.parseInt(numberOfSimulations.getText()));
        } catch (NumberFormatException e) {
        	JOptionPane.showMessageDialog(this, "\""+numberOfSimulations.getText()+"\" is not a valid integer.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }
       	conf.getGeneralConfig().setTypeOfRandom(randomNumbers.getSelectedItem().toString());
       	conf.getGeneralConfig().setUpscale(upscale.getSelectedIndex()==0);
    }

	public void changedUpdate(DocumentEvent arg0) {
		setProperties();
	}

	public void insertUpdate(DocumentEvent arg0) {
		setProperties();
	}

	public void removeUpdate(DocumentEvent arg0) {
		setProperties();
	}

	public void actionPerformed(ActionEvent arg0) {
		setProperties();	
	}
	
}
