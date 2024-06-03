package com.example.projectmanagementbackendspring.Sprint;

import com.example.projectmanagementbackendspring.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)


@RestController
@RequestMapping("/api/auth/issues")
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

    @Autowired
    private IssueService issueService;
@Autowired
private SprintService sprintService;
    @GetMapping
    public List<Issue> getAllIssues() {
        logger.info("Fetching all issues");
        return issueService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Issue> getIssueById(@PathVariable Long id) {
        logger.info("Fetching issue with id: " + id);
        Optional<Issue> issue = issueService.findById(id);
        return issue.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Issue> createIssue(@RequestBody Issue issue) {
        try {
            logger.info("Creating issue with name: " + issue.getNazwa());

            // Dodaj przypisanie sprintId do nowego zadania
            Long sprintId = issue.getSprint().getId();
            if (sprintId != null) {
                // Dodaj przypisanego użytkownika (lub użytkowników) do zbioru użytkowników nowego zadania
                if (issue.getUsers() != null && !issue.getUsers().isEmpty()) {
                    for (User user : issue.getUsers()) {
                        issue.getUsers().add(user);
                    }
                }

                // Zapisz nowe zadanie
                Issue createdIssue = issueService.save(issue);

                return ResponseEntity.ok(createdIssue);
            } else {
                return ResponseEntity.badRequest().build(); // Jeśli sprintId nie został przekazany

            }
        } catch (Exception e) {
            logger.error("Error creating issue", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/CreateBacklog")
    public ResponseEntity<Issue> createIssueBacklog(@RequestBody Issue issue) {
        try {
            logger.info("Creating issue with name: " + issue.getNazwa());


                // Dodaj przypisanego użytkownika (lub użytkowników) do zbioru użytkowników nowego zadania
                if (issue.getUsers() != null && !issue.getUsers().isEmpty()) {
                    for (User user : issue.getUsers()) {
                        issue.getUsers().add(user);
                    }


                // Zapisz nowe zadanie
                Issue createdIssue = issueService.save(issue);

                return ResponseEntity.ok(createdIssue);
            } else {
                return ResponseEntity.badRequest().build(); // Jeśli sprintId nie został przekazany

            }
        } catch (Exception e) {
            logger.error("Error creating issue", e);
            return ResponseEntity.badRequest().build();
        }
    }




    @PutMapping("/{id}")
    public ResponseEntity<Issue> updateIssue(@PathVariable Long id, @RequestBody Issue issue) {
        logger.info("Updating issue with id: " + id);
        Optional<Issue> existingIssue = issueService.findById(id);
        if (existingIssue.isPresent()) {
            issue.setId(id);

            // Check if the sprint is provided and set it accordingly
            if (issue.getSprint() != null) {
                Optional<Sprint> sprint = sprintService.findById(issue.getSprint().getId());
                if (sprint.isPresent()) {
                    issue.setSprint(sprint.get());
                } else {
                    issue.setSprint(null);
                }
            }

            Issue updatedIssue = issueService.save(issue);
            return ResponseEntity.ok(updatedIssue);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIssue(@PathVariable Long id) {
        logger.info("Deleting issue with id: " + id);
        issueService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


