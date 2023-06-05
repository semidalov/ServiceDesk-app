package sdi.servicedesk.utils;

import org.springframework.core.io.support.EncodedResource;
import org.springframework.stereotype.Component;
import sdi.servicedesk.dto.EquipmentResponse;
import sdi.servicedesk.dto.NewEquipmentDTO;
import sdi.servicedesk.models.Equipment;

import javax.xml.stream.events.EntityReference;

@Component
public class EquipmentConverter {

    public Equipment convertToEquipment(NewEquipmentDTO newEquipmentDTO) {
        Equipment equipment = new Equipment();
        equipment.setEquipmentClass(newEquipmentDTO.getEquipmentClass());
        equipment.setName(newEquipmentDTO.getName());
        equipment.setSerial(newEquipmentDTO.getSerial());

        return equipment;
    }

    public EquipmentResponse convertToResponseDTO(Equipment equipment) {
        EquipmentResponse equipmentResponse = new EquipmentResponse();

        equipmentResponse.setId(equipment.getId());
        equipmentResponse.setName(equipment.getName());
        equipmentResponse.setSerial(equipment.getSerial());
        equipmentResponse.setEquipmentClass(equipment.getEquipmentClass().getName());

        return equipmentResponse;
    }
}
