package com.userManagment.Auth.Service.StrategyCheck.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Service.StrategyCheck.CheckStrategy;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordCheck implements CheckStrategy {

    private final PasswordEncoder passwordEncoder;

    public PasswordCheck(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {
        if(patchUserDTO.getPassword() != null){
            editU.setPassword(passwordEncoder.encode(patchUserDTO.getPassword()));
        }
    }
}
