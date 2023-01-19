package com.algaworks.algafood.core.security.authorizationserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // apenas controoler, sem Rest. Pois é um endpoint html apenas
public class SecurityController {

    @GetMapping("/login")
    public String login(){
        return "pages/login";
    }

    @GetMapping("/oauth/confirm_access")
    public String approval(){
        return "pages/approval";
    }
}
