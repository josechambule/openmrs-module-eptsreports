package org.openmrs.module.eptsreports.reporting.library.queries;

public interface QualityImprovementCategory15QueriesInterface {

  class QUERY {

    public static final String findPatientsWhoAreNewlyEnrolledOnARTByAgeBetweenAgeRange(
        int startAge, int endAge) {

      final String sql =
          "SELECT patient_id FROM (                                                                                          "
              + "SELECT patient_id, MIN(art_start_date) art_start_date FROM (                                                               "
              + "SELECT p.patient_id, MIN(value_datetime) art_start_date FROM patient p                                                     "
              + "INNER JOIN encounter e ON p.patient_id=e.patient_id                                                                        "
              + "INNER JOIN obs o ON e.encounter_id=o.encounter_id                                                                          "
              + "WHERE p.voided=0 AND e.voided=0 AND o.voided=0 AND e.encounter_type=53                                                     "
              + "AND o.concept_id=1190 AND o.value_datetime is NOT NULL AND o.value_datetime<=:endInclusionDate AND e.location_id=:location "
              + "GROUP BY p.patient_id                                                                                               "
              + ") art_start GROUP BY patient_id) tx_new                                                                                    "
              + "INNER JOIN person pe ON tx_new.patient_id=pe.person_id                                                                     "
              + "WHERE (TIMESTAMPDIFF(year,birthdate,art_start_date)) BETWEEN %s AND %s AND birthdate IS NOT NULL and pe.voided = 0         "
              + "AND art_start_date BETWEEN :startInclusionDate AND :endInclusionDate                                                       ";
      return String.format(sql, startAge, endAge);
    }

    public static final String findPatientsFromFichaClinicaForGivenConceptsDenominadorCategoria15A =
        "select patient_id from (                                     "
            + "		 	select p.patient_id, e.encounter_datetime from patient p 			"
            + "		    	  join encounter e on e.patient_id  = p.patient_id   			"
            + "		          join obs o on o.encounter_id = e.encounter_id        	 		"
            + "		   	where p.voided =0 and e.voided =0 and o.voided =0            		"
            + "		     	and e.encounter_type = 6 and o.concept_id in(23724,23730) and o.value_coded =1256 		"
            + "		          and e.encounter_datetime between (:endRevisionDate - INTERVAL 14 MONTH) and (:endRevisionDate - INTERVAL 11 MONTH) and e.location_id =:location "
            + "		     group by p.patient_id                                                                                                                               "
            + "		     union                                                                                                                                                "
            + "		     select max_tipo_despensa.* from(																													"
            + "		     	select max_tl.patient_id, max_tl.max_datatl from (              																				"
            + "					select p.patient_id,max(o.obs_datetime) max_datatl 																							"
            + "					from patient p              																												"
            + "						join encounter e on p.patient_id=e.patient_id              																				"
            + "						join obs o on o.encounter_id=e.encounter_id              																				"
            + "					where e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 																		"
            + "						and o.concept_id=23739 and e.location_id=:location 																						"
            + "						and o.obs_datetime between (:endRevisionDate - INTERVAL 14 MONTH) and (:endRevisionDate - INTERVAL 11 MONTH) group by p.patient_id) max_tl "
            + "					join obs on obs.person_id=max_tl.patient_id and max_tl.max_datatl=obs.obs_datetime              												"
            + "					where obs.concept_id=23739 and obs.value_coded=23720 and obs.voided=0 and obs.location_id=:location 											"
            + "		             	) max_tipo_despensa) result                                                																	";

