package br.com.marteleto.project.analysis.model;

import java.io.Serializable;

import br.com.marteleto.project.analysis.model.type.LabelType;

public class RepositoryModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private String username;
	private String password;
	private String url;
	private LabelType labelType;
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
		return labelType;
	}
	public void setLabelType(LabelType labelType) {
		this.labelType = labelType;
	}
}