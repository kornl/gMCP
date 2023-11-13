package org.af.gMCP.gui.dialogs;

import org.af.commons.widgets.WidgetFactory;
import org.af.commons.widgets.buttons.OKButtonPane;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ShowLicense extends InfoDialog implements ActionListener {
	private static Log logger = LogFactory.getLog(ShowLicense.class);

	JButton jb = new JButton("Ok");
	
	public ShowLicense(JFrame mainFrame) {
		super(mainFrame, "License", true);

		getContentPane().setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		
		JTextArea jta3 = new JTextArea("gMCP - Graph based Multiple Comparison Procedures.\n"
				+"Copyright (C) 2009-2014 by K. Rohmeyer and F. Klinglmueller\n"
				+"\n"
				+"This program is free software; you can redistribute it and/or\n"
				+"modify it under the terms of the GNU General Public License\n"
				+"as published by the Free Software Foundation; either version 2\n"
				+"of the License, or (at your option) any later version.\n"
				+"\n"
				+"This program is distributed in the hope that it will be useful,\n"
				+"but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
				+"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
				+"GNU General Public License for more details. It is included\n" 
				+"in the R distribution (in directory share/licenses) or can be\n" 
				+"found at: http://www.gnu.org/licenses/\n");
		jta3.setFont(new Font("Monospaced", Font.PLAIN, 10));

		c.fill = GridBagConstraints.HORIZONTAL;		
		c.gridx=0; c.gridy=0;
		c.gridwidth = 1; c.gridheight = 1;
		c.ipadx=10; c.ipady=10;
		c.weightx=1; c.weighty=1;
		
		JScrollPane js = new JScrollPane(jta3);
		(getContentPane()).add(js, c);	
		
        Container cp = getContentPane();
        cp = WidgetFactory.makeDialogPanelWithButtons(cp, new OKButtonPane(), this);
		setContentPane(cp);
		
		pack();	
		
	    setLocationRelativeTo(mainFrame);
	    
		setVisible(true);		
	}
	
	public void actionPerformed(ActionEvent e) {
		dispose();
	}

}
