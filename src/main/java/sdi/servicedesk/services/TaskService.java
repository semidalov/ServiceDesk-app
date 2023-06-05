package sdi.servicedesk.services;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.EquipmentDAO;
import sdi.servicedesk.dao.IncidentDAO;
import sdi.servicedesk.dao.TaskDAO;
import sdi.servicedesk.dao.UserDAO;
import sdi.servicedesk.models.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {
    private final TaskDAO taskDAO;
    private final UserDAO userDAO;
    private final EquipmentDAO equipmentDAO;
    private final IncidentDAO incidentDAO;
    private static final Logger LOGGER = Logger.getLogger(TaskService.class);

    @Autowired
    public TaskService(TaskDAO taskDAO, UserDAO userDAO, EquipmentDAO equipmentDAO, IncidentDAO incidentDAO) {
        this.taskDAO = taskDAO;
        this.userDAO = userDAO;
        this.equipmentDAO = equipmentDAO;
        this.incidentDAO = incidentDAO;
    }

    public List<Task> findTasks(int page, int resultPerPage, Map<String, Object> searchParams, String orderBy, Map<String, Object> filterParams) {
        List<Task> tasksFromDB = taskDAO.findTasks(page, resultPerPage, searchParams, orderBy, filterParams);
        checkExpired(tasksFromDB);
        return tasksFromDB;
    }

    public Task findById(int id) {
        return taskDAO.findById(id);
    }

    public void save(Task task) {

        if (isValidIncident(task)) {
            fillTask(task);

            taskDAO.save(task);
        } else {
            LOGGER.warn("Incident " + task.getIncident().getTitle() + " with id " + task.getIncident().getId() +
                    " does not correspond to the class " + task.getEquipment().getEquipmentClass() +
                    " of the equipment " + task.getEquipment().getName());
        }
    }

    public void update(int taskId, Task task) {

        Task taskFromDB = taskDAO.findById(taskId);

        if (taskFromDB.getTaskStatus().getId() == 3) {
            LOGGER.warn("Closed tasks cannot be updated");
            return;
        }

        if (taskFromDB.getTaskStatus().getId() == 2) {
            if (task.getExecutor() == null) {
                LOGGER.warn("Attempt to remove executor. Task id " + taskId);
                return;
            } else {
                if (task.getExecutor().getId() == 0) {
                    LOGGER.warn("Attempt to remove executor. Task id " + taskId);
                    return;
                }
                if (!isExecutor(task.getExecutor())) {
                    LOGGER.warn("Employee with id " + task.getExecutor().getId() + " cannot be executor");
                    return;
                }
            }
            if (!isValidIncident(task)) {
                LOGGER.warn("Incident with id " + task.getIncident().getId() +
                    " does not correspond to the class of equipment id " + task.getEquipment().getId());
                return;
            }
            TaskStatus status = new TaskStatus();
            status.setId(2);
            task.setTaskStatus(status);
            taskDAO.update(taskId, task);
        }

        if (taskFromDB.getTaskStatus().getId() == 1) {
            if (task.getExecutor() != null) {
                if (!isExecutor(task.getExecutor())) {
                    LOGGER.warn("Employee with id " + task.getExecutor().getId() + " cannot be executor");
                    return;
                } else {
                    TaskStatus status = new TaskStatus();
                    status.setId(2);
                    task.setTaskStatus(status);
                }
            } else {
                TaskStatus status = new TaskStatus();
                status.setId(1);
                task.setTaskStatus(status);
            }
            if (!isValidIncident(task)) {
                LOGGER.warn("Incident with id " + task.getIncident().getId() +
                        " does not correspond to the class of equipment id " + task.getEquipment().getId());
                return;
            }
            taskDAO.update(taskId, task);
        }
    }

   public void closeTask(int taskId) {
        Task taskFromDB = taskDAO.findById(taskId);
        if (taskFromDB.getExecutor() == null) {
            LOGGER.warn("Attempt to close task without an executor");
            return;
        }
        if (taskFromDB.getOccasion() == null) {
            LOGGER.warn("Attempt to close task without an occasion");
            return;
        }
        TaskStatus status = new TaskStatus();
        status.setId(3);
        taskDAO.closeTask(taskId, status);
   }

    public void setExecutor(int id, Employee executor) {
        taskDAO.setExecutor(id, executor);
    }

    public int rowsCount(Map<String, Object> searchParams, Map<String, Object> filterParams) {
        return taskDAO.rowsCount(searchParams, filterParams);
    }

    private void fillTask(Task task) {
        TaskStatus status = new TaskStatus();
        status.setId(1);

        task.setTaskStatus(status);
        task.setCreated(LocalDateTime.now());
        setDeadLine(task);
    }

    private void setDeadLine(Task task) {
        Incident incident = incidentDAO.findById(task.getIncident().getId());

        int priorityId = incident.getPriority().getId();

        switch (priorityId) {
            case 1:
                task.setDeadline(task.getCreated().plus(3 * 86_400L, ChronoUnit.SECONDS));
                break;
            case 2:
                task.setDeadline(task.getCreated().plus(2 * 86_400L, ChronoUnit.SECONDS));
                break;
            case 3:
                task.setDeadline(task.getCreated().plus( 86_400L, ChronoUnit.SECONDS));
                break;
            case 4:
                task.setDeadline(task.getCreated().plus( 7_200L, ChronoUnit.SECONDS));
                break;
        }
    }

    private boolean isExecutor(Employee employee) {
        return userDAO.findUserByEmployeeId(employee.getId()).getRole().getId() == 3;
    }

    private boolean isValidIncident(Task task) {
        Equipment equipment = equipmentDAO.findOneById(task.getEquipment().getId());
        Incident incident = incidentDAO.findById(task.getIncident().getId());
        return equipment.getEquipmentClass().getId() == incident.getEquipmentClass().getId();
    }

    private void checkExpired(List<Task> tasks) {
        tasks.forEach(this::checkExpired);
    }

    private void checkExpired(Task task) {
        Duration duration = Duration.between(LocalDateTime.now(), task.getDeadline());
        if (task.getTaskStatus().getId() == 3) {
            task.setExpired(false);
            return;
        }
        if (duration.getSeconds() <= 0)
            task.setExpired(true);
        else
            task.setExpired(false);
    }
}
