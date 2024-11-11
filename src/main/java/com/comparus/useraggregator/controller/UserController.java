package com.comparus.useraggregator.controller;

import com.comparus.useraggregator.dto.User;
import com.comparus.useraggregator.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers(
            @RequestParam Optional<String> username,
            @RequestParam Optional<String> name,
            @RequestParam Optional<String> surname) {

        return userService.getAllUsers(username, name, surname);
    }
}
