package com.swaggerhub.clone.service;

import com.swaggerhub.clone.exception.ResourceNotFoundException;
import com.swaggerhub.clone.model.dto.response.UserResponse;
import com.swaggerhub.clone.model.entity.User;
import com.swaggerhub.clone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserResponse.from(user);
    }

    public UserResponse updateProfile(String email, String name, String bio, String avatarUrl) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (name != null && !name.isBlank()) user.setName(name);
        if (bio != null) user.setBio(bio);
        if (avatarUrl != null) user.setAvatarUrl(avatarUrl);

        user = userRepository.save(user);
        log.info("Updated profile for user: {}", email);
        return UserResponse.from(user);
    }
}
