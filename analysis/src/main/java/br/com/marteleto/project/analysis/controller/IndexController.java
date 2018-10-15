package br.com.marteleto.project.analysis.controller;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.faces.event.ComponentSystemEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.primefaces.event.FlowEvent;

import br.com.marteleto.project.analysis.model.ReportModel;
import br.com.marteleto.project.analysis.runnable.ReportRunnable;
import br.com.marteleto.project.analysis.util.FacesUtil;
import br.com.marteleto.project.analysis.util.PropertyUtil;

@ViewScoped
@Named
public class IndexController implements Serializable {
	private static final long serialVersionUID = 1L;
	private static final String TAB_REPOSITORY = "tabRepository";
	private static final String TAB_RESUME = "tabResume";
	private static final Integer TAB_WIZARD = Integer.valueOf(0);
	private static final Integer TAB_PROGRESS = Integer.valueOf(1);
	private final String downloadPath = PropertyUtil.getProperty("download");
	private final transient Set<ReportRunnable> listProcess = new HashSet<>();
	
	private boolean autoUpdate = false;
	private ReportModel model = new ReportModel();
	private Integer tabActiveIndex;
	
	public ReportModel getModel() {
		return model;
	}
	public void setModel(ReportModel model) {
		this.model = model;
	}
	public boolean isAutoUpdate() {
		return autoUpdate;
	}
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}
	public Integer getTabActiveIndex() {
		if (tabActiveIndex == null) {
			tabActiveIndex = TAB_WIZARD;
		}
		return tabActiveIndex;
	}
	public void setTabActiveIndex(Integer tabActiveIndex) {
		this.tabActiveIndex = tabActiveIndex;
	}
	public Set<ReportRunnable> getListProcess() {
		Set<ReportRunnable> list = new HashSet<>();
		for (ReportRunnable reportRunnable : this.listProcess) {
			if (reportRunnable.isRunning()) {
				list.add(reportRunnable);
			}
		}
		
		return list;
	}
	public Set<File> getListReport() {
		Set<File> list = new HashSet<>();
		File file = new File(downloadPath);
		if (file.exists()) {
			File[] files = file.listFiles(File::isFile);
			if (files != null && files.length > 0) {
				list.addAll(Arrays.asList(files));
			}
		}
		return list;
	}
	
	public void preRenderView(ComponentSystemEvent event) {
		//preRenderView
	}
    
    public String getTabName(Integer order) {
    	if (order.equals(Integer.valueOf(1))) {
    		return TAB_REPOSITORY;
    	} else if (order.equals(Integer.valueOf(2))) {
    		return TAB_RESUME;
    	}
    	return "tabUnknown" + order;
    }
    
    public boolean isBtFinalizeDisabled() {
    	return false;
    }
    
	public void btFinalizeActionListener() {
		ReportRunnable reportRunnable = new ReportRunnable(model);
		Thread thread = new Thread(reportRunnable, reportRunnable.getName());
		thread.start();
		this.listProcess.add(reportRunnable);
		this.setAutoUpdate(true);
		this.setTabActiveIndex(TAB_PROGRESS);
    }
	
	public boolean isPoAutoUpdateAutoStart() {
		return this.isAutoUpdate();
	}
     
    public String wizardFlowListener(FlowEvent event) {
    	String tab = event.getNewStep();
    	if (tab.equals(TAB_REPOSITORY)) {
    		processTabRepository();
    	} else if (tab.equals(TAB_RESUME)) {
    		processTabResume();
    	}
    	return tab;
    }
    
    public void btDownloadActionListener(String name) {
    	try {
    		File file = new File(downloadPath + File.separator + name);
    		Faces.sendFile(file, true);
    	} catch (Exception ex) {
			FacesUtil.exceptionProcess(ex);
		}
    }
    
    public void btCancelActionListener(ReportRunnable reportRunnable) {
    	for (Thread thread : Thread.getAllStackTraces().keySet()) {
			if (thread.getName().equals(reportRunnable.getName())) {
				thread.interrupt();
				break;
			}
		}
    }
    
    public void btDeleteActionListener(String name) {
    	try {
   			Files.delete(Paths.get(downloadPath + File.separator + name));
    	} catch (Exception ex) {
			FacesUtil.exceptionProcess(ex);
		}
    }
    
    private void processTabRepository() {
    	this.getModel().getRepositoryModel().setLabel(null);
    }
    
    private void processTabResume() {
    	//processTabResume
    }
}
