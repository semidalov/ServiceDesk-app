package sdi.servicedesk.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sdi.servicedesk.dao.EquipmentClassDAO;
import sdi.servicedesk.dto.EquipmentResponse;
import sdi.servicedesk.dto.NewEquipmentDTO;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.Equipment;
import sdi.servicedesk.models.UserRole;
import sdi.servicedesk.security.AccountDetails;
import sdi.servicedesk.services.EquipmentService;
import sdi.servicedesk.utils.EquipmentConverter;
import sdi.servicedesk.utils.EquipmentValidator;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {

    private static final Logger LOGGER = Logger.getLogger(EquipmentController.class);
    private final EquipmentService equipmentService;
    private final EquipmentConverter equipmentConverter;
    private final EquipmentClassDAO equipmentClassDAO;
    private final EquipmentValidator equipmentValidator;

    @Autowired
    public EquipmentController(EquipmentService equipmentService, EquipmentConverter equipmentConverter, EquipmentClassDAO equipmentClassDAO, EquipmentValidator equipmentValidator) {
        this.equipmentService = equipmentService;
        this.equipmentConverter = equipmentConverter;
        this.equipmentClassDAO = equipmentClassDAO;
        this.equipmentValidator = equipmentValidator;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("employee", getEmployee());
        return "/equipment/equipment_index";
    }

    @GetMapping("/new")
    public String newEquipment(Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("equipment", new NewEquipmentDTO());
        model.addAttribute("classes", equipmentClassDAO.findAll());
        return "/equipment/new_equipment";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("equipment") @Valid NewEquipmentDTO newEquipmentDTO,
                       BindingResult bindingResult,
                       Model model) {
        equipmentValidator.validate(newEquipmentDTO, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", getEmployee());
            model.addAttribute("classes", equipmentClassDAO.findAll());
            return "/equipment/new_equipment";
        }
        equipmentService.save(equipmentConverter.convertToEquipment(newEquipmentDTO));
        return "redirect:/equipment";
    }

    @GetMapping("/{id}")
    public String equipmentInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("equipment" , equipmentService.findOneById(id));
        model.addAttribute("classes", equipmentClassDAO.findAll());
        return "/equipment/show_equipment";
    }

    @PatchMapping("/{id}/update")
    public String update(@ModelAttribute("equipment") @Valid Equipment equipment, BindingResult bindingResult,
                         @PathVariable("id") int id,
                         Model model) {
        equipmentValidator.validateOnUpdate(equipment, bindingResult, id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", getEmployee());
            model.addAttribute("classes", equipmentClassDAO.findAll());
            return "/equipment/show_equipment";
        } else {
            equipmentService.update(id, equipment);
            return "redirect:/equipment";
        }
    }

    @DeleteMapping("/{id}/delete")
    private String delete(@PathVariable("id") int id) {
        equipmentService.delete(id);
        return "redirect:/equipment/" + id;
    }

    @GetMapping("/get_equipment")
    @ResponseBody
    public List<EquipmentResponse> getEquipment(@RequestParam("page") int page,
                                                @RequestParam("results_per_page") int resultPerPage,
                                                @RequestParam(value = "order_by", required = false) String orderBy,
                                                @RequestParam(value = "serial", required = false) String serial,
                                                @RequestParam(value = "name", required = false) String name,
                                                @RequestParam(value = "equipment_class", required = false) String equipmentClassId) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("e.serial", serial);
        searchParams.put("e.name", name);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("e.equipmentClass.id", equipmentClassId);

        return equipmentService.findAll(page, resultPerPage, orderBy, searchParams, filterParams).stream().map(equipmentConverter::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/rows_count")
    @ResponseBody
    public int getRowsCount(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "serial", required = false) String serial,
                            @RequestParam(value = "equipment_class", required = false) String equipmentClassId) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("e.serial", serial);
        searchParams.put("e.name", name);

        Map<String, Object> filterParams = new HashMap<>();
        filterParams.put("e.equipmentClass.id", equipmentClassId);

        return equipmentService.rowsCount(searchParams, filterParams);
    }

    private Employee getEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        return accountDetails.getAccount().getEmployee();
    }
}
