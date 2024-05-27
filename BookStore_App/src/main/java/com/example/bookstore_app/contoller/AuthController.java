package com.example.bookstore_app.contoller;

import com.example.bookstore_app.dto.AuthDTO;
import com.example.bookstore_app.entity.User;
import com.example.bookstore_app.service.UserService;
import com.example.bookstore_app.utils.CustomException;
import com.example.bookstore_app.utils.PasswordUtils;
import com.example.bookstore_app.utils.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "http://localhost:8083") // Вказати конкретний URL клієнтського додатку
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private final UserService userService;
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody AuthDTO authDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(CustomException.bindingResultToString(bindingResult) + " Please fill correct in these fields.");
            }
            Map<String, String> response = new HashMap<>();

            userService.addUser(authDTO);
            User user = userService.getByEmail(authDTO.getEmail());
            response.put("Token", TokenUtils.generateAccessToken(user.getId(), user.getRole()));
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthDTO authDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(CustomException.bindingResultToString(bindingResult) + " Please fill correct in these fields.");
            }
            Map<String, String> response = new HashMap<>();

            User user = userService.getByEmail(authDTO.getEmail());
            if (user == null ){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email not found!");
            }
            if (user.getIsBlocked()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Your account is blocked! Please contact the administrator for detail information.");
            }
            if (PasswordUtils.verifyPassword(authDTO.getPassword(), user.getP_salt(), user.getPassword())) {
                response.put("Token", TokenUtils.generateAccessToken(user.getId(), user.getRole()));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect password!");
            }
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values.");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @PostMapping("/token")
    public ResponseEntity<?> checkToken(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        Map<String, String> response = new HashMap<>();
        if (TokenUtils.validateToken(token)) {
            Claims userInfo = TokenUtils.getClaimsFromToken(token);
            System.out.println(userInfo);
            response.put("id", userInfo.get("id").toString());
            response.put("role", (String) userInfo.get("role"));
            return ResponseEntity.ok(response);
        } else {
            System.out.println("false token");
        }
        return ResponseEntity.ok("ok");
    }
}
