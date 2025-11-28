package com.example.springboot.mapper;

import com.example.springboot.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    List<Order> selectAll(Order order);
void insert(Order order);
void deleteById(Integer id);
    void update(Order order);
}
