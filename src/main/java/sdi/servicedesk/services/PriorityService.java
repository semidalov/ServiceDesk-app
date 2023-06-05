package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.PriorityDAO;
import sdi.servicedesk.models.Priority;

import java.util.List;

@Service
public class PriorityService {

    private final PriorityDAO priorityDAO;

    @Autowired
    public PriorityService(PriorityDAO priorityDAO) {
        this.priorityDAO = priorityDAO;
    }

    public List<Priority> findAll() {
        return priorityDAO.findAll();
    }
}

