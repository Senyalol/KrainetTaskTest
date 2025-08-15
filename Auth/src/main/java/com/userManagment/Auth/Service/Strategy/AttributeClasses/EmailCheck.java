package com.userManagment.Auth.Service.Strategy.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Service.Strategy.CheckStrategy;

public class EmailCheck implements CheckStrategy {
    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {
        if(patchUserDTO.getEmail() != null){
            editU.setEmail(patchUserDTO.getEmail());
        }
    }
}
