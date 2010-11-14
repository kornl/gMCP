package org.mutoss.gui.options;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.af.commons.Localizer;
import org.af.commons.widgets.validate.IntegerTextField;
import org.af.commons.widgets.validate.RealTextField;
import org.af.commons.widgets.validate.ValidationException;
import org.mutoss.config.Configuration;
import org.mutoss.config.PlotConfig;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * OptionsPanel for plot settings.
 */
public class PlotPanel extends OptionsPanel{ 


    private RealTextField tfWidth;
    private RealTextField tfHeight;
    private RealTextField tfWidthInch;
    private RealTextField tfHeightInch;
    private IntegerTextField tfPointSize;
    private IntegerTextField tfTNWidth;

    private Configuration conf;


    public PlotPanel(Configuration conf) {
        this.conf = conf;

        makeComponents();
        doTheLayout();
    }


    private void makeComponents() {
        PlotConfig pc = conf.getPlotConfig();
        Localizer loc = Localizer.getInstance();
        tfWidth = new RealTextField(loc.getString("SGTK_OPTIONS_PLOTPANEL_WIDTH"), 5, 2, 1000);
        tfWidth.setText("" + pc.getWidth());
        tfHeight = new RealTextField(loc.getString("SGTK_OPTIONS_PLOTPANEL_HEIGHT"), 5, 2, 1000);
        tfHeight.setText("" + pc.getHeight());
        tfWidthInch = new RealTextField(loc.getString("SGTK_OPTIONS_PLOTPANEL_WIDTHINCH"), 5, 0.1, 100);
        tfWidthInch.setText("" + pc.getWidthInch());
        tfHeightInch = new RealTextField(loc.getString("SGTK_OPTIONS_PLOTPANEL_HEIGHTINCH"), 5, 0.1, 100);
        tfHeightInch.setText("" + pc.getHeightInch());
        tfPointSize = new IntegerTextField(loc.getString("SGTK_OPTIONS_PLOTPANEL_POINTSIZE"), 5, 2, 20);
        tfPointSize.setText("" + pc.getPointSize());
        tfTNWidth = new IntegerTextField(loc.getString("SGTK_OPTIONS_PLOTPANEL_THUMBNAILWIDTH"), 5, 50, 300);
        tfTNWidth.setText("" + pc.getTNWidth());
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
        
        p1.add(DefaultComponentFactory.getInstance().createSeparator(
                loc.getString("SGTK_OPTIONS_PLOTPANEL_PLOTFILESEP")),                   cc.xyw(1, row, 3));
        
        row += 2;
        
        p1.add(new JLabel(loc.getString("SGTK_OPTIONS_PLOTPANEL_WIDTH")),               cc.xy(1, row));
        p1.add(tfWidth,                            cc.xy(3, row));

        row += 2;
        
        p1.add(new JLabel(loc.getString("SGTK_OPTIONS_PLOTPANEL_HEIGHT")),              cc.xy(1, row));
        p1.add(tfHeight,                           cc.xy(3, row));
        
        row += 2;
        
        p1.add(new JLabel(loc.getString("SGTK_OPTIONS_PLOTPANEL_WIDTHINCH")),           cc.xy(1, row));
        p1.add(tfWidthInch,                            cc.xy(3, row));

        row += 2;
        
        p1.add(new JLabel(loc.getString("SGTK_OPTIONS_PLOTPANEL_HEIGHTINCH")),          cc.xy(1, row));
        p1.add(tfHeightInch,                           cc.xy(3, row));
        
        row += 2;
        
        p1.add(new JLabel(loc.getString("SGTK_OPTIONS_PLOTPANEL_POINTSIZE")),           cc.xy(1, row));
        p1.add(tfPointSize,                        cc.xy(3, row));

        row += 2;
        
        p1.add(DefaultComponentFactory.getInstance().createSeparator(
                 loc.getString("SGTK_OPTIONS_PLOTPANEL_THUMBNAILSEP")),                 cc.xyw(1, row, 3));
        
        row += 2;
        
        p1.add(new JLabel(loc.getString("SGTK_OPTIONS_PLOTPANEL_THUMBNAILWIDTH")),      cc.xy(1, row));
        p1.add(tfTNWidth,                          cc.xy(3, row));

        add(p1);
    }


    public void setProperties() throws ValidationException {
        double width = tfWidth.getValidatedValue();
        double height = tfHeight.getValidatedValue();
        double widthi = tfWidthInch.getValidatedValue();
        double heighti = tfHeightInch.getValidatedValue();
        int pointSize = tfPointSize.getValidatedValue();
        int tnWidth = tfTNWidth.getValidatedValue();

        PlotConfig pc = conf.getPlotConfig();
        pc.setWidth(width);
        pc.setHeight(height);
        pc.setWidthInch(widthi);
        pc.setHeightInch(heighti);        
        pc.setPointSize(pointSize);
        pc.setTNWidth(tnWidth);
    }
}