    public static final String
        findPatientsWithLastGaacOrLastDispensaTrimestralRegisteredInFichaClinicaWithinRevisionPeriodB1 =
            "select patient_id from(																											"
                + "		 select max_fim_gaac.patient_id, max_fim_gaac.max_fim from (              													"
                + "		 	select p.patient_id,max(o.obs_datetime) max_fim 																		"
                + "		 	from patient p              																							"
                + "		 		join encounter e on p.patient_id=e.patient_id              															"
                + "		 		join obs o on o.encounter_id=e.encounter_id              															"
                + "		 	where e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 													"
                + "		 		and o.concept_id=23724 and e.location_id=:location 																	"
                + "		 		and o.obs_datetime <= :endRevisionDate group by p.patient_id) max_fim_gaac 											"
                + "		 	join obs on obs.person_id=max_fim_gaac.patient_id and max_fim_gaac.max_fim=obs.obs_datetime              				"
                + "		 	where obs.concept_id=23724 and obs.value_coded=1267 and obs.voided=0 and obs.location_id=:location 						"
                + "		 union																														"
                + "		 select max_dispensa_trimestral.patient_id, max_dispensa_trimestral.max_fim from (              							"
                + "		 	select p.patient_id,max(o.obs_datetime) max_fim 																		"
                + "		 	from patient p              																							"
                + "		 		join encounter e on p.patient_id=e.patient_id              															"
                + "		 		join obs o on o.encounter_id=e.encounter_id              															"
                + "		 	where e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 													"
                + "		 		and o.concept_id=23730 and e.location_id=:location 																	"
                + "		 		and o.obs_datetime <=:endRevisionDate group by p.patient_id) max_dispensa_trimestral   								"
                + "		 	join obs on obs.person_id=max_dispensa_trimestral.patient_id and max_dispensa_trimestral.max_fim=obs.obs_datetime  		"
                + "		 	where obs.concept_id=23730 and obs.value_coded=1267 and obs.voided=0 and obs.location_id=:location        				"
                + "		 	union																													"
                + "		  select max_tipo_dispensa.patient_id, max_tipo_dispensa.max_fim from (              										"
                + "		 	select p.patient_id,max(o.obs_datetime) max_fim 																		"
                + "		 	from patient p              																							"
                + "		 		join encounter e on p.patient_id=e.patient_id              															"
                + "		 		join obs o on o.encounter_id=e.encounter_id              															"
                + "		 	where e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 													"
                + "		 		and o.concept_id=23739 and e.location_id=:location 																	"
                + "		 		and o.obs_datetime <= :endRevisionDate group by p.patient_id) max_tipo_dispensa   									"
                + "		 	join obs on obs.person_id=max_tipo_dispensa.patient_id and max_tipo_dispensa.max_fim=obs.obs_datetime  					"
                + "		 	where obs.concept_id=23739 and obs.value_coded <>23720 and obs.voided=0 and obs.location_id=:location) result          	";

    public static final String
        findPatientsWithLastTipoDeDispensaTrimestralInFichaClinicaWithinRevisionPeriodA3 =
            "select max_tipo_dispensa.patient_id from(																														"
                + "		select max_tl.patient_id, max_tl.max_datatl from (              																				"
                + "			select p.patient_id,max(o.obs_datetime) max_datatl 																							"
                + "			from patient p              																												"
                + "				join encounter e on p.patient_id=e.patient_id              																				"
                + "				join obs o on o.encounter_id=e.encounter_id              																				"
                + "			where e.encounter_type=6 and e.voided=0 and o.voided=0 and p.voided=0 																		"
                + "				and o.concept_id=23739 and e.location_id=:location 																						"
                + "				and o.obs_datetime between (:endRevisionDate - INTERVAL 14 MONTH) and (:endRevisionDate - INTERVAL 11 MONTH) group by p.patient_id) max_tl "
                + "			join obs on obs.person_id=max_tl.patient_id and max_tl.max_datatl=obs.obs_datetime              											"
                + "			where obs.concept_id=23739 and obs.value_coded=23720 and obs.voided=0 and obs.location_id=:location 			 						    "
                + "	        	) max_tipo_dispensa                																	                                   	";

    public static final String
        findPatientsWithDispensaTrimestralInicarInFichaClinicaDuringTheRevisionPeriodA2 =
            "select p.patient_id from patient p 	                                                                      "
                + "		join encounter e on e.patient_id  = p.patient_id   			                                  "
                + "  	join obs o on o.encounter_id = e.encounter_id        	 		                                  "
                + " where p.voided =0 and e.voided =0 and o.voided =0            		                              "
                + " 	and e.encounter_type = 6 and o.concept_id = 23730 and o.value_coded =1256 						  "
                + "  	and e.encounter_datetime between (:endRevisionDate - INTERVAL 14 MONTH) and (:endRevisionDate - INTERVAL 11 MONTH) "
                + "		and e.location_id =:location group by p.patient_id                                                   ";
  }
}
