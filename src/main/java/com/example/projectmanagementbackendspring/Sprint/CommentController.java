package com.example.projectmanagementbackendspring.Sprint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private IssueService issueService;

    @GetMapping
    public List<Comment> getAllComments() {
        return commentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable Long id) {
        Optional<Comment> comment = commentService.findById(id);
        return comment.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.save(comment);
    }
    // Endpoint do przypisywania komentarzy do zadań
    @PostMapping("/assign/{issueId}")
    public ResponseEntity<Comment> createCommentForIssue(@PathVariable Long issueId, @RequestBody Comment comment) {
        Optional<Issue> issue = issueService.findById(issueId);
        if (issue.isPresent()) {
            Issue foundIssue = issue.get();

            // Ustawienie identyfikatora zadania w komentarzu
            Set<Issue> issues = new HashSet<>();
            issues.add(foundIssue);
            comment.setIssues(issues);

            Comment savedComment = commentService.save(comment);
            return ResponseEntity.ok(savedComment);
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @GetMapping("/issue/{issueId}")
    public List<Comment> getCommentsForIssue(@PathVariable Long issueId) {
        return commentService.findByIssueId(issueId);
    }

    @PutMapping("/{id}")
    public Comment updateComment(@PathVariable Long id, @RequestBody Comment comment) {
        Optional<Comment> existingComment = commentService.findById(id);
        if (existingComment.isPresent()) {
            comment.setId(id);
            return commentService.save(comment);
        } else {
            throw new RuntimeException("Comment not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.deleteById(id);
    }
}
