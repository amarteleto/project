package br.com.marteleto.project.analysis.runnable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.tmatesoft.svn.core.SVNException;

import br.com.marteleto.project.analysis.model.ReportModel;
import br.com.marteleto.project.analysis.model.type.ReportType;
import br.com.marteleto.project.analysis.model.type.RepositoryType;
import br.com.marteleto.project.analysis.util.Constants;
import br.com.marteleto.project.analysis.util.FacesUtil;
import br.com.marteleto.project.analysis.util.GitUtil;
import br.com.marteleto.project.analysis.util.JDepsUtil;
import br.com.marteleto.project.analysis.util.MavenUtil;
import br.com.marteleto.project.analysis.util.PropertyUtil;
import br.com.marteleto.project.analysis.util.SubversionUtil;

public class ReportRunnable implements Runnable {
	protected static final Logger log = Logger.getLogger(ReportRunnable.class.getName());
	private final ReportModel reportModel;
	private final Long id = Calendar.getInstance().getTimeInMillis();
	private String workspace;
	private String workspaceLabels;
	private String finalFile;
	private Integer progress = 0;
	private boolean running = true;
		
	public ReportRunnable(ReportModel reportModel) {
		super();
		this.reportModel = reportModel;
	}

	public String getName() {
		return this.reportModel.getName() + "_" + this.id;
	}

	public Integer getProgress() {
		return progress;
	}

	public boolean isRunning() {
		return running;
	}

	@Override
	public void run() {
		try {
			createDirectory();
			writeRepositoryLog();
			writeJdepsReport();
			finish();
		} catch (Exception ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			clear();
			running = false;
		}
	}
	
	private void createDirectory() {
		progress += 5;
		workspace = PropertyUtil.getProperty("workspace") + Constants.DIR_SEPARATOR + getName();
		workspaceLabels = workspace + Constants.DIR_SEPARATOR + "labels";
		File file = new File(workspace);
		if (!file.exists()) {
			file.mkdirs();
		}
		file = new File(PropertyUtil.getProperty("download"));
		if (!file.exists()) {
			file.mkdirs();
		}
		progress += 5;
    }
	
	private void writeRepositoryLog() throws SVNException, IOException, GitAPIException {
		if (this.reportModel.getReportType().equals(ReportType.REPOSITORY_LOG)) {
			progress += 35;
			Set<String> listLog = new HashSet<>();
			if (this.reportModel.getRepositoryModel().getRepositoryType().equals(RepositoryType.SUBVERSION)) {
				String url = this.reportModel.getRepositoryModel().getUrl() + Constants.URL_SEPARATOR + this.reportModel.getRepositoryModel().getLabelType().getDescription();
				if (!this.reportModel.getRepositoryModel().getLabel().equals(this.reportModel.getRepositoryModel().getLabelType().getDescription())) {
					url = url + Constants.URL_SEPARATOR + this.reportModel.getRepositoryModel().getLabel();
				}
				listLog = SubversionUtil.listLogEntries(url, this.reportModel.getRepositoryModel().getUsername(), this.reportModel.getRepositoryModel().getPassword());
			} else if (this.reportModel.getRepositoryModel().getRepositoryType().equals(RepositoryType.GIT)) {
				String label = null;
				if (!this.reportModel.getRepositoryModel().getLabel().equals(this.reportModel.getRepositoryModel().getLabelType().getDescription())) {
					label = this.reportModel.getRepositoryModel().getLabel();
				}
				String destPath = workspace + Constants.DIR_SEPARATOR + this.reportModel.getRepositoryModel().getLabel();
				listLog = GitUtil.listLogEntries(this.reportModel.getRepositoryModel().getUrl(), this.reportModel.getRepositoryModel().getUsername(), this.reportModel.getRepositoryModel().getPassword(), label, destPath);
			}
			if (!listLog.isEmpty()) {
				Path path = Paths.get(workspace + Constants.DIR_SEPARATOR + "repository_" + this.getName() + ".log");
				finalFile = path.toFile().getAbsolutePath();
				try (
						BufferedWriter writer = Files.newBufferedWriter(path)
				) {
					for (String line : listLog) {
						writer.write(line);
					}
				}
			}
			progress += 35;
		}
    }
	
