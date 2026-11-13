package com.rocs.rc.ohs.app.facade.medicalrecord.impl;

import com.rocs.rc.ohs.app.facade.medicalrecord.AddMedRecordFacade;
import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.dao.medicalrecord.AddMedRecordDao;
import com.rocs.rc.ohs.data.dao.medicalrecord.impl.AddMedRecordDaoImpl;
import com.rocs.rc.ohs.data.dao.person.PersonDao;
import com.rocs.rc.ohs.data.dao.person.impl.PersonDaoImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the operations for adding and viewing the medical records of
 * a patient.
 */
public class AddMedRecordFacadeImpl implements AddMedRecordFacade {

    /** The data access operations for persons. */
    private final PersonDao personDao;

    /** The data access operations for medical records. */
    private final AddMedRecordDao addMedRecordDao;

    /**
     * Creates a facade that uses the default data access objects.
     */
    public AddMedRecordFacadeImpl() {
        this(new PersonDaoImpl(), new AddMedRecordDaoImpl());
    }

    /**
     * Creates a facade that uses the given data access objects.
     *
     * @param newPersonDao the data access operations for persons.
     * @param newAddMedRecordDao the data access operations for medical
     *                           records.
     */
    public AddMedRecordFacadeImpl(PersonDao newPersonDao,
                                  AddMedRecordDao newAddMedRecordDao) {
        this.personDao = newPersonDao;
        this.addMedRecordDao = newAddMedRecordDao;
    }

    /**
     * Finds the patients that match a patient ID or a name. The patient ID
     * is checked first, then the name.
     *
     * @param keyword the patient ID, or the full or partial name.
     * @return the matching patients, or an empty list if there is no match.
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
     * Adds a new medical record for a patient. The medical record has no
     * consultation yet.
     *
     * @param patientId the ID of the patient.
     * @param medicalConditions the medical conditions of the patient.
     * @param allergies the allergies of the patient.
     * @return true if the medical record was added, otherwise false.
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

        return addMedRecordDao.addMedicalRecord(medicalRecord);
    }

    /**
     * Retrieves the medical records of a patient.
     *
     * @param patientId the ID of the patient.
     * @return the medical records of the patient, or an empty list if
     *         the patient has none.
     */
    @Override
    public List<MedicalRecord> findMedicalRecords(String patientId) {

        if (isBlank(patientId)) {
            return new ArrayList<>();
        }
        return addMedRecordDao.findByPatientId(patientId.trim());
    }

    /**
     * Checks whether a text is null or has no characters except spaces.
     *
     * @param text the text to check.
     * @return true if the text is blank, otherwise false.
     */
    private boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }
}
