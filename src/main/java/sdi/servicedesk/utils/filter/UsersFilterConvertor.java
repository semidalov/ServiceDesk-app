package sdi.servicedesk.utils.filter;

import org.springframework.stereotype.Component;
import sdi.servicedesk.dto.filters.UsersFilterResponse;
import sdi.servicedesk.models.Group;
import sdi.servicedesk.models.UserRole;

import java.util.List;

@Component
public class UsersFilterConvertor {

    public UsersFilterResponse convertToDTO(List<Group> groups, List<UserRole> userRoles) {

        UsersFilterResponse response = new UsersFilterResponse();

        groups.forEach(el -> response.addGroup(el.getName(), el.getId()));
        userRoles.forEach(el -> response.addRole(el.getName(), el.getId()));

        return response;
    }
}
