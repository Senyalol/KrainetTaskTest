package com.userManagment.Auth.Controller;

//import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.DTO.FullUserInfoDTO;
import com.userManagment.Auth.DTO.PatchUserDTO;
//import com.userManagment.Auth.DTO.ShortUserInfoDTO;
import com.userManagment.Auth.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Managment API", description = "API для управления пользователями")
@SecurityRequirement(name = "bearerAuth")
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
    @Operation(
            summary = "Получение всех пользователей",
            description = "Возвращает список всех пользователей системы. Требует роли ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен - требуется роль ADMIN"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public List<FullUserInfoDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    //Удалить пользователя (Только ADMIN)
    //Адрес - http://localhost:8080/api/users/admin/delete/{id}
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Удаление пользователя по ID",
            description = "Удаляет пользователя по указанному идентификатору. Требует роли ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен - требуется роль ADMIN"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
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
    @Operation(
            summary = "Редактирование пользователя по ID",
            description = "Обновляет данные пользователя по указанному идентификатору. Требует роли ADMIN."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Данные пользователя успешно обновлены"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для обновления"),
            @ApiResponse(responseCode = "403", description = "Доступ запрещен - требуется роль ADMIN"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
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
    @Operation(
            summary = "Получение информации о текущем пользователе",
            description = "Возвращает полную информацию об аутентифицированном пользователе."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о пользователе успешно получена"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    public FullUserInfoDTO getMyUserInfo(@RequestHeader("Authorization") String authHeader){
        return userService.getUserInfo(authHeader);
    }

    //Редактировать свои данные (Для всех)
    //Адрес - http://localhost:8080/api/users/user/edit
    @PatchMapping("/user/edit")
    @PreAuthorize("hasAuthority('USER') || hasAuthority('ADMIN')")
    @Operation(
            summary = "Редактирование профиля текущего пользователя",
            description = "Обновляет данные аутентифицированного пользователя."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Профиль успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверные данные для обновления"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
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
    @Operation(
            summary = "Удаление аккаунта текущего пользователя",
            description = "Удаляет профиль аутентифицированного пользователя."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Аккаунт успешно удален"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный доступ"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
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
