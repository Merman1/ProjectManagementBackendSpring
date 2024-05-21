package com.example.projectmanagementbackendspring.Web;


import com.example.projectmanagementbackendspring.user.Role;

import java.util.Set;

public class UserInfoResponse {
    private String username;
    private String email;
    private Set<Role> roles; // Dodaj listę ról

    public UserInfoResponse(String username, String email, Set<Role> roles) {
        this.username = username;
        this.email = email;
        this.roles = roles;
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
}