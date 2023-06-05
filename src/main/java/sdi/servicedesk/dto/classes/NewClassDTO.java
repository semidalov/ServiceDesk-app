package sdi.servicedesk.dto.classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class NewClassDTO {

    @Size(min = 3, max = 255, message = "Заполните наименование")
    private String name;

    @Size(min = 3, max = 255, message = "Заполните описание")
    private String description;

}
