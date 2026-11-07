package com.rocs.rc.ohs.data.dao.medicineInventory.Impl;

import com.rocs.clinic.app.model.item.MedicineInventory;
import com.rocs.clinic.data.connection.ConnectionHelper;
import com.rocs.clinic.data.dao.item.MedicineInventoryDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicineInventoryDaoImpl implements MedicineInventoryDao {

    @Override
    public List<MedicineInventory> getAllMedicine() {
        List<MedicineInventory> medicines = new ArrayList<>();
        String sql = "SELECT medicine.medicine_id, medicine.medicine_name, medicine.category, medicine.unit, " +
                     "inventory.batch_number, inventory.stock_quantity, inventory.expiration_date " +
                     "FROM medicine " +
                     "JOIN inventory On medicine.medicine_id = inventory.medicine_id";

        try {
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()){
                MedicineInventory medicine = new MedicineInventory();
                medicine.setMedicineId(resultSet.getInt("medicine_id"));
                medicine.setMedicineName(resultSet.getString("medicine_name"));
                medicine.setCategory(resultSet.getString("category"));
                medicine.setQuantity(resultSet.getInt("stock_quantity"));
                medicine.setUnit(resultSet.getString("unit"));
                medicine.setExpirationDate(resultSet.getDate("expiration_date"));
                medicines.add(medicine);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error retrieving the medicine inventory ", ex);
        }
        return medicines;
    }

    @Override
    public List<MedicineInventory> searchMedicine(String keyword) {
        List<MedicineInventory> medicines =  new ArrayList<>();
        String sql = " SELECT medicine.medicine_id, medicine.medicine_name, " +
                     "medicine.category, medicine.unit, " +
                     "inventory.batch_number, inventory.stock_quantity, inventory.expiration_date " +
                     "FROM medicine " +
                     "JOIN inventory ON medicine.medicine_id = inventory.medicine_id " +
                     "WHERE medicine.medicine_name LIKE ?";

        try {
            Connection connection = ConnectionHelper.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + keyword + "%");
            ResultSet resultSet = statement.executeQuery();

            if(resultSet.next()){
                MedicineInventory medicine = new MedicineInventory();
                medicine.setMedicineId(resultSet.getInt("medicine_id"));
                medicine.setMedicineName(resultSet.getString("medicine_name"));
                medicine.setQuantity(resultSet.getInt("stock_quantity"));
                medicine.setExpirationDate(resultSet.getDate("Expiration_date"));
                medicines.add(medicine);
            }
        } catch (Exception ex){
            throw new RuntimeException("Erorr Searching medicine Inventory", ex);
        }
        return medicines;
    }
}

