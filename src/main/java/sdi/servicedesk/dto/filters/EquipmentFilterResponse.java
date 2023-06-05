package sdi.servicedesk.dto.filters;

import lombok.Getter;
import sdi.servicedesk.models.EquipmentClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class EquipmentFilterResponse {
    private List<Map<String, Integer>> equipmentClasses;

    public EquipmentFilterResponse() {
        equipmentClasses = new ArrayList<>();
    }

    public void addClass(String name, Integer id) {
        Map<String, Integer> equipmentClass = new HashMap<>();
        equipmentClass.put(name, id);
        equipmentClasses.add(equipmentClass);
    }
}
