package org.af.gMCP.gui.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.af.gMCP.gui.RControl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PPanel implements ActionListener, KeyListener, NodeListener, FocusListener {
	
	private static final Log logger = LogFactory.getLog(PPanel.class);
	
	double p = 0;
	double w;
	String name;
	
	JLabel label;
	JTextField wTF;
	JTextField pTF;
	JButton jb;
	
	Node node;
	PView pview;
	Boolean rejected = false;
	DecimalFormat format = new DecimalFormat("#.######");//Configuration.getInstance().getGeneralConfig().getDecFormat();
	
	public Vector<Component> getComponent() {
		Vector<Component> v = new Vector<Component>();
		v.add(label);
		v.add(wTF);
		v.add(pTF);
		v.add(jb);
		return v;
	}
	
	public PPanel(Node node, PView pview) {		
		node.addNodeListener(this);
		this.name = node.getName();
		this.w = node.getWeight();
		this.node = node;
		this.pview = pview;
        
        label = new JLabel(name);
		
		wTF = new JTextField(RControl.getFraction(w), 7);
		wTF.addActionListener(this);
		wTF.addFocusListener(this);
		wTF.addKeyListener(this);
		
		pTF = new JTextField(format.format(p), 7);
		pTF.addActionListener(this);
		pTF.addKeyListener(this);
		
		jb = new JButton("Reject and pass α");
		jb.setEnabled(false);
		jb.addActionListener(this);
		if (node.isRejected()) {
			reject();
		} else {
			updateMe(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==jb) {
			reject();
			updateGraph();
		} else {
			updateMe(false);
		}
	}

	private void reject() {
		wTF.setEnabled(false);
		pTF.setEnabled(false);
		jb.setEnabled(false);
		label.setText(label.getText()+" rejected!");
		label.setForeground(new Color(0,100,0));
		node.setColor(new Color(50,255,50));
		rejected = true;
	}

	private void updateGraph() {
		node.nl.acceptNode(node);
		pview.recalculate();
	}

	public void keyPressed(KeyEvent e) {keyTyped(e);}

	public void keyReleased(KeyEvent e) {keyTyped(e);}

	public void keyTyped(KeyEvent e) {
		try {
			p = Double.parseDouble(pTF.getText().replace(",", "."));
			pTF.setBackground(Color.WHITE);
		} catch (NumberFormatException nfe) {
			//logger.warn("Either \""+pTF.getText()+"\" or \""+pTF.getText()+"\" is no double number.");
			pTF.setBackground(Color.RED);
		}
		try {
			if (wTF.getText().length()!=0) { /* This if-clause is due to a bug/version conflict in JHLIR/REngine/rJava/R for R 2.8 */
				w = RControl.getR().eval(wTF.getText().replace(",", ".")).asRNumeric().getData()[0];		
				wTF.setBackground(Color.WHITE);
			} else {
				wTF.setBackground(Color.RED);
			}
		} catch (Exception nfe) {		
			wTF.setBackground(Color.RED);
		}
		node.setWeight(w, this);
		//logger.info("P: "+p+", W: "+w);
		updateMe(false);
	}

	void updateMe(boolean setText) {
		if (setText) {
			wTF.setText(getWString());
			pTF.setText(format.format(p).replace(",", "."));
		}
		if (p<=w*pview.getTotalAlpha()) {
			//logger.debug("Is "+p+"<="+w+"*"+pview.getTotalAlpha()+"?");
			node.setColor(new Color(50, 255, 50));
			wTF.setBackground(new Color(50, 255, 50));
			if (testing) {
				jb.setEnabled(true);
			} else  {
				jb.setEnabled(false);
			}
		} else {
			node.setColor(Color.WHITE);
			wTF.setBackground(Color.WHITE);
			jb.setEnabled(false);
		}
		if (testing) {
			wTF.setEditable(false);
			pTF.setEditable(false);
		} else {
			wTF.setEditable(true);
			pTF.setEditable(true);
		}
		pview.updateLabels();
	}

	public void updated(Node node) {		
		this.name = node.getName();
		this.w = node.getWeight();
		updateMe(true);
	}

	private String getWString() {	
		if (testing) {
			return format.format(w*pview.getTotalAlpha()).replace(",", ".");
		} else {
			return RControl.getFraction(w);
		}
	}

	public double getP() {		
		return p;
	}

	public void setP(double p) {
		this.p = p;
		updateMe(true);
	}	
	
	static boolean testing;
	
	public static void setTesting(boolean b) {
		testing = b;
	}

	@Override
	public void focusGained(FocusEvent e) {	}

	@Override
	public void focusLost(FocusEvent e) {
		if (wTF.isEditable()) {
			keyTyped(null);
			if (e.getSource()==wTF && !testing) {
				wTF.setText(RControl.getFraction(w));
			}
			updateMe(true);
		}
	}	

}
