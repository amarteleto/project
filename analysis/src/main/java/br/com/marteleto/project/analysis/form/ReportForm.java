package br.com.marteleto.project.analysis.form;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.faces.component.FacesComponent;
import javax.faces.model.SelectItem;

import br.com.marteleto.project.analysis.model.ReportModel;
import br.com.marteleto.project.analysis.model.type.ReportType;
import br.com.marteleto.project.analysis.util.ResourceUtil;

@FacesComponent(value=ReportForm.COMPONENT_TYPE)
public class ReportForm extends AInput {
	private static final long serialVersionUID = 1L;
	public static final String COMPONENT_TYPE = "form.ReportForm";
	
	enum PropertyKeys {
		READONLY("readonly"),
		LABELLBREPORTTYPE("labelLbReportType"),
		LABELLBJARNAME("labelLbJarName"),
		LABELLBPOM("labelLbPom"),
		LABELLBPACKAGEPATH("labelLbPackagePath"),
		;
		
		private String description;
		PropertyKeys(String description){
			this.description = description;
		}
		
		@Override
		public String toString() {
			return this.description;
		}
	}
	
	public boolean isReadonly() {
		return (Boolean) getStateHelper().eval(PropertyKeys.READONLY,false);
	}
	public void setReadonly(boolean readonly) {
		getStateHelper().put(PropertyKeys.READONLY, readonly);
	}
	public String getLabelLbReportType() {
		return (String) getStateHelper().eval(PropertyKeys.LABELLBREPORTTYPE,this.getResourceBundleMap().get("labelLbReportType"));
	}
	public void setLabelLbReportType(String labelLbReportType) {
		getStateHelper().put(PropertyKeys.LABELLBREPORTTYPE, labelLbReportType);
	}
	public boolean isAnalysisFieldsRendered() {
		return this.getValue().getReportType().equals(ReportType.JDEPS_ANALYZE);
	}
	public String getLabelLbJarName() {
		return (String) getStateHelper().eval(PropertyKeys.LABELLBJARNAME,this.getResourceBundleMap().get("labelLbJarName"));
	}
	public void setLabelLbJarName(String labelLbJarName) {
		getStateHelper().put(PropertyKeys.LABELLBJARNAME, labelLbJarName);
	}
	public String getLabelLbPom() {
		return (String) getStateHelper().eval(PropertyKeys.LABELLBPOM,this.getResourceBundleMap().get("labelLbPom"));
	}
	public void setLabelLbPom(String labelLbPom) {
		getStateHelper().put(PropertyKeys.LABELLBPOM, labelLbPom);
	}
	public String getLabelLbPackagePath() {
		return (String) getStateHelper().eval(PropertyKeys.LABELLBPACKAGEPATH,this.getResourceBundleMap().get("labelLbPackagePath"));
	}
	public void setLabelLbPackagePath(String labelLbPackagePath) {
		getStateHelper().put(PropertyKeys.LABELLBPACKAGEPATH, labelLbPackagePath);
	}
	
	@Override
	public ReportModel getValue() {
		return (ReportModel) super.getValue();
	}
	
	public Set<SelectItem> getReportTypeOptions() {
		Set<SelectItem> items = new LinkedHashSet<>();
		SelectItem selectItem = new SelectItem();
		selectItem.setLabel(ResourceUtil.getResource("report_type_" + ReportType.REPOSITORY_LOG.getDescription()));
		selectItem.setValue(ReportType.REPOSITORY_LOG);
		items.add(selectItem);
		selectItem = new SelectItem();
		selectItem.setLabel(ResourceUtil.getResource("report_type_" + ReportType.JDEPS_ANALYZE.getDescription()));
		selectItem.setValue(ReportType.JDEPS_ANALYZE);
		items.add(selectItem);
		return items;
	}
}