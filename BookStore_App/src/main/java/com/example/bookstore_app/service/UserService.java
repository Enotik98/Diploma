package com.example.bookstore_app.service;

import com.example.bookstore_app.dto.AuthDTO;
import com.example.bookstore_app.dto.UserDTO;
import com.example.bookstore_app.entity.User;
import com.example.bookstore_app.repository.UserRepository;
import com.example.bookstore_app.utils.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<UserDTO> getAllUser() {
        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> result = new ArrayList<>();
            for (User user : users) {
                result.add(UserDTO.toDto(user));
            }
            return result;
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    public UserDTO getUserById(int id) {
        try {
            User user = userRepository.findById(id);
            return UserDTO.toDto(user);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public User getByEmail(String email) {
        try {
            return userRepository.findByEmail(email);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
    public void addUser(AuthDTO authDTO) {
        //add code for search duplicate user
        try {
            User exUser = userRepository.findByEmail(authDTO.getEmail());
            if (exUser != null) {
                //409
                throw new CustomException(HttpStatus.CONFLICT, "An account with such email already exists. Choose another email.");
            }
            User user = authDTO.convertToUser();
            userRepository.save(user);
        } catch (CustomException customException) {
            throw new CustomException(customException.getStatus(), customException.getMessage());
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
