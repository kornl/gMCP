package org.af.statguitoolkit.gui.datawizard;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.af.commons.Localizer;
import org.af.commons.widgets.WidgetFactory;
import org.af.statguitoolkit.io.datasets.FileDescriptor;
import org.af.statguitoolkit.io.datasets.FileDescriptorCSV;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPanelNavResult;


import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class LoadCSVPanel extends LoadPanel {
	
	public static String getDescription() {
		return  Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_CSV_DESC");
	}

    private JCheckBox chbWhiteSpace;
    private JTextField tfFieldSep;

    DataWizard dataWizard;
    
    public LoadCSVPanel(DataWizard dataWizard) {
		this.dataWizard = dataWizard;
        makeComponents();
        doTheLayout();
	}

	/**
     * Instantiation of Swing-Components.
     */
    protected void makeComponents() {
        super.makeComponents();
        chbWhiteSpace = new JCheckBox("", false);
        chbWhiteSpace.setMargin(new Insets(0,0,0,0));
        chbWhiteSpace.setBorder(null);
        tfFieldSep = new JTextField(";", 1);
        WidgetFactory.registerDisabler(chbWhiteSpace, tfFieldSep);
    }

    protected void doTheLayout() {
    	
        String cols = "pref, 5dlu, pref:grow, 5dlu, left:pref, 5dlu, left:pref, 5dlu, left:pref";
        String rows = "pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref:grow";
        FormLayout layout = new FormLayout(cols, rows);

        setLayout(layout);
        CellConstraints cc = new CellConstraints();
        Localizer loc = Localizer.getInstance();
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_FILE")),       cc.xy  (1, 1));
        add(tfFile,                                                             cc.xyw (3, 1, 3));
        add(bSelect,                                                            cc.xyw (7, 1, 3));
        JScrollPane sp = new JScrollPane(taPreview);
        sp.setBorder(new TitledBorder(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_PREVIEW")));
        add(sp,                                                                 cc.xywh(1, 3, 3, 13));

        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_HEADER")),     cc.xy  (5, 5));
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_FIELDSEP")),   cc.xy  (5, 7));
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_MISSING")),    cc.xy  (5, 11));
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_DECPOINT")),   cc.xy  (5, 13));

        add(chbHeader,                                                          cc.xy  (7, 5));
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_WHITESPACE")), cc.xy  (7, 7));
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_OTHER")),      cc.xy  (7, 9));
        add(tfNA,                                                               cc.xy  (7, 11));
        add(tfDec,                                                              cc.xy  (7, 13));

        add(chbWhiteSpace,                                                      cc.xy  (9, 7));
        add(tfFieldSep,                                                         cc.xy  (9, 9));
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bSelect) {
            selectFile();
        }
    }


    private void selectFile() {
        super.selectFile(new String[]{"txt", "csv"});
    }


    protected List<String> getPreview(File file) {
        return readLines(file, 20);
    }

    
    public WizardPanelNavResult allowFinish(java.lang.String stepName, java.util.Map settings, Wizard wizard) {
    	if (tfFile.getText().trim().equals("")) {
    		JOptionPane.showMessageDialog(this,
                    Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_CSV_PLSSELECTFILE"));
    		return WizardPanelNavResult.REMAIN_ON_PAGE;
    	}
    	this.dataWizard.dataFrameDescriptor = getFileDescriptor();
    	return WizardPanelNavResult.PROCEED;		
    }
    
    public FileDescriptor getFileDescriptor() {
        if (tfFile.getText().trim().equals(""))
            return null;
        else {
            String path = tfFile.getText();
            boolean h = chbHeader.isSelected();
            String na = tfNA.getText();
            Character dec = tfDec.getText().trim().charAt(0);
            String sep = "";
            if (!chbWhiteSpace.isSelected())
                sep = tfFieldSep.getText();
            return new FileDescriptorCSV(path, h, na, dec, sep);
        }
    }

}
