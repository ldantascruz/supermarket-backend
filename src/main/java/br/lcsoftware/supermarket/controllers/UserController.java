package br.lcsoftware.supermarket.controllers;

import br.lcsoftware.supermarket.dtos.UserRecordDto;
import br.lcsoftware.supermarket.dtos.UserResponseRecordDto;
import br.lcsoftware.supermarket.models.UserModel;
import br.lcsoftware.supermarket.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserRecordDto userRecordDto) {
        var userModel = new UserModel();
        BeanUtils.copyProperties(userRecordDto, userModel);
        if (userRepository.findByEmail(userModel.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        }

        userModel.setPasswordHash(passwordEncoder.encode(userModel.getPasswordHash()));
        userRepository.save(userModel);

        var responseDto = new UserResponseRecordDto(
                userModel.getId(),
                userModel.getEmail(),
                userModel.getName()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseRecordDto>> getAllUsers() {
        List<UserResponseRecordDto> usersList = userRepository.findAll().stream().map(user ->
                new UserResponseRecordDto(
                        user.getId(),
                        user.getEmail(),
                        user.getName()
                )
        ).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(usersList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") UUID id) {
        var user0 = userRepository.findById(id);
        if (user0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        UserModel user = user0.get();
        var responseDto = new UserResponseRecordDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") UUID id, @RequestBody @Valid UserRecordDto userRecordDto) {
        var user0 = userRepository.findById(id);
        if (user0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        var user = user0.get();
        BeanUtils.copyProperties(userRecordDto, user);
        userRepository.save(user);

        var responseDto = new UserResponseRecordDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );

        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id) {
        var user0 = userRepository.findById(id);
        if (user0.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userRepository.delete(user0.get());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted");
    }
}
