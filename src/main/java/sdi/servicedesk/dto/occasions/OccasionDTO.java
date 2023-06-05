package sdi.servicedesk.dto.occasions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
public class OccasionDTO {

    private int id;

    @Size(min = 10, max = 255)
    private String name;
}
