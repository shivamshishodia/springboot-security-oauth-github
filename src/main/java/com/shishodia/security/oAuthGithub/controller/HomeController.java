package com.shishodia.security.oAuthGithub.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/home/")
public class HomeController {

    @GetMapping(path = "username")
    public String username(@AuthenticationPrincipal(expression = "attributes['name']") String username) {
        return username;
    }
    
}
