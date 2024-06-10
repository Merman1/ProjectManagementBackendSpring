package com.example.projectmanagementbackendspring.Project;

import com.example.projectmanagementbackendspring.Project.Project;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateProject() throws Exception {
        // Tworzenie nowego projektu
        Project project = new Project();
        project.setName("Test Project");
        project.setKeyProject("TP");
        project.setType("Type");

        // Wykonanie żądania POST do tworzenia projektu
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/auth/projects")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)));

        // Sprawdzenie czy żądanie zostało wykonane poprawnie
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber()) // Oczekuje, że zostanie zwrócone ID projektu
                .andExpect(jsonPath("$.name").value("Test Project")) // Oczekuje, że zostanie zwrócona nazwa projektu
                .andExpect(jsonPath("$.keyProject").value("TP")) // Oczekuje, że zostanie zwrócony klucz projektu
                .andExpect(jsonPath("$.type").value("Type")); // Oczekuje, że zostanie zwrócony typ projektu
    }
}
