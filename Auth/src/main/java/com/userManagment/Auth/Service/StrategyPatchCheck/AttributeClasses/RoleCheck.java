package com.userManagment.Auth.Service.StrategyPatchCheck.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.Role;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Service.StrategyPatchCheck.CheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RoleCheck implements CheckStrategy {

    private static final Logger LOGGER = LogManager.getLogger(RoleCheck.class);

    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {

        String adminKey = patchUserDTO.getKeyForAdmin();
        if(adminKey != null && adminKey.equals("X8JGxLy8")){
            String oldRole = editU.getRole();
            editU.setRole(Role.ADMIN.toString());
            LOGGER.info("Role updated for user: {} | old Role {} | new Role {}", editU.getUsername(),oldRole,editU.getRole());
        }

        else{
            editU.setRole(Role.USER.toString());
            LOGGER.info("Role updated for user: {}", editU.getUsername());
        }
    }
}
