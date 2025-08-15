package com.userManagment.Auth.Service;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.DTO.FullUserInfoDTO;
import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.DTO.ShortUserInfoDTO;
import com.userManagment.Auth.Entity.Role;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Repository.UserRepostiory;
import com.userManagment.Auth.Service.Strategy.AttributeClasses.*;
import com.userManagment.Auth.Service.Strategy.Cheacker;
import com.userManagment.Auth.Service.Strategy.CheckStrategy;
import com.userManagment.Auth.mapping.UserMapping;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@JsonSerialize
public class UserService {

    private final UserRepostiory userRepostiory;
    private final UserMapping userMapping;

    @Autowired
    public UserService(UserRepostiory userRepostiory, UserMapping userMapping) {
        this.userRepostiory = userRepostiory;
        this.userMapping = userMapping;
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
          new UsernameCheck(), new PasswordCheck(), new EmailCheck(),
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

    

}
