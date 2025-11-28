package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Order;
import com.example.springboot.service.OrderService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//标记Controller类
@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderService orderService;
    //RequestMapping映射
@GetMapping("/selectAll")
    public Result selectAll(){
    List<Order> list = orderService.selectAll();
    return Result.success(list);
}
    /**
     * 描述：分页查询
     */
    @GetMapping("/selectPageByBuyer")
    public Result selectPageByBuyer(Order order,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
      PageInfo<Order> pageInfo=   orderService.selectPageByBuyer(order, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @GetMapping("/selectPageBySeller")
    public Result selectPageBySeller(Order order,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
      PageInfo<Order> pageInfo=   orderService.selectPageBySeller(order, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @GetMapping("/selectPage")
    public Result selectPage(Order order,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Order> pageInfo=   orderService.selectPage(order, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping("/add")
    public Result save(@RequestBody Order order){
        orderService.add(order);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        orderService.deleteById(id);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Order order) {
        orderService.update(order);
        return Result.success();
    }

}
