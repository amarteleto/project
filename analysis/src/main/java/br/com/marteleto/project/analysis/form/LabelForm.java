package br.com.marteleto.project.analysis.form;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.faces.component.FacesComponent;
import javax.faces.context.FacesContext;

import org.primefaces.model.DualListModel;

import br.com.marteleto.project.analysis.model.LabelModel;
import br.com.marteleto.project.analysis.model.RepositoryModel;
import br.com.marteleto.project.analysis.model.type.LabelType;
import br.com.marteleto.project.analysis.util.FacesUtil;
import br.com.marteleto.project.analysis.util.SubversionUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
@FacesComponent(value=LabelForm.COMPONENT_TYPE)
public class LabelForm extends AInput {
	private static final long serialVersionUID = 1L;
	public static final String COMPONENT_TYPE = "form.LabelForm";
	
	enum PropertyKeys {
		readonly,
		repositoryModel,
		labelLbAvailable,
		labelLbSelected,
		labelLbJarName,
		labelLbPom,
		labels,
	}
	
	public boolean isReadonly() {
		return (Boolean) getStateHelper().eval(PropertyKeys.readonly,false);
	}
	public void setReadonly(boolean readonly) {
		getStateHelper().put(PropertyKeys.readonly, readonly);
	}
	public RepositoryModel getRepositoryModel() {
		return (RepositoryModel) getStateHelper().eval(PropertyKeys.repositoryModel);
	}
	public void setRepositoryModel(RepositoryModel repositoryModel) {
		getStateHelper().put(PropertyKeys.repositoryModel, repositoryModel);
	}
	public String getLabelLbAvailable() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbAvailable,this.getResourceBundleMap().get("labelLbAvailable"));
	}
	public void setLabelLbAvailable(String labelLbAvailable) {
		getStateHelper().put(PropertyKeys.labelLbAvailable, labelLbAvailable);
	}
	public String getLabelLbSelected() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbSelected,this.getResourceBundleMap().get("labelLbSelected"));
	}
	public void setLabelLbSelected(String labelLbSelected) {
		getStateHelper().put(PropertyKeys.labelLbSelected, labelLbSelected);
	}
	public String getLabelLbJarName() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbJarName,this.getResourceBundleMap().get("labelLbJarName"));
	}
	public void setLabelLbJarName(String labelLbJarName) {
		getStateHelper().put(PropertyKeys.labelLbJarName, labelLbJarName);
	}
	public String getLabelLbPom() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbPom,this.getResourceBundleMap().get("labelLbPom"));
	}
	public void setLabelLbPom(String labelLbPom) {
		getStateHelper().put(PropertyKeys.labelLbPom, labelLbPom);
	}
	public DualListModel<String> getLabels() {
		return (DualListModel) getStateHelper().eval(PropertyKeys.labels,new DualListModel<>(new ArrayList<>(), new ArrayList<>()));
    }
    public void setLabels(DualListModel<String> labels) {
        getStateHelper().put(PropertyKeys.labels, labels);
        LabelModel model = this.getValue();
        model.setListLabel(new LinkedHashSet<>());
        if (!labels.getTarget().isEmpty()) {
    		model.getListLabel().addAll(this.getLabels().getTarget());
    	}
    }
    
    @Override
    public LabelModel getValue() {
    	return (LabelModel) super.getValue();
    }
    
    @Override
    public void processComponent(FacesContext context) throws Exception {
    	super.processComponent(context);
    	try {
    		Set<String> items = new LinkedHashSet<>();
    		if (!this.isReadonly()) {
	    		if (this.getRepositoryModel().getLabelType().equals(LabelType.BRANCH)) {
	    			items = SubversionUtil.listBranchesEntries(this.getRepositoryModel().getUrl(), this.getRepositoryModel().getUsername(), this.getRepositoryModel().getPassword());
	    		} else if (this.getRepositoryModel().getLabelType().equals(LabelType.TAG)) {
	    			items = SubversionUtil.listTagsEntries(this.getRepositoryModel().getUrl(), this.getRepositoryModel().getUsername(), this.getRepositoryModel().getPassword());
	    		}
    		}
    		List<String> source = new ArrayList<>();
    		source.addAll(items);
    		List<String> target = new ArrayList<>();
    		target.addAll(this.getValue().getListLabel());
    		this.setLabels(new DualListModel<>(source, target));
		} catch (Exception ex) {
			FacesUtil.exceptionProcess(ex);
		}
    }
}