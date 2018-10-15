package br.com.marteleto.project.analysis.util;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
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
	
	public static void checkout(String url, String username, String password, String destPath) throws SVNException, IOException {
		SVNRepository repository = getRepository(url, username, password);
		checkout(repository,destPath);
	}
	
	public static Set<String> listAllEntries(String url, String username, String password) throws SVNException, IOException {
		SVNRepository repository = getRepository(url, username, password);
		return listAllEntries(repository,"");
	}
	
	public static Set<String> listLabelEntries(String url, String username, String password, String label) throws SVNException, IOException {
		SVNRepository repository = getRepository(url, username, password);
		return listDirEntries(repository,label);
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
			SVNLogEntry logEntry = (SVNLogEntry) entries.next();
			Map<String, String> files = new HashMap<>();
			if (logEntry.getChangedPaths().size() > 0) {
				Set changedPathsSet = logEntry.getChangedPaths().keySet();
                for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
                    SVNLogEntryPath entryPath = logEntry.getChangedPaths().get(changedPaths.next());
                    files.put(entryPath.getPath(), String.valueOf(entryPath.getType()));
                }
            }
			logs.add(RepositoryUtil.createRepositoryLog(logEntry.getRevision(), logEntry.getAuthor(), logEntry.getDate(), logEntry.getMessage(), files));
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
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }
}
