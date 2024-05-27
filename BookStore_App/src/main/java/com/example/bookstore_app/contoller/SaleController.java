package com.example.bookstore_app.contoller;

import com.example.bookstore_app.dto.SaleBookDTO;
import com.example.bookstore_app.dto.SaleDTO;
import com.example.bookstore_app.dto.SaleOnlineDTO;
import com.example.bookstore_app.entity.Sale;
import com.example.bookstore_app.service.SaleService;
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

@RestController
@RequestMapping("/api/sale")
//@CrossOrigin(origins = "http://localhost:8083") // Вказати конкретний URL клієнтського додатку
@RequiredArgsConstructor
public class SaleController {
    @Autowired
    private final SaleService saleService;
    @GetMapping("/orders")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(saleService.getOrders());
        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/basket")
    public ResponseEntity<?> addBasket(@Valid @RequestBody SaleBookDTO saleBookDTO, BindingResult bindingResult) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());

            saleService.addBasket(saleBookDTO,userId);
            return ResponseEntity.ok("add successful");

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping("/sale_order")
    public ResponseEntity<?> complitedOrder(@Valid @RequestBody SaleOnlineDTO saleOnlineDTO, BindingResult bindingResult) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            int userId = Integer.parseInt(authentication.getName());

            saleService.finishOrder(saleOnlineDTO,userId);
            return ResponseEntity.ok("add successful");

        } catch (IllegalArgumentException FormatException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fields have incorrect values");
        } catch (CustomException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
