package com.rocs.rc.ohs.app.facade.medicalrecord.impl;

import com.rocs.rc.ohs.app.facade.medicalrecord.MedicalRecordFacade;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.dao.medicalrecord.MedicalRecordDao;
import com.rocs.rc.ohs.data.dao.person.PersonDao;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the operations for managing patient medical records.
 */
public class MedicalRecordFacadeImpl implements MedicalRecordFacade {

    /** The data access operations for persons. */
    private final PersonDao personDao;

    /** The data access operations for medical records. */
    private final MedicalRecordDao medicalRecordDao;

    /**
     * Creates a facade that uses the given data access objects.
     *
     * @param newPersonDao the person data access object
     * @param newMedicalRecordDao the medical record data access object
     */
    public MedicalRecordFacadeImpl(PersonDao newPersonDao,
                                   MedicalRecordDao newMedicalRecordDao) {
        this.personDao = newPersonDao;
        this.medicalRecordDao = newMedicalRecordDao;
    }

    /**
     * Finds patients by name.
     *
     * @param keyword the full or partial patient name
     * @return matching patients, or an empty list
     */
    @Override
    public List<Person> findPatients(String keyword) {
        if (isBlank(keyword)) {
            return new ArrayList<>();
        }

        return personDao.findByName(keyword.trim());
    }

    /**
     * Adds a medical record for an existing patient.
     *
     * @param patientName the name of the patient
     * @param medicalConditions the patient's medical conditions
     * @param allergies the patient's allergies
     * @return true if the record was added, otherwise false
     */
    @Override
    public boolean addMedicalRecord(String patientName,
                                    String medicalConditions,
                                    String allergies) {
        if (isBlank(patientName) || isBlank(medicalConditions)
                || isBlank(allergies)) {
            return false;
        }

        List<Person> patients = personDao.findByName(patientName.trim());

        if (patients.size() != 1) {
            return false;
        }

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(patients.get(0));
        medicalRecord.setMedicalConditions(medicalConditions.trim());
        medicalRecord.setAllergies(allergies.trim());

        return medicalRecordDao.addMedicalRecord(medicalRecord);
    }

    /**
     * Retrieves the records associated with a patient by name.
     *
     * @param patientName the name of the patient
     * @return the patient's medical records, or an empty list
     */
    @Override
    public List<MedicalRecord> findMedicalRecords(String patientName) {
        if (isBlank(patientName)) {
            return new ArrayList<>();
        }

        return medicalRecordDao.findByPatientName(patientName.trim());
    }

    /**
     * Checks whether the supplied text is blank.
     *
     * @param text the value to check
     * @return true if null or empty after trimming
     */
    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
