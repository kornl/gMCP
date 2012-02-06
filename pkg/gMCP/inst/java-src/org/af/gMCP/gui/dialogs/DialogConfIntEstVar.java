package org.af.gMCP.gui.dialogs;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.af.commons.widgets.validate.RealTextField;
import org.af.commons.widgets.validate.ValidationException;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.graph.NetList;
import org.af.gMCP.gui.graph.Node;
import org.mutoss.gui.JavaGDPanel;

public class DialogConfIntEstVar extends JDialog implements ActionListener, ChangeListener, DocumentListener {
	
	List<JLabel> names = new Vector<JLabel>();
	List<JLabel> alphaLabel = new Vector<JLabel>();
	List<JLabel> ci = new Vector<JLabel>();
	List <JSpinner> df = new Vector<JSpinner>();
	List <RealTextField> est = new Vector<RealTextField>();
	List <RealTextField> var = new Vector<RealTextField>();
	List <JComboBox> alt = new Vector<JComboBox>();
	List <JComboBox> dist = new Vector<JComboBox>();
	
	String[] dists = { "normal-distributed", "t-distributed" };
	String[] alternatives = { /*"two.sided",*/ "less", "greater" };
	JButton jbLoadEst = new JButton("Load mean from R");
	JButton jbLoadSD = new JButton("Load sd from R");
	
	NetList nl;	
	JFrame p;
	
	Configuration conf;
	
