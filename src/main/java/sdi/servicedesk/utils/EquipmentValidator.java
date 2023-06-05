package sdi.servicedesk.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import sdi.servicedesk.dao.EquipmentDAO;
import sdi.servicedesk.dto.NewEquipmentDTO;
import sdi.servicedesk.models.Equipment;

import java.util.List;

@Component
public class EquipmentValidator {

    public final EquipmentDAO equipmentDAO;

    @Autowired
    public EquipmentValidator(EquipmentDAO equipmentDAO) {
        this.equipmentDAO = equipmentDAO;
    }


    public void validate(Object o, Errors errors) {
        if (!(equipmentDAO.findByName(((NewEquipmentDTO) o).getName().trim()).isEmpty()))
            errors.rejectValue("name", "", "Техника с таким названием уже существует");
        if (!(equipmentDAO.findBySerial(((NewEquipmentDTO) o).getSerial().trim()).isEmpty()))
            errors.rejectValue("serial", "", "Техника с таким серийным номером уже существует");
    }

    public static void main() {

    }

    public void validateOnUpdate(Object o, Errors errors, int id) {
        List<Object[]> fromDatabaseByName = equipmentDAO.findByName(((Equipment) o).getName().trim());
        List<Object[]> fromDatabaseBySerial = equipmentDAO.findBySerial(((Equipment) o).getSerial().trim());
        if (!fromDatabaseByName.isEmpty())
            if ((Integer) fromDatabaseByName.get(0)[0] != id)
                errors.rejectValue("name", "", "Другая техника с таким названием уже существует");
        if (!fromDatabaseBySerial.isEmpty())
            if ((Integer) fromDatabaseBySerial.get(0)[0] != id)
                errors.rejectValue("serial", "", "Другая техника с таким серийным номером уже существует");
    }
}
