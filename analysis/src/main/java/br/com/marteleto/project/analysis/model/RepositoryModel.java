package br.com.marteleto.project.analysis.model;

import java.io.Serializable;

import br.com.marteleto.project.analysis.model.type.LabelType;
import br.com.marteleto.project.analysis.model.type.RepositoryType;

public class RepositoryModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String url;
	private String label;
	private LabelType labelType;
	private RepositoryType repositoryType;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public LabelType getLabelType() {
		if (labelType == null) {
			labelType = LabelType.TRUNK;
		}
		return labelType;
	}
	public void setLabelType(LabelType labelType) {
		this.labelType = labelType;
	}
	public RepositoryType getRepositoryType() {
		if (repositoryType == null) {
			repositoryType = RepositoryType.SUBVERSION;
		}
		return repositoryType;
	}
	public void setRepositoryType(RepositoryType repositoryType) {
		this.repositoryType = repositoryType;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}