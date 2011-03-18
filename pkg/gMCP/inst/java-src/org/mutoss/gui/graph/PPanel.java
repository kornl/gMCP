package org.mutoss.gui.graph;

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

import org.af.jhlir.call.RErrorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.config.Configuration;
import org.mutoss.gui.RControl;

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
		this.name = node.name;
		this.w = node.getAlpha();
		this.node = node;
		this.pview = pview;
        
        label = new JLabel(name);
		
		wTF = new JTextField(/*RControl.getFraction*/format.format(w), 7);
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
			keyTyped(null);
		}		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==jb) {
			reject();
			updateGraph();
		} else {
			keyTyped(null);
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
		node.vs.nl.acceptNode(node);
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
			w = RControl.getR().eval(wTF.getText().replace(",", ".")).asRNumeric().getData()[0];		
			wTF.setBackground(Color.WHITE);
		} catch (RErrorException nfe) {		
			wTF.setBackground(Color.RED);
		}
		node.setAlpha(w, this);
		//logger.info("P: "+p+", W: "+w);
		if (p<=w) {
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

	public void update() {
		DecimalFormat format = Configuration.getInstance().getGeneralConfig().getDecFormat();
		this.name = node.name;
		this.w = node.getAlpha();
		wTF.setText(format.format(w).replace(",", "."));		
		pTF.setText(format.format(p).replace(",", "."));
		if (!rejected) {
			keyTyped(null);
		}
	}

	public void updated(Node node) {
		DecimalFormat format = Configuration.getInstance().getGeneralConfig().getDecFormat();
		this.name = node.name;
		this.w = node.getAlpha();
		wTF.setText(format.format(w).replace(",", "."));		
		pTF.setText(format.format(p).replace(",", "."));	
		pview.updateLabels();
	}

	public double getP() {		
		return p;
	}

	public void setP(double p) {
		this.p = p;
		update();
	}	
	
	static boolean testing;
	
	public static void setTesting(boolean b) {
		testing = b;
	}

	@Override
	public void focusGained(FocusEvent e) {
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (e.getSource()==wTF) {
			wTF.setText(/*RControl.getFraction*/format.format(w));
		}
	}	
	
}
