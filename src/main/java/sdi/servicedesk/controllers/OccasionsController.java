package sdi.servicedesk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sdi.servicedesk.dto.occasions.OccasionDTO;
import sdi.servicedesk.services.OccasionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/occasions")
public class OccasionsController {

    private final OccasionService occasionService;

    @Autowired
    public OccasionsController(OccasionService occasionService) {
        this.occasionService = occasionService;
    }

    @GetMapping
    public String index() {
        return "in_development";
    }

    @RequestMapping("/get_occasions")
    @ResponseBody
    public List<OccasionDTO> getOccasions(@RequestParam("page") int page,
                                          @RequestParam("results_per_page") int resultsPerPage,
                                          @RequestParam(value = "name", required = false) String name) {
        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("o.name", name);

        return occasionService.getOccasions(page, resultsPerPage, searchParams).stream().map(o -> {
            OccasionDTO occasionDTO = new OccasionDTO();
            occasionDTO.setId(o.getId());
            occasionDTO.setName(o.getName());
            return occasionDTO;
        }).collect(Collectors.toList());
    }

    @RequestMapping("/rows_count")
    @ResponseBody
    public int rowsCount(@RequestParam(value = "name", required = false) String name) {

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("o.name", name);

        return occasionService.rowsCount(searchParams);
    }
}
