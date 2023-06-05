package sdi.servicedesk.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class IncidentServiceDTO {

    private String title;

    private String incidentClass;

    private String priority;

}
