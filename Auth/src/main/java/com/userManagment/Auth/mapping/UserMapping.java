package com.userManagment.Auth.mapping;

import com.userManagment.Auth.DTO.CreateUserDTO;
import com.userManagment.Auth.DTO.FullUserInfoDTO;
import com.userManagment.Auth.DTO.PatchUserDTO;
import com.userManagment.Auth.DTO.ShortUserInfoDTO;
import com.userManagment.Auth.Entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapping {


        default FullUserInfoDTO userToFullUserInfoDTO(User user) {
            if ( user == null ) {
                return null;
            }

            FullUserInfoDTO fullUserInfoDTO = new FullUserInfoDTO();

            fullUserInfoDTO.setUser_id( user.getId() );
            fullUserInfoDTO.setUsername( user.getUsername() );
            fullUserInfoDTO.setPassword( user.getPassword() );
            fullUserInfoDTO.setEmail( user.getEmail() );
            fullUserInfoDTO.setFirstName(user.getFirstname());
            fullUserInfoDTO.setLastName( user.getLastname() );
            fullUserInfoDTO.setRole( user.getRole() );

            return fullUserInfoDTO;
        }

        default CreateUserDTO userToCreateUserDTO(User user) {
            if ( user == null ) {
                return null;
            }

            CreateUserDTO createUserDTO = new CreateUserDTO();

            createUserDTO.setUsername( user.getUsername() );
            createUserDTO.setPassword( user.getPassword() );
            createUserDTO.setEmail( user.getEmail() );
            createUserDTO.setFirstName( user.getFirstname() );
            createUserDTO.setLastName( user.getLastname() );

            return createUserDTO;
        }

        default PatchUserDTO userToPatchUserDTO(User user) {
            if ( user == null ) {
                return null;
            }

            PatchUserDTO patchUserDTO = new PatchUserDTO();

            patchUserDTO.setUsername( user.getUsername() );
            patchUserDTO.setPassword( user.getPassword() );
            patchUserDTO.setEmail( user.getEmail() );
            patchUserDTO.setFirstName( user.getFirstname() );
            patchUserDTO.setLastName( user.getLastname() );

            return patchUserDTO;
        }

        default ShortUserInfoDTO userToShortUserInfoDTO(User user) {
            if ( user == null ) {
                return null;
            }

            ShortUserInfoDTO shortUserInfoDTO = new ShortUserInfoDTO();

            shortUserInfoDTO.setUsername( user.getUsername() );
            shortUserInfoDTO.setPassword( user.getPassword() );
            shortUserInfoDTO.setEmail( user.getEmail() );
            shortUserInfoDTO.setFirstName( user.getFirstname() );
            shortUserInfoDTO.setLastName( user.getLastname() );
            shortUserInfoDTO.setRole( user.getRole() );

            return shortUserInfoDTO;
        }

        default User userFullDTOToUser(FullUserInfoDTO fullUserInfoDTO) {
            if ( fullUserInfoDTO == null ) {
                return null;
            }

            User user = new User();

            user.setId( fullUserInfoDTO.getUser_id() );
            user.setUsername( fullUserInfoDTO.getUsername() );
            user.setPassword( fullUserInfoDTO.getPassword() );
            user.setEmail( fullUserInfoDTO.getEmail() );
            user.setFirstname( fullUserInfoDTO.getFirstName() );
            user.setLastname( fullUserInfoDTO.getLastName() );
            user.setRole( fullUserInfoDTO.getRole() );

            return user;
        }

        default User userCreateDTOToUser(CreateUserDTO createUserDTO) {
            if ( createUserDTO == null ) {
                return null;
            }

            User user = new User();

            user.setUsername( createUserDTO.getUsername() );
            user.setPassword( createUserDTO.getPassword() );
            user.setEmail( createUserDTO.getEmail() );
            user.setFirstname( createUserDTO.getFirstName() );
            user.setLastname( createUserDTO.getLastName() );

            return user;
        }

        default User userPatchDTOToUser(PatchUserDTO patchUserDTO) {
            if ( patchUserDTO == null ) {
                return null;
            }

            User user = new User();

            user.setUsername( patchUserDTO.getUsername() );
            user.setPassword( patchUserDTO.getPassword() );
            user.setEmail( patchUserDTO.getEmail() );
            user.setFirstname( patchUserDTO.getFirstName() );
            user.setLastname( patchUserDTO.getLastName() );

            return user;
        }

        default User userShortUserInfoDTOToUser(ShortUserInfoDTO shortUserInfoDTO) {
            if ( shortUserInfoDTO == null ) {
                return null;
            }

            User user = new User();

            user.setUsername( shortUserInfoDTO.getUsername() );
            user.setPassword( shortUserInfoDTO.getPassword() );
            user.setEmail( shortUserInfoDTO.getEmail() );
            user.setFirstname( shortUserInfoDTO.getFirstName() );
            user.setLastname( shortUserInfoDTO.getLastName() );
            user.setRole( shortUserInfoDTO.getRole() );

            return user;
        }



}
