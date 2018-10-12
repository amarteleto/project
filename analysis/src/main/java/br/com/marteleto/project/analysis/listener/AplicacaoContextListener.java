package br.com.marteleto.project.analysis.listener;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import br.com.marteleto.project.analysis.util.PropertyUtil;
import br.com.marteleto.project.analysis.util.ResourceUtil;

@WebListener
public class AplicacaoContextListener implements ServletContextListener, Serializable {
	private static final long serialVersionUID = 1L;
	protected static transient Logger log = Logger.getLogger(AplicacaoContextListener.class.getName());
	
    @Override
    public void contextInitialized(ServletContextEvent event) {
    	try {
			PropertyUtil.addProperty("local.properties");
			ResourceUtil.addResource("locale.messages");
		} catch (IOException ex) {
			log.log(Level.SEVERE, ex.getMessage(), ex);
		}
        log.log(Level.INFO, ResourceUtil.getResource("application.initialized"));
    }
     
    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	log.log(Level.INFO, ResourceUtil.getResource("application.destroyed"));
    }
}