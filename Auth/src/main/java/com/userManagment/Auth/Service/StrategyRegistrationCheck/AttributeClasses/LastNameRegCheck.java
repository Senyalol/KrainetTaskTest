package com.userManagment.Auth.Service.StrategyRegistrationCheck.AttributeClasses;

import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.Service.StrategyRegistrationCheck.RegCheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LastNameRegCheck implements RegCheckStrategy {

    private static final Logger LOGGER = LogManager.getLogger(LastNameRegCheck.class);

    @Override
    public boolean regCheck(CreateUserDTO createUserDTO) {

        if(createUserDTO.getLastName() != null){
            return true;
        }

        LOGGER.error("Attribute lastName = null");
        return false;

    }
}
