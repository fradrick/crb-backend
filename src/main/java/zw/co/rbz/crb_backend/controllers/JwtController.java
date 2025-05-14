package zw.co.rbz.crb_backend.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.logging.Logger;

@Slf4j
@RestController
@RequestMapping("/api")
public class JwtController {

    private static final Logger logger = Logger.getLogger(JwtController.class.getName());

    @GetMapping("/hello2")
    public String hello() {
        return "Hello.";
    }

    @GetMapping("/user/profile")
    public Map<String, Object> getUserProfile(@AuthenticationPrincipal Jwt jwt) {
        log.info("JWT Claims: {}", jwt.getClaims());
        return jwt.getClaims();
    }

    @GetMapping("/hello")
    public String sayHello() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        logger.info("User: " + auth.getName());
        logger.info("Authorities: " + auth.getAuthorities());

        return "Hello, " + auth.getName();
    }
}
