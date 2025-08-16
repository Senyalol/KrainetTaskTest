package com.userManagment.Auth.Controller;

//import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.DTO.FullUserInfoDTO;
import com.userManagment.Auth.DTO.PatchUserDTO;
//import com.userManagment.Auth.DTO.ShortUserInfoDTO;
import com.userManagment.Auth.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //Посмотреть всех пользователей (Только ADMIN)
    //Адрес - http://localhost:8080/api/auth
    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<FullUserInfoDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    //Удалить пользователя (Только ADMIN)
    //Адрес - http://localhost:8080/api/auth/delete/{id}
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUserForAdmin(id);
    }

    //Редактировать пользователя по id (Только ADMIN)
    //Адрес - http://localhost:8080/api/users/admin/edit/id
    @PatchMapping("/admin/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void editUser(@PathVariable int id, @RequestBody PatchUserDTO user) {
        userService.editUserForAdmin(id,user);
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
        userService.editUserForUser(authHeader,user);
    }

    //Удалить своего пользователя
    //Адрес - http://localhost:8080/api/users/user/delete
    @DeleteMapping("/user/delete")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    public void deleteMyUser(@RequestHeader("Authorization") String authHeader) {
        userService.deleteUserForUser(authHeader);
    }

}
