package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.af.gMCP.gui.RControl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RControlTest {


    private RControl ctrl;

    @Before
    public void setUp() {
    	ctrl = RControl.getRControl(true);
    }

    @After
    public void deleteOutputFile() {
    }

    @Test
    public void testEmptyCollection() {
        Collection collection = new ArrayList();
        assertTrue(collection.isEmpty());
    }

  }
	  

