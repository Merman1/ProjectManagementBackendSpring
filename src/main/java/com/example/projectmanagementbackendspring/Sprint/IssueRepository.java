package com.example.projectmanagementbackendspring.Sprint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    void deleteBySprintId(Long sprintId);
}
