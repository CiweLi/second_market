package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import com.example.springboot.common.GoodsEnum;
import com.example.springboot.common.ShelveEnum;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Goods;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.GoodsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GoodsService {
    @Resource
    GoodsMapper goodsMapper;

    public List<Goods> selectAll(Goods goods) {
        return goodsMapper.selectAll(goods);
    }

    public void update(Goods goods) {
        goodsMapper.update(goods);
    }

    public PageInfo<Goods> selectPage(Goods goods, Integer pageNum, Integer pageSize) {
        Account account=JwtTokenUtils.getCurrentUser();
        if(account.getRole().equals("user")){
            goods.setUserId(account.getId());
        }
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodsList = goodsMapper.selectAll(goods);
        return PageInfo.of(goodsList);
    }

    public void add(Goods goods) {
        goods.setTime(DateUtil.now());
        Account account = JwtTokenUtils.getCurrentUser();
        goods.setUserId(account.getId());
        goods.setState(GoodsEnum.PENDING.state);
        goods.setShelve(ShelveEnum.NO.state);
        goods.setWant(0);
        goods.setView(0);
        goodsMapper.insert(goods);
    }

    public void deleteById(Integer id) {
        goodsMapper.deleteById(id);
    }

    public void up(Goods goods) {
        goods.setShelve(ShelveEnum.YES.state);
        goodsMapper.update(goods);
    }
    public void down(Goods goods) {
        goods.setShelve(ShelveEnum.NO.state);
        goodsMapper.update(goods);
    }

    public PageInfo<Goods> selectShelve(Goods goods, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Goods> goodsList = goodsMapper.selectShelve(goods);
        return PageInfo.of(goodsList);
    }
    public Goods selectById(Integer id) {
        return goodsMapper.selectById(id);
    }

    public void updateView(Goods goods) {
        goods.setView(goods.getView() + 1);
        goodsMapper.update(goods);
    }
    public List<Goods> selectHotGoods(Goods goods) {
        return goodsMapper.selectHotGoods(goods);
    }

}