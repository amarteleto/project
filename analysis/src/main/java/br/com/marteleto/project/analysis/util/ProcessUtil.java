package br.com.marteleto.project.analysis.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProcessUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final transient Logger log = Logger.getLogger(ProcessUtil.class.getName());
	private static final String RUNNING_COMMAND = "running command: {0} {1} {2}";
	private static final String OS = System.getProperty("os.name").toLowerCase();
	
	public static String executeCommand(String command, boolean showLog) throws IOException, InterruptedException {
		if (command != null && !"".equals(command.trim())) {
			String[] parameters = new String[3];
		    if (isWindows()) {
		    	parameters[0] = "cmd";
		    	parameters[1] = "/c";
		    	parameters[2] = command;
		    } else if (isUnix()) {
		    	parameters[0] = "/bin/sh";
		    	parameters[1] = "-c";
		    	parameters[2] = command;
		    } else {
		    	throw new IllegalArgumentException("Operating system not mapped.");
		    }
		    log.log(Level.INFO,RUNNING_COMMAND,parameters);
			StringBuilder output = new StringBuilder();
			ProcessBuilder pb = new ProcessBuilder(parameters);
			pb.redirectErrorStream(true);
			Process process = pb.start();
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
				if (showLog) {
					log.log(Level.INFO,line);
				}
			}
			int result = process.waitFor();
			if (result == 1) {
				throw new IOException(output.toString());
			}
			return output.toString();
		}
		return null;
	}
	
	public static boolean isWindows() {
		return (OS.indexOf("win") >= 0);
	}
 
	public static boolean isUnix() {
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") >= 0 );
	}
}