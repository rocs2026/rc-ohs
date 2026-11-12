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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link ConsultationFacadeImpl} class without a real database.
 */
class ConsultationFacadeImplTest {

    /** The ID of Jowan Carl Quirona, a patient in the test data. */
    private static final String JOWAN_ID =
            "f04cac48-889d-4a62-9a2b-a274406ece20";

    /** The ID of a personnel in the test data. */
    private static final String STAFF_ID =
            "64ffaffe-79b5-4690-bbd4-587507b6859c";

    /** The ID of the Amoxicillin 500mg stock in the test data. */
    private static final String STOCK_ID =
            "70aaf305-a7f5-41f1-bd0a-6ad73c042be9";

    /** The mocked consultation DAO. */
    private ConsultationDao consultationDao;

    /** The mocked prescription DAO. */
    private PrescriptionDao prescriptionDao;

    /** The facade under test. */
    private ConsultationFacadeImpl facade;

    @BeforeEach
    void setUp() {
        consultationDao = mock(ConsultationDao.class);
        prescriptionDao = mock(PrescriptionDao.class);

        facade = new ConsultationFacadeImpl(consultationDao,
                prescriptionDao);
    }

    // The consultation and its prescription are saved.
    @Test
    void savesTheConsultationWithItsPrescriptions() {
        Consultation consultation = newConsultation();
        Prescription prescription = newPrescription();

        Consultation saved = facade.saveConsultation(consultation,
                List.of(prescription));

        assertNotNull(saved.getId());
        assertNotNull(prescription.getId());
        assertEquals(saved, prescription.getConsultation());
        verify(consultationDao).add(consultation);
        verify(prescriptionDao).add(prescription);
    }

    // The saved consultations of the patient are retrieved.
    @Test
    void returnsTheSavedConsultationsOfThePatient() {
        List<Consultation> saved = List.of(newConsultation());
        List<Prescription> items = List.of(newPrescription());
        when(consultationDao.findByPatientId(JOWAN_ID)).thenReturn(saved);
        when(prescriptionDao.findByConsultationId("c1")).thenReturn(items);

        assertEquals(saved, facade.getConsultationsByPatient(JOWAN_ID));
        assertEquals(items, facade.getPrescriptionsByConsultation("c1"));
    }

    private Consultation newConsultation() {
        Person patient = new Person();
        patient.setId(JOWAN_ID);
        Personnel staff = new Personnel();
        staff.setId(STAFF_ID);
        Consultation consultation = new Consultation();
        consultation.setPatient(patient);
        consultation.setStaff(staff);
        consultation.setRecordType(RecordType.CHECK_UP);
        consultation.setConsultationDate(
                LocalDateTime.of(2026, 10, 20, 9, 30));
        consultation.setDiagnosis("Bacterial throat infection");
        consultation.setTreatment("Antibiotic and rest");
        return consultation;
    }

    private Prescription newPrescription() {
        MedicineStock stock = new MedicineStock();
        stock.setId(STOCK_ID);
        Prescription prescription = new Prescription();
        prescription.setMedicineStock(stock);
        prescription.setQuantityToDispense(2);
        prescription.setDose("500mg");
        prescription.setFrequency("3 times a day");
        return prescription;
    }
}
