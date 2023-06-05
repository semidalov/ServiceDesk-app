package sdi.servicedesk.utils;

import org.springframework.stereotype.Component;
import sdi.servicedesk.dto.tasks.NewTaskDTO;
import sdi.servicedesk.dto.tasks.GetTasksResponse;
import sdi.servicedesk.dto.tasks.EditTaskDTO;
import sdi.servicedesk.models.*;

@Component
public class TaskConvertor {

    public GetTasksResponse convertToDTO(Task task) {
        GetTasksResponse getTasksResponse = new GetTasksResponse();

        getTasksResponse.setId(task.getId());
        getTasksResponse.setCreator(UserConverter.buildFIO(task.getCreator()));
        getTasksResponse.setTitle(task.getTitle());
        getTasksResponse.setCreated(task.getCreated().toString());
        getTasksResponse.setStatus(task.getTaskStatus().getName());
        getTasksResponse.setDeadline(task.getDeadline().toString());
        getTasksResponse.setExpired(task.getExpired());
        getTasksResponse.setPriority(task.getIncident().getPriority().getName());
        if (task.getExecutor() != null)
            getTasksResponse.setExecutor(UserConverter.buildFIO(task.getExecutor()));

        return getTasksResponse;
    }

    public Task convertToTask(Task task, NewTaskDTO newTaskDTO) {

        task.setCreator(newTaskDTO.getCreator());
        task.setTitle(newTaskDTO.getTitle());
        task.setDescription(newTaskDTO.getDescription());

        Equipment equipment = new Equipment();
        equipment.setId(newTaskDTO.getEquipmentId());

        Incident incident = new Incident();
        incident.setId(newTaskDTO.getIncidentId());

        task.setEquipment(equipment);
        task.setIncident(incident);

        return task;
    }

    public Task convertToTaskWhileUpdate(Task task, EditTaskDTO editTaskDTO) {
        task = convertToTask(task, editTaskDTO);

        task.setComment(editTaskDTO.getComment());


        if (editTaskDTO.getOccasionId() != null) {
            Occasion occasion = new Occasion();
            occasion.setId(editTaskDTO.getOccasionId());
            task.setOccasion(occasion);
        }

        if (editTaskDTO.getExecutorId() != null) {
            Employee executor = new Employee();
            executor.setId(editTaskDTO.getExecutorId());
            task.setExecutor(executor);
        }

        return task;
    }

    public EditTaskDTO convertToEditTaskDTO(Task task) {
        EditTaskDTO taskDTO = new EditTaskDTO();

        taskDTO.setId(task.getId());
        taskDTO.setComment(task.getComment());
        if (task.getOccasion() != null) {
            taskDTO.setOccasionName(task.getOccasion().getName());
            taskDTO.setOccasionId(task.getOccasion().getId());
        }
        if (task.getExecutor() != null) {
            taskDTO.setExecutorId(task.getExecutor().getId());
            taskDTO.setExecutorName(UserConverter.buildFIO(task.getExecutor()));
        }
        taskDTO.setStatus(task.getTaskStatus());
        taskDTO.setCreated(task.getCreated());
        taskDTO.setClosed(task.getClosed());

        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setCreator(task.getCreator());
        taskDTO.setEquipmentId(task.getEquipment().getId());
        taskDTO.setIncidentId(task.getIncident().getId());
        taskDTO.setEquipmentName(task.getEquipment().getName());
        taskDTO.setIncidentName(task.getIncident().getTitle());
        taskDTO.setTaskPriorityName(task.getIncident().getPriority().getName());
        return taskDTO;
    }

}
