package com.userManagment.Auth.Controller;

//import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.DTO.FullUserInfoDTO;
import com.userManagment.Auth.DTO.PatchUserDTO;
//import com.userManagment.Auth.DTO.ShortUserInfoDTO;
import com.userManagment.Auth.Service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private static final Logger LOGGER = LogManager.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Посмотреть всех пользователей (Только ADMIN)
    //Адрес - http://localhost:8080/api/admin
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<FullUserInfoDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    //Удалить пользователя (Только ADMIN)
    //Адрес - http://localhost:8080/api/users/admin/delete/{id}
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable int id) {
        try{
            userService.deleteUserForAdmin(id);
            LOGGER.info("User {} successfully deleted.", id);
        }
        catch(Exception e){
            LOGGER.error("Failed to delete user {}. By address http://localhost:8080/api/users/admin/delete/{id}", id, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }
    }

    //Редактировать пользователя по id (Только ADMIN)
    //Адрес - http://localhost:8080/api/users/admin/edit/{id}
    @PatchMapping("/admin/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void editUser(@PathVariable int id, @RequestBody PatchUserDTO user) {

        try{
            userService.editUserForAdmin(id,user);
            LOGGER.info("User {} successfully edited.", id);
        }
        catch (Exception e) {
            LOGGER.error("Failed to edit user {} by address http://localhost:8080/api/users/admin/edit/{id}, error: {}", id, e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to edit user");
        }

    }

    //Посмотреть свои данные (Для всех)
    //Адрес - http://localhost:8080/api/users/user
    @GetMapping("/user")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public FullUserInfoDTO getMyUserInfo(@RequestHeader("Authorization") String authHeader){
        return userService.getUserInfo(authHeader);
    }

    //Редактировать свои данные (Для всех)
    //Адрес - http://localhost:8080/api/users/user/edit
    @PatchMapping("/user/edit")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public void editMyUser(@RequestHeader("Authorization") String authHeader, @RequestBody PatchUserDTO user) {

        try{
            userService.editUserForUser(authHeader,user);
            LOGGER.info("User successfully edited.");
        }
        catch (Exception e) {
            LOGGER.error("Failed to edit user {} By address: http://localhost:8080/api/users/user/edit , error: {}", user.getUsername(), e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to edit user");
        }
    }

    //Удалить своего пользователя
    //Адрес - http://localhost:8080/api/users/user/delete
    @DeleteMapping("/user/delete")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public void deleteMyUser(@RequestHeader("Authorization") String authHeader) {
        try{
            LOGGER.info("User successfully deleted.");
            userService.deleteUserForUser(authHeader);
        }
        catch (Exception e) {
            LOGGER.error("Failed to delete user. By address http://localhost:8080/api/users/user/delete", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found.");
        }

    }

}
