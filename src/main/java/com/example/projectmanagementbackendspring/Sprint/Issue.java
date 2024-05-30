package com.example.projectmanagementbackendspring.Sprint;

import com.example.projectmanagementbackendspring.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "issue")
public class Issue {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nazwa;
    private String etap;
    private String scrum;

    @ManyToOne
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    private User Creator;


    @ManyToMany
    @JoinTable(
            name = "issue_users",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "issue_comment",
            joinColumns = @JoinColumn(name = "issue_id"),
            inverseJoinColumns = @JoinColumn(name = "comment_id")
    )
    private Set<Comment> comments = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "sprint_id", referencedColumnName = "id")
    private Sprint sprint;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getEtap() {
        return etap;
    }

    public void setEtap(String etap) {
        this.etap = etap;
    }

    public User getCreator() {
        return Creator;
    }

    public void setCreator(User creator) {
        Creator = creator;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Sprint getSprint() {
        return sprint;
    }

    public void setSprint(Sprint sprint) {
        this.sprint = sprint;
    }

    public String getScrum() {
        return scrum;
    }

    public void setScrum(String scrum) {
        this.scrum = scrum;
    }


}
