package com.example.projectmanagementbackendspring.Web;


import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.projectmanagementbackendspring.user.*;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "http://localhost:5173", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/users")
    public ResponseEntity<List<UserInfoResponse>> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserInfoResponse> userInfoResponses = users.stream()
                .map(user -> new UserInfoResponse(user.getId(),user.getUsername(), user.getEmail(), user.getRoles(),user.getAdress(),user.getLastName(),user.getFirstName(),user.getPositionName(),user.getLocation(),user.getNumber(),user.getOrganization(),user.getPublicName()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(userInfoResponses);
    }
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok(new MessageResponse("User logged out successfully!"));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }


        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @GetMapping("/user")
    public ResponseEntity<?> getLoggedInUser(Principal principal) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user is logged in."));
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        return ResponseEntity.ok(new UserInfoResponse(user.getId(),user.getUsername(), user.getEmail(), user.getRoles(),user.getAdress(),user.getLastName(),user.getFirstName(),user.getPositionName(),user.getLocation(),user.getNumber(),user.getOrganization(),user.getPublicName()));
    }
    @PutMapping("/user/update")
    public ResponseEntity<?> updateLoggedInUser(Principal principal, @RequestBody UpdateUserRequest updateUserRequest) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user is logged in."));
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        // Aktualizacja tylko tych pól, które zostały przekazane w żądaniu aktualizacji
        if (updateUserRequest.getUsername() != null) {
            user.setUsername(updateUserRequest.getUsername());
        }
        if (updateUserRequest.getEmail() != null) {
            user.setEmail(updateUserRequest.getEmail());
        }
        if (updateUserRequest.getAdress() != null) {
            user.setAdress(updateUserRequest.getAdress());
        }
        if (updateUserRequest.getLastName() != null) {
            user.setLastName(updateUserRequest.getLastName());
        }
        if (updateUserRequest.getFirstName() != null) {
            user.setFirstName(updateUserRequest.getFirstName());
        }
        if (updateUserRequest.getPositionName() != null) {
            user.setPositionName(updateUserRequest.getPositionName());
        }
        if (updateUserRequest.getLocation() != null) {
            user.setLocation(updateUserRequest.getLocation());
        }
        if (updateUserRequest.getNumber() != null) {
            user.setNumber(updateUserRequest.getNumber());
        }
        if (updateUserRequest.getOrganization() != null) {
            user.setOrganization(updateUserRequest.getOrganization());
        }
        if (updateUserRequest.getPublicName() != null) {
            user.setPublicName(updateUserRequest.getPublicName());
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User updated successfully!"));
    }
    @PutMapping("/user/update-password")
    public ResponseEntity<?> updateLoggedInUserPassword(Principal principal, @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        if (principal == null || principal.getName() == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: No user is logged in."));
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Error: User not found."));

        if (!encoder.matches(updatePasswordRequest.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Old password is incorrect."));
        }

        if (!updatePasswordRequest.getNewPassword().equals(updatePasswordRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: New password and confirm password do not match."));
        }

        user.setPassword(encoder.encode(updatePasswordRequest.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Password updated successfully!"));
    }
    @DeleteMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.delete(user);
            return ResponseEntity.ok(new MessageResponse("User " + username + " deleted successfully!"));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
