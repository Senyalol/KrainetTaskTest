package com.userManagment.Auth.Service.StrategyRegistrationCheck;

import com.userManagment.Auth.DTO.CreateUserDTO;

import java.util.List;

public class RegCheacker {

    private List<RegCheckStrategy> regCheckStrategies;

    //Коснтруктор класса
    public RegCheacker(List<RegCheckStrategy> regCheckStrategies) {
        this.regCheckStrategies = regCheckStrategies;
    }

    public void setCheackers(List<RegCheckStrategy> checkStrategies){
        this.regCheckStrategies = checkStrategies;
    }

    public boolean checkRegCheacker(CreateUserDTO createUserDTO){

        for(RegCheckStrategy regCheckStrategy : regCheckStrategies){

            if(!regCheckStrategy.regCheck(createUserDTO)){
                return false;
            }

        }
        return true;
    }

}
