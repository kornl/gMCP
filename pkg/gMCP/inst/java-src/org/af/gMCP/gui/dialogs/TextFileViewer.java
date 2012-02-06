package org.af.gMCP.gui.dialogs;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.af.commons.io.FileTools;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class TextFileViewer extends JDialog implements ActionListener {
	
	JTextArea jta;
	
	public TextFileViewer(JFrame p, File file) {		
		super(p, file.getName());	
		String text;
		try {
			text = FileTools.readFileAsString(file);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(p, "File \""+file.getAbsolutePath()+"\" not found!", "File not found", JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}
		setUp(text, null);		
	}
	
	public TextFileViewer(JFrame p, String title, String text) {
		super(p, title);
		setUp(text, null);
	}
	
	public TextFileViewer(JFrame p, String title, String text, String label) {
		super(p, title);
		setUp(text, label);
	}

	public void actionPerformed(ActionEvent e) {
		dispose();
	}
	
	private void setUp(String text, String label) {
		jta = new JTextArea(text);
		jta.setFont(new Font("Monospaced", Font.PLAIN, 12));
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		
		String cols = "5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        String rows = "5dlu, fill:200dlu:grow, 5dlu, pref, 5dlu, pref, 5dlu";
        
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();
		
		JScrollPane jsp = new JScrollPane(jta);
		//jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		getContentPane().add(jsp, cc.xyw(2, 2, 3));
		
		if (label!=null) {
			JTextArea jlabel = new JTextArea(label);
			jlabel.setOpaque(false);
			jlabel.setEditable(false);
			getContentPane().add(jlabel, cc.xyw(2, 4, 3));
		}
		
		JButton jb = new JButton("OK");
		jb.addActionListener(this);
		getContentPane().add(jb, cc.xy(4, 6));
		
		pack();
		setSize(800,600);
		setLocationRelativeTo(this.getParent());
		
		setVisible(true);
	}

}
