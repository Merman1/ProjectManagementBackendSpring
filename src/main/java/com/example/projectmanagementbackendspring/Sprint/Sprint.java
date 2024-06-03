package com.example.projectmanagementbackendspring.Sprint;

import com.example.projectmanagementbackendspring.Project.Project;
import com.example.projectmanagementbackendspring.user.User;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sprint")
public class Sprint {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String czasTrwania;
    @Temporal(TemporalType.DATE)
    private Date dataRozpoczecia;
    @Temporal(TemporalType.DATE)
    private Date dataZakonczenia;

    private boolean isStarted = false;  // Dodaj to pole
    private String celSprintu;

    @ManyToOne
    @JoinColumn(name = "projects_id", referencedColumnName = "id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "leader_id", referencedColumnName = "id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "sprint_issue",
            joinColumns = @JoinColumn(name = "sprint_id"),
            inverseJoinColumns = @JoinColumn(name = "issue_id")
    )
    private Set<Issue> issues = new HashSet<>();


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCzasTrwania() {
        return czasTrwania;
    }

    public void setCzasTrwania(String czasTrwania) {
        this.czasTrwania = czasTrwania;
    }

    public Date getDataRozpoczecia() {
        return dataRozpoczecia;
    }

    public void setDataRozpoczecia(Date dataRozpoczecia) {
        this.dataRozpoczecia = dataRozpoczecia;
    }

    public Date getDataZakonczenia() {
        return dataZakonczenia;
    }

    public void setDataZakonczenia(Date dataZakonczenia) {
        this.dataZakonczenia = dataZakonczenia;
    }

    public String getCelSprintu() {
        return celSprintu;
    }

    public void setCelSprintu(String celSprintu) {
        this.celSprintu = celSprintu;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Set<Issue> getIssues() {
        return issues;
    }

    public void setIssues(Set<Issue> issues) {
        this.issues = issues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }


}
