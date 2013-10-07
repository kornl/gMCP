package org.af.gMCP.gui.options;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.af.commons.widgets.validate.ValidationException;
import org.af.gMCP.config.Configuration;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * OptionsPanel for miscellaneous settings.
 */
public class MiscPanel extends OptionsPanel { 


    private JCheckBox checkOnlineForUpdate;
    private JCheckBox exportTransparent;
    private JCheckBox unanchorEdges;
    private JCheckBox focusEqualsEdit;
    private JCheckBox enableNewFeatures;
    private JCheckBox showRCode;
    
    private Configuration conf;


    public MiscPanel(Configuration conf) {
        this.conf = conf;

        makeComponents();
        doTheLayout();
    }


    private void makeComponents() {
        checkOnlineForUpdate = new JCheckBox("Check online for updates");
        checkOnlineForUpdate.setSelected(conf.getGeneralConfig().checkOnline());
        checkOnlineForUpdate.setToolTipText("<html>On start-up gMCP can check automatically whether a new version<br>\n" +
        		"of gMCP is available. Only your version of R (like 2.13.1),<br>\n" +
        		"the version of gMCP (like 0.7-5) and a random number<br>\n" +
        		"(to distinguish different requests) are transmitted.</html>");
        exportTransparent = new JCheckBox("Export images with transparent background");
        exportTransparent.setSelected(conf.getGeneralConfig().exportTransparent());
        exportTransparent.setToolTipText("<html>If checked the background of exported PNG graphics will be transparent.<br>\n" +
        		"Otherwise the graphs are displayed on a white background.</html>");
        unanchorEdges = new JCheckBox("Edge weights should adjust their position, whenever a node is dragged");
        unanchorEdges.setSelected(conf.getGeneralConfig().getUnAnchor());
        unanchorEdges.setToolTipText("<html>If selected the edges will always repositioned whenever a node is dragged.<br>\n" +
        		"Otherwise only newly added eges behave that way and edges that have<br>\n" +
        		"been dragged themselves are considered \"anchored\" and will stay<br>\n" +
        		"with the edge weight label at the same position.</html>");
        focusEqualsEdit = new JCheckBox("Automatically enter the editing mode, whenever a table cell gets the focus");
        focusEqualsEdit.setSelected(conf.getGeneralConfig().focusEqualsEdit());
        focusEqualsEdit.setToolTipText("<html>People are used to different behaviour of tables (mostly<br>"+
    "depending on which spreadsheet applications they use regularly). If this<br>"+
    "option is set to true it is easy to change the values of the cells but<br>"+
    "navigating with arrow keys is hard since in the editing mode the right and<br>"+
    "left key will move the cursor only in the currently selected cell.</html>");
        enableNewFeatures = new JCheckBox("Enable highly experimental features");
        enableNewFeatures.setSelected(conf.getGeneralConfig().experimentalFeatures());
        enableNewFeatures.setToolTipText("<html>The gMCP GUI often contains new features that are not that well tested.<br>" +
        		"If you want to use or take a look at them, activate this option.<br>" +
        		"But be prepared that things might go wrong.</html>");
        showRCode = new JCheckBox("Show R code to reproduce results in R");
        showRCode.setSelected(conf.getGeneralConfig().showRCode());
        showRCode.setToolTipText("<html>After performing a test in the GUI the result dialog does not only show the pure<br>"
        		+ "results, but also R code to reproduce these results in R. If you are not interested in this feature,<br>"
        		+ "you can disable it here.</html>");
    }

    private void doTheLayout() {
        JPanel p1 = new JPanel();

        String cols = "pref, 5dlu, fill:pref:grow";
        String rows = "pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref";
        FormLayout layout = new FormLayout(cols, rows);

        p1.setLayout(layout);
        CellConstraints cc = new CellConstraints();

        int row = 1;
        
        p1.add(checkOnlineForUpdate, cc.xyw(1, row, 3));
        
        row += 2;

        p1.add(exportTransparent, cc.xyw(1, row, 3));
        
        row += 2;
        
        p1.add(unanchorEdges, cc.xyw(1, row, 3));
        
        row += 2;
        
        p1.add(focusEqualsEdit, cc.xyw(1, row, 3));
        
        row += 2;	

        p1.add(enableNewFeatures, cc.xyw(1, row, 3));
        
        row += 2;	
        
        p1.add(showRCode, cc.xyw(1, row, 3));
        
        row += 2;	
        
        add(p1);
    }


    public void setProperties() throws ValidationException {
       	conf.getGeneralConfig().setCheckOnline(checkOnlineForUpdate.isSelected());
       	conf.getGeneralConfig().setExportTransparent(exportTransparent.isSelected());
       	conf.getGeneralConfig().setUnAnchor(unanchorEdges.isSelected());
       	conf.getGeneralConfig().setFocusEqualsEdit(focusEqualsEdit.isSelected());
       	conf.getGeneralConfig().setExperimental(enableNewFeatures.isSelected());
       	conf.getGeneralConfig().setShowRCode(showRCode.isSelected());
    }
}
