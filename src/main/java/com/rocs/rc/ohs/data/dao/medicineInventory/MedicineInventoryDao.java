package com.rocs.clinic.data.dao.item;

import com.rocs.clinic.app.model.item.MedicineInventory;

import java.util.List;

public interface MedicineInventoryDao {
    List<MedicineInventory>getAllMedicine();
    List<MedicineInventory>searchMedicine(String keyword);
}
