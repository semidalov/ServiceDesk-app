package sdi.servicedesk.dto.tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetTasksResponse {

    private int id;

    private String creator;

    private String title;

    private String created;

    private String status;

    private String deadline;

    private Boolean expired;

    private String priority;

    private String executor;

}
