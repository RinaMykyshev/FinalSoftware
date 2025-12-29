package kz.narxoz.finalproject.controllers;

import kz.narxoz.finalproject.dto.request.AdminCreateUserRequestDto;
import kz.narxoz.finalproject.repositories.UserRepository;
import kz.narxoz.finalproject.services.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminPageController {

    private final UserRepository userRepository;
    private final AdminUserService adminUserService;

    @GetMapping("/admin-page/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("form", new AdminCreateUserRequestDto());
        return "admin/users";
    }

    @PostMapping("/admin-page/users")
    public String createUser(@ModelAttribute("form") AdminCreateUserRequestDto form) {
        adminUserService.createUser(form);
        return "redirect:/admin-page/users";
    }

    @PostMapping("/admin-page/users/{id}/block")
    public String block(@PathVariable Long id) {
        adminUserService.blockUser(id);
        return "redirect:/admin-page/users";
    }

    @PostMapping("/admin-page/users/{id}/delete")
    public String delete(@PathVariable Long id) {
        adminUserService.deleteUser(id);
        return "redirect:/admin-page/users";
    }
}
