package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.IncidentDAO;
import sdi.servicedesk.models.Equipment;
import sdi.servicedesk.models.EquipmentClass;
import sdi.servicedesk.models.Incident;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IncidentService {

    private final IncidentDAO incidentDAO;

    @Autowired
    public IncidentService(IncidentDAO incidentDAO) {
        this.incidentDAO = incidentDAO;
    }

    public void saveIncident(Incident incident) {
        incidentDAO.save(incident);
    }

    public Incident findById(int id) {
        return incidentDAO.findById(id);
    }

    public void update(int id, Incident incident) {
        incidentDAO.update(id, incident);
    }

    public List<Incident> findAll(int page, int resultsPerPAge) {
        return incidentDAO.findAll(page, resultsPerPAge);
    }

    public List<Incident> findByEquipmentId(int page, int resultsPerPage, int equipmentId, String orderBy, Map<String, Object> searchParams) {
        return incidentDAO.findByEquipmentId(page, resultsPerPage, equipmentId, orderBy, searchParams);
    }

    public int rowsCount(int equipmentId, Map<String, Object> searchParams) {
        return incidentDAO.rowsCount(equipmentId, searchParams);
    }

    public void delete(int id) {
        incidentDAO.delete(id);
    }
}
