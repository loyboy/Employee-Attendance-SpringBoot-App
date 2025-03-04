package com.example.empattendance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.empattendance.model.User;
import com.example.empattendance.repository.UserRepository;
import com.example.empattendance.security.CustomUserDetailsService;
import com.example.empattendance.security.JwtTokenUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication APIs", description = "API Operations related to user authentication")
public class AuthController {

  @Autowired
  private CustomUserDetailsService userDetailsService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  /**
   * Register user API.
   *
   * @param user The user to be registered
   * @return Success message
   */
  @Operation(summary = "Register user", description = "Register a new user")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "User registered successfully"),
          @ApiResponse(responseCode = "409", description = "Username already exists")
      }
  )
  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody User user) {    
    
      Optional<User> isUserExist = userRepository.findByUsername(user.getUsername());
      if (isUserExist.isPresent()) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Username already exists.");
      }
      User createdUser = new User();
      createdUser.setUsername(user.getUsername());
      createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

      userRepository.save(createdUser);
      Authentication authentication = authenticate(user.getUsername(), user.getPassword());
		  SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = JwtTokenUtil.generateToken(authentication);

      Map<String, String> response = new HashMap<>();
      response.put("token", jwt);
      response.put("message", "You have been successfully registered with the username: " + user.getUsername());
      return new ResponseEntity<>(response, HttpStatus.OK);    
  }

  /**
   * Authenticate user API.
   *
   * @param user The user to be authenticated
   * @return JWT token
   * @throws Exception If authentication fails
   */
  @Operation(summary = "Authenticate user", description = "Authenticate a user and generate a JWT token thereafter.")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
          @ApiResponse(responseCode = "401", description = "Invalid username or password"),
          @ApiResponse(responseCode = "500", description = "Unable to authenticate user")
      }
  )
  @PostMapping("/login")
  public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) {
    try {

      Authentication authentication = authenticate(user.getUsername(), user.getPassword());
		  SecurityContextHolder.getContext().setAuthentication(authentication);
      String jwt = JwtTokenUtil.generateToken(authentication);

      Map<String, String> response = new HashMap<>();
      response.put("token", jwt);
      return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (BadCredentialsException e) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Error: Invalid username or password");
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: Unable to authenticate");
    }
  }

  /**
   * Verify if a username exists.
   *
   * @param username The username to verify
   * @return Response message indicating whether the username exists
   */
  @Operation(summary = "Verify username", description = "Verify if a username exists in the system")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Username exists"),
          @ApiResponse(responseCode = "404", description = "Username not found")
      }
  )
  @GetMapping("/verify-username/{username}")
  public ResponseEntity<?> verifyUsername(@PathVariable String username) {
    Optional<User> user = userRepository.findByUsername(username);
    if (user.isPresent()) {
      return ResponseEntity.ok("Username actually exists.");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Username not found");
    }
  }

  /**
   * Reset password for a given username.
   *
   * @param request Map object containing the username and newPassword
   * @return Response message indicating success or failure of the operation
   */
  @Operation(summary = "Reset password", description = "Reset the password for the given username")
  @ApiResponses(
      value = {
          @ApiResponse(responseCode = "200", description = "Password reset successfully"),
          @ApiResponse(responseCode = "404", description = "Username not found"),
          @ApiResponse(responseCode = "500", description = "Unable to reset password")
      })
  @PostMapping("/reset-password")
  public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
    String username = request.get("username");
    String newPassword = request.get("newPassword");

    Optional<User> user = userRepository.findByUsername(username);

    if (user.isPresent()) {
      User existingUser = user.get();
      existingUser.setPassword(passwordEncoder.encode(newPassword));
      userRepository.save(existingUser);
      return ResponseEntity.ok("Password reset successfully");
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Error: Username not found");
    }
  }

  private Authentication authenticate (String username, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);

		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username or password");
		}
		if (!passwordEncoder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
