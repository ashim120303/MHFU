package com.pack.v2.contollers;

import com.pack.v2.models.User;
import com.pack.v2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String emailOrUsername, @RequestParam String password) {
        User user = userRepository.findByEmailOrUsername(emailOrUsername, emailOrUsername);
        if (user != null && user.getPassword().equals(password)) {
            // Успешная аутентификация
            return "redirect:/";
        } else {
            // Неверные учетные данные, обработайте соответствующим образом
            return "redirect:/login";
        }
    }
}
