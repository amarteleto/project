package br.com.marteleto.project.analysis.form;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.component.FacesComponent;
import javax.faces.model.SelectItem;

import br.com.marteleto.project.analysis.model.RepositoryModel;
import br.com.marteleto.project.analysis.model.type.LabelType;
import br.com.marteleto.project.analysis.model.type.RepositoryType;
import br.com.marteleto.project.analysis.util.FacesUtil;
import br.com.marteleto.project.analysis.util.ResourceUtil;
import br.com.marteleto.project.analysis.util.SubversionUtil;

@FacesComponent(value=RepositoryForm.COMPONENT_TYPE)
public class RepositoryForm extends AInput {
	private static final long serialVersionUID = 1L;
	public static final String COMPONENT_TYPE = "form.RepositoryForm";
	
	enum PropertyKeys {
		readonly,
		labelLbUrl,
		labelLbUsername,
		labelLbPassword,
		labelLbLabelType,
		labelLbRepositoryType,
		labelLbLabel,
	}
	
	public boolean isReadonly() {
		return (Boolean) getStateHelper().eval(PropertyKeys.readonly,false);
	}
	public void setReadonly(boolean readonly) {
		getStateHelper().put(PropertyKeys.readonly, readonly);
	}
	public String getLabelLbUrl() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbUrl,this.getResourceBundleMap().get("labelLbUrl"));
	}
	public void setLabelLbUrl(String labelLbUrl) {
		getStateHelper().put(PropertyKeys.labelLbUrl, labelLbUrl);
	}
	public String getLabelLbUsername() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbUsername,this.getResourceBundleMap().get("labelLbUsername"));
	}
	public void setLabelLbUsername(String labelLbUsername) {
		getStateHelper().put(PropertyKeys.labelLbUsername, labelLbUsername);
	}
	public String getLabelLbPassword() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbPassword,this.getResourceBundleMap().get("labelLbPassword"));
	}
	public void setLabelLbPassword(String labelLbPassword) {
		getStateHelper().put(PropertyKeys.labelLbPassword, labelLbPassword);
	}
	public String getLabelLbLabelType() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbLabelType,this.getResourceBundleMap().get("labelLbLabelType"));
	}
	public void setLabelLbLabelType(String labelLbLabelType) {
		getStateHelper().put(PropertyKeys.labelLbLabelType, labelLbLabelType);
	}
	public String getLabelLbRepositoryType() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbRepositoryType,this.getResourceBundleMap().get("labelLbRepositoryType"));
	}
	public void setLabelLbRepositoryType(String labelLbRepositoryType) {
		getStateHelper().put(PropertyKeys.labelLbRepositoryType, labelLbRepositoryType);
	}
	public String getLabelLbLabel() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbLabel,this.getResourceBundleMap().get("labelLbLabel"));
	}
	public void setLabelLbLabel(String labelLbLabel) {
		getStateHelper().put(PropertyKeys.labelLbLabel, labelLbLabel);
	}
	
	@Override
	public RepositoryModel getValue() {
		return (RepositoryModel) super.getValue();
	}
	
	public void cbLabelTypeListener() {
		this.getValue().setLabel(null);
	}
	
	public Set<SelectItem> getLabelTypeOptions() {
		Set<SelectItem> items = new LinkedHashSet<>();
		SelectItem selectItem = new SelectItem();
		selectItem.setLabel(ResourceUtil.getResource("repository_label_type_" + LabelType.TRUNK.getDescription()));
		selectItem.setValue(LabelType.TRUNK);
		items.add(selectItem);
		selectItem = new SelectItem();
		selectItem.setLabel(ResourceUtil.getResource("repository_label_type_" + LabelType.BRANCH.getDescription()));
		selectItem.setValue(LabelType.BRANCH);
		items.add(selectItem);
		selectItem = new SelectItem();
		selectItem.setLabel(ResourceUtil.getResource("repository_label_type_" + LabelType.TAG.getDescription()));
		selectItem.setValue(LabelType.TAG);
		items.add(selectItem);
		return items;
	}
	
	public Set<SelectItem> getRepositoryTypeOptions() {
		Set<SelectItem> items = new LinkedHashSet<>();
		SelectItem selectItem = new SelectItem();
		selectItem.setLabel(ResourceUtil.getResource("repository_type_" + RepositoryType.SUBVERSION.getDescription()));
		selectItem.setValue(RepositoryType.SUBVERSION);
		items.add(selectItem);
		selectItem = new SelectItem();
		selectItem.setLabel(ResourceUtil.getResource("repository_type_" + RepositoryType.GIT.getDescription()));
		selectItem.setValue(RepositoryType.GIT);
		items.add(selectItem);
		return items;
	}
	
	public Set<SelectItem> getRepositoryLabelOptions() {
    	SelectItem selectItem;
    	Set<SelectItem> items = new LinkedHashSet<>();
    	if (!this.isReadonly() && !this.getValue().getLabelType().equals(LabelType.TRUNK)) {
    		try {
    			Set<String> labels = SubversionUtil.listLabelEntries(this.getValue().getUrl(), this.getValue().getUsername(), this.getValue().getPassword(),this.getValue().getLabelType().getDescription());
    			if (!labels.isEmpty()) {
    				for (String label : labels) {
    					selectItem = new SelectItem();
    					selectItem.setLabel(label);
    					selectItem.setValue(label);
    					items.add(selectItem);
    				}
    			}
    		} catch (Exception ex) {
    			FacesUtil.exceptionProcess(ex);
    		}
		} else {
			selectItem = new SelectItem();
			if (this.getValue().getLabelType().equals(LabelType.TRUNK)) {
				selectItem.setLabel(ResourceUtil.getResource("repository_label_type_" + LabelType.TRUNK.getDescription()));
				selectItem.setValue(LabelType.TRUNK.getDescription());
			} else {
				selectItem.setLabel(this.getValue().getLabel());
				selectItem.setValue(this.getValue().getLabel());
			}
			items.add(selectItem);
		}
		return items;
	}
}