package org.af.gMCP.gui.options;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.af.commons.Localizer;
import org.af.commons.widgets.validate.ValidationException;
import org.af.gMCP.config.Configuration;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * OptionsPanel for miscellaneous settings.
 */
public class MiscPanel extends OptionsPanel { 


    private JCheckBox checkOnlineForUpdate;
    
    private Configuration conf;


    public MiscPanel(Configuration conf) {
        this.conf = conf;

        makeComponents();
        doTheLayout();
    }


    private void makeComponents() {
        checkOnlineForUpdate = new JCheckBox("Check online for updates");
        checkOnlineForUpdate.setSelected(conf.getGeneralConfig().checkOnline());
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
        
        p1.add(checkOnlineForUpdate, cc.xyw(1, row, 3));
        
        row += 2;

        add(p1);
    }


    public void setProperties() throws ValidationException {
       	conf.getGeneralConfig().setCheckOnline(checkOnlineForUpdate.isSelected());
    }
}
