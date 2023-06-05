package sdi.servicedesk.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import sdi.servicedesk.dao.UserDAO;
import sdi.servicedesk.dto.users.NewUserDTO;
import sdi.servicedesk.dto.users.EditUserDTO;

import java.util.List;

@Component
public class UserValidator implements Validator {

    public final UserDAO userDAO;

    @Autowired
    public UserValidator(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return NewUserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        if (!userDAO.findByName(((NewUserDTO) o).getUsername().trim()).isEmpty()) {
            errors.rejectValue("username", "", "Пользователь с таким уменем уже существует");
        }
    }

    public void validateWhileUpdate(Object o, Errors errors) {
        List<Object[]> result = userDAO.findByName(((EditUserDTO) o).getUsername().trim());

        if (!userDAO.findByName(((EditUserDTO) o).getUsername().trim()).isEmpty()) {
            if ((int) result.get(0)[1] != ((EditUserDTO) o).getId())
                errors.rejectValue("username", "", "Пользователь с таким уменем уже существует");
        }
    }
}
