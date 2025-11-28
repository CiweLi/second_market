package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import com.example.springboot.common.OrderEnum;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Order;
import com.example.springboot.mapper.OrderMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Resource
    OrderMapper orderMapper;

    public List<Order> selectAll() {
        return orderMapper.selectAll(null);
    }

    public void update(Order order) {
        if(order.getState().equals(OrderEnum.SHIPPED.state)){
            order.setState(OrderEnum.OK.state);
        }
        if(order.getState().equals(OrderEnum.PENDING.state)){
            order.setState(OrderEnum.SHIPPED.state);
        }

        orderMapper.update(order);
    }

    public PageInfo<Order> selectPageByBuyer(Order order, Integer pageNum, Integer pageSize) {
        Account account = JwtTokenUtils.getCurrentUser();
        order.setUserId(account.getId());
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAll(order);
        return PageInfo.of(orderList);
    }
    public PageInfo<Order> selectPageBySeller(Order order, Integer pageNum, Integer pageSize) {
        Account account = JwtTokenUtils.getCurrentUser();
        order.setSellerId(account.getId());
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAll(order);
        return PageInfo.of(orderList);
    }
    public PageInfo<Order> selectPage(Order order, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAll(order);
        return PageInfo.of(orderList);
    }

    public void add(Order order) {
        order.setTime(DateUtil.today());
        Account account = JwtTokenUtils.getCurrentUser();
        order.setUserId(account.getId());
        order.setState(OrderEnum.PENDING.state);
        orderMapper.insert(order);
    }

    public void deleteById(Integer id) {
        orderMapper.deleteById(id);
    }

}