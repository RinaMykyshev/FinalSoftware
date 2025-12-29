package kz.narxoz.finalproject.services.impl;

import kz.narxoz.finalproject.dto.request.ChangePasswordRequestDto;
import kz.narxoz.finalproject.dto.request.UpdateProfileRequestDto;
import kz.narxoz.finalproject.exception.BadRequestException;
import kz.narxoz.finalproject.exception.NotFoundException;
import kz.narxoz.finalproject.models.User;
import kz.narxoz.finalproject.repositories.UserRepository;
import kz.narxoz.finalproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void updateProfile(UpdateProfileRequestDto dto) {
        if (dto == null || dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new BadRequestException("Email is required");
        }

        User currentUser = getCurrentUser();

        String newEmail = dto.getEmail().trim();

        if (!newEmail.equalsIgnoreCase(currentUser.getEmail())
                && userRepository.findByEmail(newEmail).isPresent()) {
            throw new BadRequestException("Email already in use");
        }

        currentUser.setEmail(newEmail);
        userRepository.save(currentUser);


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Authentication updatedAuth = new UsernamePasswordAuthenticationToken(
                    newEmail,
                    auth.getCredentials(),
                    auth.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(updatedAuth);
        }
    }

    @Override
    public void changePassword(ChangePasswordRequestDto dto) {
        if (dto == null || dto.getOldPassword() == null || dto.getNewPassword() == null) {
            throw new BadRequestException("Old and new password are required");
        }
        if (dto.getNewPassword().isBlank()) {
            throw new BadRequestException("New password is required");
        }

        User currentUser = getCurrentUser();

        if (!passwordEncoder.matches(dto.getOldPassword(), currentUser.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(currentUser);
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
