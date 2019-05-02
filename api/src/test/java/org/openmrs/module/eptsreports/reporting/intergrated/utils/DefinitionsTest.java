package org.openmrs.module.eptsreports.reporting.intergrated.utils;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import org.openmrs.Location;
import org.openmrs.api.context.Context;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.service.DataSetDefinitionService;
import org.openmrs.module.reporting.evaluation.Definition;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.indicator.dimension.CohortDefinitionDimension;
import org.openmrs.module.reporting.indicator.dimension.CohortDimensionResult;
import org.openmrs.module.reporting.indicator.dimension.service.DimensionService;
import org.openmrs.test.BaseModuleContextSensitiveTest;

public abstract class DefinitionsTest extends BaseModuleContextSensitiveTest {

  protected void addParameters(CohortDefinition cd) {
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));
  }

  protected void addParameters(DataSetDefinition cd) {
    cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
    cd.addParameter(new Parameter("endDate", "End Date", Date.class));
    cd.addParameter(new Parameter("location", "Location", Location.class));
  }

  protected void setParameters(
      Date startDate, Date endDate, Location location, EvaluationContext context) {
    context.addParameterValue("startDate", startDate);
    context.addParameterValue("endDate", endDate);
    context.addParameterValue("location", location);
  }

  protected Date getStartDate() {
    return DateUtil.getDateTime(1930, 1, 1);
  }

  protected Date getEndDate() {
    return DateUtil.getDateTime(2019, 4, 26);
  }

  protected Location getLocation() {
    return Context.getLocationService().getLocation(1);
  }

  protected DataSet evaluateDatasetDefinition(DataSetDefinition cd) throws EvaluationException {
    EvaluationContext context = new EvaluationContext();
    context.addParameterValue("startDate", getStartDate());
    context.addParameterValue("endDate", getEndDate());
    context.addParameterValue("location", getLocation());
    addParameters(cd);
    return Context.getService(DataSetDefinitionService.class).evaluate(cd, context);
  }

  protected EvaluatedCohort evaluateCodedObsCohortDefinition(CohortDefinition cd)
      throws EvaluationException {
    addParameters(cd);
    EvaluationContext context = new EvaluationContext();
    context.addParameterValue("onOrAfter", getStartDate());
    context.addParameterValue("onOrBefore", getEndDate());
    context.addParameterValue("locationList", getLocation());
    return Context.getService(CohortDefinitionService.class).evaluate(cd, context);
  }

  protected EvaluatedCohort evaluateCalculationCohortDefinition(CohortDefinition cd)
      throws EvaluationException {
    addParameters(cd);
    EvaluationContext context = new EvaluationContext();
    context.addParameterValue("onOrAfter", DateUtil.getDateTime(2019, 2, 6));
    context.addParameterValue("onOrBefore", getEndDate());
    context.addParameterValue("location", getLocation());
    return Context.getService(CohortDefinitionService.class).evaluate(cd, context);
  }

  protected EvaluatedCohort evaluateCohortDefinition(CohortDefinition cd)
      throws EvaluationException {
    addParameters(cd);
    EvaluationContext context = new EvaluationContext();

    setParameters(getStartDate(), getEndDate(), getLocation(), context);
    return Context.getService(CohortDefinitionService.class).evaluate(cd, context);
  }

  protected EvaluatedCohort evaluateCohortDefinition(
      CohortDefinition cd, Date startDate, Date endDate, Location location)
      throws EvaluationException {
    addParameters(cd);
    EvaluationContext context = new EvaluationContext();

    setParameters(startDate, endDate, location, context);
    return Context.getService(CohortDefinitionService.class).evaluate(cd, context);
  }

  protected EvaluatedCohort evaluateCohortDefinition(
      CohortDefinition cd, Map<Parameter, Object> parameters) throws EvaluationException {
    EvaluationContext context = getEvaluationContext(cd, parameters);
    return Context.getService(CohortDefinitionService.class).evaluate(cd, context);
  }

  private EvaluationContext getEvaluationContext(Definition cd, Map<Parameter, Object> parameters) {
    EvaluationContext context = new EvaluationContext();
    if (parameters != null) {
      Iterator it = parameters.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry p = (Map.Entry) it.next();
        Parameter parameter = (Parameter) p.getKey();
        cd.addParameter(parameter);
        context.addParameterValue(parameter.getName(), p.getValue());
      }
    }
    return context;
  }

  protected CohortDimensionResult evaluateCohortDefinitionDimension(
      CohortDefinitionDimension cohortDefinitionDimension, Map<Parameter, Object> parameters)
      throws EvaluationException {
    EvaluationContext context = getEvaluationContext(cohortDefinitionDimension, parameters);
    return (CohortDimensionResult)
        Context.getService(DimensionService.class).evaluate(cohortDefinitionDimension, context);
  }
}
