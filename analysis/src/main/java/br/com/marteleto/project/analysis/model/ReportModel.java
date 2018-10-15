package br.com.marteleto.project.analysis.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.marteleto.project.analysis.model.type.ReportType;
import br.com.marteleto.project.analysis.util.Constants;

public class ReportModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private ReportType reportType;
	private RepositoryModel repositoryModel;
	private List<String> listJar;
	private String packagePath;
	private String pom;
	public ReportType getReportType() {
		if (reportType == null) {
			reportType = ReportType.REPOSITORY_LOG;
		}
		return reportType;
	}
	public void setReportType(ReportType reportType) {
		this.reportType = reportType;
	}
	public RepositoryModel getRepositoryModel() {
		if (repositoryModel == null) {
			repositoryModel = new RepositoryModel();
		}
		return repositoryModel;
	}
	public void setRepositoryModel(RepositoryModel repositoryModel) {
		this.repositoryModel = repositoryModel;
	}
	public List<String> getListJar() {
		if (listJar == null) {
			listJar = new ArrayList<>();
		}
		return listJar;
	}
	public void setListJar(List<String> listJar) {
		this.listJar = listJar;
	}
	public String getPackagePath() {
		return packagePath;
	}
	public void setPackagePath(String packagePath) {
		this.packagePath = packagePath;
	}
	public String getPom() {
		return pom;
	}
	public void setPom(String pom) {
		this.pom = pom;
	}
	public String getName() {
		int pos = this.getRepositoryModel().getUrl().lastIndexOf(Constants.URL_SEPARATOR) + 1;
		String projectName = this.getRepositoryModel().getUrl().substring(pos, this.getRepositoryModel().getUrl().length());
		if (projectName.endsWith(".git")) {
			projectName = projectName.replaceAll(".git", "");
		}
		return projectName;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
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
		ReportModel other = (ReportModel) obj;
		if (getName() == null) {
			if (other.getName() != null)
				return false;
		} else if (!getName().equals(other.getName()))
			return false;
		return true;
	}
}