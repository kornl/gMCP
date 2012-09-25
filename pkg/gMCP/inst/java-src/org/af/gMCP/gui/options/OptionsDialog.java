package org.af.gMCP.gui.options;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.af.commons.errorhandling.ErrorHandler;
import org.af.commons.widgets.WidgetFactory;
import org.af.commons.widgets.buttons.OkApplyCancelButtonPane;
import org.af.commons.widgets.validate.ValidationException;
import org.af.gMCP.config.Configuration;
import org.af.gMCP.gui.CreateGraphGUI;
import org.af.gMCP.gui.MenuBarMGraph;
import org.af.gMCP.gui.graph.Edge;
import org.af.gMCP.gui.graph.Node;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Dialog for configuring various settings.
 */
public class OptionsDialog extends JDialog implements ActionListener {
	private static final Log logger = LogFactory.getLog(OptionsDialog.class);

	private JTabbedPane tabbedPane;
    private GeneralPanel visualPanel;
    private NumericPanel numericPanel;
    private MiscPanel miscPanel;
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
        setTitle("Options");

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
        visualPanel = new GeneralPanel(parent, this);
        numericPanel = new NumericPanel(conf);
        miscPanel = new MiscPanel(conf);        
        bp = new OkApplyCancelButtonPane();
    }

    /**
     * Do the layout.
     */
    private void doTheLayout() {
        tabbedPane.addTab("Visual", visualPanel);
        tabbedPane.addTab("Numeric", numericPanel);
        tabbedPane.addTab("Misc", miscPanel);
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
            	visualPanel.setProperties();
            	numericPanel.setProperties();
            	miscPanel.setProperties();
                if  (e.getActionCommand().equals(OkApplyCancelButtonPane.OK_CMD)) {
                	dispose();
                }
                for (Edge edge : parent.getGraphView().getNL().getEdges()) {                	
                	//TODO Update edge weights
                }
                for (Node node : parent.getGraphView().getNL().getNodes()) {
                	node.createWeightIcons();
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
        ((MenuBarMGraph)parent.getJMenuBar()).createExampleMenu();
        ((MenuBarMGraph)parent.getJMenuBar()).createExtraMenu();
    }

}


