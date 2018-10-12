package br.com.marteleto.project.analysis.form;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.component.FacesComponent;
import javax.faces.model.SelectItem;

import br.com.marteleto.project.analysis.model.type.LabelType;

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
	
	public Set<SelectItem> getLabelTypeOptions() {
		Set<SelectItem> items = new LinkedHashSet<>();
		SelectItem selectItem = new SelectItem();
		selectItem.setLabel(LabelType.BRANCH.getDescription());
		selectItem.setValue(LabelType.BRANCH);
		items.add(selectItem);
		selectItem = new SelectItem();
		selectItem.setLabel(LabelType.TAG.getDescription());
		selectItem.setValue(LabelType.TAG);
		items.add(selectItem);
		return items;
	}
}