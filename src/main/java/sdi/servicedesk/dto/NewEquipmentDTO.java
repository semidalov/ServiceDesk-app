package sdi.servicedesk.dto;

import lombok.*;
import sdi.servicedesk.models.EquipmentClass;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class NewEquipmentDTO {

    @Size(min = 1, max = 255, message = "Название не должно быть пустым.")
    private String name;

    @Size(min = 1, max = 255, message = "Серийный номер не должен быть пустым.")
    private String serial;

    @NotNull (message = "Укажите классификацию оборудования. ")
    private EquipmentClass equipmentClass;
}
