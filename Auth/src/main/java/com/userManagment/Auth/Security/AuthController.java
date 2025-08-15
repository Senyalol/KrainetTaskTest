package com.userManagment.Auth.Security;

import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.DTO.ShortUserInfoDTO;
import com.userManagment.Auth.Security.SDTO.JwtAuthenticationDTO;
import com.userManagment.Auth.Security.SDTO.RefreshTokenDTO;
import com.userManagment.Auth.Security.SDTO.UserCredentialDTO;
import com.userManagment.Auth.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.AuthenticationException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    //Метод для аутентификации
    //По адресу http://localhost:8080/auth/sign-in
    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationDTO> signIn(@RequestBody UserCredentialDTO userCredentialDTO) {
        try{
            JwtAuthenticationDTO jwtAuthenticationDTO = userService.signIn(userCredentialDTO);
            return ResponseEntity.ok(jwtAuthenticationDTO);
        }
        catch(AuthenticationException e){
            throw new RuntimeException("Authentication failed" + e.getCause());
        }
    }

    //Метод для обновления jwt токена
    //По Адресу http://localhost:8080/auth/refresh
    @PostMapping("/refresh")
    public JwtAuthenticationDTO refresh(@RequestBody RefreshTokenDTO refreshTokenDTO) throws  Exception{
        return userService.refreshToken(refreshTokenDTO);
    }

    //Зарегестрировать пользователя (Для всех)
    //Адрес - http://localhost:8080/api/auth
    @PostMapping
    public ShortUserInfoDTO createUser(@RequestBody CreateUserDTO user) {
        return userService.registerUser(user);
    }
}
