package com.example.bookstore_app.contoller;

import com.example.bookstore_app.dto.SaleDTO;
import com.example.bookstore_app.dto.SaleOnlineDTO;
import com.example.bookstore_app.dto.UserDTO;
import com.example.bookstore_app.entity.Role;
import com.example.bookstore_app.service.SaleService;
import com.example.bookstore_app.service.UserService;
import com.example.bookstore_app.utils.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
//@CrossOrigin(origins = "http://localhost:8083") // Вказати конкретний URL клієнтського додатку
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final SaleService saleService;
    @Autowired
    private final UserService userService;
    @GetMapping("")
    public ResponseEntity<?> getUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());
            return ResponseEntity.ok(userService.getUserById(userId));

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
    @GetMapping("/all")
    public ResponseEntity<?> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUser());

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/basket")
    public ResponseEntity<?> getBasket() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());

            SaleDTO sale = saleService.getBasket(userId);
            return ResponseEntity.ok(sale);

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<?> getUserOrders() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());

            List<SaleDTO> sale = saleService.getOrderUser(userId);
            return ResponseEntity.ok(sale);

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody SaleOnlineDTO saleOnlineDTO, BindingResult bindingResult) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());

            saleService.createOnlineOrder(saleOnlineDTO,userId);
            return ResponseEntity.ok("add successful");

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(CustomException.bindingResultToString(bindingResult) + " Please fill correct in these fields.");
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());
            userDTO.setId(userId);
            userService.updateUser(userDTO);
            return ResponseEntity.ok("ok");

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PutMapping("/permission")
    public ResponseEntity<?> updateUserPermission(@RequestBody Map<String, String> body) {
        try {
            int userId = Integer.parseInt(body.get("id"));
            Role newRole = Role.valueOf(body.get("role").toUpperCase());
            boolean newPermission = Boolean.parseBoolean(body.get("blocked"));

            userService.updateUserPermission(userId, newRole, newPermission);
            return ResponseEntity.ok("ok");

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

}
