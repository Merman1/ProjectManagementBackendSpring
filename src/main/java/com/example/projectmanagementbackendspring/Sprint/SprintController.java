package com.example.projectmanagementbackendspring.Sprint;


import com.example.projectmanagementbackendspring.Project.Project;
import com.example.projectmanagementbackendspring.Project.ProjectRepository;
import com.example.projectmanagementbackendspring.Project.ProjectService;
import com.example.projectmanagementbackendspring.user.User;
import com.example.projectmanagementbackendspring.user.UserRepository;
import com.example.projectmanagementbackendspring.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/sprints")
public class SprintController {

    @Autowired
    private SprintService sprintService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProjectService projectService;

    @GetMapping
    public List<Sprint> getAllSprints() {
        return sprintService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sprint> getSprintById(@PathVariable Long id) {
        Optional<Sprint> sprint = sprintService.findById(id);
        return sprint.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping("/{projectId}/project_id")
    public ResponseEntity<List<Sprint>> getSprintsByProjectId(@PathVariable Long projectId) {
        List<Sprint> sprints = sprintService.findByProjectId(projectId);
        if (sprints.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(sprints);
    }
    @PutMapping("/{id}/start")
    public ResponseEntity<Sprint> startSprint(@PathVariable Long id) {
        Optional<Sprint> sprintOptional = sprintService.findById(id);
        if (sprintOptional.isPresent()) {
            Sprint sprint = sprintOptional.get();
            sprint.setStarted(true);
            sprintService.save(sprint);
            return ResponseEntity.ok(sprint);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/end")
    public ResponseEntity<Sprint> endSprint(@PathVariable Long id) {
        Optional<Sprint> sprintOptional = sprintService.findById(id);
        if (sprintOptional.isPresent()) {
            Sprint sprint = sprintOptional.get();
            sprint.setStarted(false);
            sprintService.save(sprint);
            return ResponseEntity.ok(sprint);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping
    public Sprint createSprint(@RequestBody Map<String, Object> sprintData) {
        Long projectId = ((Number) sprintData.get("projectId")).longValue();
        String name = (String) sprintData.get("name");

        // Pobierz inne pola potrzebne do utworzenia sprintu

        // Pobierz ID lidera z danych żądania
        Long leaderId = ((Number) sprintData.get("leaderId")).longValue();

        // Utwórz nowy obiekt Sprint i ustaw jego pola
        Sprint sprint = new Sprint();
        sprint.setName(name);

        // Ustaw inne pola sprintu

        // Pobierz projekt na podstawie projectId
        Project project = projectService.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        sprint.setProject(project);

        // Pobierz lidera na podstawie leaderId
        User leader = userService.findById(leaderId);
        sprint.setUser(leader);

        // Zapisz sprint za pomocą serwisu
        return sprintService.save(sprint);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Sprint> updateSprint(@PathVariable Long id, @RequestBody Sprint sprintDetails) {
        Optional<Sprint> existingSprint = sprintService.findById(id);
        if (existingSprint.isPresent()) {
            Sprint sprint = existingSprint.get();
            sprint.setName(sprintDetails.getName());
            sprint.setType(sprintDetails.getType());
            sprint.setCzasTrwania(sprintDetails.getCzasTrwania());
            sprint.setDataRozpoczecia(sprintDetails.getDataRozpoczecia());
            sprint.setDataZakonczenia(sprintDetails.getDataZakonczenia());
            sprint.setCelSprintu(sprintDetails.getCelSprintu());

            if (sprintDetails.getUser() != null && sprintDetails.getUser().getId() != null) {
                Optional<User> user = userRepository.findById(sprintDetails.getUser().getId());
                user.ifPresent(sprint::setUser);
            }

            final Sprint updatedSprint = sprintService.save(sprint);
            return ResponseEntity.ok(updatedSprint);
        } else {
            return ResponseEntity.notFound().build();
        }
    }




    @DeleteMapping("/{id}")
    public void deleteSprint(@PathVariable Long id) {
        // Usuń powiązane zadania
        issueService.deleteBySprintId(id);
        // Usuń sprint
        sprintService.deleteById(id);


    }

}

