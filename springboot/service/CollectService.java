package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.springboot.common.ResultCode;
import com.example.springboot.common.config.JwtTokenUtils;
import com.example.springboot.entity.Account;
import com.example.springboot.entity.Collect;
import com.example.springboot.exception.ServiceException;
import com.example.springboot.mapper.CollectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialException;
import java.util.List;

@Service
public class CollectService {
    @Resource
    private CollectMapper collectMapper;

    public void add(Collect collect) {
        Account account = JwtTokenUtils.getCurrentUser();
        collect.setUserId(account.getId());
        List<Collect> list = collectMapper.selectAll(collect);
        // 正确逻辑：如果已存在收藏记录，抛出异常
        if(ObjectUtil.isNotEmpty(list)){
            throw new ServiceException(ResultCode.COLLECT_ALREADY);
        }
        // 不存在收藏记录时，执行新增
        collectMapper.insert(collect);
    }

    public void deleteById(Integer id) {
        collectMapper.deleteById(id);
    }

    public void update(Collect collect) {
        collectMapper.update(collect);
    }

    public List<Collect> selectAll() {
        return collectMapper.selectAll(null);
    }


    public PageInfo<Collect> selectPage(Collect collect, Integer pageNum, Integer pageSize) {
        Account account = JwtTokenUtils.getCurrentUser();
        collect.setUserId(account.getId());
        PageHelper.startPage(pageNum, pageSize);
        List<Collect> collectList = collectMapper.selectAll(collect);
        return PageInfo.of(collectList);
    }

}