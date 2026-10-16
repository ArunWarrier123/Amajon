package com.warrier.amajon.controllers;


import com.warrier.amajon.models.AppRole;
import com.warrier.amajon.models.Role;
import com.warrier.amajon.models.User;
import com.warrier.amajon.payload.LoginRequest;
import com.warrier.amajon.payload.LoginResponse;
import com.warrier.amajon.payload.SignUpRequest;
import com.warrier.amajon.repositories.RoleRepository;
import com.warrier.amajon.repositories.UserRepository;
import com.warrier.amajon.security.JwtUtils;
import com.warrier.amajon.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication;
        LoginResponse loginResponse = null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            System.out.println(userDetails.getUsername());
            String jwtToken = jwtUtils.generateTokenFromUsername(userDetails);

            List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            loginResponse = new LoginResponse(jwtToken, userDetails.getUsername(), roles);
        } catch (Exception e) {
            System.out.println(e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", false);
            return  new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(loginResponse);

    }



    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest){
        if( userRepository.existsByUserName(signUpRequest.getUsername())){
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        if( userRepository.existsByEmail(signUpRequest.getUsername())){
            return ResponseEntity.badRequest().body("Username is already in use");
        }
        System.out.println(signUpRequest);
        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );

        Set<String> payloadRoles = signUpRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        if(payloadRoles == null){
            Role role = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> new RuntimeException("Role not found"));
            roles.add(role);
        }
        else{
            payloadRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(adminRole);
                        break;
                    case "seller":
                        Role sellerRole = roleRepository.findByRoleName(AppRole.ROLE_SELLER).orElseThrow(() -> new RuntimeException("Role not found"));
                        roles.add(sellerRole);
                         break;
                        default:
                            Role userRole = roleRepository.findByRoleName(AppRole.ROLE_USER).orElseThrow(() -> new RuntimeException("Role not found"));
                            roles.add(userRole);
                }
            });

        }
        user.setRoles(roles);
        userRepository.save(user);
        return ResponseEntity.ok(user);

    }
}
