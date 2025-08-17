package com.userManagment.Auth.Service.StrategyPatchCheck.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Repository.UserRepostiory;
import com.userManagment.Auth.Service.StrategyPatchCheck.CheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailCheck implements CheckStrategy {

    private static final Logger LOGGER = LogManager.getLogger(EmailCheck.class);
    private final UserRepostiory userRepostiory;

    public EmailCheck(UserRepostiory userRepostiory) {
        this.userRepostiory = userRepostiory;
    }

    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {

        if(patchUserDTO.getEmail() != null) {

            if (!userRepostiory.existsByEmail(patchUserDTO.getEmail())) {

                try {
                    String oldEmail = editU.getEmail();
                    editU.setEmail(patchUserDTO.getEmail());
                    LOGGER.info("Email updated for user: {} | old Email {} | new Email {}", patchUserDTO.getUsername(),oldEmail,patchUserDTO.getEmail());
                } catch (DataAccessException e) {
                    LOGGER.error("Database error during email update", e);
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Database error"
                    );
                }

            } else {
                LOGGER.error("Email: {}", patchUserDTO.getEmail() + " is already in use");
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Email '" + patchUserDTO.getEmail() + "' already exists"
                );
            }

        }
    }
}
