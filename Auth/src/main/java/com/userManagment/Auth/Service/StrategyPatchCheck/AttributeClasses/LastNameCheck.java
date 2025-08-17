package com.userManagment.Auth.Service.StrategyPatchCheck.AttributeClasses;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;
import com.userManagment.Auth.Service.StrategyPatchCheck.CheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LastNameCheck implements CheckStrategy {

    private static final Logger LOGGER = LogManager.getLogger(LastNameCheck.class);

    @Override
    public void check(User editU, PatchUserDTO patchUserDTO) {
        if(patchUserDTO.getLastName() != null){
            String oldLastName = editU.getLastname();
            editU.setLastname(patchUserDTO.getLastName());
            LOGGER.info("LastName updated for user: {} | old LastName {} | new LastName {}", editU.getUsername(),oldLastName,patchUserDTO.getLastName());
        }
    }
}
