package com.userManagment.Auth.Service.StrategyPatchCheck.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Service.StrategyPatchCheck.CheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FirstNameCheck implements CheckStrategy {

    private static final Logger LOGGER = LogManager.getLogger(FirstNameCheck.class);

    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {
        if(patchUserDTO.getFirstName() != null){
            String oldFirstName = editU.getFirstname();
            editU.setFirstname(patchUserDTO.getFirstName());
            LOGGER.info("FirstName updated for user: {} | old FirstName {} | new FirstName {}", editU.getUsername(),oldFirstName,patchUserDTO.getFirstName());
        }
    }
}
