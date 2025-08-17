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
import com.userManagment.Auth.Service.StrategyPatchCheck.AttributeClasses.*;
import com.userManagment.Auth.Service.StrategyPatchCheck.Cheacker;
import com.userManagment.Auth.Service.StrategyPatchCheck.CheckStrategy;
import com.userManagment.Auth.Service.StrategyRegistrationCheck.AttributeClasses.*;
import com.userManagment.Auth.Service.StrategyRegistrationCheck.RegCheacker;
import com.userManagment.Auth.Service.StrategyRegistrationCheck.RegCheckStrategy;
import com.userManagment.Auth.mapping.UserMapping;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mapping.MappingException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
//import org.springframework.web.bind.annotation.RequestHeader;

import javax.naming.AuthenticationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@JsonSerialize
public class UserService {

    private static final Logger LOGGER = LogManager.getLogger(UserService.class);
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
            LOGGER.error("Attempt to register with null user DTO");
            throw new IllegalArgumentException("User data cannot be null");
        }

        //Ряд необходимых проверок для создания пользователя
        List<RegCheckStrategy>  regCheckStrategies = Arrays.asList(
                new UsernameRegCheck(userRepostiory), new PasswordRegCheck(), new EmailRegCheck(userRepostiory),
                new FirstNameRegCheck(), new LastNameRegCheck()
        );
        RegCheacker regCheacker = new RegCheacker(regCheckStrategies);

        if(regCheacker.checkRegCheacker(createUserDTO)) {

            try {

                User newUser = userMapping.userCreateDTOToUser(createUserDTO);

                //Если специальный ключ введен правильно - регистрируем пользователя как админа
                //В противном случае он по дефолту будет USER
                String adminKey = createUserDTO.getKeyForAdmin();
                if (adminKey != null && adminKey.equals("X8JGxLy8")) {
                    newUser.setRole(Role.ADMIN.toString());
                } else {
                    newUser.setRole(Role.USER.toString());
                }

                userRepostiory.save(newUser);
                LOGGER.info("User registered successfully: {} ", newUser.getUsername());
                return userMapping.userToShortUserInfoDTO(newUser);
            } catch (DataAccessException e) {
                LOGGER.error("Database error during registration: {}", e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Registration failed"
                );
            } catch (MappingException e) {
                LOGGER.error("Mapping error: {}", e.getMessage());
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Invalid user data"
                );
            }

        }

        return null;
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
          new UsernameCheck(userRepostiory), new PasswordCheck(passwordEncoder), new EmailCheck(userRepostiory),
                new FirstNameCheck(), new LastNameCheck(), new RoleCheck()
        );

        Cheacker cheacker = new Cheacker(cheackers);
        cheacker.cheack(editableUser, patchUserDTO);


        userRepostiory.save(editableUser);

    }

    //Метод для удаления пользователя по id (Только для Admin)
    public void deleteUserForAdmin(int id) {
        String username = userRepostiory.findById(id).getUsername();
        userRepostiory.deleteById(id);
        LOGGER.info("User: {} has been deleted",username);
    }

    //Метод для получения информации о себе для пользователя (Для всех)
    public FullUserInfoDTO getUserInfo(String authHeader){
       String username = extractUsernameFromJwt(authHeader);

        User user = userRepostiory.findByUsername(username)
               .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        FullUserInfoDTO infoUserDTO = userMapping.userToFullUserInfoDTO(user);

        return infoUserDTO;
    }

    //Метод для редактирования информации о себе для пользователя (Для всех)
    public void editUserForUser(String authHeader,PatchUserDTO patchUserDTO) {

        String username = extractUsernameFromJwt(authHeader);
        User editUser = userRepostiory.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        //Есди пользователя не существует
        if(editUser == null) {
            System.out.println("User with username " + username + " not found");
        }

        //Если данных для изменений нет
        if(patchUserDTO == null) {
            System.out.println("No data to edit user");
        }

        //Необходимые проверки
        List<CheckStrategy> checkStrategies = Arrays.asList(
                new UsernameCheck(userRepostiory), new PasswordCheck(passwordEncoder), new EmailCheck(userRepostiory),
                    new FirstNameCheck(), new LastNameCheck(), new RoleCheck()
        );

        Cheacker cheacker = new Cheacker(checkStrategies);
        cheacker.cheack(editUser, patchUserDTO);

        userRepostiory.save(editUser);

    }

    //Метод для удаления своего юзера
    public void deleteUserForUser(String authHeader) {
        String username = extractUsernameFromJwt(authHeader);
        User user = userRepostiory.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        userRepostiory.deleteById(user.getId());

    }

    //Метод позволяющий получить username из jwt токена
    private String extractUsernameFromJwt(String authHeader){

        if(authHeader == null){
            throw new IllegalArgumentException("Authorization header is null");
        }

        if(!authHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is incorrect");
        }

        String jwtToken = authHeader.substring(7);
        String username = jwtService.getUsernameFromToken(jwtToken);

        return username;
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
