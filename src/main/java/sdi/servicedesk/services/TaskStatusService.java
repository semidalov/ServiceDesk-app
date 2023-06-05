package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.TaskStatusDAO;
import sdi.servicedesk.models.TaskStatus;

import java.util.List;

@Service
public class TaskStatusService {

    private final TaskStatusDAO taskStatusDAO;

    @Autowired
    public TaskStatusService(TaskStatusDAO taskStatusDAO) {
        this.taskStatusDAO = taskStatusDAO;
    }

    public List<TaskStatus> findAll() {
        return taskStatusDAO.findAll();
    }
}
