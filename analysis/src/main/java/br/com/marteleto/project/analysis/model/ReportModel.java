package br.com.marteleto.project.analysis.model;

import java.io.Serializable;

public class ReportModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private RepositoryModel repositoryModel;
	private LabelModel labelModel;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public LabelModel getLabelModel() {
		if (labelModel == null) {
			labelModel = new LabelModel();
		}
		return labelModel;
	}
	public void setLabelModel(LabelModel labelModel) {
		this.labelModel = labelModel;
	}
}
