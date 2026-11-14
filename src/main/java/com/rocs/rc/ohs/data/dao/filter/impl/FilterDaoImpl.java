package com.rocs.rc.ohs.data.dao.filter.impl;

import com.rocs.rc.ohs.app.model.medicalrecord.MedicalRecord;
import com.rocs.rc.ohs.app.model.person.Person;
import com.rocs.rc.ohs.data.connection.ConnectionHelper;
import com.rocs.rc.ohs.data.dao.filter.FilterDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implements the data access operations for filtering medical records.
 */
public class FilterDaoImpl implements FilterDao {

    /**
     * Logger of this class.
     */
    private static final Logger LOGGER =
            Logger.getLogger(FilterDaoImpl.class.getName());

    /**
     * Retrieves the medical records that match the given criteria.
     * A null or blank criterion is ignored.
     * @param patientName first or last name of the patient, may be null.
     * @param medicalCondition medical condition of the patient, may be null.
     * @param allergy allergy of the patient, may be null.
     * @return list of matching {@link MedicalRecord} records,
     *         or an empty list if none match.
     */
    @Override
    public List<MedicalRecord> filterMedicalRecords(String patientName,
                                                    String medicalCondition,
                                                    String allergy) {

        List<MedicalRecord> records = new ArrayList<>();
        List<String> params = new ArrayList<>();
        List<String> conditions = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "select m.id, m.medical_conditions, m.allergies, "
                        + "p.id as patient_id, p.first_name, "
                        + "p.middle_name, p.last_name "
                        + "from medical_record m "
                        + "join person p on p.id = m.patient_id");

        if (hasValue(patientName)) {
            conditions.add("(lower(p.first_name) like ?"
                    + " or lower(p.last_name) like ?)");
            params.add(toPattern(patientName));
            params.add(toPattern(patientName));
        }
        if (hasValue(medicalCondition)) {
            conditions.add("lower(m.medical_conditions) like ?");
            params.add(toPattern(medicalCondition));
        }
        if (hasValue(allergy)) {
            conditions.add("lower(m.allergies) like ?");
            params.add(toPattern(allergy));
        }
        if (!conditions.isEmpty()) {
            sql.append(" where ").append(String.join(" and ", conditions));
        }
        sql.append(" order by p.last_name, p.first_name");

        try (Connection conn = ConnectionHelper.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Person patient = new Person();
                    patient.setId(rs.getString("patient_id"));
                    patient.setFirstName(rs.getString("first_name"));
                    patient.setMiddleName(rs.getString("middle_name"));
                    patient.setLastName(rs.getString("last_name"));
                    MedicalRecord record = new MedicalRecord();
                    record.setId(rs.getString("id"));
                    record.setPatient(patient);
                    record.setMedicalConditions(
                            rs.getString("medical_conditions"));
                    record.setAllergies(rs.getString("allergies"));

                    records.add(record);
                }
            }

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE,
                    "An exception thrown while filter medical records.", e);
        }
        return records;
    }

    /**
     * Check the given criterion has a usable value.
     * @param value criterion to check.
     * @return true if criterion is not null and not blank.
     */
    private boolean hasValue(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * Converts the given criterion to a case-insensitive "contains" pattern.
     * @param value criterion to convert.
     * @return lowercase pattern wrapped in wildcards.
     */
    private String toPattern(String value) {
        return "%" + value.trim().toLowerCase(Locale.ROOT) + "%";
    }
}
