package com.celal258.cagoz.entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name="Customer")
public class Customer {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "userId", referencedColumnName = "id", nullable = false)
    private User user;

    @OneToMany( mappedBy = "customer",  targetEntity = Project.class)
    private Collection<Project> projects;

    @Column(name="firstName")
    private String firstName;

    @Column(name="lastName")
    private String lastName;

    @Column(name="number")
    private String number;

    @Column(name="company")
    private String company;

    @Column(name="customerDescription")
    private String customerDescription;

    @Column(name="debt")
    private int debt;

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

    public Collection<Project> getProjects() {
        return projects;
    }

    public void setProjects(Collection<Project> projects) {
        this.projects = projects;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCustomerDescription() {
        return customerDescription;
    }

    public void setCustomerDescription(String customerDescription) {
        this.customerDescription = customerDescription;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public Customer() {
    }

    public Customer(Long id, String firstName, String number, String company, String customerDescription, int debt) {
        this.id=id;
        this.firstName = firstName;
        this.number = number;
        this.company = company;
        this.customerDescription = customerDescription;
        this.debt = debt;
    }

    /*@Override
    public String toString() {
        return "CustomerRepository{" +
                "id=" + id +
                ", user=" + user +
                ", projects=" + projects +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", number='" + number + '\'' +
                ", company='" + company + '\'' +
                ", customerDescription='" + customerDescription + '\'' +
                ", debt='" + debt + '\'' +
                '}';
    }*/
}
