package kz.narxoz.finalproject.controllers;

import kz.narxoz.finalproject.dto.request.ChangePasswordRequestDto;
import kz.narxoz.finalproject.dto.request.UpdateProfileRequestDto;
import kz.narxoz.finalproject.repositories.UserRepository;
import kz.narxoz.finalproject.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ProfilePageController {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/profile-page")
    public String profile(Model model) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UpdateProfileRequestDto dto = new UpdateProfileRequestDto();
        dto.setEmail(email);
        model.addAttribute("profile", dto);
        model.addAttribute("password", new ChangePasswordRequestDto());
        return "profile/profile";
    }

    @PostMapping("/profile-page")
    public String updateProfile(@ModelAttribute("profile") UpdateProfileRequestDto dto) {
        userService.updateProfile(dto);
        return "redirect:/profile-page";
    }

    @PostMapping("/profile-page/password")
    public String changePassword(@ModelAttribute("password") ChangePasswordRequestDto dto) {
        userService.changePassword(dto);
        return "redirect:/profile-page";
    }
}
