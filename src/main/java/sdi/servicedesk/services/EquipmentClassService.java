package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.EquipmentClassDAO;
import sdi.servicedesk.models.EquipmentClass;

import java.util.List;
import java.util.Map;

@Service
public class EquipmentClassService {

    private final EquipmentClassDAO equipmentClassDAO;

    @Autowired
    public EquipmentClassService(EquipmentClassDAO equipmentDAO) {
        this.equipmentClassDAO = equipmentDAO;
    }

    public List<EquipmentClass> findAll(int page, int resultPerPage, String orderBy, Map<String, Object> searchParams) {
        return equipmentClassDAO.findAll(page, resultPerPage, orderBy, searchParams);
    }

    public List<EquipmentClass> findAll() {
        return equipmentClassDAO.findAll();
    }

    public EquipmentClass findOneById(int id) {
        return equipmentClassDAO.findOnaById(id);
    }
    public void save(EquipmentClass equipmentClass) {
        equipmentClassDAO.save(equipmentClass);
    }

    public void update(EquipmentClass equipmentClass, int classId) {
        equipmentClass.setId(classId);
        equipmentClassDAO.update(equipmentClass);
    }

    public int rowsCount() {
        return equipmentClassDAO.rowsCount();
    }
}
