package com.userManagment.Auth.Controller;

import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.DTO.FullUserInfoDTO;
import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.DTO.ShortUserInfoDTO;
import com.userManagment.Auth.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @GetMapping
    public List<FullUserInfoDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    //Удалить пользователя (Только ADMIN)
    //Адрес - http://localhost:8080/api/auth/delete/{id}
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUserForAdmin(id);
    }

    @PatchMapping("/edit/{id}")
    public void editUser(@PathVariable int id, @RequestBody PatchUserDTO user) {
        userService.editUserForAdmin(id,user);
    }

}
