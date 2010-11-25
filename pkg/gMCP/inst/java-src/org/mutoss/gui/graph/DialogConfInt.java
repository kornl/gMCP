package org.mutoss.gui.graph;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
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
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.mutoss.gui.RControl;

public class DialogConfInt extends JDialog implements ActionListener, ChangeListener, DocumentListener {
	
	ControlMGraph control;
	List<JLabel> names = new Vector<JLabel>();
	List<JLabel> alpha = new Vector<JLabel>();
	List<JLabel> ci = new Vector<JLabel>();
	List <JSpinner> df = new Vector<JSpinner>();
	List <JComboBox> alt = new Vector<JComboBox>();
	List <JComboBox> dist = new Vector<JComboBox>();
	JButton jbAttach = new JButton("Attach to Report");
	
	String[] dists = { "normal-distributed", "t-distributed" };
	String[] alternatives = { "less", "greater" };
	NetList nl;	
	
	public DialogConfInt(JFrame p, ControlMGraph control) {		
		super(p, "Confidence intervals", true);
		setLocationRelativeTo(p);
		this.control = control;
		this.nl = control.getNL();
				
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;	
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=5; c.ipady=5;
		c.weightx=1; c.weighty=0;
		
		getContentPane().setLayout(new GridBagLayout());
		
		getContentPane().add(new JScrollPane(getPanel()), c);
		
		c.fill = GridBagConstraints.BOTH;
		
		c.gridy++;
		
		JLabel label = new JLabel("Confidence Intervals:");		
		getContentPane().add(label, c);
		
		c.gridy++;
		
		getContentPane().add(new JScrollPane(getCIPanel()), c);
		
		c.gridy++;
		
		c.fill = GridBagConstraints.NONE;
		getContentPane().add(jbAttach, c);
		jbAttach.addActionListener(this);
		
		//getContentPane().add(new ConfIntPlot(this), c);
		
		pack();
		setVisible(true);
	}
	
	private JPanel getCIPanel() {
		JPanel panel = new JPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;	
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=5; c.ipady=5;
		c.weightx=1; c.weighty=1;
		
		panel.setLayout(new GridBagLayout());	
		
		for (int i=0; i<control.getNL().getKnoten().size(); i++) {
			Node node = control.getNL().getKnoten().get(i);
			c.gridx=0;
			
			JLabel hypothesis = new JLabel(node.getName()+":");			
			panel.add(hypothesis, c);
			c.gridx++;
			
			JLabel ci = new JLabel("]"+format.format(Double.NEGATIVE_INFINITY)+","+format.format(Double.POSITIVE_INFINITY)+"[");			
			this.ci.add(ci);
			panel.add(ci, c);
			c.gridx++;
			
			c.gridy++;
			
		}

		calculateCI();
		
		return panel;
	}

	private void calculateCI() {
		for (int i=0; i<nl.getKnoten().size(); i++) {
			Node node = nl.getKnoten().get(i);
			Double lb, ub;
			String d1 = "qnorm(";
			String d2 = ",)";
			Double ste = 1.0;
			Double pEst;

			if (dist.get(i).getSelectedItem().equals(dists[1])) {
				d1 = "qt(";
				d2 = ","+Integer.parseInt(df.get(i).getValue().toString())+")";
			}

			if (alt.get(i).getSelectedItem().equals("greater")) {	
				lb = RControl.getR().eval(d1+node.getAlpha()+d2).asRNumeric().getData()[0];				
				ub = Double.POSITIVE_INFINITY;
				pEst = RControl.getR().eval(d1+(1-control.getPView().getPValue(node))+d2).asRNumeric().getData()[0];		
			} else {
				lb = Double.NEGATIVE_INFINITY;
				ub = RControl.getR().eval(d1+(1-node.getAlpha())+d2).asRNumeric().getData()[0];
				pEst = RControl.getR().eval(d1+control.getPView().getPValue(node)+d2).asRNumeric().getData()[0];
			}				
			ci.get(i).setText("]"+format.format(pEst+lb*ste)+","+format.format(pEst+ub*ste)+"[");
		}
	}

	DecimalFormat format = new DecimalFormat("#.###");
	
	public JPanel getPanel() {
		
		JPanel panel = new JPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;	
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=5; c.ipady=5;
		c.weightx=1; c.weighty=1;
		
		panel.setLayout(new GridBagLayout());		
		
		for (Node node : control.getNL().getKnoten()) {
			c.gridx=0;
			
			JLabel hypothesis = new JLabel(node.getName()+":");			
			names.add(hypothesis);
			panel.add(hypothesis, c);
			c.gridx++;
			
			JLabel alpha = new JLabel("α="+format.format(node.getAlpha()).replace(",","."));
			this.alpha.add(alpha);
			panel.add(alpha, c);
			c.gridx++;
			
			JComboBox dist = new JComboBox(dists);
			dist.addActionListener(this);
			this.dist.add(dist);			
			panel.add(dist, c);			
			c.gridx++;			

			panel.add(new JLabel("Degree of freedom:"), c);
			c.gridx++;
			
			SpinnerModel dfModel = new SpinnerNumberModel(9, 1, Integer.MAX_VALUE, 1);
			JSpinner df = new JSpinner(dfModel);
			((JSpinner.DefaultEditor)df.getEditor()).getTextField().setColumns(4);
			df.setEnabled(dist.getSelectedItem().equals(dists[1]));
			this.df.add(df);
			df.addChangeListener(this);
			c.weightx=0;
			panel.add(df, c);
			c.weightx=1;
			c.gridx++;
			
			JComboBox alt = new JComboBox(alternatives);
			alt.addActionListener(this);
			this.alt.add(alt);			
			panel.add(alt, c);			
			c.gridx++;
			
			c.gridy++;
		}
		
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==jbAttach) {			
			//control.getNL().attachCI(calculateCI());
			dispose();
			return;
		}
		int i = dist.indexOf(e.getSource());
		if (i != -1) {			 
			df.get(i).setEnabled(dist.get(i).getSelectedItem().equals(dists[1]));				
		}
		calculateCI();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		calculateCI();	
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		calculateCI();		
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		calculateCI();			
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		calculateCI();			
	}

}
