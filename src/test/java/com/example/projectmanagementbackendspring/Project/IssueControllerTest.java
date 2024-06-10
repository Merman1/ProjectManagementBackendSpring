package com.example.projectmanagementbackendspring.Project;

import com.example.projectmanagementbackendspring.Sprint.Issue;
import com.example.projectmanagementbackendspring.Sprint.IssueController;
import com.example.projectmanagementbackendspring.Sprint.IssueService;
import com.example.projectmanagementbackendspring.Sprint.SprintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(IssueController.class)
@AutoConfigureMockMvc
public class IssueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IssueService issueService;

    @MockBean
    private SprintService sprintService;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetAllIssues() throws Exception {
        // Arrange
        when(issueService.findAll()).thenReturn(Arrays.asList(new Issue(), new Issue()));

        // Act & Assert
        mockMvc.perform(get("/api/auth/issues"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[1]").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetIssueById() throws Exception {
        // Arrange
        Issue issue = new Issue();
        issue.setId(1L);
        when(issueService.findById(1L)).thenReturn(Optional.of(issue));

        // Act & Assert
        mockMvc.perform(get("/api/auth/issues/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateIssue() throws Exception {
        // Arrange
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setNazwa("Test Issue");

        // Mocking service call
        when(issueService.save(any(Issue.class))).thenReturn(issue);

        // Act & Assert
        mockMvc.perform(post("/api/auth/issues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nazwa").value("Test Issue"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testUpdateIssue() throws Exception {
        // Arrange
        Issue issue = new Issue();
        issue.setId(1L);
        issue.setNazwa("Updated Issue");

        // Mocking service call
        when(issueService.findById(1L)).thenReturn(Optional.of(issue));
        when(issueService.save(any(Issue.class))).thenReturn(issue);

        // Act & Assert
        mockMvc.perform(put("/api/auth/issues/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issue)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nazwa").value("Updated Issue"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteIssue() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(delete("/api/auth/issues/1"))
                .andExpect(status().isNoContent());
    }

}
