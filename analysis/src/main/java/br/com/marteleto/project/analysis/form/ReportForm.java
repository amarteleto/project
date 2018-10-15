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
		readonly,
		labelLbReportType,
		labelLbJarName,
		labelLbPom,
		labelLbPackagePath,
	}
	
	public boolean isReadonly() {
		return (Boolean) getStateHelper().eval(PropertyKeys.readonly,false);
	}
	public void setReadonly(boolean readonly) {
		getStateHelper().put(PropertyKeys.readonly, readonly);
	}
	public String getLabelLbReportType() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbReportType,this.getResourceBundleMap().get("labelLbReportType"));
	}
	public void setLabelLbReportType(String labelLbReportType) {
		getStateHelper().put(PropertyKeys.labelLbReportType, labelLbReportType);
	}
	public boolean isAnalysisFieldsRendered() {
		return this.getValue().getReportType().equals(ReportType.JDEPS_ANALYZE);
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
	public String getLabelLbPackagePath() {
		return (String) getStateHelper().eval(PropertyKeys.labelLbPackagePath,this.getResourceBundleMap().get("labelLbPackagePath"));
	}
	public void setLabelLbPackagePath(String labelLbPackagePath) {
		getStateHelper().put(PropertyKeys.labelLbPackagePath, labelLbPackagePath);
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