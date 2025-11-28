package com.example.springboot.mapper;

import com.example.springboot.entity.Collect;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectMapper {
    List<Collect> selectAll(Collect collect);
void insert(Collect collect);
void deleteById(Integer id);
    void update(Collect collect);
}
