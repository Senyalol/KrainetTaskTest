package com.userManagment.Auth.Service.StrategyCheck.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.Role;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Service.StrategyCheck.CheckStrategy;

public class RoleCheck implements CheckStrategy {
    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {

        String adminKey = patchUserDTO.getKeyForAdmin();
        if(adminKey != null && adminKey.equals("X8JGxLy8")){
            editU.setRole(Role.ADMIN.toString());
        }

        else{
            editU.setRole(Role.USER.toString());
        }
    }
}
