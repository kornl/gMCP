package org.af.gMCP.gui.dialogs;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.graph.Node;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PowerParameterPanel extends JPanel implements ActionListener {
	
	GridBagConstraints c = new GridBagConstraints();
	List<JTextField> tfl = new Vector<JTextField>(); 
	Vector<Node> nodes;
	CreateGraphGUI parent;
	JPanel panel = new JPanel();
	JButton newSetting = new JButton("Add setting");
	JButton loadRSetting = new JButton("Load settings from R");
	String setting;
	
	Double defaultValue;
	
	public PowerParameterPanel(String setting, Double defaultValue, Vector<Node> nodes, CreateGraphGUI parent) {

		this.nodes = nodes;
		this.defaultValue = defaultValue;
		this.parent = parent;
		this.setting = setting;
		
		String cols = "5dlu, fill:min:grow, 5dlu, fill:min:grow, 5dlu";        
        String rows = "5dlu, fill:min:grow, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        setLayout(layout);

        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        add(new JScrollPane(getPanel()), cc.xyw(2, row, 3));
        
        row += 2;
		
        add(newSetting, cc.xy(2, row));
		newSetting.addActionListener(this);
		add(loadRSetting, cc.xy(4, row));
		loadRSetting.addActionListener(this);
		
	}
	
	public JPanel getPanel() {
		c.fill = GridBagConstraints.BOTH;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=0;
		//c.insets = new Insets(4,4,4,4);
		
		panel.setLayout(new GridBagLayout());	
		
		panel.add(new JLabel("Setting "+(tfl.size()+1)), c);
		c.gridx++;
		JTextField tf = new JTextField(""+defaultVector());
		tfl.add(tf);
		panel.add(tf, c);
		c.gridx=0; c.gridy++;
		return panel;
	}
	
	public void addRow() {
		panel.add(new JLabel("Setting "+(tfl.size()+1)), c);
		c.gridx++;
		JTextField tf = new JTextField(""+defaultVector());
		tfl.add(tf);
		panel.add(tf, c);
		revalidate();
		repaint();
		c.gridx=0; c.gridy++;
	}
	
	DecimalFormat f = new DecimalFormat("#0.###"); 
	
	public String defaultVector() {
		String s = "c(";
		for (int i=1;i<nodes.size();i++) {
			s+=f.format(defaultValue)+", ";
		}
		return s+f.format(defaultValue)+")";
	}
	
	public void loadRObject() {
		VariableNameDialog vnd = new VariableNameDialog(parent);     
		try {
			double[] data = RControl.getR().eval(vnd.getName()).asRNumeric().getData();
			if (data.length!=nodes.size()) {
				JOptionPane.showMessageDialog(parent, "Number of hypotheses and values do not match.", 
						"Number of hypotheses and values do not match", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			
			
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error loading values from R:\n"+ex.getMessage(), 
					"Error loading values from R", JOptionPane.ERROR_MESSAGE);
		}	
	}

	public String getRList() {
		String s = "list(";
		for (int i=0; i<tfl.size()-1; i++) {
			s += tfl.get(i).getText()+",";
		}
		s += tfl.get(tfl.size()-1).getText()+")";
		return s;
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newSetting) {
			addRow();
		}
		if (e.getSource() == loadRSetting) {
			loadRObject(); 
		}
	}
	
}
