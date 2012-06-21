package tests;

import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestSuite {
	
	public static void main(String[] args) {
		Result result = org.junit.runner.JUnitCore.runClasses(getClasses());
		System.out.println(getResultString(result));
	}
	
    public static Class[] getClasses() {
    	return new Class[] { RControlTest.class };
    }
    
    public static String getResultString(Result result) {
    	String s = "There were "+result.getFailureCount()+" failures ("+result.getRunCount()+" tests; runtime "+result.getRunTime()/1000+" sec):\n\n";
    	for (Failure f : result.getFailures()) {
    		s += "Failure in "+f.getDescription().getClassName()+"."+f.getDescription().getMethodName()+"()\n";
    		s += "Message: "+f.getMessage()+"\n\nTrace: "+f.getTrace()+"\n\n";
    	}
    	return s;
    }
}
