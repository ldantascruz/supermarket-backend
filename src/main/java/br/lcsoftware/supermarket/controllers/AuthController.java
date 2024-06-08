package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.AuthenticationDto;
import br.lcsoftware.supermarket.dtos.ErrorResponseRecordDto;
import br.lcsoftware.supermarket.dtos.LoginResponseRecordDto;
import br.lcsoftware.supermarket.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationDto authDto) {
        try {
            if (authDto.email() == null || authDto.password() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
            }

            LoginResponseRecordDto response = authService.login(authDto);

            return ResponseEntity.ok(response);

        } catch (UsernameNotFoundException | BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseRecordDto(HttpStatus.UNAUTHORIZED.value(), "Invalid email or password"));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponseRecordDto(e.getStatusCode().value(), e.getReason()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseRecordDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred: " + e.getMessage()));
        }
    }
}
