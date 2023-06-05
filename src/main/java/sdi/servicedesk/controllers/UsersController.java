package sdi.servicedesk.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sdi.servicedesk.dto.users.GetExecutorsWhileTaskUpdateDTO;
import sdi.servicedesk.dto.users.NewUserDTO;
import sdi.servicedesk.dto.users.EditUserDTO;
import sdi.servicedesk.dto.users.GetUsersResponseDTO;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.User;
import sdi.servicedesk.models.UserRole;
import sdi.servicedesk.security.AccountDetails;
import sdi.servicedesk.services.GroupService;
import sdi.servicedesk.services.UserRoleService;
import sdi.servicedesk.services.UserService;
import sdi.servicedesk.utils.UserConverter;
import sdi.servicedesk.utils.UserValidator;
import sdi.servicedesk.utils.converters.ExecutorConverter;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserRoleService roleService;
    private final GroupService groupService;
    private final UserService userService;
    private final UserValidator userValidator;
    private final UserConverter userConverter;
    private final ExecutorConverter executorConverter;
    private static final Logger LOGGER = Logger.getLogger(UsersController.class);

    @Autowired
    public UsersController(UserRoleService roleService, GroupService groupService, UserService userService, UserValidator userValidator, UserConverter userConverter, ExecutorConverter executorConverter) {
        this.roleService = roleService;
        this.groupService = groupService;
        this.userService = userService;
        this.userValidator = userValidator;
        this.userConverter = userConverter;
        this.executorConverter = executorConverter;
    }

    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("employee", getEmployee());
        return "/users/users_index";
    }

    @GetMapping("/get_user/{id}")
    public String getUser(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userConverter.convertToUserDTOForEditForm(userService.getUserById(id)));
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("employee", getEmployee());

        return "/users/edit_user";
    }

    @PatchMapping("/{id}/update")
    public String update(@PathVariable("id") int id, @ModelAttribute("user") @Valid EditUserDTO userDTO, BindingResult bindingResult, Model model) {
        LOGGER.info(userDTO);
        userValidator.validateWhileUpdate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("groups", groupService.findAll());
            model.addAttribute("employee", getEmployee());
            model.addAttribute("errorMsg", "Неверно заполнены все поля!");
            return "/users/edit_user";
        }

        System.out.println("after validation");

        userService.update(userConverter.convertToUserWhileUpdate(userDTO, new User()), id);
        return "redirect:/users/get_user/" + id;
    }


    @GetMapping("/get_users")
    @ResponseBody
    public List<GetUsersResponseDTO> getUsers(@RequestParam("page") int page,
                                              @RequestParam("results_per_page") int resultsPerPage,
                                              @RequestParam(value = "order_by", required = false) String orderBy,
                                              @RequestParam(value = "last_name", required = false) String whereName,
                                              @RequestParam(value = "work_phone", required = false) String whereWorkPhone,
                                              @RequestParam(value = "username", required = false) String whereUsername,
                                              @RequestParam(value = "group", required = false) String group,
                                              @RequestParam(value = "role", required = false) String role,
                                              @RequestParam(value = "locked", required = false) String locked,
                                              @RequestParam(value = "date", required = false) String date) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("e.lastName", whereName);
        searchParams.put("e.workPhone", whereWorkPhone);
        searchParams.put("u.username", whereUsername);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("e.group.id", group);
        filterParams.put("u.role.id", role);
        filterParams.put("u.locked", locked);
        filterParams.put("u.joinDate", date);

        return userService.getUsers(page, resultsPerPage, searchParams, orderBy, filterParams)
                .stream().map(userConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("get_executors")
    @ResponseBody
    public List<GetExecutorsWhileTaskUpdateDTO> getExecutors(@RequestParam("page") int page,
                                                             @RequestParam("results_per_page") int resultsPerPage,
                                                             @RequestParam(value = "last_name", required = false) String lastName,
                                                             @RequestParam(value = "order_by", required = false) String orderBy) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("e.lastName", lastName);
        searchParams.put("e.user.role.id", 3);

        return userService.getExecutors(page, resultsPerPage, searchParams, orderBy).stream()
                .map(executorConverter::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/new")
    public String newUser(Model model) {
        model.addAttribute("newUser", new NewUserDTO());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("groups", groupService.findAll());
        model.addAttribute("employee", getEmployee());
        return "/users/new_user";
    }

    @PostMapping("/save")
    public String addNewUser(@ModelAttribute("newUser") @Valid NewUserDTO userDTO, BindingResult bindingResult, Model model) {
        LOGGER.info(userDTO);
        userValidator.validate(userDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("roles", roleService.findAll());
            model.addAttribute("groups", groupService.findAll());
            model.addAttribute("employee", getEmployee());
            return "/users/new_user";
        } else {
            userService.saveUser(userConverter.convertToUserWhileSave(userDTO));
            return "redirect:/users";
        }
    }

    @GetMapping("/rows_count")
    @ResponseBody
    public int getRowsCount(@RequestParam(value = "order_by", required = false) String orderBy,
                            @RequestParam(value = "last_name", required = false) String whereName,
                            @RequestParam(value = "work_phone", required = false) String whereWorkPhone,
                            @RequestParam(value = "username", required = false) String whereUsername,
                            @RequestParam(value = "group", required = false) String group,
                            @RequestParam(value = "role", required = false) String role,
                            @RequestParam(value = "locked", required = false) String locked,
                            @RequestParam(value = "date", required = false) String date) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("e.lastName", whereName);
        searchParams.put("e.workPhone", whereWorkPhone);
        searchParams.put("e.user.username", whereUsername);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("e.group.id", group);
        filterParams.put("e.user.role.id", role);
        filterParams.put("e.user.locked", locked);
        filterParams.put("e.user.joinDate", date);

        return userService.employeesRowsCount(searchParams, filterParams);
    }

    @PatchMapping("/block/{id}")
    public String blockUser(@PathVariable("id") int id){
        userService.blockUser(id);
        return "redirect:/users/get_user/" + id;
    }

    @PatchMapping("/unblock/{id}")
    public String unblockUser(@PathVariable("id") int id){
        userService.unblockUser(id);
        return "redirect:/users/get_user/" + id;
    }

    private Employee getEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        return accountDetails.getAccount().getEmployee();
    }
}
