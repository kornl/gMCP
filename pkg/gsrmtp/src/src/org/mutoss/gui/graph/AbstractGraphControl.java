package af.statguitoolkit.graph;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import af.statguitoolkit.AbstractControl;
import af.statguitoolkit.Analysis;
import af.statguitoolkit.config.Configuration;

public abstract class AbstractGraphControl extends AbstractControl {
	
	private static final Log logger = LogFactory.getLog(AbstractGraphControl.class);
	
	protected AbstractGraphControl(Configuration conf, String helpSystemFile) {
		super(conf, helpSystemFile);

	}
	
	public NetzListe getNL() {
		return ((ViewMapGraph)viewMap).getGraphView().nl;
	}
	
	public abstract void addNode(Node node);

	@Override
	protected void construct() {
		viewMap = new ViewMapGraph(this);
		assaycontrol = null;
	}

	@Override
	public Analysis getAnalysis() {
		return analysis;
	}
	
	public void updateEdge(int from, int to, Double w) {
		logger.info("Adding Edge from "+from+" to "+to+" with weight "+w+".");
		getNL().addEdge(getNL().knoten.get(from), getNL().knoten.get(to), w);
	}

}
