package sdi.servicedesk.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "employees")
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ToString.Exclude
    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private User user;

    @NotEmpty
    @Column(name = "firstname")
    private String firstName;

    @NotEmpty
    @Column(name = "lastname")
    private String lastName;

    @Column(name = "patronymic")
    private String patronymic;

    @Column(name = "work_phone")
    private String workPhone;

    @ToString.Exclude
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "creator")
    private List<Task> serviceRequested;

    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "executor")
    private List<Task> serviceExecuted;

}
