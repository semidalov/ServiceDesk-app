package sdi.servicedesk.dto.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetExecutorsWhileTaskUpdateDTO {
    private int id;
    private String fullName;
    private String groupName;
}
