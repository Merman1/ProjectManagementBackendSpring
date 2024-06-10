package com.example.projectmanagementbackendspring.Project;

import com.example.projectmanagementbackendspring.Web.*;
import com.example.projectmanagementbackendspring.user.*;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder encoder;

    @MockBean
    private JwtUtils jwtUtils;


    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAuthenticateUser() throws Exception {
        // Arrange
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        // Mocking authentication
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Act & Assert
        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwt").exists())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.roles[0]").value("ROLE_USER"));
    }





    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testRegisterUser() throws Exception {
        // Arrange
        Set<String> roles = new HashSet<>();
        roles.add("admin");

        // Act & Assert
        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"email\":\"test@test.com\",\"password\":\"password\",\"role\":[\"admin\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }


    @Test
    public void testLogout() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User logged out successfully!"));
    }

    @Test
    public void testGetLoggedInUser() throws Exception {
        // Arrange
        UserDetails userDetails = new UserDetailsImpl(1L, "testuser", "testuser@example.com", "testpassword", Collections.emptyList());

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));
        when(authenticationManager.authenticate(any())).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/auth/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"));
    }

    @Test
    public void testUpdateLoggedInUser() throws Exception {
        // Arrange
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setUsername("testuser");
        updateUserRequest.setEmail("testuser@example.com");
        updateUserRequest.setAdress("Test Address");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));
        when(encoder.encode("testpassword")).thenReturn("testencodedpassword");

        // Act & Assert
        mockMvc.perform(put("/api/auth/user/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testuser\",\"email\":\"testuser@example.com\",\"adress\":\"Test Address\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User updated successfully!"));
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    public void testUpdateLoggedInUserPassword() throws Exception {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User("testuser", "test@test.com", "testpassword")));

        // Act & Assert
        mockMvc.perform(put("/api/auth/user/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"oldPassword\":\"oldpassword\",\"newPassword\":\"newpassword\",\"confirmPassword\":\"newpassword\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated successfully!"));
    }


    @Test
    public void testDeleteUser() throws Exception {
        // Arrange
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));

        // Act & Assert
        mockMvc.perform(delete("/api/auth/delete/{username}", "testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User testuser deleted successfully!"));
    }
}


