package br.com.marteleto.project.analysis.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GitUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String newLine = "\r\n";
	
	public static Set<String> listLabelEntries(String url, String username, String password) throws GitAPIException {
		Set<String> list = new LinkedHashSet<>();
		Collection<Ref> refs = Git.lsRemoteRepository()
                .setHeads(true)
                .setTags(true)
                .setRemote(url)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username,password))
                .call();

        for (Ref ref : refs) {
        	list.add(ref.getName());
        }
		return list;
	}
	
	public static Set<String> listLogEntries(String url, String username, String password, String destPath) throws GitAPIException, IOException {
		File file = new File(destPath);
		if (!file.exists()) {
			clone(url, username, password, file);
		}
		return getHistory(file);
	}
	
	private static void clone(String url, String username, String password, File destPath) throws GitAPIException {
		Git.cloneRepository()
		  .setURI(url)
		  .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username,password))
		  .setDirectory(destPath)
		  //.setBranchesToClone(Arrays.asList("refs/heads/specific-branch"))
		  //.setBranch("refs/heads/specific-branch")
		  .call()
		 ;
	}
	
	private static Set<String> getHistory(File destPath) throws GitAPIException, IOException {
		if (!destPath.exists()) {
			throw new IOException("destPath not found.");
		}
		Set<String> logs = new LinkedHashSet<>();
        try (
        	Git git = Git.open(destPath);
        ) {
	        LogCommand logCommand = git.log();
	        Iterable<RevCommit> logMsgs = logCommand.call();
	        for (RevCommit commit : logMsgs) {
	        	StringBuilder log = new StringBuilder();
	        	Calendar calendar = Calendar.getInstance();
	        	calendar.setTimeInMillis(commit.getCommitTime());
	            log.append("------------------------------------------------------------------------");
	            log.append(newLine + "r" + commit.getName() + " | " + commit.getAuthorIdent().getName() + " | " + DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss Z (E, dd MMM yyyy)",calendar.getTime()) + " | " + countLines(commit.getFullMessage()) + " lines");
	            RevTree tree = commit.getTree();
	            TreeWalk treeWalk = new TreeWalk(git.getRepository());
	            treeWalk.addTree(tree);
	            treeWalk.setRecursive(true);
	            boolean showHeader = true;
	            while( treeWalk.next() ) {
	            	if (showHeader) {
	            		log.append(newLine + "Changed paths:");
	            		showHeader = false;
	            	}
	            	log.append(newLine + "   " + treeWalk.getOperationType() + " " + treeWalk.getPathString());
	            }
	            treeWalk.close();
	            log.append(newLine + "");
	            log.append(newLine + commit.getFullMessage());
	            log.append(newLine + "");
	            logs.add(log.toString());
	        }
        }
        return logs;
	}
	
	private static int countLines(String str){
		if (str != null && !"".equals(str.trim())) {
			String[] lines = str.split("\r\n|\r|\n");
			return  lines.length;
		}
		return 1;
	}
}
