package sdi.servicedesk.utils;

import org.springframework.stereotype.Component;
import sdi.servicedesk.dto.EquipmentClassResponse;
import sdi.servicedesk.dto.classes.EditClassDTO;
import sdi.servicedesk.dto.classes.NewClassDTO;
import sdi.servicedesk.models.EquipmentClass;

@Component
public class EquipmentClassConverter {

    public EquipmentClass convertToClassWhileSave(EquipmentClass equipmentClass, NewClassDTO newClassDTO) {
        equipmentClass.setName(newClassDTO.getName());
        equipmentClass.setDescription(newClassDTO.getDescription());

        return equipmentClass;
    }

    public EquipmentClass convertToClassWhileUpdate(EquipmentClass equipmentClass, EditClassDTO classDTO) {
        equipmentClass = convertToClassWhileSave(equipmentClass, classDTO);
        equipmentClass.setId(classDTO.getId());

        return equipmentClass;
    }

    public EditClassDTO convertToDTOWhileUpdate(EquipmentClass equipmentClass) {
        EditClassDTO classDTO = new EditClassDTO();
        classDTO.setId(equipmentClass.getId());
        classDTO.setName(equipmentClass.getName());
        classDTO.setDescription(equipmentClass.getDescription());

        return classDTO;
    }

    public EquipmentClassResponse convertToResponseDTO(EquipmentClass equipmentClass) {
        EquipmentClassResponse equipmentClassResponse = new EquipmentClassResponse();
        equipmentClassResponse.setId(equipmentClass.getId());
        equipmentClassResponse.setName(equipmentClass.getName());
        equipmentClassResponse.setDescription(equipmentClass.getDescription());

        return equipmentClassResponse;
    }
}
