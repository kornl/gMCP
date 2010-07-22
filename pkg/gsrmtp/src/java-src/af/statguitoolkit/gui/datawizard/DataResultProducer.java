package org.af.statguitoolkit.gui.datawizard;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.netbeans.spi.wizard.WizardException;
import org.netbeans.spi.wizard.WizardPage.WizardResultProducer;

public class DataResultProducer implements WizardResultProducer {

	private static final Log logger = LogFactory.getLog(DataResultProducer.class);
	
	DataWizard dataWizard;
	
    public DataResultProducer(DataWizard dataWizard) {
    	this.dataWizard = dataWizard;
	}

	public Object finish(Map settings) throws WizardException {
        logger.info("Wizard has finished - loading data");
        logger.info(settings.toString());   	
        return dataWizard.dataFrameDescriptor;
    }

    public boolean cancel(Map settings) {
        return true;
    }
	
}