package com.celal258.cagoz.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Logging")
public class Logging {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName")
    private String userName;

    @Column(name = "projectId")
    private Long projectId;

    @Column(name = "loggingText")
    private String loggingText;

    @OneToOne
    @JoinColumn(name = "loggingEventTypeId", referencedColumnName = "id")
    private LoggingEventType loggingEventType;

    @Column(name = "loggingDate")
    private Date loggingDate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getLoggingText(){
        return loggingText;
    }

    public void setLoggingText(String loggingText){
        this.loggingText = loggingText;
    }

    public LoggingEventType getLoggingEventType() {
        return loggingEventType;
    }

    public void setLoggingEventType(LoggingEventType loggingEventType) {
        this.loggingEventType = loggingEventType;
    }

    public Date getLoggingDate() {
        return loggingDate;
    }

    public void setLoggingDate(Date loggingDate) {
        this.loggingDate = loggingDate;
    }


}
