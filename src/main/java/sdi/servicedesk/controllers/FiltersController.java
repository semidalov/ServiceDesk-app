package sdi.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdi.servicedesk.dto.filters.EquipmentFilterResponse;
import sdi.servicedesk.dto.filters.TasksFilterResponse;
import sdi.servicedesk.dto.filters.UsersFilterResponse;
import sdi.servicedesk.services.*;
import sdi.servicedesk.utils.filter.EquipmentFilterConvertor;
import sdi.servicedesk.utils.filter.TasksFilterConvertor;
import sdi.servicedesk.utils.filter.UsersFilterConvertor;


@RestController
@RequestMapping("/filter")
public class FiltersController {

    private final UsersFilterConvertor usersFilterConvertor;
    private final TasksFilterConvertor tasksFilterConvertor;
    private final EquipmentFilterConvertor equipmentFilterConvertor;
    private final GroupService groupService;
    private final UserRoleService roleService;
    private final TaskStatusService taskStatusService;
    private final PriorityService priorityService;
    private final EquipmentClassService equipmentClassService;

    @Autowired
    public FiltersController(UsersFilterConvertor usersFilterConvertor, GroupService groupService, UserRoleService roleService, TasksFilterConvertor tasksFilterConvertor, EquipmentFilterConvertor equipmentFilterConvertor, TaskStatusService taskStatusService, PriorityService priorityService, EquipmentClassService equipmentClassService) {
        this.usersFilterConvertor = usersFilterConvertor;
        this.groupService = groupService;
        this.roleService = roleService;
        this.tasksFilterConvertor = tasksFilterConvertor;
        this.equipmentFilterConvertor = equipmentFilterConvertor;
        this.taskStatusService = taskStatusService;
        this.priorityService = priorityService;
        this.equipmentClassService = equipmentClassService;
    }


    @RequestMapping("/users")
    public UsersFilterResponse getUsersFilterData() {
        return usersFilterConvertor.convertToDTO(groupService.findAll(),roleService.findAll());
    }

    @RequestMapping("/tasks")
    public TasksFilterResponse getTasksFilterData() {
        return tasksFilterConvertor.convertToDTO(taskStatusService.findAll(), priorityService.findAll());
    }

    @RequestMapping("/equipment")
    public EquipmentFilterResponse getEquipmentFilterData() {
        return equipmentFilterConvertor.convertToDTO(equipmentClassService.findAll());
    }
}
