package com.example.springboot.mapper;

import com.example.springboot.entity.Type;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeMapper {
    List<Type> selectAll(Type type);
void insert(Type type);
void deleteById(Integer id);
    void update(Type type);
}
