package br.com.marteleto.project.analysis.model.type;

public enum LabelType {
	TRUNK("trunk"),
	BRANCH("branches"), 
	TAG("tags"),
	;
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
			} else if (TRUNK.description.equalsIgnoreCase(tipo)) {
				return LabelType.TRUNK;
			}
		}
		return null;
	}	
}
