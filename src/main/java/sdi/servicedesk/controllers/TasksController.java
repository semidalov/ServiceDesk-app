package sdi.servicedesk.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sdi.servicedesk.dto.tasks.NewTaskDTO;
import sdi.servicedesk.dto.tasks.GetTasksResponse;
import sdi.servicedesk.dto.tasks.EditTaskDTO;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.Task;
import sdi.servicedesk.models.UserRole;
import sdi.servicedesk.security.AccountDetails;
import sdi.servicedesk.services.IncidentService;
import sdi.servicedesk.services.TaskService;
import sdi.servicedesk.services.TaskStatusService;
import sdi.servicedesk.utils.TaskConvertor;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TasksController {


    private final TaskService taskService;
    private final TaskConvertor taskConvertor;

    @Autowired
    public TasksController(TaskService taskService, TaskConvertor taskConvertor) {
        this.taskService = taskService;
        this.taskConvertor = taskConvertor;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("employee", getEmployee());
        return "/tasks/tasks_index";
    }

    @GetMapping("/get_opened")
    @ResponseBody
    public List<GetTasksResponse> getOpened(@RequestParam("page") int page,
                                            @RequestParam("results_per_page") int resultsPerPage,
                                            @RequestParam(value = "creator", required = false) String creatorId,
                                            @RequestParam(value = "executor", required = false) String executorId,
                                            @RequestParam(value = "title", required = false) String title,
                                            @RequestParam(value = "order_by", required = false) String orderBy,
                                            @RequestParam(value = "status", required = false) String status,
                                            @RequestParam(value = "priority", required = false) String priority,
                                            @RequestParam(value = "date", required = false) String date) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("t.creator.lastName", creatorId);
        searchParams.put("t.executor.lastName", executorId);
        searchParams.put("t.title", title);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("t.taskStatus.id", Objects.requireNonNullElse(status, "!3"));
        filterParams.put("t.incident.priority.id", priority);
        filterParams.put("t.created", date);


        return taskService.findTasks(page, resultsPerPage, searchParams, orderBy, filterParams).stream()
                .map(taskConvertor::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/get_mine")
    @ResponseBody
    public List<GetTasksResponse> getMine(@RequestParam("page") int page,
                                          @RequestParam("results_per_page") int resultsPerPage,
                                          @RequestParam(value = "executor", required = false) String executor,
                                          @RequestParam(value = "title", required = false) String title,
                                          @RequestParam(value = "order_by", required = false) String orderBy,
                                          @RequestParam(value = "status", required = false) String status,
                                          @RequestParam(value = "priority", required = false) String priority,
                                          @RequestParam(value = "date", required = false) String date) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("t.executor.lastName", executor);
        searchParams.put("t.title", title);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("t.taskStatus.id", status);
        filterParams.put("t.incident.priority.id", priority);
        filterParams.put("t.created", date);
        filterParams.put("t.creator.id", String.valueOf(getEmployee().getId()));


        return taskService.findTasks(page, resultsPerPage, searchParams, orderBy, filterParams).stream()
                .map(taskConvertor::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/get_attached")
    @ResponseBody
    public List<GetTasksResponse> getAttached(@RequestParam("page") int page,
                                              @RequestParam("results_per_page") int resultsPerPage,
                                              @RequestParam(value = "creator", required = false) String creator,
                                              @RequestParam(value = "title", required = false) String title,
                                              @RequestParam(value = "order_by", required = false) String orderBy,
                                              @RequestParam(value = "status", required = false) String status,
                                              @RequestParam(value = "priority", required = false) String priority,
                                              @RequestParam(value = "date", required = false) String date) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("t.creator.lastName", creator);
        searchParams.put("t.title", title);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("t.taskStatus.id", status);
        filterParams.put("t.incident.priority.id", priority);
        filterParams.put("t.created", date);
        filterParams.put("t.executor.id", String.valueOf(getEmployee().getId()));

        return taskService.findTasks(page, resultsPerPage, searchParams, orderBy, filterParams).stream()
                .map(taskConvertor::convertToDTO).collect(Collectors.toList());
    }

    @GetMapping("/get_all")
    @ResponseBody
    public List<GetTasksResponse> getAll(@RequestParam("page") int page,
                                         @RequestParam("results_per_page") int resultsPerPage,
                                         @RequestParam(value = "creator", required = false) String creator,
                                         @RequestParam(value = "executor", required = false) String executor,
                                         @RequestParam(value = "title", required = false) String title,
                                         @RequestParam(value = "order_by", required = false) String orderBy,
                                         @RequestParam(value = "status", required = false) String status,
                                         @RequestParam(value = "priority", required = false) String priority,
                                         @RequestParam(value = "date", required = false) String date) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("t.creator.lastName", creator);
        searchParams.put("t.executor.lastName", executor);
        searchParams.put("t.title", title);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("t.taskStatus.id", status);
        filterParams.put("t.incident.priority.id", priority);
        filterParams.put("t.created", date);

        return taskService.findTasks(page, resultsPerPage, searchParams, orderBy, filterParams).stream()
                .map(taskConvertor::convertToDTO).collect(Collectors.toList());
    }


    @GetMapping("/new")
    public String newTask(Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("task", new NewTaskDTO());
        return "/tasks/new-task";
    }


    @PostMapping("/save")
    public String save(@ModelAttribute("task") @Valid NewTaskDTO newTaskDTO, BindingResult bindingResult, Model model) {

        newTaskDTO.setCreator(getEmployee());
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", getEmployee());
            return "/tasks/new-task";
        }
        taskService.save(taskConvertor.convertToTask(new Task(), newTaskDTO));
        return "redirect:/tasks";
    }

    @PatchMapping("/{id}/update")
    public String update(@PathVariable("id") int id, @Valid EditTaskDTO editTaskDTO, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", getEmployee());
            model.addAttribute("errorMsg", "Неверно заполнены все поля!");
            model.addAttribute("task", taskConvertor.convertToEditTaskDTO(taskService.findById(id)));
            return "/tasks/show_task";
        }

        taskService.update(id, taskConvertor.convertToTaskWhileUpdate(new Task(), editTaskDTO));
        return "redirect:/tasks/" + id;
    }

    @GetMapping("/{id}")
    public String showOne(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("task", taskConvertor.convertToEditTaskDTO(taskService.findById(id)));
        return "/tasks/show_task";
    }

    @PatchMapping("/take/{id}")
    public String takeOn(@PathVariable("id") int id) {
        taskService.setExecutor(id, getEmployee());
        return "redirect:/tasks/" + id;
    }

    @PatchMapping("/close/{id}")
    public String close(@PathVariable("id") int id) {
        taskService.closeTask(id);
        return "redirect:/tasks";
    }


    @GetMapping("/rows_count")
    @ResponseBody
    public int rowsCount(@RequestParam(value = "creator", required = false) String creator,
                         @RequestParam(value = "executor", required = false) String executor,
                         @RequestParam(value = "title", required = false) String title,
                         @RequestParam(value = "status", required = false) String status,
                         @RequestParam(value = "priority", required = false) String priority,
                         @RequestParam(value = "date", required = false) String date) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("t.creator.lastName", creator);
        searchParams.put("t.executor.lastName", executor);
        searchParams.put("t.title", title);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("t.taskStatus.id", status);
        filterParams.put("t.incident.priority.id", priority);
        filterParams.put("t.created", date);

        return taskService.rowsCount(searchParams, filterParams);
    }

    private Employee getEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        return accountDetails.getAccount().getEmployee();
    }
}
