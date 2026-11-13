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
     * Finds patients by ID or name, checking the ID first.
     *
     * @param keyword patient ID or full or partial name
     * @return matching patients, or an empty list
     */
    @Override
    public List<Person> findPatients(String keyword) {
        List<Person> patients = new ArrayList<>();

        if (isBlank(keyword)) {
            return patients;
        }

        String text = keyword.trim();
        Person patient = personDao.findById(text);

        if (patient != null) {
            patients.add(patient);
            return patients;
        }

        return personDao.findByName(text);
    }

    /**
     * Adds a medical record for an existing patient.
     *
     * @param patientId the patient's ID
     * @param medicalConditions the patient's medical conditions
     * @param allergies the patient's allergies
     * @return true if the record was added, otherwise false
     */
    @Override
    public boolean addMedicalRecord(String patientId,
                                    String medicalConditions,
                                    String allergies) {
        if (isBlank(patientId) || isBlank(medicalConditions)
                || isBlank(allergies)) {
            return false;
        }

        Person patient = new Person();
        patient.setId(patientId.trim());

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPatient(patient);
        medicalRecord.setMedicalConditions(medicalConditions.trim());
        medicalRecord.setAllergies(allergies.trim());

        return medicalRecordDao.addMedicalRecord(medicalRecord);
    }

    /**
     * Retrieves the records associated with a patient.
     *
     * @param patientId the patient's ID
     * @return the patient's medical records, or an empty list
     */
    @Override
    public List<MedicalRecord> findMedicalRecords(String patientId) {
        if (isBlank(patientId)) {
            return new ArrayList<>();
        }

        return medicalRecordDao.findByPatientId(patientId.trim());
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
