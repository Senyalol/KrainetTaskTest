package com.userManagment.Auth.Service.StrategyRegistrationCheck.AttributeClasses;

import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.Repository.UserRepostiory;
import com.userManagment.Auth.Service.StrategyRegistrationCheck.RegCheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailRegCheck implements RegCheckStrategy {

    private static final Logger LOGGER = LogManager.getLogger(EmailRegCheck.class);
    private final UserRepostiory userRepostiory;

    public EmailRegCheck(UserRepostiory userRepostiory) {
        this.userRepostiory = userRepostiory;
    }

    @Override
    public boolean regCheck(CreateUserDTO createUserDTO) {

        if(createUserDTO.getEmail() != null){

            if(!userRepostiory.existsByEmail(createUserDTO.getEmail())){
                return true;
            }

            else{
                LOGGER.error("Email: {} already exists", createUserDTO.getEmail());
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Email " + createUserDTO.getEmail() + " already exists"
                );
            }

        }

        LOGGER.error("Attribute email = null");
        return false;
    }
}
