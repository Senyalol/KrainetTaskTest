package com.userManagment.Auth.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateUserDTO {

    @NotNull
    @JsonProperty("username")
    private String username;

    @NotNull
    @JsonProperty("password")
    private String password;

    @NotNull
    @JsonProperty("email")
    private String email;

    @NotNull
    @JsonProperty("firstName")
    private String firstName;

    @NotNull
    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("key_for_admin")
    private String keyForAdmin;

}
