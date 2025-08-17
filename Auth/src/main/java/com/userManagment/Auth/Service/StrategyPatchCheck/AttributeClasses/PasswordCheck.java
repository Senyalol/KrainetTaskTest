package com.userManagment.Auth.Service.StrategyPatchCheck.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Service.StrategyPatchCheck.CheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordCheck implements CheckStrategy {

    private final PasswordEncoder passwordEncoder;
    private static final Logger LOGGER = LogManager.getLogger(PasswordCheck.class);

    public PasswordCheck(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {
        if(patchUserDTO.getPassword() != null){
            editU.setPassword(passwordEncoder.encode(patchUserDTO.getPassword()));
            LOGGER.info("Password updated for user: {}", editU.getUsername());
        }
    }
}
