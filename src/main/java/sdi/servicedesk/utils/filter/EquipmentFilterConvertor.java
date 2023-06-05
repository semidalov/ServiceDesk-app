package sdi.servicedesk.utils.filter;

import org.springframework.stereotype.Component;
import sdi.servicedesk.dto.filters.EquipmentFilterResponse;
import sdi.servicedesk.models.EquipmentClass;

import java.util.List;

@Component
public class EquipmentFilterConvertor {

    public EquipmentFilterResponse convertToDTO(List<EquipmentClass> equipmentClassList) {
        EquipmentFilterResponse response = new EquipmentFilterResponse();

        equipmentClassList.forEach(e -> response.addClass(e.getName(), e.getId()));

        return response;
    }
}
