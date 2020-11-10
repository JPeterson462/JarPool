package net.digiturtle.jarpool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Jonathan Peterson
 */
public class JarPool {
	
	/**
	 * Map of JAR processes: Identifier -> Process
	 */
	private HashMap<Integer, Process> runningProcesses;
	
	/**
	 * Relative JRE filepath
	 */
	private String jrePath;
	
	/**
	 * Instantiate this instance of JarPool
	 * @param jrePath Relative JRE filepath
	 */
	public JarPool (String jrePath) {
		runningProcesses = new HashMap<>();
		this.jrePath = jrePath;
		if (jrePath == null) {
			this.jrePath = "";
		}
	}
	
	/**
	 * Retrieve the input stream for a process
	 * @param identifier Unique identifier for the JAR process
	 * @return The target process' input stream
	 */
	public InputStream getJarInputStream (int identifier) {
		if (!runningProcesses.containsKey(identifier)) {
			throw new NullPointerException("Identifier {" + identifier + "} not found in JarPool.");
		}
		return runningProcesses.get(identifier).getInputStream();
	}

	/**
	 * Retrieve the output stream for a process
	 * @param identifier Unique identifier for the JAR process
	 * @return The target process' output stream
	 */
	public OutputStream getJarOutputStream (int identifier) {
		if (!runningProcesses.containsKey(identifier)) {
			throw new NullPointerException("Identifier {" + identifier + "} not found in JarPool.");
		}
		return runningProcesses.get(identifier).getOutputStream();
	}
	
	/**
	 * Launch a new running instance of the JAR file
	 * @param identifier Unique identifier for the JAR process
	 * @param jarFilepath Relative filepath of the target JAR file
	 * @param arguments JAR file program arguments
	 * @throws IOException If there were any errors running the target JAR file
	 */
	public void runJar (int identifier, String jarFilepath, String arguments) throws IOException {
		Process process = Runtime.getRuntime().exec(jrePath + "java -jar " + jarFilepath + " " + arguments);
		runningProcesses.put(identifier, process);
	}
	
	/**
	 * Stops a running JAR process given its identifier
	 * @param identifier Unique identifier for the JAR process
	 */
	public void stopJar (int identifier) {
		Process process = runningProcesses.get(identifier);
		if (process == null) {
			throw new NullPointerException("Identifier {" + identifier + "} not found in JarPool.");
		}
		process.destroy();
		runningProcesses.remove(identifier);
	}
	
	/**
	 * Shutdown all running JAR processes
	 */
	public void shutdown () {
		Set<Integer> identifiers = new HashSet<Integer>(runningProcesses.keySet());
		for (int identifier : identifiers) {
			stopJar(identifier);
		}
	}

}
