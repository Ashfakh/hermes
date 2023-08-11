package com.ashfakh.hermes.repository;

import com.ashfakh.hermes.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByChannelAndChannelId(String channel, String channelId);
}
