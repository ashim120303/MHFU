package com.pack.v2.contollers;

import com.pack.v2.models.Post;
import com.pack.v2.models.User;
import com.pack.v2.repositories.PostRepository;
import com.pack.v2.repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @GetMapping("/settings")
    private String settings(@RequestParam(required = false) String error, Model model, Principal principal) {
        // Применение выбранной темы
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("userTheme", user.getTheme());
        }

        if (error != null) {
            model.addAttribute("error", "(Был введен неправильный пароль.)");
        }
        return "settings";
    }
    @PostMapping("/settings")
    private String deleteAccount(@RequestParam String password, Principal principal, HttpServletRequest request) {
        // получение текущего пользователя
        String username = principal.getName();
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // проверка пароля
            if (passwordEncoder.matches(password, user.getPassword())) {
                // удаление постов пользователя
                List<Post> userPosts = postRepository.findAllByUserIdOrderByCreatedDateDesc(user.getId());
                postRepository.deleteAll(userPosts);
                // удаление аккаунта пользователя
                userRepository.delete(user);
                // выполнение выхода из системы
                SecurityContextHolder.getContext().setAuthentication(null);
                try {
                    request.logout();
                } catch (ServletException e) {
                    e.printStackTrace();
                }
                return "redirect:/login";
            }
        }
        // если пароль не верный или пользователь не найден
        return "redirect:/settings?error";
    }

    @PostMapping("/updateTheme")
    public String changeTheme(@RequestParam String theme, Principal principal) {
        Optional<User> optionalUser = userRepository.findByUsername(principal.getName());
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setTheme(theme);
            userRepository.save(user);
        }
        return "redirect:/settings"; // предполагая, что URL для страницы настроек это /settings
    }
}
