package br.com.marteleto.project.analysis.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.CloneCommand;
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
	
	public static Set<String> listLogEntries(String url, String username, String password, String label, String destPath) throws GitAPIException, IOException {
		File file = new File(destPath);
		if (!file.exists()) {
			clone(url, username, password, label, file);
		}
		return getHistory(file);
	}
	
	public static void clone(String url, String username, String password, String label, String destPath) throws GitAPIException {
		clone(url, username, password, label, new File(destPath));
	}
	
	private static void clone(String url, String username, String password, String label, File destPath) throws GitAPIException {
		CloneCommand cloneCommand =  Git.cloneRepository()
		  .setURI(url)
		  .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username,password))
		  .setDirectory(destPath)
		 ;
		if (label != null && !"".equals(label.trim())) {
			cloneCommand.setBranchesToClone(Arrays.asList(label));
			cloneCommand.setBranch(label);
		}
		Git git = cloneCommand.call();
		git.close();
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
	        	Calendar calendar = Calendar.getInstance();
	        	calendar.setTimeInMillis(commit.getCommitTime());
	            RevTree tree = commit.getTree();
	            TreeWalk treeWalk = new TreeWalk(git.getRepository());
	            treeWalk.addTree(tree);
	            treeWalk.setRecursive(true);
	            Map<String, String> files = new HashMap<>();
	            while(treeWalk.next()) {
	            	files.put(treeWalk.getPathString(), treeWalk.getOperationType().name());
	            }
	            treeWalk.close();
	            logs.add(RepositoryUtil.createRepositoryLog(commit.getName(), commit.getAuthorIdent().getName(), calendar.getTime(), commit.getFullMessage(), files));
	        }
        }
        return logs;
	}
}
