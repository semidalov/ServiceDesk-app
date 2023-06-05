package sdi.servicedesk.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "equipment_classes")
@ToString
public class EquipmentClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @NotEmpty
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipmentClass")
    private List<Equipment> equipments;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "equipmentClass")
    private List<Incident> incidents;

}
