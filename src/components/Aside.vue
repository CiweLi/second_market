<template>
  <div >
    <el-menu router :default-openeds="['1']" :default-active="router.currentRoute.value.path" class="menu">
      <el-menu-item index="/manage/home">
        <el-icon><house /></el-icon>
        <span>首页</span>
      </el-menu-item>
      <el-menu-item index="/manage/notice">
        <el-icon><ChatDotSquare /></el-icon>
        <span>公告管理</span>
      </el-menu-item>
      <el-menu-item index="/manage/type">
        <el-icon><Grid /></el-icon>
        <span>分类管理</span>
      </el-menu-item>
      <el-menu-item index="/manage/goods">
        <el-icon><Document/></el-icon>
        <span>物品管理</span>
      </el-menu-item>
      <el-menu-item index="/manage/order">
        <el-icon><Tickets/></el-icon>
        <span>订单管理</span>
      </el-menu-item>
      <el-sub-menu index="1">
        <template #title>
          <el-icon><user /></el-icon>
          <span>用户管理</span>
        </template>
        <el-menu-item index="/manage/user">用户信息</el-menu-item>
        <el-menu-item index="/manage/admin">管理员信息</el-menu-item>
      </el-sub-menu>
    </el-menu>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import {Document} from "@element-plus/icons-vue";
</script>

