package org.af.statguitoolkit.gui.datawizard;

import org.netbeans.spi.wizard.Wizard;
import org.netbeans.spi.wizard.WizardBranchController;
import org.netbeans.spi.wizard.WizardPage;

import af.statguitoolkit.io.datasets.DataFrameDescriptor;

/**
 * 
 */
// TODO try to decouple the whole package from all other classes

public class DataWizard extends WizardBranchController {

	DataFrameDescriptor dataFrameDescriptor;

	public DataWizard(WizardPage[] pages) {
		super(pages);
	}

	protected Wizard getWizardForStep(java.lang.String step, java.util.Map settings) {	
		if (settings.get("csv")!=null && (Boolean)settings.get("csv")) {
			WizardPage[] pages = { new LoadCSVPanel(this) };
			return WizardPage.createWizard(pages, new DataResultProducer(this));
		} else if (settings.get("xls")!=null && (Boolean)settings.get("xls")) {		
			WizardPage[] pages = { new LoadXLSPanel(this) };
			return WizardPage.createWizard(pages, new DataResultProducer(this));
		} else if (settings.get("rdata")!=null && (Boolean)settings.get("rdata")) {
			WizardPage[] pages = { new PanelRDataSet(this) };
			return WizardPage.createWizard(pages, new DataResultProducer(this));
		} else {
			WizardPage[] pages = { new PanelRDataSet(this) };
			return WizardPage.createWizard(pages, new DataResultProducer(this));
		}
	}

}