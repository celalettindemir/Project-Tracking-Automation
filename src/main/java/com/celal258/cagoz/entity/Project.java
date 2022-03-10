package com.celal258.cagoz.entity;


import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;
import java.util.Date;

@Entity
@Table(name = "Project")
public class Project {


    public enum ProjectDraw{
        PRESENT, WILLBEDRAWN, WILLBECOME
    }


    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO,generator="native")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @OneToOne(mappedBy = "project",cascade = CascadeType.ALL, orphanRemoval = true, targetEntity = Remind.class)
    private Remind remind;

    @OneToMany(mappedBy = "project",cascade = CascadeType.ALL,targetEntity = Paper.class)
    @OrderBy("id desc")
    private List<Paper> papers;

    @OneToMany(mappedBy="project")
    private List<Document> document;

    @Column(name = "projectName")
    private String projectName;

    @Column(name = "projectAda")
    private String projectAda;

    @Column(name = "date")
    private Date date;

    @Column(name = "projectType")
    private String projectType;

    @Column(name = "price")
    private int price;

    @Column(name = "receivedMoney")
    private int receivedMoney;

    @Column(name = "projectDescription")
    private String projectDescription;

    @Column(name = "projectOrder")
    private String projectOrder;

    @Column(name = "projectRemind")
    private String projectRemind;

    @Column(name = "projectDraw")
    private ProjectDraw projectDraw;

    @Column(name = "projectTapu")
    private Boolean projectTapu=false;

    @Column(name = "completed")
    private Boolean completed=false;

    @Column(name = "projectRuhsat")
    private Boolean projectRuhsat=false;

    @Column(name = "projectVekalet")
    private Boolean projectVekalet=false;

    @Column(name = "projectImarAffi")
    private Boolean projectImarAffi=false;

    @Column(name = "projectBelediyeYazisi")
    private Boolean projectBelediyeYazisi=false;

    @Column(name = "projectIlceTarim")
    private Boolean projectIlceTarim=false;

    @Column(name = "projectDSI")
    private Boolean projectDSI=false;

    @Column(name = "projectMevcutFatura")
    private Boolean projectMevcutFatura=false;

    @Column(name = "projectEmlakBeyani")
    private Boolean projectEmlakBeyani=false;

    @Column(name = "projectDurum")
    private Boolean projectDurum=false;


    public Boolean getCompleted() {
        return projectDurum;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }


    public Boolean getProjectDurum() {
        return projectDurum;
    }

    public void setProjectDurum(Boolean projectDurum) {
        this.projectDurum = projectDurum;
    }

    public Boolean getProjectTapu() {
        return projectTapu;
    }

    public void setProjectTapu(Boolean projectTapu) {
        this.projectTapu = projectTapu;
    }

    public Boolean getProjectRuhsat() {
        return projectRuhsat;
    }

    public void setProjectRuhsat(Boolean projectRuhsat) {
        this.projectRuhsat = projectRuhsat;
    }

    public Boolean getProjectVekalet() {
        return projectVekalet;
    }

    public void setProjectVekalet(Boolean projectVekalet) {
        this.projectVekalet = projectVekalet;
    }

    public Boolean getProjectImarAffi() {
        return projectImarAffi;
    }

    public void setProjectImarAffi(Boolean projectImarAffi) {
        this.projectImarAffi = projectImarAffi;
    }

    public Boolean getProjectBelediyeYazisi() {
        return projectBelediyeYazisi;
    }

    public void setProjectBelediyeYazisi(Boolean projectBelediyeYazisi) {
        this.projectBelediyeYazisi = projectBelediyeYazisi;
    }

    public Boolean getProjectIlceTarim() {
        return projectIlceTarim;
    }

    public void setProjectIlceTarim(Boolean projectIlceTarim) {
        this.projectIlceTarim = projectIlceTarim;
    }

    public Boolean getProjectDSI() {
        return projectDSI;
    }

    public void setProjectDSI(Boolean projectDSI) {
        this.projectDSI = projectDSI;
    }

    public Boolean getProjectMevcutFatura() {
        return projectMevcutFatura;
    }

    public void setProjectMevcutFatura(Boolean projectMevcutFatura) {
        this.projectMevcutFatura = projectMevcutFatura;
    }

    public Boolean getProjectEmlakBeyani() {
        return projectEmlakBeyani;
    }

    public void setProjectEmlakBeyani(Boolean projectEmlakBeyani) {
        this.projectEmlakBeyani = projectEmlakBeyani;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Remind getRemind(){
        return remind;
    }

    public void setRemind(Remind remind) {
        this.remind = remind;
    }

    public List<Paper> getPapers() {
        return papers;
    }

    public void setPapers(List<Paper> papers) {
        this.papers = papers;
    }

    public List<Document> getDocument() {
        return document;
    }

    public void setDocument(List<Document> document) {
        this.document = document;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectAda() {
        return projectAda;
    }

    public void setProjectAda(String projectAda) {
        this.projectAda = projectAda;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getReceivedMoney() {
        return receivedMoney;
    }

    public void setReceivedMoney(int receivedMoney) {
        this.receivedMoney = receivedMoney;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectOrder() {
        return projectOrder;
    }

    public void setProjectOrder(String projectOrder) {
        this.projectOrder = projectOrder;
    }



    public String getProjectRemind() {
        return projectRemind;
    }

    public void setProjectRemind(String projectRemind) {
        this.projectRemind = projectRemind;
    }


    public ProjectDraw getProjectDraw() {
        return projectDraw;
    }

    public void setProjectDraw(ProjectDraw projectDraw) {
        this.projectDraw = projectDraw;
    }
}
