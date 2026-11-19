package com.rocs.rc.ohs.app.facade.consultation.impl;

import com.rocs.rc.ohs.app.model.consultation.Consultation;
import com.rocs.rc.ohs.app.model.enums.recordtype.RecordType;
import com.rocs.rc.ohs.app.model.medicine.MedicineStock;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.app.model.personnel.Personnel;
import com.rocs.rc.ohs.app.model.prescription.Prescription;
import com.rocs.rc.ohs.data.dao.consultation.ConsultationDao;
import com.rocs.rc.ohs.data.dao.prescription.PrescriptionDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link ConsultationFacadeImpl} class without a real database.
 */
class ConsultationFacadeImplTest {

    private static final String JOWAN_ID =
            "f04cac48-889d-4a62-9a2b-a274406ece20";

    private static final String ANTONIO_ID =
            "6e00132d-76c0-4c86-82c2-53ba0211aaf0";

    private static final String LOGIN_STAFF_ID =
            "b74de6ca-b83f-4c55-b896-9f517bbeac2f";

    private static final String STAFF_ID =
            "64ffaffe-79b5-4690-bbd4-587507b6859c";

    private static final String STOCK_ID =
            "70aaf305-a7f5-41f1-bd0a-6ad73c042be9";

    private static final String CONSULTATION_ID =
            "a451a504-a3d4-4721-bf8d-a18b8c199356";

    private static final LocalDateTime DATE =
            LocalDateTime.of(2026, 10, 20, 9, 30);

    private static final String DIAGNOSIS = "Bacterial throat infection";

    private static final String TREATMENT = "Antibiotic course and rest";

    private ConsultationDao consultationDao;

    private PrescriptionDao prescriptionDao;

    private ConsultationFacadeImpl facade;

    @BeforeEach
    void setUp() {
        consultationDao = mock(ConsultationDao.class);
        prescriptionDao = mock(PrescriptionDao.class);

        facade = new ConsultationFacadeImpl(consultationDao,
                prescriptionDao);
    }

    /**
     * Test save consultation with prescriptions.
     */
    @Test
    void testSaveConsultation() {
        Consultation consultation =
                newConsultation(null, DIAGNOSIS, TREATMENT);
        Prescription prescription =
                newPrescription("500mg", "3 times a day", 2);

        Consultation saved = facade.saveConsultation(consultation,
                List.of(prescription));

        verify(consultationDao).add(consultation);
        verify(prescriptionDao).add(prescription);
        assertNotNull(saved.getId());
        assertEquals(JOWAN_ID, saved.getPatient().getId());
        assertEquals(STAFF_ID, saved.getStaff().getId());
        assertEquals(DATE, saved.getConsultationDate());
        assertEquals(DIAGNOSIS, saved.getDiagnosis());
        assertEquals(TREATMENT, saved.getTreatment());

        assertNotNull(prescription.getId());
        assertEquals(saved, prescription.getConsultation());
        assertEquals(STAFF_ID, prescription.getPersonnel().getId());
        assertEquals(STOCK_ID, prescription.getMedicineStock().getId());
        assertEquals("500mg", prescription.getDose());
        assertEquals("3 times a day", prescription.getFrequency());
        assertEquals(2, prescription.getQuantityToDispense());
    }

    /**
     * Test find consultations of a patient.
     */
    @Test
    void testFindRecords() {
        Consultation consultation =
                newConsultation(CONSULTATION_ID, DIAGNOSIS, TREATMENT);
        Prescription prescription =
                newPrescription("500mg", "3 times a day", 2);
        when(consultationDao.findByPatientId(JOWAN_ID))
                .thenReturn(List.of(consultation));
        when(prescriptionDao.findByConsultationId(CONSULTATION_ID))
                .thenReturn(List.of(prescription));

        List<Consultation> consultations =
                facade.getConsultationsByPatient(JOWAN_ID);
        List<Prescription> prescriptions =
                facade.getPrescriptionsByConsultation(CONSULTATION_ID);

        assertEquals(1, consultations.size());
        assertEquals(CONSULTATION_ID, consultations.get(0).getId());
        assertEquals(DIAGNOSIS, consultations.get(0).getDiagnosis());
        assertEquals(TREATMENT, consultations.get(0).getTreatment());
        assertEquals(1, prescriptions.size());
        assertEquals("500mg", prescriptions.get(0).getDose());
        assertEquals(2, prescriptions.get(0).getQuantityToDispense());
    }

