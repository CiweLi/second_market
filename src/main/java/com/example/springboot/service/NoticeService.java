package com.example.springboot.service;

import cn.hutool.core.date.DateUtil;

import com.example.springboot.mapper.NoticeMapper;
import com.example.springboot.entity.Notice;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class NoticeService {
    @Resource
    NoticeMapper noticeMapper;

    public List<Notice> selectAll() {
        return noticeMapper.selectAll(null);
    }

    public void update(Notice notice) {
        noticeMapper.update(notice);
    }

    public PageInfo<Notice> selectPage(Notice notice, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Notice> noticeList = noticeMapper.selectAll(notice);
        return PageInfo.of(noticeList);
    }

    public void add(Notice notice) {
        notice.setTime(DateUtil.today());
        noticeMapper.insert(notice);
    }

    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }

}