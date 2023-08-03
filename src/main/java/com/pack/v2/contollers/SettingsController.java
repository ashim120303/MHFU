package com.pack.v2.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SettingsController {
    @GetMapping("/settings")
    private String settings() {
        return "settings";
    }
}