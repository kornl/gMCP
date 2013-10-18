import org.rosuda.JRI.Rengine;

class Example {

  public static void main(String[] args) {
    if (!Rengine.versionCheck()) {
      System.err.println("Error: API version of the Rengine class and the native binary differ.");
      System.exit(1);
    }
    Rengine rengine = Rengine.getMainEngine();
    if (rengine == null) {
		// Call java with VM arguments: -Declipse="true"
		if (System.getProperty("eclipse") != null) {
			rengine = new Rengine(new String[] {"--vanilla"}, true, null);
		} else {
			rengine = new Rengine();
		}
	}
    rengine.eval("plot(rnorm(1000))");
    //TODO Find stuff that crashes on Windows 32 "reliably".
    //For example: Loading graph from Hommel et al. (2007).
  }
}
