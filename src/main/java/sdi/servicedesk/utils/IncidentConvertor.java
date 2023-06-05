package sdi.servicedesk.utils;

import org.springframework.stereotype.Component;
import sdi.servicedesk.dto.IncidentServiceDTO;
import sdi.servicedesk.dto.NewIncidentDTO;
import sdi.servicedesk.models.Incident;

@Component
public class IncidentConvertor {

    public Incident convertToIncident(NewIncidentDTO incidentDTO) {
        Incident incident = new Incident();

        incident.setTitle(incidentDTO.getTitle());
        incident.setEquipmentClass(incidentDTO.getEquipmentClass());
        incident.setPriority(incidentDTO.getPriority());

        return incident;
    }

    public IncidentServiceDTO convertToDTO(Incident incident) {
        IncidentServiceDTO incidentServiceDTO = new IncidentServiceDTO();

        incidentServiceDTO.setTitle(incident.getTitle());
        incidentServiceDTO.setIncidentClass(incident.getEquipmentClass().getName());
        incidentServiceDTO.setPriority(incident.getPriority().getName());

        return incidentServiceDTO;
    }
}
