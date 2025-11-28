<template>
  <div>
    <el-input class="search-input" v-model="data.name" placeholder="请输入物品名称" ></el-input>
    <el-button type="success" @click="load">搜索</el-button>
    <el-button type="warning" plain @click="reset">重置</el-button>

    <div style="margin-bottom: 15px"></div>
    <el-row :gutter="15">
      <el-col :span="6" v-for="item in data.collectData">
        <el-card class="collect-goods">
          <img :src="item.pic" class="img" @click="navToDetail(item.goodsId)">
          <p class="name">{{item.name}}</p>
          <div class="price">
            <span v-if="item.shelve=='是'">¥</span>
            <span class="num" v-if="item.shelve=='是'">{{item.price}}</span>
            <span style="color: #a1a1a1" v-else>物品已下架</span>
          </div>
          <el-button type="danger" icon="Delete" circle class="btn" @click="del(item.id)"/>
        </el-card>
      </el-col>
    </el-row>
    <div class="pagination">
      <el-pagination
          v-model:current-page="data.pageNum"
          :page-size="data.pageSize"
          layout="total, prev, pager, next"
          :total="data.total"
          @current-change="load">
      </el-pagination>
    </div>

  </div>
</template>

<script setup>
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue' // 确保导入正确
import request from "@/utils/request.js";
import { reactive, onBeforeUnmount, ref, shallowRef } from 'vue'
import { ElMessage, ElMessageBox } from "element-plus";
import { Search, Plus, ArrowUp, ArrowDown } from "@element-plus/icons-vue";
import router from "@/router/index.js";
const data = reactive({
  form:{},
  pageNum:1,
  pageSize:8,
  total:0,
  name:null,
  collectData:[],
});
const load=()=>{
  request.get('/collect/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      name:data.name
    }
  }).then(res=>{
    if (res.code=='0'){
      console.log(res.data)
      data.collectData = res.data.list
      data.total=res.data.total
    }
    else {
      ElMessage.error(res.msg)
    }
  })
}
load()
const reset=()=>{
  data.name=null;
  load()
}
const del = (id) => {
  ElMessageBox.confirm('您确定取消收藏吗？', '确认', {type: "warning"}).then(() => {  // 正确
    request.delete('/collect/delete/' + id).then(res => {
      if (res.code === '0') {
        ElMessage.success('取消成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(()=>{})
}
const navToDetail = (id)=>{
  router.push({path:'/front/goodsDetail',query:{id:id}});
}
</script>