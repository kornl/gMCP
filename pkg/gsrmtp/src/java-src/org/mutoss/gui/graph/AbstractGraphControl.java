package org.mutoss.gui.graph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractGraphControl  {
	
	private static final Log logger = LogFactory.getLog(AbstractGraphControl.class);
	

	public NetzListe getNL() {
		return null;
	}
	
	
	public void updateEdge(int from, int to, Double w) {
		logger.info("Adding Edge from "+from+" to "+to+" with weight "+w+".");
		getNL().addEdge(getNL().knoten.get(from), getNL().knoten.get(to), w);
	}


	public Object getDataTable() {
		// TODO Auto-generated method stub
		return null;
	}


	public Object getDataFrame() {
		// TODO Auto-generated method stub
		return null;
	}

}
