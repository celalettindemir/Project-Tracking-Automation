package com.celal258.cagoz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Remind")
public class Remind {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO,generator="remindNative")
    @GenericGenerator(
            name = "remindNative",
            strategy = "native"
    )
    private Long id;

    @OneToOne
    @JoinColumn(name = "projectId", referencedColumnName = "id", nullable = false)
    private Project project;

    @Column(name = "startDate")
    private Date startDate;

    @Column(name = "repeat")
    private String repeat;

    @Column(name = "description")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "RemindRepository{" +
                "id=" + id +
                ", project=" + project +
                ", repeat='" + repeat + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
