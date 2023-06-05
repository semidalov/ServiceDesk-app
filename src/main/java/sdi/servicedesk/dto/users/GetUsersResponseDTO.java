package sdi.servicedesk.dto.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class GetUsersResponseDTO {

    private int id;

    private String fullName;

    private String workPhone;

    private String groupName;

    private String username;

    private String role;

    private Boolean locked;

    private String joinDate;

    private String lastLogin;

}
