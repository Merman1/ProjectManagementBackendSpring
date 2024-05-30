package com.example.projectmanagementbackendspring.Sprint;

import com.example.projectmanagementbackendspring.user.User;
import com.example.projectmanagementbackendspring.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class IssueService {

    private static final Logger logger = LoggerFactory.getLogger(IssueService.class);

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private UserService userService;

    public List<Issue> findAll() {
        return issueRepository.findAll();
    }

    public Optional<Issue> findById(Long id) {
        return issueRepository.findById(id);
    }

    public Issue save(Issue issue) {
        logger.info("Saving issue with name: " + issue.getNazwa());

        // Przypisanie twórcy
        if (issue.getCreator() != null && issue.getCreator().getId() != null) {
            logger.info("Finding creator with id: " + issue.getCreator().getId());
            User creator = userService.findById(issue.getCreator().getId());
            issue.setCreator(creator);
        }

        // Przypisanie użytkowników
        if (issue.getUsers() != null && !issue.getUsers().isEmpty()) {
            Set<User> users = new HashSet<>();
            for (User user : issue.getUsers()) {
                logger.info("Finding user with id: " + user.getId());
                User existingUser = userService.findById(user.getId());
                users.add(existingUser);
            }
            issue.setUsers(users);
        }

        Issue savedIssue = issueRepository.save(issue);
        logger.info("Issue saved with id: " + savedIssue.getId());
        return savedIssue;
    }

    public void deleteById(Long id) {
        logger.info("Deleting issue with id: " + id);
        issueRepository.deleteById(id);
    }
    @Transactional
    public void deleteBySprintId(Long sprintId) {
        logger.info("Deleting issues by sprintId: " + sprintId);
        issueRepository.deleteBySprintId(sprintId);
        logger.info("Issues deleted for sprintId: " + sprintId);
    }
}

