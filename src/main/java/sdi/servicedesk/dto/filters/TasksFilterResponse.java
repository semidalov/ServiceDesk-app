package sdi.servicedesk.dto.filters;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TasksFilterResponse {

    private List<Map<String, Integer>> statuses;

    private List<Map<String, Integer>> priorities;

    public TasksFilterResponse() {
        statuses = new ArrayList<>();
        priorities = new ArrayList<>();
    }

    public void addStatus(String name, Integer id) {
        Map<String, Integer> status = new HashMap<>();
        status.put(name, id);
        statuses.add(status);
    }

    public void addPriority(String name, Integer id) {
        Map<String, Integer> priority = new HashMap<>();
        priority.put(name, id);
        priorities.add(priority);
    }
}
