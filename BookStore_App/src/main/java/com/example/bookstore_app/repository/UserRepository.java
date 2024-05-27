package com.example.bookstore_app.repository;

import com.example.bookstore_app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByPhone(String phone);
    User findByEmail(String email);
    User findById(int id);

//    @Query("select u.id AS user_id, u.email AS email, u.first_name AS first_name, u.last_name AS last_name, u.phone AS phone, u.isSanctions AS sanctions, u.isBlocked AS blocked, COUNT (o.id) AS total_orders " +
//            "FROM users u LEFT JOIN orders o ON u.id = o.user.id WHERE (u.isSanctions = true OR u.isBlocked = true) AND u.role = 'USER'" +
//            "GROUP BY user_id, u.email, u.first_name, u.last_name, u.phone, u.isSanctions, u.isBlocked")
//    List<Map<String, Object>> getRestrictionsUserReport();

}
