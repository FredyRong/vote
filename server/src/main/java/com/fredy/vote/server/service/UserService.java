package com.fredy.vote.server.service;

import com.fredy.vote.model.entity.User;
import com.fredy.vote.server.dto.UserDto;

import java.util.List;

/**
 * @author Fredy
 * @date 2020-12-14 14:41
 */
public interface UserService {

    List<User> getUserList();

    void addUser(UserDto userDto);

    void updateUser(UserDto userDto);

    void deleteUser(UserDto userDto);
}
