package br.com.marteleto.project.analysis.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class LabelModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Set<String> listLabel;
	private List<String> listJar;
	private String packagePath;
	private String pom;
	public Set<String> getListLabel() {
		if (listLabel == null) {
			listLabel = new LinkedHashSet<>();
		}
		return listLabel;
	}
	public void setListLabel(Set<String> listLabel) {
		this.listLabel = listLabel;
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
}