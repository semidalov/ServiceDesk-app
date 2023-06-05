package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.UserDAO;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserDAO userDaO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDAO userDaO, PasswordEncoder passwordEncoder) {
        this.userDaO = userDaO;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getUsers(int page, int resultsPerPage, Map<String, Object> searchParams, String orderBy,
                               Map<String, Object> filterParams) {
        return userDaO.findUsers(page, resultsPerPage, searchParams, orderBy, filterParams);
    }

    public List<Employee> getExecutors(int page, int resultsPerPage, Map<String, Object> searchParams, String orderBy) {
        return userDaO.findExecutors(page, resultsPerPage, searchParams, orderBy);
    }

    public int employeesRowsCount(Map<String, Object> searchParams, Map<String, Object> filterParams) {
        return userDaO.employeesRowsCount(searchParams, filterParams);
    }

    public void update(User user, int id) {
        userDaO.update(user, id);
    }

    public void saveUser(User user) {
        fillUser(user);
        userDaO.saveUser(user);
    }

    public User getUserById(int id) {
        return userDaO.findUserById(id);
    }

    public void setUserLastLogin(User user) {
        userDaO.updateUserLastLogin(LocalDateTime.now(), user);
    }

    public void blockUser(int id) {
        userDaO.blockUser(id);
    }

    public void unblockUser(int id) {
        userDaO.unblockUser(id);
    }

    private void fillUser(User user) {
        user.setPassword(passwordEncoder.encode("admin"));
        user.setJoinDate(LocalDateTime.now());
        user.setLocked(false);
    }


}
