package br.com.marteleto.project.analysis.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNDirEntry;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

@SuppressWarnings("rawtypes")
public class SubversionUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String newLine = "\r\n";
	
	public static void checkout(String url, String username, String password, String destPath) throws SVNException, IOException {
		SVNRepository repository = getRepository(url, username, password);
		checkout(repository,destPath);
	}
	
	public static Set<String> listAllEntries(String url, String username, String password) throws SVNException, IOException {
		SVNRepository repository = getRepository(url, username, password);
		return listAllEntries(repository,"");
	}
	
	public static Set<String> listTagsEntries(String url, String username, String password) throws SVNException, IOException {
		SVNRepository repository = getRepository(url, username, password);
		return listDirEntries(repository,"/tags");
	}
	
	public static Set<String> listBranchesEntries(String url, String username, String password) throws SVNException, IOException {
		SVNRepository repository = getRepository(url, username, password);
		return listDirEntries(repository,"/branches");
	}
	
	public static Set<String> listLogEntries(String url, String username, String password) throws SVNException, IOException {
		SVNRepository repository = getRepository(url, username, password);
		return listLogEntries(repository);
	}
	
	private static SVNRepository getRepository(String url, String username, String password) throws SVNException, IOException {
    	setupLibrary();
    	ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password.toCharArray());
    	SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(url));
        repository.setAuthenticationManager(authManager);        	
        SVNNodeKind nodeKind = repository.checkPath("", -1);
        if (nodeKind == SVNNodeKind.NONE) {
        	throw new IOException("There is no entry at '" + url + "'.");
        } else if (nodeKind == SVNNodeKind.FILE) {
        	throw new IOException("The entry at '" + url + "' is a file while a directory was expected.");
        }
        return repository;
	}
	
	private static Set<String> listDirEntries(SVNRepository repository, String path) throws SVNException {
        Set<String> paths = new LinkedHashSet<>(); 
		Collection entries = repository.getDir(path, -1, null,(Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            paths.add(entry.getName());
        }
        return paths;
	}
	
	private static Set<String> listAllEntries(SVNRepository repository, String path) throws SVNException {
		Set<String> files = new LinkedHashSet<>(); 
        Collection entries = repository.getDir(path, -1, null,(Collection) null);
        Iterator iterator = entries.iterator();
        while (iterator.hasNext()) {
            SVNDirEntry entry = (SVNDirEntry) iterator.next();
            files.add(entry.getName());
            if (entry.getKind() == SVNNodeKind.DIR) {
            	listAllEntries(repository, (path.equals("")) ? entry.getName() : path + "/" + entry.getName());
            }
        }
        return files;
	}
	 
	private static Set<String> listLogEntries(SVNRepository repository) throws SVNException {
		Set<String> logs = new LinkedHashSet<>();
		long startRevision = 1;
        long endRevision = repository.getLatestRevision();
		Collection logEntries = repository.log(new String[] {""}, null, startRevision, endRevision, true, true);
		for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
			StringBuilder log = new StringBuilder();
            SVNLogEntry logEntry = (SVNLogEntry) entries.next();
            log.append("------------------------------------------------------------------------");
            log.append(newLine + "r" + logEntry.getRevision() + " | " + logEntry.getAuthor() + " | " + DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss Z (E, dd MMM yyyy)",logEntry.getDate()) + " | " + countLines(logEntry.getMessage()) + " lines");
            if (logEntry.getChangedPaths().size() > 0) {
            	log.append(newLine + "Changed paths:");
				Set changedPathsSet = logEntry.getChangedPaths().keySet();
                for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
                    SVNLogEntryPath entryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                    log.append(newLine + "   " + entryPath.getType() + " " + entryPath.getPath() + ((entryPath.getCopyPath() != null) ? " (from " + entryPath.getCopyPath() + " revision " + entryPath.getCopyRevision() + ")" : ""));
                }
            }
            log.append(newLine + "");
            log.append(newLine + logEntry.getMessage());
            log.append(newLine + "");
            logs.add(log.toString());
        }
		return logs;
	}
	
	private static void checkout(SVNRepository repository, String destPath) throws SVNException {
		SVNClientManager ourClientManager = SVNClientManager.newInstance(null,repository.getAuthenticationManager());
		SVNUpdateClient updateClient = ourClientManager.getUpdateClient();
		updateClient.setIgnoreExternals(false);
		updateClient.doCheckout(repository.getLocation(),new File(destPath),SVNRevision.UNDEFINED,SVNRevision.HEAD,SVNDepth.INFINITY,true);
	}
	
	private static void setupLibrary() {
        //For using over http:// and https://
        DAVRepositoryFactory.setup();
        //For using over svn:// and svn+xxx://
        SVNRepositoryFactoryImpl.setup();
        //For using over file:///
        FSRepositoryFactory.setup();
    }

	private static int countLines(String str){
		if (str != null && !"".equals(str.trim())) {
			String[] lines = str.split("\r\n|\r|\n");
			return  lines.length;
		}
		return 1;
	}
}
