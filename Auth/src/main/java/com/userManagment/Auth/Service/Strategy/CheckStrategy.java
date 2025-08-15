package com.userManagment.Auth.Service.Strategy;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;

public interface CheckStrategy {

    void check(User editU, PatchUserDTO patchUserDTO);

}