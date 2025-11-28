package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Collect;
import com.example.springboot.service.CollectService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//标记Controller类
@RestController
@RequestMapping("/collect")
public class CollectController {
    @Resource
    CollectService collectService;
    //RequestMapping映射
@GetMapping("/selectAll")
    public Result selectAll(){
    List<Collect> list = collectService.selectAll();
    return Result.success(list);
}
    /**
     * 描述：分页查询
     */
    @GetMapping("/selectPage")
    public Result page(Collect collect,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "5") Integer pageSize) {
      PageInfo<Collect> pageInfo=   collectService.selectPage(collect, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping("/add")
    public Result save(@RequestBody Collect collect){
        collectService.add(collect);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        collectService.deleteById(id);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Collect collect) {
        collectService.update(collect);
        return Result.success();
    }

}
