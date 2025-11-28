<template>
  <div>
    <!--头部-->
    <div class="header">
      <div class="header-title">
        <img src="@/assets/img/logo.png" class="logo">
        <a class="name" href="/front/index">校园二手交易市场</a>
      </div>

        <div class="header-nav">
          <el-menu  :default-active="router.currentRoute.value.path" mode="horizontal" router background-color="#409eff">
						<el-menu-item index="/front/index" class="el-menu-nav" >首页</el-menu-item>
						<el-menu-item index="/front/goods" class="el-menu-nav" >物品信息</el-menu-item>
						<el-menu-item index="/front/notice" class="el-menu-nav">公告信息</el-menu-item>
          </el-menu>
        </div>
      <div class="header-right">
        <div v-if="!data.user.username">
          <el-button @click="router.push('/login')">登录</el-button>
          <el-button @click="router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <el-dropdown>
            <div class="info">
              <img :src="data.user.avatar" class="avatar">
              <span class="name">{{data.user.name}}</span>
              <el-icon class="el-icon--right">
                <arrow-down />
              </el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="router.push('/front/center')">个人中心</el-dropdown-item>
                <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </div>
    <!--主体-->
    <div class="main-body">
      <router-view/>
    </div>
    <div class="footer">


        <div class="footer-info">
          <el-row  :gutter="20" class="row">
            <el-col :span="8">
              <div >
              <p class="title">校园二手交易市场</p>
              <p class="text">copyright © All rights reserved </p>
              </div>
            </el-col>
            <el-col :span="8">
              <div>
<!--              <p class="text"><el-icon><Timer /></el-icon><span> 周一至周五：9:00-18:00</span></p>-->
              <p class="text"><el-icon><Phone /></el-icon> <span> 8742651</span></p>
              <p class="text"><el-icon><Location /></el-icon><span> 杭州市番茄大街3号街</span></p>
              </div>
            </el-col>
            <el-col :span="8" >
              <div>
              <p class="add">有问题请及时联系我们</p>
              <p class="text">简介</p>
              </div>
            </el-col>
          </el-row>
        </div>

      </div>

  </div>

</template>

<script setup>
import "@/assets/css/front.css";
import { reactive } from 'vue';
import router from "@/router/index.js";
const data = reactive({
  user: JSON.parse(localStorage.getItem('userInfo') || '{}'),
});
const logout = () => {
  localStorage.removeItem('userInfo')
  localStorage.removeItem('token')
  router.push('/login')
}
</script>