	private void writeJdepsReport() throws SVNException, IOException, InterruptedException, GitAPIException  {
		if (this.reportModel.getReportType().equals(ReportType.JDEPS_ANALYZE)) {
			progress += 10;
			checkout();
			progress += 20;
			maven();
			progress += 20;
			jdeps();
			progress += 20;
		}
	}
    
    private void checkout() throws SVNException, IOException, GitAPIException {
    	String destPath = workspaceLabels + Constants.DIR_SEPARATOR + this.reportModel.getRepositoryModel().getLabel();
    	if (this.reportModel.getRepositoryModel().getRepositoryType().equals(RepositoryType.SUBVERSION)) {
			String url = this.reportModel.getRepositoryModel().getUrl() + Constants.URL_SEPARATOR + this.reportModel.getRepositoryModel().getLabelType().getDescription();
			if (!this.reportModel.getRepositoryModel().getLabel().equals(this.reportModel.getRepositoryModel().getLabelType().getDescription())) {
				url = url + Constants.URL_SEPARATOR + this.reportModel.getRepositoryModel().getLabel();
			}
			SubversionUtil.checkout(url, this.reportModel.getRepositoryModel().getUsername(), this.reportModel.getRepositoryModel().getPassword(),destPath);
		} else if (this.reportModel.getRepositoryModel().getRepositoryType().equals(RepositoryType.GIT)) {
			String label = null;
			if (!this.reportModel.getRepositoryModel().getLabel().equals(this.reportModel.getRepositoryModel().getLabelType().getDescription())) {
				label = this.reportModel.getRepositoryModel().getLabel();
			}
			GitUtil.clone(this.reportModel.getRepositoryModel().getUrl(), this.reportModel.getRepositoryModel().getUsername(), this.reportModel.getRepositoryModel().getPassword(), label, destPath);
		}
    }
    
    private void maven() throws InterruptedException, IOException {
    	String pomPath = workspaceLabels + Constants.DIR_SEPARATOR + this.reportModel.getRepositoryModel().getLabel() + Constants.DIR_SEPARATOR + this.reportModel.getPom();
    	MavenUtil.execute(pomPath);
    }
    
    private void jdeps() throws InterruptedException, IOException {
		StringBuilder jarPath = new StringBuilder();
		String separator = "";
		for (String jar : this.reportModel.getListJar()) {
			jarPath.append(separator);
			jarPath.append(workspaceLabels);
			jarPath.append(Constants.DIR_SEPARATOR);
			jarPath.append(this.reportModel.getRepositoryModel().getLabel());
			jarPath.append(jar);
			separator = " ";
		}
		finalFile = workspace + Constants.DIR_SEPARATOR + "jdeps_" + this.getName() + ".log";
		JDepsUtil.execute(this.reportModel.getPackagePath(), jarPath.toString(), finalFile);
    }

    private void finish() {
    	progress += 5;
    	File origin = new File(finalFile);
    	if (origin.exists()) {
    		try {
    			String target = PropertyUtil.getProperty("download") + Constants.DIR_SEPARATOR + origin.getName();
				Files.move(Paths.get(finalFile), Paths.get(target), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				FacesUtil.exceptionProcess(ex);
			}
    	}
    	progress += 5;
    }
    
    private void clear() {
    	progress += 5;
    	try (
    		Stream<File> stream = Files.walk(Paths.get(workspace)).sorted(Comparator.reverseOrder()).map(Path::toFile);
    	) {
    		stream.forEach(File::delete);
    	} catch (IOException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}
    	progress += 5;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		return !id.equals(((ReportRunnable) obj).id);
	}
}