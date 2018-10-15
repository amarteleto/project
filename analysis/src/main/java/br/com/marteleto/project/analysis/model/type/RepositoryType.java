package br.com.marteleto.project.analysis.model.type;

public enum RepositoryType {
	GIT("git"),
	SUBVERSION("subversion"), 
	;
	private String description;
	RepositoryType(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}
	public static RepositoryType getType(String tipo){
		if (tipo != null) {
			if (SUBVERSION.description.equalsIgnoreCase(tipo)) {
				return RepositoryType.SUBVERSION;
			} else if (GIT.description.equalsIgnoreCase(tipo)) {
				return RepositoryType.GIT;
			}
		}
		return null;
	}	
}
