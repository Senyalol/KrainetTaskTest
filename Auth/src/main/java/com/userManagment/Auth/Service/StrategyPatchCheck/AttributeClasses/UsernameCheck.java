package com.userManagment.Auth.Service.StrategyPatchCheck.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Repository.UserRepostiory;
import com.userManagment.Auth.Service.StrategyPatchCheck.CheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UsernameCheck implements CheckStrategy {

    private static final Logger LOGGER = LogManager.getLogger(UsernameCheck.class);
    private final UserRepostiory userRepostiory;

    public UsernameCheck(UserRepostiory userRepostiory) {
        this.userRepostiory = userRepostiory;
    }

    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {

        if(patchUserDTO.getUsername() != null) {

            if (!userRepostiory.existsByUsername(patchUserDTO.getUsername())) {

                try {
                    String oldUsername = editU.getUsername();
                    editU.setUsername(patchUserDTO.getUsername());
                    LOGGER.info("Username updated for user: {} | new Username: {}", oldUsername,patchUserDTO.getUsername());
                }
                catch (DataAccessException e) {
                    LOGGER.error("Database error during username update", e);
                    throw new ResponseStatusException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            "Database error"
                    );
                }

            } else {
                LOGGER.error("Username: {}", patchUserDTO.getUsername() + " is already in use");
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Username '" + patchUserDTO.getUsername() + "' already exists"
                );
            }

        }

    }

}
