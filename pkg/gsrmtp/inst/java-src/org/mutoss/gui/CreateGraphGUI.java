package org.mutoss.gui;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.af.commons.errorhandling.ErrorDialog;
import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.logging.ApplicationLog;
import org.af.commons.logging.LoggingSystem;

public class CreateGraphGUI extends JFrame {
	public CreateGraphGUI(String graph) {
		super("MuToss GUI");
		//setIconImage((new ImageIcon(getClass().getResource("/org/mutoss/images/mutoss.png"))).getImage());
		
	}
	
	public static void main(String[] args) {
		new CreateGraphGUI("graph");
	}
}
