package sdi.servicedesk.utils.converters;

import org.springframework.stereotype.Component;
import sdi.servicedesk.dto.users.GetExecutorsWhileTaskUpdateDTO;
import sdi.servicedesk.models.Employee;
import sdi.servicedesk.utils.UserConverter;

@Component
public class ExecutorConverter {

    public GetExecutorsWhileTaskUpdateDTO convertToDTO(Employee employee) {
        GetExecutorsWhileTaskUpdateDTO getExecutorsWhileTaskUpdateDTO = new GetExecutorsWhileTaskUpdateDTO();

        getExecutorsWhileTaskUpdateDTO.setId(employee.getId());
        getExecutorsWhileTaskUpdateDTO.setFullName(UserConverter.buildFIO(employee));
        getExecutorsWhileTaskUpdateDTO.setGroupName(employee.getGroup().getName());

        return getExecutorsWhileTaskUpdateDTO;
    }
}
