package org.mutoss.gui.options;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.af.commons.Localizer;
import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.WidgetFactory;
import org.af.commons.widgets.buttons.OkApplyCancelButtonPane;
import org.af.commons.widgets.validate.ValidationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mutoss.config.Configuration;

/**
 * Dialog for configuring various settings.
 */
public class OptionsDialog extends JDialog implements ActionListener {
	private static final Log logger = LogFactory.getLog(OptionsDialog.class);

	private JTabbedPane tabbedPane;
    private GeneralPanel generalPanel;
    private PlotPanel plotPanel;
    private OkApplyCancelButtonPane bp;

    private Configuration conf;
    private Properties returnVal;

    private boolean revertChange = false;
    JFrame parent;

    /**
     * Standard constructor
     */
    public OptionsDialog(JFrame p) {
    	super(p);
    	this.parent = p;
        this.conf = Configuration.getInstance();
        setModal(true);
        setTitle(Localizer.getInstance().getString("SGTK_OPTIONS_OPTIONSDIALOG_TITLE"));

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        makeComponents();
        doTheLayout();
        
        pack();
        setLocationRelativeTo(p);
        setVisible(true);
    }

    /**
     * Instantiation of Swing-Components.
     */
    private void makeComponents() {
        tabbedPane = new JTabbedPane();
        generalPanel = new GeneralPanel(parent, this);
        plotPanel = new PlotPanel(conf);
        bp = new OkApplyCancelButtonPane();
    }

    /**
     * Do the layout.
     */
    private void doTheLayout() {
        Localizer loc = Localizer.getInstance();
        tabbedPane.addTab(loc.getString("SGTK_OPTIONS_OPTIONSDIALOG_GENERALTAB"),
                generalPanel);
        /*tabbedPane.addTab(loc.getString("SGTK_OPTIONS_OPTIONSDIALOG_PLOTTAB"),
                plotPanel);*/
        Container cp = getContentPane();
        cp.add(tabbedPane);
        cp = WidgetFactory.makeDialogPanelWithButtons(cp, bp, this);
        setContentPane(cp);
    }

    /**
     * Calls setProperties of the selected OptionsPanel of the TabbedPane on OK.
     * @param e ActionEvent to process.
     */
    public void actionPerformed(ActionEvent e) {
    	logger.debug("Got ActionCommand "+e.getActionCommand());
        if ( (e.getActionCommand().equals(OkApplyCancelButtonPane.OK_CMD)) ||
        		(e.getActionCommand().equals(OkApplyCancelButtonPane.APPLY_CMD)) ) {
            try {
            	generalPanel.setProperties();
            	plotPanel.setProperties();
                if  (e.getActionCommand().equals(OkApplyCancelButtonPane.OK_CMD)) {
                	dispose();
                }                
            } catch (ValidationException exc) {
                JOptionPane.showMessageDialog(this, exc.getMessage());
            } catch (SetLookAndFeelException exc) {
                ErrorHandler.getInstance().makeErrDialog(exc.getMessage(), exc);
            }
        }
        if (e.getActionCommand().equals(OkApplyCancelButtonPane.CANCEL_CMD)) {
            dispose();
        }
    }

}


