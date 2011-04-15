package org.mutoss.gui.options;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
import org.mutoss.gui.CreateGraphGUI;
import org.mutoss.gui.graph.Edge;
import org.mutoss.gui.graph.Node;

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

    CreateGraphGUI parent;

    /**
     * Standard constructor
     */
    public OptionsDialog(CreateGraphGUI p) {
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
                for (Edge edge : parent.getGraphView().getNL().getEdges()) {
                	double[] weight = edge.getW(null);
                	if (weight.length==1) {
                		edge.setW(weight[0]);
                	}
                }
                for (Node node : parent.getGraphView().getNL().getKnoten()) {
                	node.setWeight(node.getWeight(), null);
                }
                parent.repaint();
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


