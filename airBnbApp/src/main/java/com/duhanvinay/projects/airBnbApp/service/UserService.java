package com.duhanvinay.projects.airBnbApp.service;

import com.duhanvinay.projects.airBnbApp.dto.ProfileUpdateRequestDto;
import com.duhanvinay.projects.airBnbApp.dto.UserDto;
import com.duhanvinay.projects.airBnbApp.entity.User;

public interface UserService {

    User getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDto profileUpdateRequestDto);

    UserDto getMyProfile();

}
