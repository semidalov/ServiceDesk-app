package sdi.servicedesk.models;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "tasks")
@ToString(onlyExplicitlyIncluded = true)
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private Employee creator;

    @ToString.Include
    @NotEmpty
    @Column(name = "title")
    private String title;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @Column(name = "created")
    private LocalDateTime created;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private TaskStatus taskStatus;

    @NotNull
    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Transient
    private Boolean expired;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incident_id", referencedColumnName = "id")
    private Incident incident;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "occasion_id", referencedColumnName = "id")
    private Occasion occasion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "executor_id", referencedColumnName = "id")
    private Employee executor;

    @Column(name = "closed_datetime")
    private LocalDateTime closed;

}
