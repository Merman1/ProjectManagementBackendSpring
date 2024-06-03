package com.example.projectmanagementbackendspring.Sprint;

import com.example.projectmanagementbackendspring.user.User;
import com.example.projectmanagementbackendspring.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SprintService {

    @Autowired
    private SprintRepository sprintRepository;

    public List<Sprint> findAll() {
        return sprintRepository.findAll();
    }

    @Autowired
    private UserRepository userRepository;
    
    public Optional<Sprint> findById(Long id) {
        return sprintRepository.findById(id);
    }
    public List<Sprint> findByProjectId(Long projectId) {
        return sprintRepository.findByProjectId(projectId);
    }

    public Sprint save(Sprint sprint) {
        return sprintRepository.save(sprint);
    }

    public void deleteById(Long id) {
        sprintRepository.deleteById(id);
    }
}
