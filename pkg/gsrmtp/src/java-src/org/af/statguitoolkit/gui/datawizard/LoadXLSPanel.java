package org.af.statguitoolkit.gui.datawizard;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;

import org.af.commons.Localizer;
import org.af.commons.widgets.lists.MyJComboBox;
import org.af.statguitoolkit.io.datasets.FileDescriptorXLS;
import org.af.statguitoolkit.io.datasets.RFileFormatException;
import org.af.statguitoolkit.io.datasets.XLSToolkit;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPanelNavResult;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class LoadXLSPanel extends LoadPanel implements ActionListener {

	public static String getDescription() {
        return  Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_XLS_DESC");
	}
	

    private MyJComboBox cbSheet;

    DataWizard dataWizard;
    
    
    public LoadXLSPanel(DataWizard dataWizard) {
    	this.dataWizard = dataWizard;        
        makeComponents();
        doTheLayout();

    }

    /**
     * Instantiation of Swing-Components.
     */
    protected void makeComponents() {
    	super.makeComponents();
        cbSheet = new MyJComboBox();
        cbSheet.addActionListener(this);
    }

    protected void doTheLayout() {

        Localizer loc = Localizer.getInstance();

        String cols = "pref, 5dlu, pref:grow, 5dlu, left:pref, 5dlu, left:pref";
        String rows = "pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref, 5dlu, pref:grow";
        FormLayout layout = new FormLayout(cols, rows);

        setLayout(layout);
        CellConstraints cc = new CellConstraints();
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_FILE")),       cc.xy  (1, 1));
        add(tfFile,                                                             cc.xyw (3, 1, 3));
        add(bSelect,                                                            cc.xy  (7, 1));
        JScrollPane sp = new JScrollPane(taPreview);
        sp.setBorder(new TitledBorder(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_PREVIEW")));
        add(sp,                                                                 cc.xywh(1, 3, 3, 9));

        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_XLS_SHEET")),      cc.xy  (5, 3));
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_HEADER")),     cc.xy  (5, 5));
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_MISSING")),    cc.xy  (5, 7));
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_CSV_DECPOINT")),   cc.xy(5, 9));

        add(cbSheet,                                                            cc.xy  (7, 3));
        add(chbHeader,                                                          cc.xy  (7, 5));
        add(tfNA,                                                               cc.xy  (7, 7));
        add(tfDec,                                                              cc.xy  (7, 9));
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == bSelect) {
            selectFile();
        }
        if (e.getSource() == cbSheet) {
           setPreview(getPreview(new File(tfFile.getText())));
        }
    }

    private void selectFile() {
        super.selectFile(new String[]{"xls", "xlsx"});
    }

    protected void setFile(File file) {
        try {
            fillSheetBox(file);
            super.setFile(file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected List<String> getPreview(File file) {
        List<String> result;
        Random random = new Random();
        File previewFile = new File(getProjectDataSetsPath(),
                "preview_" + random.nextLong() + ".csv");
        try {
            XLSToolkit.convertXls2Csv(file, cbSheet.getSelectedIndex(), previewFile, ";");
            result = readLines(previewFile, 20);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        previewFile.delete();
        return result;
    }

    private String getProjectDataSetsPath() {
		// TODO Auto-generated method stub
		return null;
	}

	private void fillSheetBox(File file) throws RFileFormatException, IOException {
        List<String> tables = XLSToolkit.getXLSTables(file);

        cbSheet.setModel(tables);
        cbSheet.setEnabled(true);
        revalidate();
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

    public FileDescriptorXLS getFileDescriptor() {
        if (tfFile.getText().trim().equals(""))
            return null;
        else {
            String path = tfFile.getText();
            int sheet = cbSheet.getSelectedIndex();
            String sheetname = cbSheet.getSelectedObject().toString();
            boolean h = chbHeader.isSelected();
            String na = tfNA.getText();
            Character dec = tfDec.getText().trim().charAt(0) ;
            return new FileDescriptorXLS(path, sheet, sheetname, h, na, dec);
        }
    }
}
