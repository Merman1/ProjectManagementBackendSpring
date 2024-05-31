package com.example.projectmanagementbackendspring.Sprint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    public Optional<Comment> findById(Long id) {
        return commentRepository.findById(id);
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    // Metoda do pobierania komentarzy dla określonego zadania
    public List<Comment> findByIssueId(Long issueId) {
        return commentRepository.findByIssues_Id(issueId);
    }
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
