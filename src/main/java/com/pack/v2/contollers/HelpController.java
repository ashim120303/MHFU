package com.pack.v2.contollers;

import com.pack.v2.models.User;
import com.pack.v2.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.Optional;

@Controller
public class HelpController {
    @Autowired
    private UserRepository userRepository;
    @GetMapping("/help")
    private String help(Model model, Principal principal) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("userTheme", user.getTheme());
        }
        return "help";
    }
}
