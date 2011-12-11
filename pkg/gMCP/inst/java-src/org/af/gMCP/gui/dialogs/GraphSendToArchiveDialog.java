package org.af.gMCP.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.af.commons.errorhandling.HTTPPoster;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.ErrorDialogGMCP;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class GraphSendToArchiveDialog extends JDialog implements ActionListener {
	JButton ok = new JButton("Load");
	JButton cancel = new JButton("Cancel");

    CreateGraphGUI parent;
    JTextArea jtInfo = new JTextArea(12, 40);
    
    Hashtable<String,String> table;
    Hashtable<String,File> files;
    String urlString;
    
    
	public GraphSendToArchiveDialog(CreateGraphGUI parent) {
		super(parent, "Select an R object to load", true);
		this.parent = parent;

				
        String cols = "5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu, fill:pref:grow, 5dlu";
        String rows = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;

        getContentPane().add(new JLabel("Graph objects"), cc.xy(2, row));
        getContentPane().add(new JLabel("Quadratic matrices"), cc.xy(4, row));
        getContentPane().add(new JLabel("Object info"), cc.xy(6, row));

        row += 2;
        
        getContentPane().add(new JScrollPane(jtInfo), cc.xy(6, row));

        row += 2;
                        
        getContentPane().add(ok, cc.xy(4, row));
        ok.addActionListener(this);        

        pack();
        setSize(760,500);
		setLocationRelativeTo(parent);
        setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			(new HTTPPoster()).post(urlString, table, files);
		} catch (IOException e1) {
			new ErrorDialogGMCP("An error occured submitting the graph.", e1, false);
		}
		dispose();
	}

}