package fr.miage.conference.api.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public ResponseEntity hello() {
        return ResponseEntity.ok("Hello");
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> helloUser(Authentication authentication) {
        final String body = "Path: /user | user: " + authentication.getName();
        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping(value = "/any")
    public ResponseEntity<String> helloAny(Authentication authentication) {
        final String body = "Path: /any | user: " + authentication.getName();
        return ResponseEntity.ok(body);
    }
    
    @GetMapping(value = "/anonymous")
    public ResponseEntity<String> helloAnonymous() {
        final String body = "Open bar";
        return ResponseEntity.ok(body);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/admin")
    public ResponseEntity<String> helloAdmin(Authentication authentication) {
        final String body = "Path: /admin | user: " + authentication.getName();
        return ResponseEntity.ok(body);
    }

}
