package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.AuthenticationDto;
import br.lcsoftware.supermarket.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDto authDto){
        return ResponseEntity.ok(authService.login(authDto));
    }
}
