package com.pack.v2.contollers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SettingsController {
    @GetMapping("/settings")
    private String settings() {
        return "settings";
    }
}