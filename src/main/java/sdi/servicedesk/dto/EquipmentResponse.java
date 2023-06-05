package sdi.servicedesk.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EquipmentResponse {

    private int id;

    private String name;

    private String serial;

    private String equipmentClass;
}
