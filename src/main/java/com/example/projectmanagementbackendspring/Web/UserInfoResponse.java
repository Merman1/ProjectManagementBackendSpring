package com.example.projectmanagementbackendspring.Web;


import com.example.projectmanagementbackendspring.user.Role;

import java.util.Set;

public class UserInfoResponse {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles; // Dodaj listę ról

    private String location;
    private String organization;

    private String adress;
    private String firstName;

    private String lastName;
    private String publicName;
    private String positionName;
    private Integer number;

    public UserInfoResponse(Long id, String username, String email, Set<Role> roles, String lastName, String firstName, String adress, String positionName, String location, Integer number,String organization, String publicName) {
      this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.publicName = publicName;
        this.positionName = positionName;
        this.number = number;
        this.organization = organization;
        this.location = location;
        this.adress=adress;
    }
    public void addRole(Role role) {
        this.roles.add(role);
    }
    public String getUsername() {
        return username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
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

    public String getPublicName() {
        return publicName;
    }

    public void setPublicName(String publicName) {
        this.publicName = publicName;
    }

    public String getPositionName() {
        return positionName;
    }

    public void setPositionName(String positionName) {
        this.positionName = positionName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}