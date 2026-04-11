package com.swaggerhub.clone.service;

import com.swaggerhub.clone.config.JwtConfig;
import com.swaggerhub.clone.exception.DuplicateResourceException;
import com.swaggerhub.clone.exception.UnauthorizedException;
import com.swaggerhub.clone.model.dto.request.LoginRequest;
import com.swaggerhub.clone.model.dto.request.RegisterRequest;
import com.swaggerhub.clone.model.dto.response.AuthResponse;
import com.swaggerhub.clone.model.dto.response.UserResponse;
import com.swaggerhub.clone.model.entity.RefreshToken;
import com.swaggerhub.clone.model.entity.User;
import com.swaggerhub.clone.repository.RefreshTokenRepository;
import com.swaggerhub.clone.repository.UserRepository;
import com.swaggerhub.clone.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User with email '" + request.getEmail() + "' already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .build();

        user = userRepository.save(user);
        log.info("Registered new user: {}", user.getEmail());

        return generateAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new UnauthorizedException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        // Invalidate existing refresh tokens
        refreshTokenRepository.deleteByUserId(user.getId());

        log.info("User logged in: {}", user.getEmail());
        return generateAuthResponse(user);
    }

    public AuthResponse refreshToken(String refreshToken) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (storedToken.isExpired()) {
            refreshTokenRepository.delete(storedToken);
            throw new UnauthorizedException("Refresh token expired. Please login again.");
        }

        User user = storedToken.getUser();

        // Rotate refresh token
        refreshTokenRepository.delete(storedToken);

        return generateAuthResponse(user);
    }

    public void logout(String refreshToken) {
        refreshTokenRepository.findByToken(refreshToken)
                .ifPresent(token -> {
                    refreshTokenRepository.delete(token);
                    log.info("User logged out, refresh token revoked");
                });
    }

    private AuthResponse generateAuthResponse(User user) {
        String accessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        RefreshToken tokenEntity = RefreshToken.builder()
                .token(newRefreshToken)
                .user(user)
                .expiresAt(LocalDateTime.now().plusSeconds(jwtConfig.getRefreshExpiration() / 1000))
                .build();

        refreshTokenRepository.save(tokenEntity);

        return AuthResponse.of(
                accessToken,
                newRefreshToken,
                jwtConfig.getExpiration(),
                UserResponse.from(user)
        );
    }
}
