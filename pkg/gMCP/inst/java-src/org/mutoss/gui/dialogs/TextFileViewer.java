package org.mutoss.gui.dialogs;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.af.commons.io.FileTools;

public class TextFileViewer extends JDialog implements ActionListener {
	
	JTextArea jta;
	
	public TextFileViewer(JFrame p, File file) {		
		super(p, file.getName());	
		String news;
		try {
			news = FileTools.readFileAsString(file);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(p, "File \""+file.getAbsolutePath()+"\" not found!", "File not found", JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}
		
		jta = new JTextArea(news);
		jta.setFont(new Font("Monospaced", Font.PLAIN, 10));
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
				
		GridBagConstraints c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.BOTH;	
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=5; c.ipady=5;
		c.weightx=1; c.weighty=1;
		
		getContentPane().setLayout(new GridBagLayout());
		
		JScrollPane jsp = new JScrollPane(jta);
		//jsp.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		getContentPane().add(jsp, c);
				
		c.gridy++;
			
		pack();
		setSize(800,600);
		setLocationRelativeTo(p);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
		dispose();
	}

}
