package br.com.marteleto.project.analysis.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.primefaces.event.FlowEvent;

import br.com.marteleto.project.analysis.model.ReportModel;
import br.com.marteleto.project.analysis.runnable.ReportRunnable;

@ViewScoped
@Named
public class IndexController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String TAB_REPOSITORY = "tabRepository";
	private static final String TAB_LABEL = "tabLabel";
	private static final String TAB_RESUME = "tabResume";
	private static final List<Thread> listProcess = new ArrayList<>();
	private ReportModel model = new ReportModel();
	
	public ReportModel getModel() {
		return model;
	}
	public void setModel(ReportModel model) {
		this.model = model;
	}

	public List<Thread> getListProcess() {
		return listProcess;
	}
	
	public void preRenderView(ComponentSystemEvent event) {
	}
    
    public String getTabName(Integer order) {
    	if (order.equals(Integer.valueOf(1))) {
    		return TAB_REPOSITORY;
    	} else if (order.equals(Integer.valueOf(2))) {
    		return TAB_LABEL;
    	} else if (order.equals(Integer.valueOf(3))) {
    		return TAB_RESUME;
    	}
    	return "tabUnknown" + order;
    }
    
    public boolean isBtFinalizeDisabled() {
    	return false;
    }
    
	public void btFinalizeActionListener() {
		Thread thread = new Thread(new ReportRunnable(model),model.getName());
		thread.start();
		listProcess.add(thread);
    }
     
    public String wizardFlowListener(FlowEvent event) {
    	String tab = event.getNewStep();
    	if (tab.equals(TAB_REPOSITORY)) {
    		processTabRepository();
    	} else if (tab.equals(TAB_LABEL)) {
    		processTabLabel();
    	} else if (tab.equals(TAB_RESUME)) {
    		processTabResume();
    	}
    	return tab;
    }
    
    private void processTabRepository() {
    	this.getModel().getLabelModel().setListLabel(null);
    }
    
    private void processTabLabel() {
    }
    
    private void processTabResume() {
    }
}
