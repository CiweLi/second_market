<template>
  <div class="center-aside" >
    <el-row :gutter="20">
      <el-col :span="5">
        <el-card>
          <el-menu router
                   :default-openeds="['1']"
                   :default-active="router.currentRoute.value.path"
                   background="#fff"
                   class="menu">
            <el-sub-menu index="1">
              <template #title>
                <el-icon><Menu /></el-icon>
                <span>个人中心</span>
              </template>
              <el-menu-item index="/front/center/Collect" >我的收藏</el-menu-item>
              <el-menu-item index="/front/center/Buy" >我买到的</el-menu-item>
              <el-menu-item index="/front/center/Sell" >我卖出的</el-menu-item>
              <el-menu-item index="/front/center/MyGoods" >我的发布</el-menu-item>
              <el-menu-item index="/front/center/userInfo" >个人信息</el-menu-item>
              <el-menu-item index="/front/center/password">修改密码</el-menu-item>
            </el-sub-menu>
          </el-menu>
        </el-card>
      </el-col>
      <el-col :span="19">
        <el-card>
          <router-view />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import router from "@/router/index.js";
import "@/assets/css/front.css";

</script>