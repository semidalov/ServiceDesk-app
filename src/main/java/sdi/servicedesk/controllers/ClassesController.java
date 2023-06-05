package sdi.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sdi.servicedesk.dto.EquipmentClassResponse;
import sdi.servicedesk.dto.classes.EditClassDTO;
import sdi.servicedesk.dto.classes.NewClassDTO;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.EquipmentClass;
import sdi.servicedesk.models.UserRole;
import sdi.servicedesk.security.AccountDetails;
import sdi.servicedesk.services.EquipmentClassService;
import sdi.servicedesk.utils.EquipmentClassConverter;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/classes")
public class ClassesController {

    private final EquipmentClassService equipmentClassService;
    private final EquipmentClassConverter equipmentClassConverter;

    @Autowired
    public ClassesController(EquipmentClassService equipmentClassService, EquipmentClassConverter equipmentClassConverter) {
        this.equipmentClassService = equipmentClassService;
        this.equipmentClassConverter = equipmentClassConverter;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("employee", getEmployee());
        return "/equipment_classes/equipment_classes_index";
    }

    @GetMapping("/new")
    public String newClass(Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("newClass", new NewClassDTO());
        return "/equipment_classes/new_class";
    }

    @PostMapping("save")
    public String save(@ModelAttribute("newClass") @Valid NewClassDTO newClassDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()){
            model.addAttribute("employee", getEmployee());
            return "/equipment_classes/new_class";
        }
        equipmentClassService.save(equipmentClassConverter.convertToClassWhileSave(new EquipmentClass(), newClassDTO));
        return "redirect:/classes";
    }

    @GetMapping("/{id}")
    public String showClass(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("equipmentClass", equipmentClassConverter.convertToDTOWhileUpdate(equipmentClassService.findOneById(id)));

        return "/equipment_classes/show_class";
    }

    @PatchMapping("/{id}/update")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("equipmentClass") @Valid EditClassDTO classDTO,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", getEmployee());
            return "/equipment_classes/show_class";
        }
        equipmentClassService.update(equipmentClassConverter.convertToClassWhileUpdate(new EquipmentClass(), classDTO), id);
        return "redirect:/classes";

    }


    @GetMapping("/get_classes")
    @ResponseBody
    public List<EquipmentClassResponse> getClasses(@RequestParam("page") int page,
                                                   @RequestParam("results_per_page") int resultPerPage,
                                                   @RequestParam(value = "order_by", required = false) String orderBy,
                                                   @RequestParam(value = "name", required = false) String name) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("c.name", name);

        return equipmentClassService.findAll(page, resultPerPage, orderBy, searchParams).stream()
                .map(equipmentClassConverter::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/rows_count")
    @ResponseBody
    public int getRowsCount() {
        return equipmentClassService.rowsCount();
    }

    private Employee getEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        return accountDetails.getAccount().getEmployee();
    }

}
