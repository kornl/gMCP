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
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.af.gMCP.gui.RControl;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PPanel implements ActionListener, KeyListener, NodeListener, FocusListener {
	
	private static final Log logger = LogFactory.getLog(PPanel.class);
	
	double p = 0;
	List<Double> w;
	String name;
	
	JLabel label;
	private List<JTextField> wTFList = new Vector<JTextField>();
	private JTextField pTF;
	JButton jb;
	
	Node node;
	PView pview;
	Boolean rejected = false;
	DecimalFormat format = new DecimalFormat("#.######");//Configuration.getInstance().getGeneralConfig().getDecFormat();
	
	public Vector<Component> getComponent() {
		Vector<Component> v = new Vector<Component>();
		v.add(label);
		for (JTextField wTF : wTFList) {
			v.add(wTF);
		}
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
		
        for (Double wd : w) {
        	JTextField wTF = new JTextField(RControl.getFraction(wd), 7);
        	wTF.addActionListener(this);
        	wTF.addFocusListener(this);
        	wTF.addKeyListener(this);
        	wTFList.add(wTF);
        }
		
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
			node.reject();			
		} else {
			updateMe(false);
		}
	}

	/**
	 *  
	 * @see org.af.gMCP.gui.graph.NodeListener#reject()
	 */
	public void reject() {
		for (JTextField wTF : wTFList) {
			wTF.setEnabled(false);
		}
		pTF.setEnabled(false);
		jb.setEnabled(false);
		label.setText(label.getText()+" rejected!");
		label.setForeground(new Color(0,100,0));		
		rejected = true;
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
		w = new Vector<Double>();
		for (JTextField wTF : wTFList) {
			try {
				if (wTF.getText().length()!=0) { /* This if-clause is due to a bug/version conflict in JHLIR/REngine/rJava/R for R 2.8 */
					double tempw = RControl.getR().eval(wTF.getText().replace(",", ".")).asRNumeric().getData()[0];		
					if (!Double.isInfinite(tempw) && !Double.isNaN(tempw)) {
						wTF.setBackground(Color.WHITE);
						w.add(tempw);
					} else {
						wTF.setBackground(Color.RED);
						return;
					}				
				} else {
					wTF.setBackground(Color.RED);
					return;
				}
			} catch (Exception nfe) {		
				wTF.setBackground(Color.RED);
				return;
			}	
		}
		node.setWeight(ArrayUtils.toPrimitive((Double[])w.toArray(new Double[0])), this);
		//logger.info("P: "+p+", W: "+w);
		updateMe(false);
	}

	/**
	 * Update the Panel, i.e.
	 * - calculate and show which nodes are rejectable
	 * - update the labels showing the total sum of weights
	 *   and possible warnings (like alpha or weight >1)
	 * - if (setText==true) set the p-values and weights
	 *   in the corresponding text fields. 
	 * @param setText Should the p-values and weights be updated in the corresponding text fields?
	 */
	void updateMe(boolean setText) {
		if (setText) {
			pTF.setText(format.format(p).replace(",", "."));
		}		
		if (testing) {
			pTF.setEditable(false);
		} else {
			pTF.setEditable(true);
		}
		for (int i=0; i<wTFList.size(); i++) {
			JTextField wTF = wTFList.get(i); 
			if (setText) {
				wTF.setText(getWString().get(i));
			}
			if (true) { //TODO p<=w*pview.getTotalAlpha()) {
				//logger.debug("Is "+p+"<="+w+"*"+pview.getTotalAlpha()+"?");
				node.setRejectable(true);
				wTF.setBackground(new Color(50, 255, 50));
				if (testing) {
					jb.setEnabled(true);
				} else  {
					jb.setEnabled(false);
				}
			} else {
				node.setRejectable(false);
				wTF.setBackground(Color.WHITE);
				jb.setEnabled(false);
			}
			if (testing) {
				wTF.setEditable(false);
			} else {
				wTF.setEditable(true);
			}
		}
		pview.updateLabels();
	}

	public void updated(Node node) {		
		this.name = node.getName();
		this.w = node.getWeight();
		updateMe(true);
	}

	private Vector<String> getWString() {
		Vector<String> result = new Vector<String>();
		for (double wd : w) {
			if (testing) {
				result.add(format.format(wd/**pview.getTotalAlpha()*/).replace(",", "."));
			} else {
				result.add(RControl.getFraction(wd));
			}		
		}
		return result;
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

	public void focusGained(FocusEvent e) {	}

	public void focusLost(FocusEvent e) {
		if (wTFList.get(0).isEditable()) {
			keyTyped(null);
			for (int i=0; i<wTFList.size(); i++) {
				JTextField wTF = wTFList.get(i); 
				if (e.getSource()==wTF && !testing) {
					wTF.setText(RControl.getFraction(w.get(i)));
				}
			}
			updateMe(true);
		}
	}

	public void addEntangledLayer() {
		JTextField wTF = new JTextField("0", 7);
    	wTF.addActionListener(this);
    	wTF.addFocusListener(this);
    	wTF.addKeyListener(this);
    	wTFList.add(wTF);	
    	w.add(0.0);
	}

	public void removeEntangledLayer(int layer) {
		wTFList.get(layer).removeActionListener(this);
		wTFList.remove(layer);
		/* We don't have to remove a value from variable w,
		 * since it will be updated via the NodeListener functionality.
		 */		
	}

}
