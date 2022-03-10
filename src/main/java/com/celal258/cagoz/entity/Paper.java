package com.celal258.cagoz.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Paper")
public class Paper {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO,generator="paperNative")
    @GenericGenerator(
            name = "paperNative",
            strategy = "native"
    )
    private Long id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "projectId", referencedColumnName = "id", nullable = false)
    private Project project;

    @Column(name = "operationType")
    private String operationType;

    @Column(name = "dateOfReturn")
    private Date dateOfReturn;

    @Column(name = "dateOfGoing")
    private Date dateOfGoing;

    enum ApprovalStatus{
        REJECT, APPROVE, NOTSPECIFIED
    }

    @Column(name = "approvalStatus")
    private String approvalStatus;

    @Column(name = "paperNote")
    private String paperNote;

    @Column(name = "recordNumber")
    private String recordNumber;

    @Column(name = "paperRemind")
    private String paperRemind;

    @OneToOne(mappedBy = "paper",cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = RemindWork.class)
    private RemindWork remindWork;



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

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Date getDateOfReturn() {
        return dateOfReturn;
    }

    public void setDateOfReturn(Date dateOfReturn) {
        this.dateOfReturn = dateOfReturn;
    }

    public Date getDateOfGoing() {
        return dateOfGoing;
    }

    public void setDateOfGoing(Date dateOfGoing) {
        this.dateOfGoing = dateOfGoing;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public String getPaperNote() {
        return paperNote;
    }

    public void setPaperNote(String paperNote) {
        this.paperNote = paperNote;
    }

    public String getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(String recordNumber) {
        this.recordNumber = recordNumber;
    }

    public RemindWork getRemindWork() {
        return remindWork;
    }

    public void setRemindWork(RemindWork remindWork) {
        this.remindWork = remindWork;
    }

    public String getPaperRemind() {
        return paperRemind;
    }

    public void setPaperRemind(String paperRemind) {
        this.paperRemind = paperRemind;
    }


}
