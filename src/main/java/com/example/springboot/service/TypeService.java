package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import com.example.springboot.entity.Type;
import com.example.springboot.mapper.TypeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeService {
    @Resource
    TypeMapper typeMapper;

    public List<Type> selectAll() {
        return typeMapper.selectAll(null);
    }

    public void update(Type type) {
        typeMapper.update(type);
    }

    public PageInfo<Type> selectPage(Type type, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Type> typeList = typeMapper.selectAll(type);
        return PageInfo.of(typeList);
    }

    public void add(Type type) {
        typeMapper.insert(type);
    }

    public void deleteById(Integer id) {
        typeMapper.deleteById(id);
    }

}