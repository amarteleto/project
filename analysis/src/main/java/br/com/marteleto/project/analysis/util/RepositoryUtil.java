package br.com.marteleto.project.analysis.util;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class RepositoryUtil implements Serializable {
	private static final long serialVersionUID = 1L;

	public static int countLines(String str){
		if (str != null && !"".equals(str.trim())) {
			String[] lines = str.split("\r\n|\r|\n");
			return  lines.length;
		}
		return 1;
	}
	
	
	public static String createRepositoryLog(Object revision, String author, Date date, String message, Map<String, String> files) {
		StringBuilder log = new StringBuilder();
        log.append("------------------------------------------------------------------------");
        log.append(Constants.NEW_LINE);
        log.append("r");
        log.append(revision);
        log.append(" | ");
        log.append(author);
        log.append(" | ");
        log.append(DateUtil.convertDateToString("yyyy-MM-dd HH:mm:ss Z (E, dd MMM yyyy)",date));
        log.append(" | ");
        log.append(countLines(message));
        log.append(" lines");
        if (!files.isEmpty()) {
        	log.append(Constants.NEW_LINE + "Changed paths:");
            for (Entry<String, String> entry : files.entrySet()) {
                log.append(Constants.NEW_LINE + "   " + entry.getKey() + " " + entry.getValue());
            }
        }
        log.append(Constants.NEW_LINE);
        log.append(Constants.NEW_LINE);
        log.append(message);
        log.append(Constants.NEW_LINE);
		return log.toString();
	}
}
