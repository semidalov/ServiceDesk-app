package sdi.servicedesk.dto.users;


import lombok.*;
import sdi.servicedesk.models.Group;
import sdi.servicedesk.models.UserRole;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NewUserDTO {

    @Size(min = 1, max = 255, message = "Поле учетная запись не должно быть пустым")
    private String username;

    @NotNull (message = "Укажите роль пользователя")
    private UserRole role;


    @Size(min = 1, max = 255, message = "Поле имя не должно быть пустым")
    private String firstname;

    @Size(min = 1, max = 255, message = "Поле фамилия не должно быть пустым")
    private String lastname;

    private String patronymic;

    @NotNull (message = "Укажите отдел пользователя")
    private Group group;

    private String workPhone;
}

