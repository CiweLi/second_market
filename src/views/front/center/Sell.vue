<template>
  <el-input class="search-input" v-model="data.name" placeholder="请输入物品名称" ></el-input>
  <el-button type="success" @click="load">搜索</el-button>
  <el-button type="warning" plain @click="reset">重置</el-button>
  <div class="user-order" v-for="item in data.orderData">
    <!-- 顶部信息区域：用户+订单状态 -->
    <div class="user-order-top">
      <div class="userinfo">
        <img :src="item.userAvatar" class="avatar">
        <span class="name">{{ item.username }}</span>
      </div>
      <span class="state">{{ item.state }}</span>
    </div>

    <!-- 商品+操作区域 -->
    <div class="user-order-center">
      <!-- 商品图片 -->
      <img :src="item.pic" class="img">

      <!-- 商品信息+按钮区域 -->
      <div class="goods-wrap">
        <p class="name">{{item.goodsName}}</p>
        <p class="addredd">交易地址：{{item.address}}</p>

        <div class="info">
          <span class="price">¥<span class="number">{{item.price}}</span></span>

          <div class="btn-group">
            <el-button type="warning" round size="small" class="btn" @click="send(item)" v-if="item.state=='等待见面交易'">发货</el-button>
            <!--
            <el-button type="success" round size="small" class="btn" @click="commentBtn(item)">评价</el-button>
            -->
            <el-button type="primary" round size="small" class="btn" @click="see(item)">订单详情</el-button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div class="pagination">
    <el-pagination
        v-model:current-page="data.pageNum"
        :page-size="data.pageSize"
        layout="total, prev, pager, next"
        :total="data.total"
        @current-change="load">
    </el-pagination>
    <el-dialog v-model="data.formVisible" title="订单详情" width="40%" destroy-on-close>
      <el-form :model="data.form" ref="formRef" :rules="data.rules"  class="form">
        <el-form-item label="物品名称" label-width="80px" >
          <el-input v-model="data.form.goodsName"/>
        </el-form-item>
        <el-form-item label="价格" label-width="80px">
          <el-input v-model="data.form.price"/>
        </el-form-item>
        <el-form-item label="交易地点" label-width="80px"  >
          <el-input v-model="data.form.address"/>
        </el-form-item>
        <el-form-item label="买家" label-width="80px"  >
          <el-input v-model="data.form.username"/>
        </el-form-item>
        <el-form-item label="订单状态" label-width="80px"  >
          <el-input v-model="data.form.state"/>
        </el-form-item>
        <el-form-item label="下单时间" label-width="80px"  >
          <el-input v-model="data.form.time"/>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>

</template>

<script setup lang="ts">
import request from "@/utils/request.js";
import { reactive, onBeforeUnmount, ref, shallowRef } from 'vue'
import { ElMessage, ElMessageBox } from "element-plus";
import router from "@/router/index.js";
const data = reactive({
  form:{},
  pageNum:1,
  pageSize:8,
  total:0,
  name:null,
  orderData:[],
  form:{},
  formVisible:false,
});
const load=()=>{
  request.get('/order/selectPageBySeller',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      goodsName:data.name
    }
  }).then(res=>{
    if (res.code=='0'){
      console.log(res.data)
      data.orderData = res.data.list
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
const send=(orderData)=>{
  request.put("/order/update",orderData).then(res => {
    if (res.code === '0') {
      ElMessage.success('发货成功');
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}


// 查看订单详情方法
const see = (orderData) => {
  data.form=JSON.parse(JSON.stringify(orderData))
  data.formVisible=true;
}
</script>

<style scoped>
/* 订单卡片容器：增加阴影、圆角、内边距 */
.user-order {
  max-width: 800px;
  margin: 16px auto;
  padding: 16px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

/* 顶部区域：用户+状态 左右分布 */
.user-order-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 12px;
  border-bottom: 1px solid #f5f5f5;
}

/* 用户信息：头像+昵称 横向排列 */
.userinfo {
  display: flex;
  align-items: center;
  gap: 8px;
}
.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  object-fit: cover;
}
.name {
  font-size: 16px;
  color: #333;
}

/* 订单状态：突出显示 */
.state {
  font-size: 13px;
  color: #ff7d00; /* 橙色突出“等待交易”状态 */
  font-weight: 500;
}

/* 商品区域：图片+信息 横向排列 */
.user-order-center {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 13px 0;
}
.img {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  object-fit: cover;
  background: #f9f9f9;
}

/* 商品信息容器：占满剩余宽度 */
.goods-wrap {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 80px;
}
.goods-wrap .name {
  font-size: 18px;
  color: #333;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

/* 价格+按钮区域：左右分布 */
.info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.price {
  font-size: 20px;
  color: #f56c6c;
}
.number {
  font-size: 18px;
}

/* 按钮组：增加间距 */
.btn-group {
  display: flex;
  gap: 8px;
}
.btn {
  padding: 4px 12px;
}
</style>
<script setup lang="ts">
</script>