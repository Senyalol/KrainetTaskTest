package com.userManagment.Auth.Service.StrategyCheck.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Service.StrategyCheck.CheckStrategy;

public class LastNameCheck implements CheckStrategy {
    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {
        if(patchUserDTO.getLastName() != null){
            editU.setLastname(patchUserDTO.getLastName());
        }
    }
}
