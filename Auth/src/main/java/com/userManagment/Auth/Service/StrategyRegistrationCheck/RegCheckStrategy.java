package com.userManagment.Auth.Service.StrategyRegistrationCheck;

import com.userManagment.Auth.DTO.CreateUserDTO;

public interface RegCheckStrategy {

    boolean regCheck(CreateUserDTO createUserDTO);

}
