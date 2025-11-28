package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Goods;
import com.example.springboot.service.GoodsService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//标记Controller类
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Resource
    GoodsService goodsService;
    //RequestMapping映射
@GetMapping("/selectAll")
    public Result selectAll(Goods goods){
    List<Goods> list = goodsService.selectAll(goods);
    return Result.success(list);
}
    /**
     * 描述：分页查询
     */
    @GetMapping("/selectPage")
    public Result page(Goods goods,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
      PageInfo<Goods> pageInfo=   goodsService.selectPage(goods, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping("/add")
    public Result save(@RequestBody Goods goods){
        goodsService.add(goods);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        goodsService.deleteById(id);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Goods goods) {
        goodsService.update(goods);
        return Result.success();
    }
    @PutMapping("/up")
    public Result up(@RequestBody Goods goods) {
        goodsService.up(goods);
        return Result.success();
    }
    @PutMapping("/down")
    public Result down(@RequestBody Goods goods) {
        goodsService.down(goods);
        return Result.success();
    }
    @GetMapping("/selectShelve")
    public Result selectPage(Goods goods,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Goods> pageInfo=   goodsService.selectShelve(goods, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(goodsService.selectById(id));
    }
    @PutMapping("/updateView")
    public Result updateView(@RequestBody Goods goods) {
        goodsService.updateView(goods);
        return Result.success();
    }
    @GetMapping("/selectHotGoods")
    public Result selectHoyGoods(Goods goods){
        List<Goods> list = goodsService.selectHotGoods(goods);
        return Result.success(list);
    }
}
