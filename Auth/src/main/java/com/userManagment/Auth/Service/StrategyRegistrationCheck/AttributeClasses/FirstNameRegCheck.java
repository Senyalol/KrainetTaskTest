package com.userManagment.Auth.Service.StrategyRegistrationCheck.AttributeClasses;

import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.Service.StrategyRegistrationCheck.RegCheckStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FirstNameRegCheck implements RegCheckStrategy {

    private static final Logger LOGGER = LogManager.getLogger(FirstNameRegCheck.class);

    @Override
    public boolean regCheck(CreateUserDTO createUserDTO) {

        if(createUserDTO.getFirstName() != null){
            return true;
        }

        LOGGER.error("Attribute firstName = null");
        return false;

    }

}
