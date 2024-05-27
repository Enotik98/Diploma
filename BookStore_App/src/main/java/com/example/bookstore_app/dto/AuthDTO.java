package com.example.bookstore_app.dto;

import com.example.bookstore_app.entity.User;
import com.example.bookstore_app.utils.PasswordUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class AuthDTO {
    @NotEmpty(message = "Email mustn't be empty.")
    @Pattern(regexp = "^[^\\s]+$", message = "Email can't contain spaces.")
    @Email(message = "Email format should be valid.")
    @Size(min = 6, message = "The email length must be more than 6 characters.")
    @Size(max = 25, message = "The email length must be less than 25 characters.")
    private String email;
    @NotEmpty(message = "Password mustn't be empty.")
    @Pattern(regexp = "^[^\\s]+$", message = "Password can't contain spaces.")
    @Size(min = 3, message = "The password length must be more than 3 characters.")
    @Size(max = 15, message = "The password length must be less than 15 characters.")
    private String password;

    public User convertToUser() {
        User user = new User();
        user.setEmail(this.getEmail());
        String salt = PasswordUtils.generateSalt();
        user.setP_salt(salt);
        String hashedPassword = PasswordUtils.hashPassword(this.getPassword(), salt);
        user.setPassword(hashedPassword);
        return user;
    }
}
