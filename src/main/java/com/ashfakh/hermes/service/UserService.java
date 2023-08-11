package com.ashfakh.hermes.service;


import com.ashfakh.hermes.dto.UserDTO;
import com.ashfakh.hermes.entity.User;
import com.ashfakh.hermes.repository.UserRepository;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDTO addUser(String channel, String channelId, String name) {
        User n = new User();
        n.setName(name);
        n.setChannel(channel);
        n.setChannelId(channelId);
        n.setUserCreationTime(System.currentTimeMillis());
        User user = userRepository.save(n);
        return UserDTO.builder().id(user.getId()).name(user.getName()).id(user.getId())
                .channelId(channelId).channel(channel).build();
    }

    public UserDTO getOrCreateUser(String channel, String channelId, String name) {
        User user = userRepository.findUserByChannelAndChannelId(channel, channelId);
        UserDTO userDTO = UserDTO.builder().name(name).channelId(channelId).build();
        if (user == null) {
            userDTO = addUser(channel, channelId, name);
        } else {
            userDTO.setId(user.getId());
        }
        return userDTO;
    }

}
