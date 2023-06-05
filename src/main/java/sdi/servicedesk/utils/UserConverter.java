package sdi.servicedesk.utils;

import org.springframework.stereotype.Component;
import sdi.servicedesk.dto.users.GetUsersResponseDTO;
import sdi.servicedesk.dto.users.NewUserDTO;
import sdi.servicedesk.dto.users.EditUserDTO;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.models.User;

@Component
public class UserConverter {

    public User convertToUserWhileSave(NewUserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername().trim());
        user.setRole(userDTO.getRole());

        Employee employee = new Employee();
        employee.setUser(user);
        employee.setFirstName(userDTO.getFirstname().trim());
        employee.setLastName(userDTO.getLastname().trim());
        employee.setPatronymic(userDTO.getPatronymic().trim());
        employee.setWorkPhone(userDTO.getWorkPhone().trim());
        employee.setGroup(userDTO.getGroup());

        user.setEmployee(employee);

        return user;
    }

    public User convertToUserWhileUpdate (EditUserDTO userDTO, User user) {

        return convertToUserWhileSave(userDTO);
    }

    public EditUserDTO convertToUserDTOForEditForm(User user) {
        EditUserDTO userDTO = new EditUserDTO();

        userDTO.setUsername(user.getUsername());
        userDTO.setRole(user.getRole());
        userDTO.setFirstname(user.getEmployee().getFirstName());
        userDTO.setLastname(user.getEmployee().getLastName());
        userDTO.setPatronymic(user.getEmployee().getPatronymic());
        userDTO.setGroup(user.getEmployee().getGroup());
        userDTO.setWorkPhone(user.getEmployee().getWorkPhone());
        userDTO.setId(user.getId());
        userDTO.setLocked(user.getLocked());
        userDTO.setJoinDate(user.getJoinDate());
        userDTO.setLastLogin(user.getLastLogin());

        return userDTO;
    }

    public GetUsersResponseDTO convertToDTO(User user) {
        GetUsersResponseDTO getUsersResponseDTO = new GetUsersResponseDTO();

        getUsersResponseDTO.setId(user.getId());
        getUsersResponseDTO.setFullName(buildFIO(user.getEmployee()));
        getUsersResponseDTO.setWorkPhone(user.getEmployee().getWorkPhone());
        getUsersResponseDTO.setGroupName(user.getEmployee().getGroup().getName());
        getUsersResponseDTO.setUsername(user.getUsername());
        getUsersResponseDTO.setRole(user.getRole().getName());
        getUsersResponseDTO.setLocked(user.getLocked());
        getUsersResponseDTO.setJoinDate(user.getJoinDate().toString());
        if (user.getLastLogin() != null)
            getUsersResponseDTO.setLastLogin(user.getLastLogin().toString());

        return getUsersResponseDTO;
    }

    public static String buildFIO(Employee employee) {

        return employee.getPatronymic() == null ? employee.getLastName() + " " + employee.getFirstName() :
                employee.getLastName() + " " + employee.getFirstName() + " " + employee.getPatronymic();
    }
}