package org.af.gMCP.gui.power;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.af.gMCP.gui.graph.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class UserDefinedPanel extends JPanel implements ActionListener {
	
	List<JButton> buttons = new Vector<JButton>();
	List<JButton> buttons2 = new Vector<JButton>();  
	
	JTextField jtUserDefined = new JTextField();
	JButton addAnother = new JButton("Add another power function");    
    JButton clearList = new JButton("Clear");
    
	DefaultListModel listModel;
    JList listUserDefined;

    JTextArea jta = new JTextArea();
    
    JButton loadUDPF = new JButton("Load");
    JButton saveUDPF = new JButton("Save");

	
	public UserDefinedPanel(List<Node> nodes) {		
		
		JButton b = new JButton("(");
		b.setActionCommand("(");
		buttons.add(b);

		b = new JButton(")");
		b.setActionCommand(")");
		buttons.add(b);
		
		b = new JButton("AND");
		b.setActionCommand("&&");
		buttons.add(b);
		
		b = new JButton("OR");
		b.setActionCommand("||");
		buttons.add(b);
		
		b = new JButton("NOT");
		b.setActionCommand("!");		
		buttons.add(b);		
		
		for (int i=0; i<nodes.size(); i++) {
			b = new JButton(nodes.get(i).getName());
			b.setActionCommand("x["+(i+1)+"]");			
			buttons2.add(b);
		}
		
		JPanel hypPanel = new JPanel();
		for (JButton button : buttons2) {
			button.addActionListener(this);
			hypPanel.add(button);
		}
		
		JPanel opPanel = new JPanel();
		for (JButton button : buttons) {
			button.addActionListener(this);
			opPanel.add(button);
		}
		
		jta.setMargin(new Insets(4,4,4,4));
		jta.setText(
				"In the text field above you can enter an user defined power function.\n" +
				"Use the R syntax and \"x[i]\" to specify the proposition that hypothesis i\n"+
				"could be rejected. Alternatively use the buttons below.\n" +
				"Example:  (x[1] && x[2]) || x[4]\n" +
				"This calculates the probability that the first and second\n" +
				"or (not exclusive) the fourth null hypothesis can be rejected.\n"+
				/*"- if the test statistic follows a t-distribution, enter the non-centrality parameter µ*sqrt(n)/σ\n"+
				"  (µ=difference of real mean and mean under null hypothesis, n=sample size, σ=standard deviation)\n"+
				"- triangle(min, peak, max)\n"+
				"- rnorm(1, mean=0.5, sd=1)\n"+*/
				"Note that you can use all R commands, for example also\n"+
				"any(x) to see whether any hypotheses was rejected or\n" +
				"all(x[1:4]) to see whether all of the first four hypotheses were rejected.\n"+
				"Hit return to add another power function.");
		

        String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";
        
        setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		jtUserDefined.addActionListener(this);
		add(jtUserDefined, cc.xy(2, row));
		
		addAnother.addActionListener(this);
		add(addAnother, cc.xy(4, row));
		
		/*clearList.addActionListener(this);
		mPanel.add(addAnother, cc.xy(4, row));*/		
		
		row +=2;
		
		listModel = new DefaultListModel();
		listUserDefined = new JList(listModel);
		
		add(new JScrollPane(jta), cc.xywh(2, row, 1, 3));
	
		add(new JScrollPane(listUserDefined), cc.xy(4, row));
	
		row +=2;
		
		clearList.addActionListener(this);
		add(clearList, cc.xy(4, row));		
		//mPanel.add(saveUDPF, cc.xy(6, row));
		
		row +=2;		
				
		add(new JScrollPane(hypPanel), cc.xyw(2, row, 3));

		row +=2;
		
		add(new JScrollPane(opPanel), cc.xyw(2, row, 3));
		
	}
	
	/**
	 * Constructs String that contains the parameter f for user defined
	 * functions used by calcPower and extractPower
	 * @return String that contains the parameter f for user defined
	 * functions used by calcPower and extractPower. Either empty or
	 * of the form ", f=list(...)".
	 */
	String getUserDefined() {
		if (listModel.getSize()==0) return "";
		String s = ", f=list(";
		for (int i=0; i<listModel.getSize(); i++) {
			s +="userDefined"+i+"=function(x) {"+listModel.get(i)+"}";
			if (i!=listModel.getSize()-1) s+= ",";
		}		
		return s + ")";
	}

	public void actionPerformed(ActionEvent e) {
		if (buttons.contains(e.getSource()) || buttons2.contains(e.getSource())) {
			jtUserDefined.setText(jtUserDefined.getText()+" "+((JButton)e.getSource()).getActionCommand());
			return;
		}
		if (e.getSource() == clearList) {
			listModel.removeAllElements();
			return;
		}	
		if (jtUserDefined.getText().length()>0) {
			listModel.insertElementAt(jtUserDefined.getText(), 0);
			//listUserDefined.ensureIndexIsVisible(0);
			jtUserDefined.setText("");
		}
	}

	public Element getConfigNode(Document document) {
		Element e = document.createElement("powerfunctions");
		for (int i=0; i<listModel.getSize(); i++) {
			Element ef = document.createElement("userdefined");
			ef.setAttribute("expression", ""+listModel.get(i));
			e.appendChild(ef);
		}
		return e;
	}

	public void loadConfig(Element e) {
		NodeList nlist = e.getChildNodes();
		int i = nlist.getLength()-1;
		while(listModel.getSize()<nlist.getLength()) {
			listModel.insertElementAt(((Element)nlist.item(i)).getAttribute("expression"), 0);
			i--;
		}			
	}

	
	    
}
