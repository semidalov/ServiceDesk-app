package sdi.servicedesk.dto.filters;

import lombok.Getter;
import lombok.Setter;
import sdi.servicedesk.models.Incident;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class UsersFilterResponse {

    private List<Map<String, Integer>> groups;

    private List<Map<String, Integer>> roles;

    public UsersFilterResponse() {
        roles = new ArrayList<>();
        groups = new ArrayList<>();
    }

    public void addGroup(String name, Integer id) {
        Map<String, Integer> group = new HashMap<>();
        group.put(name, id);
        groups.add(group);
    }

    public void addRole(String name, Integer id) {
        Map<String, Integer> role = new HashMap<>();
        role.put(name, id);
        roles.add(role);
    }
}
