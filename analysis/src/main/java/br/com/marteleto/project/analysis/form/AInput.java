package br.com.marteleto.project.analysis.form;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.component.NamingContainer;
import javax.faces.component.UIInput;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

import br.com.marteleto.project.analysis.util.FacesUtil;

public abstract class AInput extends UIInput implements NamingContainer, Serializable {
	private static final long serialVersionUID = 1L;
	protected final transient Logger log = Logger.getLogger(this.getClass().getName());
	public static final String COMPONENT_FAMILY = UINamingContainer.COMPONENT_FAMILY;
	
	@Override
    public String getFamily() {
        return AInput.COMPONENT_FAMILY;
    }
	
	@Override
	public void encodeBegin(FacesContext context) throws IOException {
		try {
			this.processComponent(context);
		} catch (Exception ex) {
			FacesUtil.exceptionProcess(ex, "Falha ao processar o componente: " + this.getId());
		}
		super.encodeBegin(context);
	}

	public void processComponent(FacesContext context) {
		context.getCurrentPhaseId();
		log.log(Level.INFO, "{0} processComponent", this.getId());
	}
}