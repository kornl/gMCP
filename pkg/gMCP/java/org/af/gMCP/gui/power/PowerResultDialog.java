package org.af.gMCP.gui.power;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;

import javax.swing.JDialog;

public class PowerResultDialog extends JDialog {
	
	public String NEW_CELL = "\t";
	public String NEW_LINE = "\n";    
	
	public void copyTableToClipboard() {
		StringBuffer s = new StringBuffer();
		
		StringSelection stringTable  = new StringSelection(s.toString()); 
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringTable, stringTable);
	}
	
	
}
