/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.eptsreports.reporting.library.indicators;

import org.openmrs.module.eptsreports.reporting.library.cohorts.SqlCohortQueries;
import org.openmrs.module.eptsreports.reporting.utils.EptsReportUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HivIndicators extends BaseIndicators {
	
	@Autowired
	private SqlCohortQueries scq;
	
	public CohortIndicator patientBelow1YearEnrolledInHIVStartedARTIndicator(CohortDefinition cohortDefinition) {
		return newCohortIndicator("patientBelow1YearEnrolledInHIVStartedARTIndicator",
		    EptsReportUtils.map(cohortDefinition, "onOrAfter=${startDate},onOrBefore=${endDate},effectiveDate=${endDate}"));
	}
	
	public CohortIndicator patientBetween1And9YearsEnrolledInHIVStartedARTIndicator(CohortDefinition cohortDefinition) {
		return newCohortIndicator("patientBelow1YearEnrolledInHIVStartedARTIndicator",
		    EptsReportUtils.map(cohortDefinition, "onOrAfter=${startDate},onOrBefore=${endDate},effectiveDate=${endDate}"));
	}
	
	public CohortIndicator patientInYearRangeEnrolledInHIVStartedARTIndicator(CohortDefinition cohortDefinition) {
		return newCohortIndicator("patientInYearRangeEnrolledInHIVStartedARTIndicator",
		    EptsReportUtils.map(cohortDefinition, "onOrAfter=${startDate},onOrBefore=${endDate},effectiveDate=${endDate}"));
	}
	
	public CohortIndicator patientEnrolledInHIVStartedARTIndicator(CohortDefinition cohortDefinition) {
		return newCohortIndicator("patientNewlyEnrolledInHIVIndicator",
		    EptsReportUtils.map(cohortDefinition, "onOrAfter=${startDate},onOrBefore=${endDate}"));
	}
	
	/**
	 * The number of patients having viral load within 12 months period denominator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsHavingViralLoadWithin12Months() {
		return newCohortIndicator("patientsHavingViralLoadWithin12Months",
		    EptsReportUtils.map(scq.getPatientsViralLoadWithin12Months(), "endDate=${endDate},location={location}"));
	}
	
	/**
	 * Number of adult and pediatric patients on ART with suppressed viral load results (<1,000
	 * copies/ml) Numerator
	 * 
	 * @return CohortIndicator
	 */
	public CohortIndicator patientsWithSuppressedViralLoadWithin12Months() {
		return newCohortIndicator("patientsWithSuppressedViralLoadWithin12Months", EptsReportUtils
		        .map(scq.getPatientsWithSuppressedViralLoadWithin12Months(), "endDate=${endDate},location={location}"));
	}
}
