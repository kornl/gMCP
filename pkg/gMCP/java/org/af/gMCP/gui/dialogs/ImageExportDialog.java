package org.af.gMCP.gui.dialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.graph.GraphView;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class ImageExportDialog extends JDialog implements ActionListener {

	JButton ok = new JButton("Save file");
	JTextField tfFile = new JTextField();
	JCheckBox cbColored = new JCheckBox();
	JCheckBox cbTransparent = new JCheckBox();
	JButton selectFile = new JButton("Choose file:");
	ImagePanel ip;
	
	
	CreateGraphGUI parent;
	GraphView control;
	
	public ImageExportDialog(CreateGraphGUI parent) {
		super(parent, "Export Image", true);
		setLocationRelativeTo(parent);
		this.parent = parent;
		control = parent.getGraphView();		
		
        String cols = "5dlu, pref, 5dlu, fill:pref:grow, 5dlu, pref, 5dlu";
        String rows = "5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu";    
               
        FormLayout layout = new FormLayout(cols, rows);
        getContentPane().setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 2;
        
        ip = new ImagePanel(getImage());
        
        getContentPane().add(ip, cc.xyw(2, row, 3));
                
        row += 2;
        
        cbColored = new JCheckBox("Colored graph");
        cbColored.addActionListener(this);
        cbColored.setSelected(Configuration.getInstance().getGeneralConfig().getColoredImages());
        getContentPane().add(cbColored, cc.xyw(2, row, 3));        
        
        row += 2;
        
        cbTransparent = new JCheckBox("Transparent background (recommended)");
        cbTransparent.addActionListener(this);
        cbTransparent.setSelected(Configuration.getInstance().getGeneralConfig().exportTransparent());
        getContentPane().add(cbTransparent, cc.xyw(2, row, 3));     
        
        row += 2;
        selectFile.addActionListener(this);
        tfFile.setText(Configuration.getInstance().getClassProperty(this.getClass(), "LastImage", ""));
        getContentPane().add(selectFile, cc.xy(2, row));
        getContentPane().add(tfFile, cc.xyw(4, row, 3));
        
        row += 2;
        
        getContentPane().add(ok, cc.xy(6, row));
        ok.addActionListener(this);        
        
        pack();
        setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==ok) {	
			File f = new File(tfFile.getText());
			if (f.exists()) {
				int answer = JOptionPane.showConfirmDialog(this, "File exists. Overwrite file?", "File exists", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (answer==JOptionPane.NO_OPTION) return;
			}
			if (f.isDirectory()) {
				JOptionPane.showMessageDialog(this, "You only specified a directory.\nPlease enter also a file name for saving the image.", "Can not save file", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if (f.getParentFile()==null || !f.getParentFile().exists()) {
				JOptionPane.showMessageDialog(this, "You either did not specify a file or the specified parent directory does not exist.\nPlease choose another file for saving the image.", "Can not save file", JOptionPane.ERROR_MESSAGE);
				return;
			}
            control.saveGraphImage(f);
            parent.getMBar().showFile(f);
			dispose();
		} else if (e.getSource()==cbColored) {
			Configuration.getInstance().getGeneralConfig().setColoredImages(cbColored.isSelected());
		} else if (e.getSource()==cbTransparent) {
			Configuration.getInstance().getGeneralConfig().setExportTransparent(cbTransparent.isSelected());
		} else if (e.getSource()==selectFile) {
			JFileChooser fc = new JFileChooser(Configuration.getInstance().getClassProperty(this.getClass(), "ImageDirectory"));		
	        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
	        fc.setDialogType(JFileChooser.SAVE_DIALOG);
	        fc.setFileFilter(new FileFilter() {
				public boolean accept(File f) {
					if (f.isDirectory()) return true;
					return f.getName().toLowerCase().endsWith(".png");
				}
				public String getDescription () { return "PNG image files"; }  
			});
	        int returnVal = fc.showOpenDialog(this);
	        if (returnVal == JFileChooser.APPROVE_OPTION) {
	            File f = fc.getSelectedFile();
	            Configuration.getInstance().setClassProperty(this.getClass(), "ImageDirectory", f.getParent());
	            if (!f.getName().toLowerCase().endsWith(".png")) {
	            	f = new File(f.getAbsolutePath()+".png");
	            }
	            tfFile.setText(f.getAbsolutePath());
	            Configuration.getInstance().setClassProperty(this.getClass(), "LastImage", f.getAbsolutePath());
	        }		
		}
		ip.setImage(getImage());
		repaint();
	}
	
	public BufferedImage getImage() {
		//TODO Zoom? Configuration.getInstance().getGeneralConfig().getExportZoom();
		return control.getNL().getImage(1d, Configuration.getInstance().getGeneralConfig().getColoredImages());
	}
	
	public void save() {
		

		
	}

}
