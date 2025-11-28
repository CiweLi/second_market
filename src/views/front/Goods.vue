<template>
  <div class="main-content">
    <el-select v-model="data.search.typeId" class="search-input" placeholder="请选择分类">
      <!-- 新增：默认空选项，value 为 null -->
      <el-option label="全部分类" value=null></el-option>
      <el-option :label="item.name" :value="item.id" v-for="item in data.typeData"></el-option>
    </el-select>
    <el-input class="search-input" v-model="data.search.name" placeholder="请输入物品名称" ></el-input>
    <el-button type="success" @click="load">搜索</el-button>
    <el-button type="warning" plain @click="reset">重置</el-button>
    <el-row :gutter="15">
        <el-col :span="6" v-for="item in data.goodsData" >
          <el-card class="goods-card" >
            <img :src="item.pic" class="img" @click="navToDetail(item)">
            <el-tooltip content="item.name" placement="top-start">
              <p class="name">{{ item.name }}</p>
            </el-tooltip>
            <p class="goods-info">
              <span class="price">¥ <span class="num">{{ item.price }}</span></span>
              <span class="want" v-if="item.want !=0 ">{{item.want}}人想要</span>
            </p>
            <div class="userinfo">
              <img :src="item.avatar" class="avatar"> <span class="username">{{item.username}}</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    <el-pagination
        background
        v-model:current-page="data.pageNum"
        :page-size="data.pageSize"
        layout="total, prev, pager, next"
        :total="data.total"
        @current-change="load">
    </el-pagination>
  </div>
</template>

<script setup>
import { reactive } from 'vue';
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
import {useRoute} from "vue-router";
const route = useRoute();
// goods.vue 的 data 定义
const data = reactive({
  name: null,
  goodsData:[],
  pageNum:1,
  pageSize:8,
  total:0,
  search:{
    // 修复：若 route.query.typeId 不存在/非数字，设为 null
    typeId: route.query.typeId ? (isNaN(Number(route.query.typeId)) ? null : Number(route.query.typeId)) : null,
  },
})
const load=()=>{
  // 过滤 typeId：若为 NaN/undefined，转为 null
  const typeId = isNaN(data.search.typeId) ? null : data.search.typeId;

  request.get('/goods/selectShelve',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      shelve:'是',
      name:data.search.name,
      typeId: typeId, // 传递过滤后的值
    }
  }).then(res=>{
    if (res.code=='0'){
      console.log(res.data)
      data.goodsData = res.data.list
      data.total=res.data.total
    }
    else {
      ElMessage.error(res.msg)
    }
  })
}
load()
const loadType=()=>{
  request.get('/type/selectAll').then(res=>{  // 正确：请求分类列表接口
    if (res.code=='0'){
      console.log("分类数据：", res.data)  // 打印确认分类数据是否加载
      data.typeData = res.data
    }
    else {
      ElMessage.error(res.msg)
    }
  }).catch(err=>{
    ElMessage.error('获取分类失败');
    console.error('分类接口错误：', err);
  })
}

loadType();
const reset=()=>{
  data.search={};
  load()
}
const navToDetail = (item)=>{
  request.put("/goods/updateView", item).then(res => {
    if (res.code === '0') {
      router.push({path:'/front/goodsDetail',query:{id:item.id}});
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>
