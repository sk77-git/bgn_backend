package com.skthakur.bgn_backend.service;

import com.skthakur.bgn_backend.enums.UserRole;
import com.skthakur.bgn_backend.model.User;
import com.skthakur.bgn_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User createUser(String fullName, String email, String phone, String password, UserRole role) throws IOException {

        User user = new User(fullName, email, phone, password, role, LocalDateTime.now());
        return repository.save(user);
    }

    public Optional<User> updateUser(Long id, String fullName, String email, String phone, String password, UserRole role) throws IOException {
        Optional<User> existingRecordOpt = repository.findById(id);

        if (existingRecordOpt.isPresent()) {
            User existingRecord = existingRecordOpt.get();
            existingRecord.setFullName(fullName);
            existingRecord.setEmail(email);
            existingRecord.setPhone(phone);
            existingRecord.setPassword(password);
            existingRecord.setRole(role);
            existingRecord.setUpdatedAt(LocalDateTime.now());
            return Optional.of(repository.save(existingRecord));
        }
        return existingRecordOpt;


    }


    public List<User> getUserList() {
        return repository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return repository.findById(id);
    }


    public boolean deleteUser(Long id) {
        Optional<User> existingRecordOpt = repository.findById(id);

        if (existingRecordOpt.isPresent()) {
            User existingRecord = existingRecordOpt.get();
            repository.deleteById(id);
            return true;
        }
        return false;
    }

}
