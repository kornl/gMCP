package org.mutoss.gui.graph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AbstractGraphControl  {
	
	private static final Log logger = LogFactory.getLog(AbstractGraphControl.class);
	
	String name;
	
	public AbstractGraphControl(String name) {
		this.name = name;
	}
	
	public NetzListe getNL() {
		return null;
	}
	
	public String getName() {		
		return name;
	}

}
