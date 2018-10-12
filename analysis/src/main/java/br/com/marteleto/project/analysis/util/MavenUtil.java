package br.com.marteleto.project.analysis.util;

import java.io.IOException;
import java.io.Serializable;

public class MavenUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static void execute(String pomPath) throws IOException, InterruptedException {
		String mavenHome = System.getenv("MAVEN_HOME");
		if (mavenHome == null || "".equals(mavenHome.trim())) {
			throw new IllegalArgumentException("MAVEN_HOME not found");
		}
		mavenHome = mavenHome + "/bin/";
		String command = mavenHome + "mvn clean package -Dmaven.test.skip=true -f " + pomPath;
		ProcessUtil.executeCommand(command);
	}
}
