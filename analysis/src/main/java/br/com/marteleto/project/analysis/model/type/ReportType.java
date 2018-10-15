package br.com.marteleto.project.analysis.model.type;

public enum ReportType {
	REPOSITORY_LOG("repository_log"),
	JDEPS_ANALYZE("jdeps_analyze"), 
	;
	private String description;
	ReportType(String description){
		this.description = description;
	}
	public String getDescription(){
		return this.description;
	}
	public static ReportType getType(String tipo){
		if (tipo != null) {
			if (REPOSITORY_LOG.description.equalsIgnoreCase(tipo)) {
				return ReportType.REPOSITORY_LOG;
			} else if (JDEPS_ANALYZE.description.equalsIgnoreCase(tipo)) {
				return ReportType.JDEPS_ANALYZE;
			}
		}
		return null;
	}	
}
