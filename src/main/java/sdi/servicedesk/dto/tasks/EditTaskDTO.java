package sdi.servicedesk.dto.tasks;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sdi.servicedesk.models.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class EditTaskDTO extends NewTaskDTO{

    private Integer id;

    private String comment;

    private String occasionName;

    private Integer occasionId;

    private Integer executorId;

    private String executorName;

    private TaskStatus status;

    private LocalDateTime created;

    private LocalDateTime closed;

    private String EquipmentName;

    private String IncidentName;

    private String taskPriorityName;
}
