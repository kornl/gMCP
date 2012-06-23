package tests;

import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.graph.NetList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class LoadSaveTest {

    protected RControl ctrl;
    protected NetList nl;

    @Before
    public void setUp() {
    	ctrl = RControl.getRControl(true);
    }

    @After
    public void cleanUp() {
    }

    @Test
    public void testLoadSave() {
        for (String graph : TestSuite.exampleGraphs) {
        	//TODO Netlist should be created with GraphView=null
        	//TODO Implement R function for equals(graph1, graph2) 
        	//nl.loadGraph("graph");
        	//nl.saveGraph(".testGraph", false);        	 
        	//ctrl.getR().eval("");
        }
    }
    
  }
	  

