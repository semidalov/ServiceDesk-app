package sdi.servicedesk.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sdi.servicedesk.dao.UserDAO;
import sdi.servicedesk.models.User;
import sdi.servicedesk.security.AccountDetails;

import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {


    private final UserDAO userDAO;

    @Autowired
    public UserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> account = userDAO.findAccountByName(s);
        if (account.isEmpty()) throw new UsernameNotFoundException("Пользователь не найден");
        return new AccountDetails(account.get());
    }
}
