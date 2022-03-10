package com.celal258.cagoz.entity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
@Entity
@Table(name = "RemindWork")
public class RemindWork {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO,generator="remindNative")
    @GenericGenerator(
            name = "remindNative",
            strategy = "native"
    )
    private Long id;

    @OneToOne
    @JoinColumn(name = "paperId", referencedColumnName = "id", nullable = false)
    private Paper paper;

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

    public Paper getWork() {
        return paper;
    }

    public void setWork(Paper paper) {
        this.paper = paper;
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
                ", work=" + paper +
                ", repeat='" + repeat + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

}
