package com.example.projectmanagementbackendspring.Project;


import com.example.projectmanagementbackendspring.user.User;
import com.example.projectmanagementbackendspring.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
@Autowired
private UserRepository userRepository;
@Autowired
private ProjectRepository projectRepository;
    @GetMapping
    public List<Project> getAllProjects() {
        return projectService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.findById(id);
        return project.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {

        // Przypisz lidera na podstawie leaderId
        userRepository.findById(project.getLeader().getId()).ifPresent(project::setLeader);

        // Przypisz użytkowników na podstawie userIds
        Set<User> users = project.getUsers();
        Set<User> updatedUsers = new HashSet<>();
        for (User user : users) {
            userRepository.findById(user.getId()).ifPresent(updatedUsers::add);
        }
        project.setUsers(updatedUsers);

        projectRepository.save(project);
        return ResponseEntity.ok(project);
    }
    @PutMapping("/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody Project project) {
        Optional<Project> existingProject = projectService.findById(id);
        if (existingProject.isPresent()) {
            project.setId(id);
            return projectService.save(project);
        } else {
            throw new RuntimeException("Project not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projectService.deleteById(id);
    }
}

