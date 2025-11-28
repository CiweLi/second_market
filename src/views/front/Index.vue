<template>
  <!-- 通知信息 -->
  <div class="notice-content">
    <div class="index-notice">【最新】
      <a href="/front/notice">{{data.noticeData.content}}</a>
    </div>
  </div>


  <!-- 热门分类 -->
  <div class="main-content">
    <el-row :gutter="16">
      <el-col :span="6">
        <img src="@/assets/img/1.jpg" class="show-img">
      </el-col>

      <el-col :span="9">
        <div class="hot-type" style="background-color: #a6b8ea;margin-bottom: 15px">
          <div class="left-div">
            <p class="title" @click=navToType(1)>书籍捡漏 <el-icon class="icon"><ArrowRight /></el-icon></p>
            <p class="text">高质量书籍低价淘</p>
          </div>
          <div class="right-div" style=" background-color: #d2d9ec;" v-for="item in data.booksData">
            <div class="right-goods-info">
              <img :src="item.pic" class="img" @click="navToDetail(item)">
              <p class="price">¥<span class="num">{{item.price}}</span></p>
            </div>
          </div>
        </div>

        <div class="hot-type" style="background-color: #f4ee96">
          <div class="left-div">
            <p class="title" @click=navToType(2)>电子产品 <el-icon class="icon"><ArrowRight /></el-icon></p>
            <p class="text">热门产品省心买</p>
          </div>
          <div class="right-div" style="background-color: #f9f7df" v-for="item in data.electronicData">
            <div class="right-goods-info">
              <img :src="item.pic" class="img" @click="navToDetail(item)">
              <p class="price">¥<span class="num">{{item.price}}</span></p>
            </div>
          </div>
        </div>
      </el-col>

      <el-col :span="9">
        <div class="hot-type" style="background-color: #b8def4;margin-bottom: 15px">
          <div class="left-div">
            <p class="title" @click=navToType(3)>生活用品 <el-icon class="icon"><ArrowRight /></el-icon></p>
            <p class="text">各种文具放心购</p>
          </div>
          <div class="right-div" style=" background-color: #d2eaf7;" v-for="item in data.lifeData">
            <div class="right-goods-info">
              <img :src="item.pic" class="img" @click="navToDetail(item)">
              <p class="price">¥<span class="num">{{item.price}}</span></p>
            </div>
          </div>
        </div>

        <div class="hot-type" style="background-color: #7cd98d">
          <div class="left-div">
            <p class="title" @click=navToType(8)>运动器材 <el-icon class="icon"><ArrowRight /></el-icon></p>
            <p class="text">体育装备随手入</p>
          </div>
          <div class="right-div" style="background-color: #daf4df" v-for="item in data.exerciseData">
            <div class="right-goods-info">
              <img :src="item.pic" class="img" @click="navToDetail(item)">
              <p class="price">¥<span class="num">{{item.price}}</span></p>
            </div>
          </div>
        </div>
      </el-col>
    </el-row>
  </div>


  <!-- 热门物品信息 -->
  <div class="hot-content">
    <el-button type="primary" round>热门物品</el-button>
    <el-row :gutter="15">
      <el-col :span="6" v-for="item in data.goodsData">
        <el-card class="goods-card">
          <img :src="item.pic" class="img" @click="navToDetail(item)">
          <el-tooltip :content="item.name" placement="top-start">
            <p class="name">{{ item.name }}</p>
          </el-tooltip>
          <p class="goods-info">
            <span class="price">¥<span class="num">{{item.price}}</span></span>
            <span class="want" v-if="item.want !=0 ">{{item.want}}人想要</span>
          </p>
          <div class="userinfo">
            <img :src="item.avatar" class="avatar"> <span class="username">{{ item.username }}</span>
          </div>
        </el-card>
      </el-col>

    </el-row>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
const data = reactive({
  goodsData:[],
  booksData:[],
  electronicData:[],
  lifeData:[],
  exerciseData:[],
  noticeData:{},
})
//加载公告信息
const loadNotice=()=>{
  request.get('/notice/selectAll',).then(res=>{
    if (res.code=='0'){
      data.noticeData = res.data[0]
    }
    else {
      ElMessage.error(res.msg)
    }
  })
}
loadNotice()
const navToType=(typeId)=>{
  router.push({path:'/front/goods',query:{typeId: typeId}}); // 参数名改为 typeId
}
const loadHotType=()=>{
  /*图书*/
  request.get('/goods/selectAll',{
    params:{
      typeId:1,
      shelve:'是'
    }
  }).then(res=>{  // 正确：请求分类列表接口
    if (res.code=='0'){
      data.booksData = res.data.sort((a, b) => a.price - b.price).slice(0, 2);
      console.log(data.booksData)
    }
    else {
      ElMessage.error(res.msg)
    }
  }).catch(err=>{
    ElMessage.error('获取分类失败');
    console.error('分类接口错误：', err);
  })
  /*电子产品*/
  request.get('/goods/selectAll',{
    params:{
      typeId:2,
      shelve:'是'
    }
  }).then(res=>{  // 正确：请求分类列表接口
    if (res.code=='0'){
      data.electronicData = res.data.sort((a, b) => a.price - b.price).slice(0, 2);
      console.log(data.electronicData)
    }
    else {
      ElMessage.error(res.msg)
    }
  }).catch(err=>{
    ElMessage.error('获取分类失败');
    console.error('分类接口错误：', err);
  })
  /*生活用品*/
  request.get('/goods/selectAll',{
    params:{
      typeId:3,
      shelve:'是'
    }
  }).then(res=>{  // 正确：请求分类列表接口
    if (res.code=='0'){
      data.lifeData = res.data.sort((a, b) => a.price - b.price).slice(0, 2);
      console.log(data.lifeData)
    }
    else {
      ElMessage.error(res.msg)
    }
  }).catch(err=>{
    ElMessage.error('获取分类失败');
    console.error('分类接口错误：', err);
  })
  /*运动器材*/
  request.get('/goods/selectAll',{
    params:{
      typeId:8,
      shelve:'是'
    }
  }).then(res=>{  // 正确：请求分类列表接口
    if (res.code=='0'){
      data.exerciseData = res.data.sort((a, b) => a.price - b.price).slice(0, 2);
      console.log(data.exerciseData)
    }
    else {
      ElMessage.error(res.msg)
    }
  }).catch(err=>{
    ElMessage.error('获取分类失败');
    console.error('分类接口错误：', err);
  })
}

loadHotType();
const navToDetail = (item)=>{
  request.put("/goods/updateView", item).then(res => {
    if (res.code === '0') {
      router.push({path:'/front/goodsDetail',query:{id:item.id}});
    } else {
      ElMessage.error(res.msg)
    }
  })
}
//加载公告信息
const loadHotGoods=()=>{
  request.get('/goods/selectHotGoods', {  // 调用热门物品接口
    params:{
      shelve:'是'  // 若接口需要筛选上架状态，保留该参数
    }
  }).then(res=>{
    if (res.code=='0'){
      data.goodsData = res.data;  // 赋值给热门物品列表
      console.log("热门物品数据：", res.data);
    } else {
      ElMessage.error(res.msg);
    }
  }).catch(err=>{
    ElMessage.error('获取热门物品失败');
    console.error('热门物品接口错误：', err);
  })
}
loadHotGoods()
</script>

<style scoped>
/* 引入样式文件 */
@import "@/assets/css/front.css";
</style>