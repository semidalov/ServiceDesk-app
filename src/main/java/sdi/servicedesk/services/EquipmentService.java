package sdi.servicedesk.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.EquipmentDAO;
import sdi.servicedesk.models.Equipment;
import sdi.servicedesk.models.EquipmentClass;

import java.util.List;
import java.util.Map;

@Service
public class EquipmentService {

    private final EquipmentDAO equipmentDAO;
    private static final Logger LOGGER = Logger.getLogger(EquipmentDAO.class);


    @Autowired
    public EquipmentService(EquipmentDAO equipmentDAO) {
        this.equipmentDAO = equipmentDAO;
    }

    public void save(Equipment equipment) {
        trim(equipment);
        equipmentDAO.save(equipment);
    }

    public List<Equipment> findAll(int page, int resultPerPage, String orderBy, Map<String, Object> searchParams, Map<String, Object> filterParams) {
        return equipmentDAO.findAll(page, resultPerPage, orderBy, searchParams, filterParams);
    }

    public Equipment findOneById(int id) {
        Equipment equipment = equipmentDAO.findOneById(id);
        LOGGER.info(equipment);
        return equipment;
    }

    public void update(int id, Equipment equipment) {
        trim(equipment);
        equipmentDAO.update(id, equipment);
    }

    public void delete(int id) {
        equipmentDAO.delete(id);
    }

    public int rowsCount(Map<String, Object> searchParams, Map<String, Object> filterParams) {
        return equipmentDAO.rowsCount(searchParams, filterParams);
    }

    public void trim(Equipment equipment) {
        equipment.setName(equipment.getName().trim());
        equipment.setSerial(equipment.getSerial().trim());
    }
}
