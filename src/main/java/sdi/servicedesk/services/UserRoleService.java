package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.UserRoleDAO;
import sdi.servicedesk.models.UserRole;

import java.util.List;

@Service
public class UserRoleService {

    private final UserRoleDAO roleDAO;

    @Autowired
    public UserRoleService(UserRoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public List<UserRole> findAll() {
        return roleDAO.findAll();
    }
}
