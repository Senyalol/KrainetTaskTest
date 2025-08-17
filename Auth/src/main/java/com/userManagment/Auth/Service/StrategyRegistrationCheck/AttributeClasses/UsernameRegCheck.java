package com.userManagment.Auth.Service.StrategyRegistrationCheck.AttributeClasses;

import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.Repository.UserRepostiory;
import com.userManagment.Auth.Service.StrategyRegistrationCheck.RegCheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameRegCheck implements RegCheckStrategy {

    private static final Logger LOGGER = LogManager.getLogger(UsernameRegCheck.class);
    private final UserRepostiory userRepostiory;

    public UsernameRegCheck(UserRepostiory userRepostiory) {
        this.userRepostiory = userRepostiory;
    }

    @Override
    public boolean regCheck(CreateUserDTO createUserDTO) {

        if(createUserDTO.getUsername() != null) {

            if(!userRepostiory.existsByUsername(createUserDTO.getUsername())) {
                return true;
            }

            else{
                LOGGER.error("Username: {} already exists", createUserDTO.getUsername());
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Username " + createUserDTO.getUsername() + " already exists"
                );
            }

        }

        LOGGER.error("Attribute username = null");
        return false;
    }
}
