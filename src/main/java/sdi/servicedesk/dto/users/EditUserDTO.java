package sdi.servicedesk.dto.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class EditUserDTO extends NewUserDTO {
    private int id;

    private Boolean locked;

    private LocalDateTime joinDate;

    private LocalDateTime lastLogin;
}