package br.com.marteleto.project.analysis.form;

import javax.faces.component.FacesComponent;

@FacesComponent(value=ReportForm.COMPONENT_TYPE)
public class ReportForm extends AInput {
	private static final long serialVersionUID = 1L;
	public static final String COMPONENT_TYPE = "form.ReportForm";
	
	enum PropertyKeys {
		readonly,
		labelLbName,
	}
	
	public boolean isReadonly() {
		return (Boolean) getStateHelper().eval(PropertyKeys.readonly,false);
	}
	public void setReadonly(boolean readonly) {
		getStateHelper().put(PropertyKeys.readonly, readonly);
	}
	public String getLabelLbName() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbName,this.getResourceBundleMap().get("labelLbName"));
	}
	public void setLabelLbName(String labelLbName) {
		getStateHelper().put(PropertyKeys.labelLbName, labelLbName);
	}
}