package com.example.springboot.mapper;

import com.example.springboot.entity.Admin;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminMapper {
    List<Admin> selectAll(Admin admin);

    void insert(Admin admin);

    void deleteById(Integer id);

    void update(Admin admin);
    Admin selectAdminByUsername(String username);
    Admin selectById(Integer id);
}
