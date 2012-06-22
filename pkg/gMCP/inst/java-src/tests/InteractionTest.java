package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.af.gMCP.gui.RControl;
import org.af.gMCP.gui.graph.NetList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class InteractionTest {

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
    }
    
  }
	  

