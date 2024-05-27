package com.example.bookstore_app.entity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@org.hibernate.annotations.TypeDef(name = "order_status", typeClass = SQLEnumType.class)
@org.hibernate.annotations.TypeDef(name = "sales_type", typeClass = SQLEnumType.class)

@Entity
@Table(name = "sales")
@Getter
@Setter
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "order_date", nullable = false)
    private Date orderDate;
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @Type(type = "order_status")
    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "seller", nullable = false)
    private String seller;

    @Column(name = "sale_date")
    private Date saleDate;
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @Type(type = "sales_type")
    @Column(name = "type_sale", nullable = false)
    private SalesType typeSale;

    @Column(name = "amount", nullable = false)
    private int amount;

    @OneToMany(mappedBy = "sale")
    private List<SaleBook> saleBooks;
}