    /**
     * Test find patient by full name.
     */
    @Test
    void testFindPatient() {
        when(consultationDao.findPatientsByName("Jowan", "Quirona"))
                .thenReturn(List.of(
                        newPerson(JOWAN_ID, "Jowan", "Carl", "Quirona")));

        Person patient = facade.findPatientByName("Jowan Carl Quirona");

        assertEquals(JOWAN_ID, patient.getId());
    }

    /**
     * Test find patient without a middle name.
     */
    @Test
    void testFindNoMiddle() {
        when(consultationDao.findPatientsByName("Antonio", "Ferrer"))
                .thenReturn(List.of(
                        newPerson(ANTONIO_ID, "Antonio", null, "Ferrer")));

        Person patient = facade.findPatientByName("Antonio Ferrer");

        assertEquals(ANTONIO_ID, patient.getId());
    }

    /**
     * Test find patient with an unknown name.
     */
    @Test
    void testFindUnknown() {
        when(consultationDao.findPatientsByName("Maria", "Santos"))
                .thenReturn(List.of());

        assertThrows(IllegalArgumentException.class,
                () -> facade.findPatientByName("Maria Santos"));
    }

    /**
     * Test find patient with a shared name.
     */
    @Test
    void testFindShared() {
        when(consultationDao.findPatientsByName("Jowan", "Quirona"))
                .thenReturn(List.of(
                        newPerson(JOWAN_ID, "Jowan", "Carl", "Quirona"),
                        newPerson(ANTONIO_ID, "Jowan", "Dee", "Quirona")));

        assertThrows(IllegalArgumentException.class,
                () -> facade.findPatientByName("Jowan Quirona"));
    }

    /**
     * Test find patient with one word only.
     */
    @Test
    void testFindOneWord() {
        assertThrows(IllegalArgumentException.class,
                () -> facade.findPatientByName("Jowan"));
    }

    /**
     * Test find staff by username.
     */
    @Test
    void testFindStaff() {
        Personnel staff = new Personnel();
        staff.setId(LOGIN_STAFF_ID);
        when(consultationDao.findStaffByUsername("staff"))
                .thenReturn(List.of(staff));

        assertEquals(LOGIN_STAFF_ID,
                facade.findStaffByUsername("staff").getId());
    }

    /**
     * Test find medicine stock by name.
     */
    @Test
    void testFindStock() {
        MedicineStock stock = new MedicineStock();
        stock.setId(STOCK_ID);
        when(prescriptionDao.findStocksByMedicineName("Amoxicillin 500mg"))
                .thenReturn(List.of(stock));

        assertEquals(STOCK_ID,
                facade.findMedicineStockByName("Amoxicillin 500mg").getId());
    }

    private Person newPerson(String id, String firstName, String middleName,
                             String lastName) {
        Person person = new Person();
        person.setId(id);
        person.setFirstName(firstName);
        person.setMiddleName(middleName);
        person.setLastName(lastName);
        return person;
    }

    private Consultation newConsultation(String id, String diagnosis,
                                         String treatment) {
        Person patient = new Person();
        patient.setId(JOWAN_ID);
        Personnel staff = new Personnel();
        staff.setId(STAFF_ID);
        Consultation consultation = new Consultation();
        consultation.setId(id);
        consultation.setPatient(patient);
        consultation.setStaff(staff);
        consultation.setRecordType(RecordType.CHECK_UP);
        consultation.setConsultationDate(DATE);
        consultation.setDiagnosis(diagnosis);
        consultation.setTreatment(treatment);
        return consultation;
    }

    private Prescription newPrescription(String dose, String frequency,
                                         int quantity) {
        MedicineStock stock = new MedicineStock();
        stock.setId(STOCK_ID);
        Prescription prescription = new Prescription();
        prescription.setMedicineStock(stock);
        prescription.setDose(dose);
        prescription.setFrequency(frequency);
        prescription.setQuantityToDispense(quantity);
        return prescription;
    }
}
