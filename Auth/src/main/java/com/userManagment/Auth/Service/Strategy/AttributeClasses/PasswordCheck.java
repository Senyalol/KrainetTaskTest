package com.userManagment.Auth.Service.Strategy.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Service.Strategy.CheckStrategy;

public class PasswordCheck implements CheckStrategy {
    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {
        if(patchUserDTO.getPassword() != null){
            editU.setPassword(patchUserDTO.getPassword());
        }
    }
}
