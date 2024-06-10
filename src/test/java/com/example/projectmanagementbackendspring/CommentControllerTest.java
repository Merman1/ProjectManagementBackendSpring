package com.example.projectmanagementbackendspring;
import com.example.projectmanagementbackendspring.Sprint.Comment;
import com.example.projectmanagementbackendspring.Sprint.CommentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    public void testGetAllComments() throws Exception {
        // Arrange
        Comment comment1 = new Comment();
        comment1.setOpis("First comment");

        Comment comment2 = new Comment();
        comment2.setOpis("Second comment");

        commentRepository.saveAll(Arrays.asList(comment1, comment2));

        // Act & Assert
        mockMvc.perform(get("http://localhost:8000/api/auth/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void testGetCommentById() throws Exception {
        // Arrange
        Comment comment = new Comment();
        comment.setOpis("Test comment");
        Comment savedComment = commentRepository.save(comment);

        // Act & Assert
        mockMvc.perform(get("http://localhost:8000/api/auth/comments/{id}", savedComment.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedComment.getId()));
    }

    @Test
    public void testCreateComment() throws Exception {
        // Arrange
        Comment comment = new Comment();
        comment.setOpis("New comment");

        // Act & Assert
        mockMvc.perform(post("/api/auth/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("New comment"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testCreateCommentForIssue() throws Exception {
        // Arrange
        Comment comment = new Comment();
        comment.setOpis("New comment");
        comment.setId(52L); // Ustawienie identyfikatora komentarza

        // Act & Assert
        mockMvc.perform(post("http://localhost:8000/api/auth/comments/assign/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("New comment"));
    }

    @Test
    public void testUpdateComment() throws Exception {
        // Arrange
        Comment comment = new Comment();
        comment.setOpis("Test comment");
        Comment savedComment = commentRepository.save(comment);

        savedComment.setOpis("Updated comment");

        // Act & Assert
        mockMvc.perform(put("http://localhost:8000/api/auth/comments/{id}", savedComment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedComment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Updated comment"));
    }

    @Test
    public void testDeleteComment() throws Exception {
        // Arrange
        Comment comment = new Comment();
        comment.setOpis("Test comment");
        Comment savedComment = commentRepository.save(comment);

        // Act & Assert
        mockMvc.perform(delete("http://localhost:8000/api/auth/comments/{id}", savedComment.getId()))
                .andExpect(status().isOk());

        Optional<Comment> deletedComment = commentRepository.findById(savedComment.getId());
        assertThat(deletedComment).isEmpty();
    }
}
