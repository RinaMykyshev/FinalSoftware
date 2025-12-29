package kz.narxoz.finalproject.controllers;

import kz.narxoz.finalproject.dto.request.RegisterRequestDto;
import kz.narxoz.finalproject.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class AuthPageController {

    private final AuthService authService;

    @GetMapping("/sign-in")
    public String signIn(@RequestParam(required = false) String error,
                         @RequestParam(required = false) String registered,
                         Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("registered", registered != null);
        return "auth/sign-in";
    }

    @GetMapping("/sign-up")
    public String signUpForm(Model model) {
        model.addAttribute("form", new RegisterRequestDto());
        return "auth/sign-up";
    }

    @PostMapping("/sign-up")
    public String signUpSubmit(@ModelAttribute("form") RegisterRequestDto form) {
        authService.register(form);
        return "redirect:/sign-in?registered";
    }
}
