package com.example.bookstore_app.dto;

import com.example.bookstore_app.entity.Role;
import com.example.bookstore_app.entity.User;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.Date;
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private int id;
    @NotEmpty(message = "Email is empty.")

    private String email;
    @NotEmpty(message = "First name is empty.")

    private String firstName;
    @NotEmpty (message = "Last name is empty.")

    private String lastName;
    @NotEmpty (message = "Phone name is empty.")

    private String phone;
    private boolean blocked;
    private Role role;
    private Date createdAt;

    public static UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getPhone(),
                user.getIsBlocked(),
                user.getRole(),
                user.getCreated_at()
        );
    }
    public static User toEntity(UserDTO userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setEmail(userDto.getEmail());
        user.setFirst_name(userDto.getFirstName());
        user.setLast_name(userDto.getLastName());
        user.setPhone(userDto.getPhone());
        user.setIsBlocked(userDto.isBlocked());
        user.setRole(userDto.getRole());
        user.setCreated_at(userDto.getCreatedAt());
        return user;
    }

}
