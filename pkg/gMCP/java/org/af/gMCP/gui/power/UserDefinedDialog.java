package org.af.gMCP.gui.power;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
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

public class UserDefinedDialog extends JDialog implements ActionListener {
	

    UserDefinedPanel udp;
    JButton ok = new JButton("OK");

	
	public UserDefinedDialog(PDialog pd) {
		super(pd, "User Defined Power Function");
		setLocationRelativeTo(pd);
		
		List<Node> nodes = pd.getNodes();

        String cols = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        String rows = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        
        getContentPane().setLayout(new FormLayout(cols, rows));
        CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		udp = new UserDefinedPanel(pd, pd.getNodes(), true);		
		getContentPane().add(udp, cc.xyw(2, row, 3));
		
		row +=2;

		ok.addActionListener(this);
		getContentPane().add(ok, cc.xy(4, row));
		
		pack();
		setVisible(true);
		
	}
	
	public void actionPerformed(ActionEvent e) {

	}

	    
}
