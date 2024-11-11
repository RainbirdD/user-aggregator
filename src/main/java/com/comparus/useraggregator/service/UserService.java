package com.comparus.useraggregator.service;

import com.comparus.useraggregator.dto.User;
import com.comparus.useraggregator.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {

    private final UserRepository userRepository;

    public List<User> getAllUsers(Optional<String> username, Optional<String> name, Optional<String> surname) {
        return userRepository.getUsersFromAllSources(username, name, surname);
    }
}
