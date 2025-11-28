package com.example.springboot.mapper;

import com.example.springboot.entity.Goods;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsMapper {
    List<Goods> selectAll(Goods goods);
void insert(Goods goods);
void deleteById(Integer id);
    void update(Goods goods);
    List<Goods> selectShelve(Goods goods);
    Goods selectById(Integer id);
    List<Goods> selectHotGoods(Goods goods);

}
