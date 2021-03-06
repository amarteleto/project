package br.com.marteleto.project.analysis.util;

import java.io.IOException;
import java.io.Serializable;

public class JDepsUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static void execute(String packagePath, String jarPath, String output) throws IOException, InterruptedException {
		execute(packagePath, jarPath, output, true);
	}
	
	public static void execute(String packagePath, String jarPath, String output, boolean showLog) throws IOException, InterruptedException {
		String javaHome = System.getenv("JAVA_HOME");
		if (javaHome == null || "".equals(javaHome.trim())) {
			throw new IllegalArgumentException("JAVA_HOME not found");
		}
		javaHome = javaHome + "/bin/";
		String command = javaHome + "jdeps -v";
		if (packagePath != null && !"".equals(packagePath.trim())) {
			command = command + " -e " + packagePath;
		}
		command = command + " " + jarPath;
		if (output != null && !"".equals(output.trim())) {
			command = command + " > " + output;
		}
		ProcessUtil.executeCommand(command,showLog);
	}
}