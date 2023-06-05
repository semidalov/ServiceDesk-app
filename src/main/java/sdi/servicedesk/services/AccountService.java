package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.UserDAO;
import sdi.servicedesk.models.User;

@Service
public class AccountService {
    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AccountService(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public void updatePassword(String pass, User user) {
        userDAO.updatePasswordByUserId(passwordEncoder.encode(pass), user.getId());
    }

    public void resetPasswordByUserId(int id) {
        userDAO.updatePasswordByUserId(passwordEncoder.encode("admin"), id);
    }

}
