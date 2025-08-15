package com.userManagment.Auth.Service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.DTO.FullUserInfoDTO;
import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.DTO.ShortUserInfoDTO;
import com.userManagment.Auth.Entity.Role;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Repository.UserRepostiory;
import com.userManagment.Auth.Security.JWTService;
import com.userManagment.Auth.Security.SDTO.JwtAuthenticationDTO;
import com.userManagment.Auth.Security.SDTO.RefreshTokenDTO;
import com.userManagment.Auth.Security.SDTO.UserCredentialDTO;
import com.userManagment.Auth.Service.StrategyCheck.AttributeClasses.*;
import com.userManagment.Auth.Service.StrategyCheck.Cheacker;
import com.userManagment.Auth.Service.StrategyCheck.CheckStrategy;
import com.userManagment.Auth.mapping.UserMapping;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@JsonSerialize
public class UserService {

    private final UserRepostiory userRepostiory;
    private final UserMapping userMapping;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;

    //Конструктор класса
    @Autowired
    public UserService(UserRepostiory userRepostiory,
                       UserMapping userMapping,
                       PasswordEncoder passwordEncoder,
                       JWTService jwtService) {

        this.userRepostiory = userRepostiory;
        this.userMapping = userMapping;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    //Метод для регистрации пользователя (Для всех)
    public ShortUserInfoDTO registerUser(CreateUserDTO createUserDTO) {

        //Если пользователь для регистрации null
        if(createUserDTO == null) {
            System.out.println("createUserDTO is null");
            throw new IllegalArgumentException("User data cannot be null");
        }

        User newUser = userMapping.userCreateDTOToUser(createUserDTO);

        //Если специальный ключ введен правильно - регистрируем пользователя как админа
        //В противном случае он по дефолту будет USER
        String adminKey = createUserDTO.getKeyForAdmin();
        if (adminKey != null && adminKey.equals("X8JGxLy8")) {
            newUser.setRole(Role.ADMIN.toString());
        }
        else {
            newUser.setRole(Role.USER.toString());
        }

        userRepostiory.save(newUser);
        System.out.println("User - " + newUser.getUsername() + " has been registered");
        return userMapping.userToShortUserInfoDTO(newUser);
    }

    //Метод для просмотра всех пользователей (Только ADMIN)
    public List<FullUserInfoDTO> getAllUsers() {
        return userRepostiory.findAll()
                .stream()
                .map(userMapping::userToFullUserInfoDTO)
                .collect(Collectors.toList());
    }

    //Метод для редактирования пользователя по id (Только для Admin)
    public void editUserForAdmin(int id, PatchUserDTO patchUserDTO) {

        User editableUser = userRepostiory.findById(id);

        //Есди пользователя не существует
        if(editableUser == null) {
            System.out.println("User with id " + id + " not found");
        }

        //Если данных для изменений нет
        if(patchUserDTO == null) {
            System.out.println("No data to edit user");
        }


        List<CheckStrategy> cheackers = Arrays.asList(
          new UsernameCheck(), new PasswordCheck(passwordEncoder), new EmailCheck(),
                new FirstNameCheck(), new LastNameCheck(), new RoleCheck()
        );

        Cheacker cheacker = new Cheacker(cheackers);
        cheacker.cheack(editableUser, patchUserDTO);


        userRepostiory.save(editableUser);

    }

    //Метод для удаления пользователя по id (Только для Admin)
    public void deleteUserForAdmin(int id) {
        userRepostiory.deleteById(id);
    }

    //Метод для Авторизации и аутентификации
    public JwtAuthenticationDTO signIn(UserCredentialDTO userCredentialDTO) throws AuthenticationException {
        User user = findByCredentials(userCredentialDTO);
        return jwtService.generateJwtAuthToken(user.getUsername());
    }

    //Метод для нахождения пользователя прошедшего аутентификацию
    private User findByCredentials(UserCredentialDTO userCredentialDTO) throws AuthenticationException {
        Optional<User> user = userRepostiory.findByUsername(userCredentialDTO.getUsername());
        if(user.isPresent()) {
            User certainUser = user.get();
            if(passwordEncoder.matches(userCredentialDTO.getPassword(), certainUser.getPassword())) {
                return certainUser;
            }
        }
        throw new AuthenticationException("Username or password is incorrect");
    }

    //Метод для обновления jwt токена
    public JwtAuthenticationDTO refreshToken(RefreshTokenDTO refreshTokenDTO) throws AuthenticationException {
        String refreshToken = refreshTokenDTO.getRefreshToken();
        if(refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            try{
                User user = findByUsername(jwtService.getUsernameFromToken(refreshToken));
                return jwtService.refreshBaseToken(user.getUsername(), refreshToken);
            }

            catch(Exception e){
                throw new AuthenticationException("User not found " + e.getMessage());
            }
        }
        throw new AuthenticationException("Invalid refresh token");
    }

    //Найти пользователя по Username
    private User findByUsername(String username) throws  Exception {
        return userRepostiory.findByUsername(username)
                .orElseThrow(() -> new Exception(String.format("User with email  %s not found", username)));
    }

}
