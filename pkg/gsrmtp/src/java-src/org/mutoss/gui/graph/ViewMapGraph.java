package af.statguitoolkit.graph;

import af.statguitoolkit.gui.docking.ViewMapSGTK;
import af.statguitoolkit.gui.docking.ViewMultipleData;

public class ViewMapGraph extends ViewMapSGTK {

	private GraphView graphView;
    private PView pView;
    
    public ViewMultipleData getViewMultipleData() {
    	return viewMultipleData;
    }
    
    public ViewMapGraph(AbstractGraphControl abstractGraphControl) {
        super(abstractGraphControl);
        graphView = new GraphView(abstractGraphControl);
        removeView(ASSAYVIEW);
        addView(GRAPHVIEW, graphView);
        pView = new PView(abstractGraphControl);        
        addView(PVIEW, pView);
 
    }

    public GraphView getGraphView() {
        return graphView;
    }
    
    public PView getPView() {
        return pView;
    }
}
