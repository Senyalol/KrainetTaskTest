package com.userManagment.Auth.Service.StrategyPatchCheck;

import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.Entity.User;

import java.util.List;

public class Cheacker {

    private List<CheckStrategy> strategies;

    public Cheacker(List<CheckStrategy> strategies) {
        this.strategies = strategies;
    }

    public void SetStrategies (List<CheckStrategy> newStrategies) {
        this.strategies = newStrategies;
    }

    public void cheack(User user, PatchUserDTO editUserDTO) {

        for(CheckStrategy strategy : strategies){
            strategy.check(user, editUserDTO);
        }

    }

}
