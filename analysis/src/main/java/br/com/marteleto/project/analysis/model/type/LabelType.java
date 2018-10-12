package br.com.marteleto.project.analysis.model.type;

public enum LabelType {
	BRANCH("branch"), TAG("tag");
	private String description;
	LabelType(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}
	public static LabelType getType(String tipo){
		if (tipo != null) {
			if (BRANCH.description.equalsIgnoreCase(tipo)) {
				return LabelType.BRANCH;
			} else if (TAG.description.equalsIgnoreCase(tipo)) {
				return LabelType.TAG;
			}
		}
		return null;
	}	
}
