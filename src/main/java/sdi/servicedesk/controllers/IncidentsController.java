package sdi.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sdi.servicedesk.dto.IncidentNewOrderDTO;
import sdi.servicedesk.dto.IncidentServiceDTO;
import sdi.servicedesk.dto.NewIncidentDTO;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.Incident;
import sdi.servicedesk.security.AccountDetails;
import sdi.servicedesk.services.EquipmentClassService;
import sdi.servicedesk.services.IncidentService;
import sdi.servicedesk.services.PriorityService;
import sdi.servicedesk.utils.IncidentConvertor;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("incidents")
public class IncidentsController {
    private final EquipmentClassService equipmentClassService;
    private final PriorityService priorityService;
    private final IncidentConvertor incidentConvertor;
    private final IncidentService incidentService;

    @Autowired
    public IncidentsController(EquipmentClassService equipmentClassService, PriorityService priorityService, IncidentConvertor incidentConvertor, IncidentService incidentService) {
        this.equipmentClassService = equipmentClassService;
        this.priorityService = priorityService;
        this.incidentConvertor = incidentConvertor;
        this.incidentService = incidentService;
    }

    @GetMapping
    public String index() {
        return "/in_development";
    }


    @GetMapping("/incidents")
    @ResponseBody
    public List<IncidentNewOrderDTO> getIncidentsByEquipmentID(@RequestParam("equipment_id") int equipmentId,
                                                               @RequestParam("page") int page,
                                                               @RequestParam("results_per_page") int resultsPerPage,
                                                               @RequestParam(value = "order_by", required = false) String orderBy,
                                                               @RequestParam(value = "title", required = false) String title) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("i.title", title);

        List<Incident> incidents = incidentService.findByEquipmentId(page, resultsPerPage, equipmentId, orderBy, searchParams);
        return incidents.stream().map(i -> {
            IncidentNewOrderDTO incidentNewOrderDTO = new IncidentNewOrderDTO();
            incidentNewOrderDTO.setId(i.getId());
            incidentNewOrderDTO.setTitle(i.getTitle());
            return incidentNewOrderDTO;
        }).collect(Collectors.toList());
    }

    @GetMapping("/new")
    public String newIncident(Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("classes", equipmentClassService.findAll());
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("incident", new NewIncidentDTO());
        return "incidents/new";
    }

    @GetMapping("/{id}")
    public String info(@PathVariable("id") int id, Model model) {
        model.addAttribute("employee", getEmployee());
        model.addAttribute("classes", equipmentClassService.findAll());
        model.addAttribute("priorities", priorityService.findAll());
        model.addAttribute("incident", incidentService.findById(id));
        return "/incidents/edit";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute("incident") @Valid NewIncidentDTO newIncidentDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", getEmployee());
            model.addAttribute("classes", equipmentClassService.findAll());
            model.addAttribute("priorities", priorityService.findAll());
            return "incidents/new";
        }
        incidentService.saveIncident(incidentConvertor.convertToIncident(newIncidentDTO));
        return "redirect:/incidents/new";
    }

    @PatchMapping("/{id}/update")
    public String update(@PathVariable("id") int id, @ModelAttribute("incident") @Valid NewIncidentDTO newIncidentDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("employee", getEmployee());
            model.addAttribute("classes", equipmentClassService.findAll());
            model.addAttribute("priorities", priorityService.findAll());
            return "incidents/new";
        }
        incidentService.update(id, incidentConvertor.convertToIncident(newIncidentDTO));
        return "redirect:/incidents/new";
    }

    @DeleteMapping("/{id}/delete")
    private String delete(@PathVariable("id") int id) {
        incidentService.delete(id);
        return "redirect:/service";
    }

    @GetMapping("/rows_count/{id}")
    @ResponseBody
    public int rowsCount(@PathVariable("id") int equipmentId,
                            @RequestParam(value = "title", required = false) String title) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("i.title", title);

        return incidentService.rowsCount(equipmentId, searchParams);
    }

    private Employee getEmployee() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();
        return accountDetails.getAccount().getEmployee();
    }
}
