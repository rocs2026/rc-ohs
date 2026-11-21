package com.rocs.rc.ohs.data.dao.consultation;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.app.model.personnel.Personnel;

import java.util.List;

/**
 * Defines the data access operations for consultations.
 */
public interface ConsultationDao {

    /**
     * Saves a new consultation so the clinic staff can find it later in
     * the patient's medical records.
     *
     * @param consultation The consultation to save. Its ID must be set.
     * @return The saved {@link Consultation}.
     */
    Consultation add(Consultation consultation);

    /**
     * Retrieves the consultations of a patient, latest first, so they
     * can be shown in the patient's medical records.
     *
     * @param patientId The UUID of the patient.
     * @return A list of consultations of the patient.
     */
    List<Consultation> findByPatientId(String patientId);

    /**
     * Finds the persons with the given first and last name, so the staff
     * can pick the patient of a consultation by name.
     *
     * @param firstName The first name of the patient.
     * @param lastName The last name of the patient.
     * @return A list of the persons with that name.
     */
    List<Person> findPatientsByName(String firstName, String lastName);

    /**
     * Finds the personnel of the login account with the given username.
     *
     * @param username The username of the account that logged in.
     * @return A list with the personnel of the account.
     */
    List<Personnel> findStaffByUsername(String username);
}
