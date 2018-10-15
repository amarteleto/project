package br.com.marteleto.project.analysis.util;

import java.io.Serializable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

public class FacesUtil implements Serializable {
	private static final long serialVersionUID = 1L;
	protected static final transient Logger log = Logger.getLogger(FacesUtil.class.getName());
	
	public static String getResource(String chave) {
		return FacesUtil.getResource(chave, null);
	}
	
	public static String getResource(String chave, String[] params) {
		String message = chave;
		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			String messageBundleName = facesContext.getApplication().getMessageBundle();
			Locale locale = facesContext.getViewRoot().getLocale();
			ResourceBundle bundle = ResourceBundle.getBundle(messageBundleName, locale);
			message = bundle.getString(chave);
			if (params != null  && params.length > 0) {
				int cont=0;
				for (String param : params) {
					message = message.replace("{" + cont + "}", param);
					cont++;
				}
			}
		} catch (Exception ex) {
			log.log(Level.INFO, ex.getMessage(), ex);
		}
		return message;
	}
	public static void showMessageWarn(String message) {
		FacesUtil.showMessageWarn(null,message);
	}
	
	public static void showMessageInfo(String message) {
		FacesUtil.showMessageInfo(null,message);
	}
	
	public static void showMessageError(String message) {
		FacesUtil.showMessageError(null,message);
	}
	
	public static void showMessageWarn(String title, String message) {
		FacesUtil.showMessageWarn(null,title,message,null);
	}
	
	public static void showMessageInfo(String title,String message) {
		FacesUtil.showMessageInfo(null,title,message,null);
	}
	
	public static void showMessageError(String title,String message) {
		FacesUtil.showMessageError(null,title,message,null);
	}
	
	public static void showMessageWarn(String title, String message, String[] params) {
		FacesUtil.showMessageWarn(null,title,message,params);
	}
	
	public static void showMessageInfo(String title,String message, String[] params) {
		FacesUtil.showMessageInfo(null,title,message,params);
	}
	
	public static void showMessageError(String title,String message, String[] params) {
		FacesUtil.showMessageError(null,title,message,params);
	}
	
	public static void showMessageWarn(String client,String title,String message) {
		FacesUtil.showMessage(FacesMessage.SEVERITY_WARN,client,title,message);
	}
	
	public static void showMessageInfo(String client,String title,String message) {
		FacesUtil.showMessage(FacesMessage.SEVERITY_INFO,client,title,message);
	}
	
	public static void showMessageError(String client,String title,String message) {
		FacesUtil.showMessage(FacesMessage.SEVERITY_ERROR,client,title,message);
	}
	
	public static void showMessageWarn(String client,String title,String message,String[] params) {
		FacesUtil.showMessage(FacesMessage.SEVERITY_WARN,client,title,message, params);
	}
	
	public static void showMessageInfo(String client,String title,String message, String[] params) {
		FacesUtil.showMessage(FacesMessage.SEVERITY_INFO,client,title,message, params);
	}
	
	public static void showMessageError(String client,String title,String message, String[] params) {
		FacesUtil.showMessage(FacesMessage.SEVERITY_ERROR,client,title,message, params);
	}
	
	public static void showMessage(Severity severidade,String client, String title,String message) {
		FacesUtil.showMessage(severidade, client, title, message, null);
	}
	
	public static void showMessage(Severity severidade,String client, String title,String message, String[] params) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Flash flash = facesContext.getExternalContext().getFlash();
		flash.setKeepMessages(true);
		flash.setRedirect(true);
		facesContext.addMessage(client, new FacesMessage(severidade,FacesUtil.getResource(title),FacesUtil.getResource(message,params)));
	}
	
	public static void exceptionProcess(Exception ex) {
		FacesUtil.exceptionProcess(ex,null);
	}
	
	public static void exceptionProcess(String message) {
		FacesUtil.exceptionProcess(null,message);
	}
	
	public static void exceptionProcess(Exception ex, String message) {
		if (ex != null && ex.getMessage() != null) {
			message = ex.getMessage();
		}
		if (message == null || "".equals(message.trim())) {
			message = "falha_nao_mapeada";
		}
		FacesUtil.showMessageError(null, message);
		log.log(Level.SEVERE, message, ex);
	}
}
