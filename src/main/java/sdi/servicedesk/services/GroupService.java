package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.GroupDAO;
import sdi.servicedesk.models.Group;

import java.util.List;

@Service
public class GroupService {

    private final GroupDAO groupDAO;

    @Autowired
    public GroupService(GroupDAO groupDAO) {
        this.groupDAO = groupDAO;
    }

    public List<Group> findAll() {
        return groupDAO.findAll();
    }
}
