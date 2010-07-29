package org.mutoss.gui.graph;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PPanel implements ActionListener, KeyListener, NodeListener {
	
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
		this.w = node.getW();
		this.node = node;
		this.pview = pview;
        
        label = new JLabel(name);
		
		wTF = new JTextField(""+w);
		wTF.addActionListener(this);
		wTF.addKeyListener(this);
		
		pTF = new JTextField(""+p);
		pTF.addActionListener(this);
		pTF.addKeyListener(this);
		
		jb = new JButton("Reject and pass α");
		jb.setEnabled(false);
		jb.addActionListener(this);
		keyTyped(null);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==jb) {
			wTF.setEnabled(false);
			pTF.setEnabled(false);
			jb.setEnabled(false);
			label.setText(label.getText()+" rejected!");
			label.setForeground(new Color(0,100,0));
			node.setColor(new Color(50,255,50));
			rejected = true;
			updateGraph();
		} else {
			keyTyped(null);
		}
	}

	private void updateGraph() {
		((MGraphListe)(node.vs.nl)).acceptNode(node);
		pview.recalculate();
	}

	public void keyPressed(KeyEvent e) {keyTyped(e);}

	public void keyReleased(KeyEvent e) {keyTyped(e);}

	public void keyTyped(KeyEvent e) {
		try  {
			p = Double.parseDouble(pTF.getText());
			wTF.setBackground(Color.WHITE);
		} catch (NumberFormatException nfe) {
			//logger.warn("Either \""+pTF.getText()+"\" or \""+pTF.getText()+"\" is no double number.");
			wTF.setBackground(Color.RED);
		}
		try  {
			w = Double.parseDouble(wTF.getText());		
			wTF.setBackground(Color.WHITE);
		} catch (NumberFormatException nfe) {		
			wTF.setBackground(Color.RED);
		}
		node.setW(w, this);
		//logger.info("P: "+p+", W: "+w);
		if (p<=w) {
			node.setColor(new Color(50, 255, 50));
			wTF.setBackground(new Color(50, 255, 50));
			jb.setEnabled(true);
		} else {
			node.setColor(Color.WHITE);
			wTF.setBackground(Color.WHITE);
			jb.setEnabled(false);
		}
		pview.updateLabels();
	}

	public void update() {
		if (!rejected) {
			this.name = node.name;
			this.w = node.getW();
			wTF.setText(""+w);		
			pTF.setText(""+p);	
			keyTyped(null);
		}
	}

	public void updated(Node node) {
		this.name = node.name;
		this.w = node.getW();
		wTF.setText(""+w);		
		pTF.setText(""+p);	
		pview.updateLabels();
	}	
	
}
