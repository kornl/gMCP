package org.mutoss.gui.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.af.commons.Localizer;
import org.af.commons.widgets.WidgetFactory;
import org.af.commons.widgets.lists.IntegerJComboBox;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.config.Configuration;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * OptionsPanel for general settings.
 */
public class GeneralPanel extends OptionsPanel implements ActionListener {
    private static final Log logger = LogFactory.getLog(GeneralPanel.class);

    private IntegerJComboBox cbFontSize;
    private JComboBox cbLookAndFeel;
    //private JComboBox numberOfDigits;
    private JTextField jtfGrid;

    private JTextField tfPDFPath;
    
    private JButton jbPDFPath = new JButton(
            Localizer.getInstance().getString("SGTK_OPTIONS_GENERALPANEL_PATHTOREPORTSDIR")
    );
    
    private Configuration conf;
    private OptionsDialog odialog;
    private JCheckBox colorImages;
    private JCheckBox showRejected;
    //private JTextField numberOfDigits;
    
	JFrame parent;

    public GeneralPanel(JFrame parent, OptionsDialog odialog) {
        this.conf = Configuration.getInstance();
        this.odialog = odialog;
        this.parent = parent;

        makeComponents();
        doTheLayout();
    }

    /**
     * Instantiation of Swing-Components.
     */
    private void makeComponents() {
        cbFontSize = new IntegerJComboBox(8, 20);
        cbFontSize.setSelectedObject(conf.getGeneralConfig().getFontSize());
        tfPDFPath = new JTextField(30);
        tfPDFPath.setText(conf.getGeneralConfig().getProjectPDFsPath().getAbsolutePath()); 
        jtfGrid = new JTextField(30);
        jtfGrid.setText(""+conf.getGeneralConfig().getGridSize()); 
        
        Vector<String> looknfeel = new Vector<String>();
        looknfeel.add("System");
        looknfeel.add("Windows");
        looknfeel.add("Mac OS");        
        looknfeel.add("Motif");
        looknfeel.add("Metal");

        cbLookAndFeel = new JComboBox(looknfeel);
        logger.info("LooknFeel is " + conf.getJavaConfig().getLooknFeel() + ".");
        for (int i = 0; i < looknfeel.size(); i++) {
            cbLookAndFeel.setSelectedIndex(i);
            if (getLooknFeel().equals(conf.getJavaConfig().getLooknFeel())) break;
            logger.debug("Not " + getLooknFeel());
        }
        
        colorImages = new JCheckBox("Colored image files and pdf reports");
        colorImages.setSelected(conf.getGeneralConfig().getColoredImages());
        
        showRejected = new JCheckBox("Show rejected nodes in GUI");
        showRejected.setSelected(conf.getGeneralConfig().showRejected());
    }

    private void doTheLayout() {

        Localizer loc = Localizer.getInstance();
        JPanel p1 = new JPanel();
        String cols = "pref, 5dlu, fill:pref:grow";
        String rows = "pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref";
        
        FormLayout layout = new FormLayout(cols, rows);
        p1.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 1;
        
        p1.add(new JLabel("Grid:"),     cc.xy(1, row));
        p1.add(jtfGrid, cc.xy(3, row));        
        
        row += 2;
        
        p1.add(new JLabel(loc.getString("SGTK_OPTIONS_GENERALPANEL_FONTSIZE")),     cc.xy(1, row));
        p1.add(cbFontSize, cc.xy(3, row));
        
        row += 2;
        
        jbPDFPath.addActionListener(this);
        p1.add(jbPDFPath, cc.xy(1, row));
        p1.add(tfPDFPath, cc.xy(3, row));
        
        row += 2;

        p1.add(new JLabel(loc.getString("SGTK_OPTIONS_GENERALPANEL_LF")),           cc.xy(1, row));
        p1.add(cbLookAndFeel, cc.xy(3, row));
        
        row += 2;
        
        p1.add(colorImages, cc.xyw(1, row, 3));
        
        row += 2;        
        
        p1.add(showRejected, cc.xyw(1, row, 3));
        

        add(p1);
    }


    private String lfID2FullName(String id) {
        if (id.equals("Metal")) {
            return UIManager.getCrossPlatformLookAndFeelClassName();
        } else if (id.equals("System")) {
            return UIManager.getSystemLookAndFeelClassName();
        } else if (id.equals("Motif")) {
            return "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
        } else if (id.equals("Windows")) {
            return "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        } else if (id.equals("Mac OS")) {
            return "javax.swing.plaf.mac.MacLookAndFeel";
        }
        return null;
    }

    private String getLooknFeel() {
        String lf = lfID2FullName(cbLookAndFeel.getSelectedItem().toString());
        if (lf == null)
            logger.warn("No LooknFeel selected! How can this be?");
        return lf;
    }

    public void setProperties() throws SetLookAndFeelException {
        int fontSize = cbFontSize.getSelectedObject();
        conf.getGeneralConfig().setFontSize(fontSize);
        try {
        	int grid = Integer.parseInt(jtfGrid.getText());
        	conf.getGeneralConfig().setGridSize(grid);
        } catch (NumberFormatException e) {
        	JOptionPane.showMessageDialog(this, "\""+jtfGrid.getText()+"\" is not a valid integer for grid size.", "Invalid input", JOptionPane.ERROR_MESSAGE);
        }        
        conf.getGeneralConfig().setProjectPDFsPath(tfPDFPath.getText());
       	conf.getGeneralConfig().setColoredImages(colorImages.isSelected());
       	conf.getGeneralConfig().setShowRejected(showRejected.isSelected());
        
        try {
            LookAndFeel currentLF = UIManager.getLookAndFeel();
            logger.info("Selected LooknFeel:" + getLooknFeel());
            setLooknFeel(getLooknFeel());

            if (!getLooknFeel().equals(conf.getJavaConfig().getLooknFeel())) {
                int n = JOptionPane.showConfirmDialog(parent,
                        Localizer.getInstance().getString("SGTK_OPTIONS_GENERALPANEL_KEEPLF"),
                        Localizer.getInstance().getString("SGTK_OPTIONS_GENERALPANEL_KEEPLF"),
                        JOptionPane.YES_NO_OPTION);

                if (n == JOptionPane.YES_OPTION) {
                    conf.getJavaConfig().setLooknFeel(getLooknFeel());
                } else {
                    setLooknFeel(currentLF);
                }
            }
        } catch (Exception exc) {
            // look&feel exception
            //throw new SetLookAndFeelException(exc);
        	JOptionPane.showMessageDialog(parent, "The selected LooknFeel is not available.", "Selected LooknFeel not available.", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setLooknFeel(String id) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, UnsupportedLookAndFeelException {
        UIManager.setLookAndFeel(id);
        WidgetFactory.setFontSizeGlobal(conf.getGeneralConfig().getFontSize());
        SwingUtilities.updateComponentTreeUI(parent);
        SwingUtilities.updateComponentTreeUI(odialog);
        odialog.pack();
    }

    private void setLooknFeel(LookAndFeel lf) throws ClassNotFoundException, IllegalAccessException,
            InstantiationException, UnsupportedLookAndFeelException {
        setLooknFeel(lfID2FullName(lf.getID()));
    }

	public void actionPerformed(ActionEvent e) {
		JFileChooser fc = new JFileChooser();
		if (e.getSource()==jbPDFPath) {
        	fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);            	
        }	
		int returnVal = fc.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File f = fc.getSelectedFile();
            if (e.getSource()==jbPDFPath) {
            	tfPDFPath.setText(f.getAbsolutePath());            	
            }
        }		
	}
}

