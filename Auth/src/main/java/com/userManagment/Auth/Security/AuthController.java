package com.userManagment.Auth.Security;

import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.DTO.ShortUserInfoDTO;
import com.userManagment.Auth.Security.SDTO.JwtAuthenticationDTO;
import com.userManagment.Auth.Security.SDTO.RefreshTokenDTO;
import com.userManagment.Auth.Security.SDTO.UserCredentialDTO;
import com.userManagment.Auth.Service.UserService;
//import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.naming.AuthenticationException;

//@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger LOGGER = LogManager.getLogger(AuthController.class);
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    //Метод для аутентификации
    //По адресу http://localhost:8080/auth/sign-in
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationDTO> signIn(@RequestBody UserCredentialDTO userCredentialDTO) {
        try{
            JwtAuthenticationDTO jwtAuthenticationDTO = userService.signIn(userCredentialDTO);
            LOGGER.info("Authentication successful for user: {}",userCredentialDTO.getUsername());
            return ResponseEntity.ok(jwtAuthenticationDTO);
        }
        catch(AuthenticationException e){
            LOGGER.error("Authentication failed | Error type: {} | Message: {}",
                    e.getClass().getSimpleName(),
                    e.getMessage());

            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentication failed", e);
        }
    }

    //Метод для обновления jwt токена
    //По Адресу http://localhost:8080/auth/refresh
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationDTO> refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) throws  Exception{
        try{
            JwtAuthenticationDTO jwtAuthenticationDTO = userService.refreshToken(refreshTokenDTO);
            LOGGER.info("Token successfully updated");
            return ResponseEntity.ok(jwtAuthenticationDTO);
        }
        catch(AuthenticationException e){
            LOGGER.error("Token update failed");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token update failed", e);
        }

    }

    //Зарегестрировать пользователя (Для всех)
    //Адрес - http://localhost:8080/auth/registration
    @PostMapping("/registration")
    public ResponseEntity<ShortUserInfoDTO> createUser(@RequestBody CreateUserDTO user) {
        try {
            ShortUserInfoDTO shortUserInfoDTO = userService.registerUser(user);
            LOGGER.info("Registration successful for user: {}",user.getUsername());
            return ResponseEntity.ok(shortUserInfoDTO);
        }
       catch (Exception e) {
            LOGGER.error("Registration failed", e);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Registration failed", e);
       }
    }
}
