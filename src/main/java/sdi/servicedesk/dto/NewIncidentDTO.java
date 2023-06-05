package sdi.servicedesk.dto;

import lombok.*;
import sdi.servicedesk.models.EquipmentClass;
import sdi.servicedesk.models.Priority;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NewIncidentDTO {

    @NotEmpty(message = "Описание не должно быть пустым")
    private String title;

    @NotNull
    private EquipmentClass equipmentClass;

    @NotNull
    private Priority priority;
}
