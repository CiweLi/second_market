package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Type;
import com.example.springboot.service.TypeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//标记Controller类
@RestController
@RequestMapping("/type")
public class TypeController {
    @Resource
    TypeService typeService;
    //RequestMapping映射
@GetMapping("/selectAll")
    public Result selectAll(){
    List<Type> list = typeService.selectAll();
    return Result.success(list);
}
    /**
     * 描述：分页查询
     */
    @GetMapping("/selectPage")
    public Result page(Type type,
                       @RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize) {
      PageInfo<Type> pageInfo=   typeService.selectPage(type, pageNum, pageSize);
        return Result.success(pageInfo);
    }
    @PostMapping("/add")
    public Result save(@RequestBody Type type){
        typeService.add(type);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result deleteById(@PathVariable Integer id) {
        typeService.deleteById(id);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Type type) {
        typeService.update(type);
        return Result.success();
    }

}
