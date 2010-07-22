package org.af.statguitoolkit.gui.datawizard;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.af.commons.Localizer;
import org.af.commons.widgets.lists.MyListModel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardPage;
import org.netbeans.spi.wizard.WizardPanelNavResult;

import af.statguitoolkit.io.datasets.RDataFrameDescriptor;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PanelRDataSet extends WizardPage implements ListSelectionListener, ActionListener  {
	private static final Log logger = LogFactory.getLog(PanelRDataSet.class);
    static List<String> packages = null;
    private JList liPackages = null;
    private JList liDataSets = null;
    static private Hashtable<String, List<String>> toDataSets = null;
    DataWizard wizard;
    JTextField tfPackage = new JTextField(30);

	public static String getDescription() {
        return  Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_RDATA_DESC");
	}
    
    public PanelRDataSet(DataWizard wizard) {
    	logger.debug("Constructor");
        this.wizard = wizard;        
        if (packages==null || toDataSets==null) {
        	packages = getLoadedRPackages();
            filterPackagesWithNoDatasets();	
        	getDataSetsFromPackages();
        }

        MyListModel<String> modelPackages = new MyListModel<String>(packages);
        liPackages = new JList(modelPackages);
        liPackages.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        liPackages.setSelectedIndex(0);
        liPackages.addListSelectionListener(this);

        liDataSets = new JList(new MyListModel<String>());
        liDataSets.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setDataSets();


        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        Box hBox = Box.createHorizontalBox();
        Box vBox;

        Localizer loc = Localizer.getInstance();

        vBox = Box.createVerticalBox();
        vBox.add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_RDATA_PACK")));
        vBox.add(Box.createVerticalStrut(5));
        vBox.add(new JScrollPane(liPackages));
        hBox.add(vBox);
        hBox.add(Box.createHorizontalGlue());

        vBox = Box.createVerticalBox();
        vBox.add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_RDATA_DATASET")));
        vBox.add(Box.createVerticalStrut(5));
        vBox.add(new JScrollPane(liDataSets));
        hBox.add(vBox);

        panel.add(hBox);

        String cols = "pref:grow, 5dlu, pref:grow";
        String rows = "pref:grow, 5dlu, pref";
        FormLayout layout = new FormLayout(cols, rows);

        setLayout(layout);
        CellConstraints cc = new CellConstraints();
        add(panel, cc.xyw(1,1,3));
        
        add(new JLabel(loc.getString("SGTK_DATAWIZARD_PANEL_RDATA_LOADPACK")), cc.xy(1, 3));
        add(tfPackage, cc.xy(3, 3)); 
        
        tfPackage.addActionListener(this);
        
    }

    
    private List<String> getLoadedRPackages() {
		// TODO Auto-generated method stub
		return null;
	}

	private List<String> getDataSetsFromPackage(String p) {
    	logger.debug("getDataSetsFromPackage");
        // TODO Get packages
        return null;
    }
    
    String p = "";
    String ds = "";
    
    public WizardPanelNavResult allowFinish(java.lang.String stepName, java.util.Map settings, Wizard wizard) {
    	logger.debug("allowFinish");
    	if (liDataSets.getSelectedValue() == null) {
    		JOptionPane.showMessageDialog(this,
                    Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_RDATA_PLSSELECTPACK"));
    		return WizardPanelNavResult.REMAIN_ON_PAGE;
    	}
    	String p = liPackages.getSelectedValue().toString();    	
    	String ds = liDataSets.getSelectedValue().toString();
    	logger.info("Loading data set \""+ds+"\" from package\""+p+"\".");
    	if (!this.p.equals(p) || !this.ds.equals(ds)) {
    		this.p = p; this.ds = ds;
    		this.wizard.dataFrameDescriptor = new RDataFrameDescriptor(ds, p);
    	}
    	return WizardPanelNavResult.PROCEED;		
    }


    private void getDataSetsFromPackages() {   
    	logger.debug("getDataSetsFromPackages");
    	toDataSets = new Hashtable<String, List<String>>();
        for (String p : packages) {
            toDataSets.put(p, getDataSetsFromPackage(p));
        }
    }

    public void valueChanged(ListSelectionEvent e) {
    	logger.debug("valueChanged");
        if (!e.getValueIsAdjusting()) {
            if (e.getSource() == liPackages) {
                setDataSets();
            }
        }
    }

    private void setDataSets() {
    	logger.debug("setDataSets");
    	if (liPackages.getSelectedValue()==null) return;
        String selPackage = liPackages.getSelectedValue().toString();
        List<String> sets = toDataSets.get(selPackage);
        liDataSets.setModel(new MyListModel<String>(sets));
        if (!sets.isEmpty()) {
            liDataSets.setSelectedIndex(0);
        }
    }

    private void filterPackagesWithNoDatasets() {
    	logger.debug("filterPackagesWithNoDatasets");
        List<String> result = new ArrayList<String>();
        for (String p:packages) {
            if(getDataSetsFromPackage(p).size() > 0) {
                result.add(p);
            }
        }
        packages = result;
    }

	public void actionPerformed(ActionEvent e) {
    	logger.debug("actionPerformed");
		if (e.getSource().equals(tfPackage)) {
			String pckg = tfPackage.getText();
			Boolean loaded = null;
			//try {
				//loaded = (Boolean) wizard.getRControl().getRServices().getObjectConverted("require(\""+pckg+"\")");
			//} catch (RemoteException e1) {
				loaded = false;
				//e1.printStackTrace();
			//}
			// loaded can be null, try (faulty) single letter package names
            if (loaded == null || !loaded) {
				JOptionPane.showMessageDialog(this,
                        Localizer.getInstance().getString("SGTK_DATAWIZARD_PANEL_RDATA_ERRLOADPACK") + "\n"+pckg);
			} else {
				((MyListModel<String>)liPackages.getModel()).addElement(pckg);
				toDataSets.put(pckg, getDataSetsFromPackage(pckg));
			}
			tfPackage.setText("");
		}
	}

}
