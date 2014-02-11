import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.RuntimeMXBean;
import java.util.List;

public class SystemInfo {

	public static String getSystemInfo(boolean filesystem, boolean memory, boolean runtime) {
		String result = "";
		if (memory) {
			result += "Available processors (cores): " + Runtime.getRuntime().availableProcessors()+"\n";
			result += "Free memory (bytes): " +	Runtime.getRuntime().freeMemory()+"\n";
			long maxMemory = Runtime.getRuntime().maxMemory();
			result += "Maximum memory (bytes): " + (maxMemory == Long.MAX_VALUE ? "no limit" : maxMemory)+"\n";
			result += "Total memory (bytes): " + Runtime.getRuntime().totalMemory()+"\n\n";

			MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
			MemoryUsage heap = memBean.getHeapMemoryUsage();
			MemoryUsage nonheap = memBean.getNonHeapMemoryUsage();
			
			result += "HEAP: "+ heap+"\n\n";
			result += "NONHEAP: "+ nonheap+"\n\n";
		}
		if (filesystem) {			
			File[] roots = File.listRoots();
			for (File root : roots) {
				result += "File system root: " + root.getAbsolutePath()+"\n";
				result += "Total space (bytes): " + root.getTotalSpace()+"\n";
				result += "Free space (bytes): " + root.getFreeSpace()+"\n";
				result += "Usable space (bytes): " + root.getUsableSpace()+"\n\n";
			}						
		}
		if (runtime) {
			RuntimeMXBean runtimeMxBean = ManagementFactory.getRuntimeMXBean();
			
			result += "Class Path:\n "+runtimeMxBean.getClassPath().replaceAll(System.getProperty("path.separator"), "\n ")+"\n";
			result += "Boot Class Path:\n "+runtimeMxBean.getBootClassPath().replaceAll(System.getProperty("path.separator"), "\n ")+"\n";
			result += "Library Path:\n "+runtimeMxBean.getLibraryPath().replaceAll(System.getProperty("path.separator"), "\n ")+"\n";
			List<String> args = runtimeMxBean.getInputArguments();
			result += "Input Arguments: \n"; //+runtimeMxBean.getLibraryPath()+"\n";
			for (String a : args) {
				result += " " + a + "\n";
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		printInfo();		
	}

	private static void printInfo() {
		System.out.println(getSystemInfo(true, true, true));		
	}

}
