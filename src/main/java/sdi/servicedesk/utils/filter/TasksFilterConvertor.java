package sdi.servicedesk.utils.filter;

import org.springframework.stereotype.Component;
import sdi.servicedesk.dto.filters.TasksFilterResponse;
import sdi.servicedesk.dto.filters.UsersFilterResponse;
import sdi.servicedesk.models.Group;
import sdi.servicedesk.models.Priority;
import sdi.servicedesk.models.TaskStatus;
import sdi.servicedesk.models.UserRole;

import java.util.List;

@Component
public class TasksFilterConvertor {

    public TasksFilterResponse convertToDTO(List<TaskStatus> statuses, List<Priority> priorities) {

        TasksFilterResponse response = new TasksFilterResponse();

        statuses.forEach(el -> response.addStatus(el.getName(), el.getId()));
        priorities.forEach(el -> response.addPriority(el.getName(), el.getId()));

        return response;
    }
}
