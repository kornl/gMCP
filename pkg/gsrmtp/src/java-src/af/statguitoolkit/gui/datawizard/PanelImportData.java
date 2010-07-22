package org.af.statguitoolkit.gui.datawizard;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.af.commons.Localizer;
import org.netbeans.spi.wizard.WizardPage;

public class PanelImportData extends WizardPage {
    private JRadioButton csv = new JRadioButton(
            Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_IMPORTDATA_CSV"));
    private JRadioButton xls = new JRadioButton(
            Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_IMPORTDATA_XLS"));
    private JRadioButton rdata = new JRadioButton(
            Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_IMPORTDATA_RDATA"));
    private JRadioButton clipboard = new JRadioButton(
            Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_IMPORTDATA_CLIPBOARD"));

    public static String getDescription() {
        return  Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_IMPORTDATA_DESC");
	}
    
    public PanelImportData() {

        clipboard.setEnabled(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(csv);
        csv.setName("csv");
        buttonGroup.add(xls);
        xls.setName("xls");
        buttonGroup.add(rdata);
        rdata.setName("rdata");
        buttonGroup.add(clipboard);
        clipboard.setName("clipboard");
        setSelectedButton();

        Box box = Box.createVerticalBox();
        box.add(Box.createVerticalGlue());
        box.add(csv);
        box.add(xls);
        box.add(rdata);
        box.add(clipboard);
        box.add(Box.createVerticalGlue());
        panel.add(Box.createHorizontalGlue());
        panel.add(box);
        panel.add(Box.createHorizontalGlue());

        add(panel);
    }

    private void setSelectedButton() {
    	xls.setSelected(true);        
    }
}