	public DialogConfIntEstVar(JFrame p, NetList nl, boolean[] rejected, double[] alpha) {		
		super(p, "Confidence intervals", true);
		this.p = p;
		this.nl = nl;
		this.alpha = alpha;
		this.rejected = rejected;
		
		conf = Configuration.getInstance();
				
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;	
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=5; c.ipady=5;
		c.weightx=1; c.weighty=1;
		
		getContentPane().setLayout(new GridBagLayout());
		
		getContentPane().add(new JScrollPane(getPanel()), c);
		
		c.gridy++;
		
		c.weightx=0;c.weighty=0;
		JLabel label = new JLabel("Confidence Intervals:");		
		getContentPane().add(label, c);
		c.weightx=1;c.weighty=1;
		
		c.gridy++;
		
		RControl.getR().eval("JavaGD()");
		
		getContentPane().add(new JScrollPane(getCIPanel()), c);
		
		c.gridy++;
		c.weighty=1;
		
		getContentPane().add(new JavaGDPanel(), c);
		
		pack();
		setLocationRelativeTo(p);
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
		
		for (int i=0; i<nl.getNodes().size(); i++) {
			Node node = nl.getNodes().get(i);
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
	
	boolean[] rejected;
	double[] alpha;	

	private void calculateCI() {
		
		int n = nl.getNodes().size();
		double[] rLB = new double[n];
		double[] rUB = new double[n];
		double[] rEst = new double[n];
		
		for (int i=0; i<n; i++) {
			Double lb, ub;
			String d1 = "qnorm(";
			String d2 = ",)";			

			if (dist.get(i).getSelectedItem().equals(dists[1])) {
				d1 = "qt(";
				d2 = ","+Integer.parseInt(df.get(i).getValue().toString())+")";
			}

			conf.setClassProperty(this.getClass(), "alternative", alt.get(i).getSelectedItem().toString());
			if (alt.get(i).getSelectedItem().equals("greater")) {	
				lb = RControl.getR().eval(d1+alpha[i]+d2).asRNumeric().getData()[0];				
				ub = Double.POSITIVE_INFINITY;				
			} else if (alt.get(i).getSelectedItem().equals("less")) {
				lb = Double.NEGATIVE_INFINITY;
				ub = RControl.getR().eval(d1+(1-alpha[i])+d2).asRNumeric().getData()[0];				
			} else {
				lb = RControl.getR().eval(d1+alpha[i]/2+d2).asRNumeric().getData()[0];
				ub = RControl.getR().eval(d1+(1-alpha[i]/2)+d2).asRNumeric().getData()[0];
			}

			try {
				Double ste = var.get(i).getValidatedValue();
				Double pEst = est.get(i).getValidatedValue();				
				double lb2 = pEst+lb*ste;
				double ub2 = pEst+ub*ste;
				if (alt.get(i).getSelectedItem().equals("greater")) {
					if (rejected[i]) {
						lb2 = Math.max(lb2, 0);
					}
				} else if (alt.get(i).getSelectedItem().equals("less")) {
					if (rejected[i]) {
						ub2 = Math.min(ub2, 0);
					}
				}
				rLB[i]  = lb2;
				rUB[i]  = ub2;
				rEst[i] = pEst;
				ci.get(i).setText("]"+format.format(lb2)+","+format.format(ub2)+"[");
			} catch (ValidationException e) {
				ci.get(i).setText("Please specify a real decimal number for the estimate!");
			}
		}
		
		RControl.getR().eval("plotSimCI(cbind("+RControl.getRString(rLB)+", "
				+RControl.getRString(rEst)+", "
				+RControl.getRString(rUB)+"))");
	}

	DecimalFormat format = Configuration.getInstance().getGeneralConfig().getDecFormat();
	
	public JPanel getPanel() {
		
		JPanel panel = new JPanel();
		
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;	
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=5; c.ipady=5;
		c.weightx=1; c.weighty=1;
		
		panel.setLayout(new GridBagLayout());		
		
		panel.add(new JLabel("Hypotheses"), c);
		c.gridx++;
		panel.add(new JLabel("Initial weights"), c);
		c.gridx++;
		panel.add(new JLabel("Estimate"), c);
		c.gridx++;
		panel.add(new JLabel("Standard error/deviation"), c);
		c.gridx++;		
		panel.add(new JLabel("Distribution"), c);
		c.gridx++;
		panel.add(new JLabel("df"), c);
		c.gridx++;
		panel.add(new JLabel("Alternative"), c);
		c.gridy++;
		
		for (int i = 0; i < nl.getNodes().size(); i++) {
			Node node = nl.getNodes().get(i);
			c.gridx=0;
			
			JLabel hypothesis = new JLabel(node.getName()+":");			
			names.add(hypothesis);
			panel.add(hypothesis, c);
			c.gridx++;
			
			JLabel alpha = new JLabel(format.format(node.getWeight()));
			this.alphaLabel.add(alpha);
			panel.add(alpha, c);
			c.gridx++;
			
			RealTextField estimate = new RealTextField("Point estimate");
			estimate.setColumns(8);
			estimate.setText("0");
			estimate.getDocument().addDocumentListener(this);
			est.add(estimate);
			panel.add(estimate, c);
			c.gridx++;	
						
			RealTextField ste = new RealTextField("Standard error");
			ste.setColumns(8);
			ste.setText("1");
			ste.getDocument().addDocumentListener(this);
			var.add(ste);
			panel.add(ste, c);
			c.gridx++;
			
			JComboBox dist = new JComboBox(dists);
			dist.addActionListener(this);
			this.dist.add(dist);			
			panel.add(dist, c);			
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
			alt.setSelectedIndex(conf.getClassProperty(this.getClass(), "alternative", "less").equals("less")?0:1);
			alt.addActionListener(this);
			this.alt.add(alt);			
			panel.add(alt, c);			
			c.gridx++;			
			
			c.gridy++;
		}
		
		c.gridx=0;
		c.gridx++;
		c.gridx++;
		panel.add(jbLoadEst, c);
		jbLoadEst.addActionListener(this);
		c.gridx++;
		panel.add(jbLoadSD, c);
		jbLoadSD.addActionListener(this);
		c.gridx++;
		
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource().getClass().equals(JComboBox.class)) {
			int i = dist.indexOf(e.getSource());
			if (i != -1) {			 
				df.get(i).setEnabled(dist.get(i).getSelectedItem().equals(dists[1]));				
			}		
		} else if (e.getSource().getClass().equals(JButton.class)) {
			VariableNameDialog vnd = new VariableNameDialog(p, "");     
			try {
				double[] data = RControl.getR().eval(vnd.getName()).asRNumeric().getData();
				if (data.length!=names.size()) {
					JOptionPane.showMessageDialog(this, "Number of hypotheses and values do not match.", 
							"Number of hypotheses and values do not match", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (jbLoadEst.equals(e.getSource())) {
					for (int i=0; i<data.length; i++) {
						est.get(i).setText(""+data[i]);
					}
				} else if (jbLoadSD.equals(e.getSource())) {
					for (int i=0; i<data.length; i++) {
						var.get(i).setText(""+data[i]);
					}
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Error loading values from R:\n"+ex.getMessage(), 
						"Error loading values from R", JOptionPane.ERROR_MESSAGE);
			}        	
		}
		calculateCI();
	}

	public void stateChanged(ChangeEvent e) {
		calculateCI();	
	}

	public void changedUpdate(DocumentEvent e) {
		calculateCI();		
	}

	public void insertUpdate(DocumentEvent e) {
		calculateCI();			
	}

	public void removeUpdate(DocumentEvent e) {
		calculateCI();			
	}

}
