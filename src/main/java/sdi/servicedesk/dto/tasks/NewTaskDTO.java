package sdi.servicedesk.dto.tasks;

import lombok.*;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.Equipment;
import sdi.servicedesk.models.Incident;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class NewTaskDTO {

    @NotEmpty(message = "Заполните название")
    private String title;

    @NotEmpty(message = "Заполните описание")
    private String description;

    private Employee creator;

    @NotNull(message = "Заполните поле техника")
    private Integer equipmentId;

    @NotNull(message = "Заполните поле инцидент")
    private Integer incidentId;

}

