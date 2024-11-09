package com.skthakur.bgn_backend.controller;

import com.skthakur.bgn_backend.model.User;
import com.skthakur.bgn_backend.service.UserService;
import com.skthakur.bgn_backend.util.ResponseWrapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Validated
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    private <T> ResponseEntity<ResponseWrapper<T>> buildResponse(HttpStatus status, String message, T data, Map<String, String> errors) {
        return ResponseEntity.status(status).body(new ResponseWrapper<>(status, message, data, errors));
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper<User>> createRecord(@Valid @ModelAttribute User data) throws IOException {
        User record = service.createUser(
                data.getFullName(),
                data.getEmail(),
                data.getPhone(),
                data.getPassword(),
                data.getRole()
        );

        return buildResponse(HttpStatus.CREATED, "User created successfully.", record, null);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseWrapper<User>> updateUser(@PathVariable Long id, @Valid @ModelAttribute User data) {
        try {
            Optional<User> updatedUser = service.updateUser(
                    id,
                    data.getFullName(),
                    data.getEmail(),
                    data.getPhone(),
                    data.getPassword(),
                    data.getRole()
            );
            return updatedUser
                    .map(user -> buildResponse(HttpStatus.OK, "User updated successfully.", user, null))
                    .orElseGet(() -> buildResponse(HttpStatus.NOT_FOUND, "User not found.", null, null));
        } catch (IOException e) {
            return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null, null);
        }
    }


    @GetMapping("/list")
    public ResponseEntity<ResponseWrapper<List<User>>> getUserList(
    ) {
        List<User> users = service.getUserList();
        return buildResponse(HttpStatus.OK, "Users fetched successfully.", users, null);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseWrapper<User>> getPostById(@PathVariable Long id) {
        return service.getUserById(id)
                .map(post -> buildResponse(HttpStatus.OK, "User fetched successfully.", post, null))
                .orElseGet(() -> buildResponse(HttpStatus.NOT_FOUND, "User not found.", null, null));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseWrapper<String>> deletePost(@PathVariable Long id) {
        boolean deleted = service.deleteUser(id);
        return deleted
                ? buildResponse(HttpStatus.OK, "User deleted successfully.", null, null)
                : buildResponse(HttpStatus.NOT_FOUND, "User not found.", null, null);
    }
}
