package com.userManagment.Auth.Security;

import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Repository.UserRepostiory;
import com.userManagment.Auth.Security.SDTO.JwtAuthenticationDTO;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Service
public class JWTService {

    private static final Logger LOGGER = LogManager.getLogger(JWTService.class);

    private final UserRepostiory userRepostiory;

    @Autowired
    public JWTService(UserRepostiory userRepostiory) {
        this.userRepostiory = userRepostiory;
    }

    @Value("YNExgYL2JM7tf5Cb5tU65YkLmjV0E696jOCCapDgYVkx")
    private String jwtsecret;

    //Получаем token и refreshToken для конкретного пользователя
    public JwtAuthenticationDTO generateJwtAuthToken(String username){

        String email = userRepostiory.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"))
                .getEmail();

        String role = userRepostiory.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"))
                .getRole();

        JwtAuthenticationDTO jwtAuthenticationDTO = new JwtAuthenticationDTO();
        jwtAuthenticationDTO.setToken(generateJwtToken(username, email, role));
        jwtAuthenticationDTO.setRefreshToken(generateJwtToken(username, email, role));
        return jwtAuthenticationDTO;


    }

    //Генерируем JWT token
    private String generateJwtToken(String username, String email, String role){

        //Устанавливаем время жизни токена
        Date tokenLiveTime = Date.from(LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(username)
                .claim("email",email)
                .claim("role",role)
                .setExpiration(tokenLiveTime)
                .signWith(getSignInKey())
                .compact();

    }

    //Создать подпись
    private SecretKey getSignInKey(){
        byte[] KeyBytes = Decoders.BASE64.decode(jwtsecret);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

    //Валидация jwt токена
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().verifyWith(getSignInKey()).build().parseClaimsJws(token).getPayload();
            return true;
        }
        catch (ExpiredJwtException  | UnsupportedJwtException | MalformedJwtException
        | SecurityException e){
            LOGGER.error("Jwt validation failed: {}",e.getMessage(),e);
        }
        catch(Exception e){
            LOGGER.error("Unexpected error during JWT validation ",e.getMessage(),e);
        }
        return false;
    }

    //Получить username из jwt токена
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(getSignInKey()).build().parseSignedClaims(token).getPayload();
        return claims.getSubject();
    }

    //
    public JwtAuthenticationDTO refreshBaseToken(String username, String refreshToken) {

        User user = userRepostiory.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        String role = user.getRole();

        JwtAuthenticationDTO jwtDTO = new JwtAuthenticationDTO();
        jwtDTO.setToken(generateJwtToken(username, user.getEmail(), role));
        jwtDTO.setRefreshToken(refreshToken);
        return jwtDTO;

    }

}
