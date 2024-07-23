package com.ebirdspace.urlshortenerservice.controller;

import com.ebirdspace.urlshortenerservice.dto.LoginResponse;
import com.ebirdspace.urlshortenerservice.dto.LoginUserDTO;
import com.ebirdspace.urlshortenerservice.dto.RegisterUserDTO;
import com.ebirdspace.urlshortenerservice.model.User;
import com.ebirdspace.urlshortenerservice.repository.UserRepository;
import com.ebirdspace.urlshortenerservice.service.AuthenticationService;
import com.ebirdspace.urlshortenerservice.service.JwtService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

  private final JwtService jwtService;
  private final AuthenticationService authenticationService;
  private final UserRepository userRepository;

  @PostMapping("/signup")
  public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerUserDto) {
    User registeredUser = authenticationService.signup(registerUserDto);

    return ResponseEntity.ok(registeredUser);
  }

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDTO loginUserDto) {
    User authenticatedUser = authenticationService.authenticate(loginUserDto);

    String jwtToken = jwtService.generateToken(authenticatedUser);

    LoginResponse loginResponse = new LoginResponse(jwtToken, jwtService.getExpirationTime());

    return ResponseEntity.ok(loginResponse);
  }

  @PostMapping("/secure/{userId}")
  public ResponseEntity<User> getUser(@PathVariable Integer userId) {
    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      return ResponseEntity.ok(user.get());
    }else {
      return ResponseEntity.notFound().build();
    }
  }
}
