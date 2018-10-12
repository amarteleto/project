package br.com.marteleto.project.analysis.runnable;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.tmatesoft.svn.core.SVNException;

import br.com.marteleto.project.analysis.model.ReportModel;
import br.com.marteleto.project.analysis.model.type.LabelType;
import br.com.marteleto.project.analysis.util.JDepsUtil;
import br.com.marteleto.project.analysis.util.MavenUtil;
import br.com.marteleto.project.analysis.util.PropertyUtil;
import br.com.marteleto.project.analysis.util.SubversionUtil;

public class ReportRunnable implements Runnable {
	private final ReportModel reportModel;
	private final Set<String> listZipFile = new HashSet<>();
	private final Set<String> listLabelFile = new HashSet<>();
	private String workspace;
	private String workspaceLabels;
	
	public ReportRunnable(ReportModel reportModel) {
		super();
		this.reportModel = reportModel;
	}

	@Override
	public void run() {
		try {
			initWorkspace();
			createListLabel();
			writeSvnLog();
			checkout();
			maven();
			jdeps();
			compactFiles();
			clearTemp();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		/*
		1 - SALVAR EM LOG SVN EM ARQUIVO 
		2 - EXECUTAR JDEPS EM CADA ROTULO SELECIONADO
		3 - COMPACTAR RESULTADOS E ENTREGAR PRA DOWNLOAD
		*/
	}

	private void initWorkspace() {
		String project = this.reportModel.getRepositoryModel().getUrl().substring(this.reportModel.getRepositoryModel().getUrl().lastIndexOf("/"), this.reportModel.getRepositoryModel().getUrl().length()); 
		workspace = PropertyUtil.getProperty("workspace") + project + "_" + Calendar.getInstance().getTimeInMillis();
		workspaceLabels = workspace + "/labels";
		File file = new File(workspaceLabels);
		file.mkdirs();
    }
	
	private void createListLabel() {
		for (String label : this.reportModel.getLabelModel().getListLabel()) {
			File file = new File(workspaceLabels + "/" + label);
    		file.mkdirs();
			listLabelFile.add(file.getAbsolutePath());
		}
	}
	
	private void writeSvnLog() throws SVNException, IOException {
    	Set<String> listSvnLog = SubversionUtil.listLogEntries(this.reportModel.getRepositoryModel().getUrl(), this.reportModel.getRepositoryModel().getUsername(), this.reportModel.getRepositoryModel().getPassword());
		Path path = Paths.get(workspace + "/svn.log");
		listZipFile.add(path.toAbsolutePath().toString());
		try (
				BufferedWriter writer = Files.newBufferedWriter(path)
		) {
			for (String svnLog : listSvnLog) {
				writer.write(svnLog);
			}
		}
    }
    
    private void checkout() throws SVNException, IOException {
		for (String label : this.listLabelFile) {
			String url = "";
			if (this.reportModel.getRepositoryModel().getLabelType().equals(LabelType.BRANCH)) {
				url = this.reportModel.getRepositoryModel().getUrl() + "/branches/" + label;
			} else if (this.reportModel.getRepositoryModel().getLabelType().equals(LabelType.TAG)) {
				url = this.reportModel.getRepositoryModel().getUrl() + "/tags/" + label;
			}
			SubversionUtil.checkout(url, this.reportModel.getRepositoryModel().getUsername(), this.reportModel.getRepositoryModel().getPassword(),label);
		}
    }
    
    private void maven() throws InterruptedException, IOException {
    	for (String label : this.listLabelFile) {
    		String pomPath = label + "/" + this.reportModel.getLabelModel().getPom();
    		MavenUtil.execute(pomPath);
    	}
    }
    
    private void jdeps() throws InterruptedException, IOException {
    	for (String label : this.listLabelFile) {
			StringBuilder jarPath = new StringBuilder();
    		String separator = "";
			for (String jar : this.reportModel.getLabelModel().getListJar()) {
				jarPath.append(separator);
				jarPath.append(label);
				jarPath.append(jar);
				separator = " ";
			}
			String output = workspace + "/teste.log";
			JDepsUtil.execute(this.reportModel.getLabelModel().getPackagePath(), jarPath.toString(), output);
		}
    }
    
    private void compactFiles() {
    	// compactar E
    	//MOVER ARQUIVO
    }
    
    private void clearTemp() {
    	File file = new File(workspaceLabels);
    	file.delete();
    	for (String label : this.listLabelFile) {
    		file = new File(label);
    		file.delete();
    	}
    }
}
