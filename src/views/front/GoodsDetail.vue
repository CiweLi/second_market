<template>
  <div class="detail-content">
    <!-- 顶部店铺信息 -->
    <div class="detail-seller">
      <img :src="data.goodsData.avatar" class="avatar" >
      <div class="info">
        <p class="name">{{data.goodsData.username}}</p>
        <el-tag>信用优秀</el-tag>
      </div>
      <el-button round><el-icon class="icon"><HomeFilled /></el-icon>&nbsp卖家主页</el-button>
    </div>

    <!-- 商品详情信息（改为弹性容器，让左右区域高度对齐） -->
    <div class="goods-detail">
      <div class="goods-main"> <!-- 新增弹性容器 -->
        <div class="goods-img-wrap"> <!-- 图片容器 -->
          <img :src="data.goodsData.pic" class="goods-img">
        </div>
        <div class="goods-info-wrap"> <!-- 文字信息容器 -->
          <div>
            <p class="name">{{data.goodsData.name}}</p>
            <div>
              <el-tag type="warning" class="tag">超300人加入收藏</el-tag>
              <el-tag>{{data.goodsData.fineness}}</el-tag>
            </div>
          </div>

          <p class="label">
            <span class="price-icon">¥</span>
            <span class="price">{{data.goodsData.price}}</span>
            <span class="num">20人想要</span>
            <span class="num" >{{data.goodsData.view}}次浏览</span>
          </p>

          <p class="label">
            <span class="label-text">保&nbsp &nbsp障</span>
            <span>交易无忧 <span class="interval">|</span> 物品真实 <span class="interval">|</span> 审核后上架 </span>
          </p>

          <p class="label">
            <span class="label-text">承&nbsp &nbsp诺</span>
            <span>价格合理 <span class="interval">|</span> 品质保证 </span>
          </p>

          <p class="label">
            <span class="label-text">交易方式</span>
            <span>当面交易 </span>
          </p>

          <p class="label">
            <span class="label-text">分&nbsp &nbsp类</span>
            <span>{{data.goodsData.typeName}}</span>
          </p>

          <p class="label">
            <span class="label-text">上架时间</span>
            <span>{{data.goodsData.time}}</span>
          </p>

          <p class="label">
            <span class="label-text">注意事项：</span>
            <span>请同学们诚信交易，若出现欺诈，发布虚假物品等情况，账户将无法继续发</span>
          </p>

          <div class="btn">
            <el-tag type="info" class="collect" @click="collect(data.goodsData)"> 收藏</el-tag>
            <el-button type="warning"><el-icon class="icon"><ChatLineRound /></el-icon>&nbsp我想要</el-button>
            <el-button type="primary" @click="handleBuy(data.goodsData)"><el-icon class="icon"><ShoppingCart /></el-icon>&nbsp购买</el-button>
          </div>
        </div>
      </div>

      <!-- 评论详情 -->
      <div class="goods-tabs">
        <el-tabs v-model="activeName">
          <el-tab-pane label="详细信息" name="first">
            <div v-html="data.goodsData.content" class="detail-html"></div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
    <el-dialog v-model="data.formVisible" title="交易地址" width="40%" destroy-on-close>
      <el-form :model="data.form" ref="formRef" :rules="data.rules"  class="form">
        <el-form-item label="地址" label-width="80px" prop="address" >
          <el-input v-model="data.form.address"/>
        </el-form-item>
      </el-form>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup>
import { reactive,ref } from 'vue';
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
import { HomeFilled, ChatLineRound, ShoppingCart } from '@element-plus/icons-vue' // 补充图标导入
import {useRoute} from "vue-router";
const route = useRoute();
const data = reactive({
  name: null,
  goodsData:{}, // 初始化为对象（原数组会导致属性读取报错）
  id:route.query.id,
  form:{},
  user: JSON.parse(localStorage.getItem('userInfo') || '{}'),
  formVisible:false,
  rules: {
    address: [
      { required: true, message: "请输入地址", trigger: "blur" },
    ],
  }
})
const activeName = ref('first')

const load=()=>{
  request.get('/goods/selectById/' + data.id).then(res=>{
    if (res.code=='0'){
      data.goodsData = res.data
      console.log(data.goodsData)
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()
const collect = (item) => {
  // 先打印数据，确认字段名和值是否正确
  console.log("商品发布者ID：", item.user_id || item.userId);
  console.log("当前用户ID：", data.user.id || data.user.userId);

  // 统一字段名判断（根据实际返回的字段调整）
  const publisherId = item.user_id || item.userId;
  const currentUserId = data.user.id || data.user.userId;

  if (publisherId && currentUserId && publisherId === currentUserId) {
    ElMessage.error("无法收藏自己发布的物品");
    return;
  }

  data.form.goodsId = item.id;
  request.post("/collect/add", data.form).then(res => {
    if (res.code === '0') {
      ElMessage.success('收藏成功');
      load();
    } else {
      ElMessage.error(res.msg);
    }
  });
};
const handleBuy = (item) => {
  // 增加空值校验
  if (!item) {
    ElMessage.error("商品信息异常");
    return;
  }

  // 打印数据确认字段
  console.log("商品发布者ID：", item.user_id || item.userId);
  console.log("当前用户ID：", data.user.id || data.user.userId);

  // 统一字段名判断（根据后端实际返回的字段调整）
  const publisherId = item.user_id || item.userId;
  const currentUserId = data.user.id || data.user.userId;

  // 增加类型转换（避免数字/字符串类型不匹配）
  if (publisherId && currentUserId && String(publisherId) === String(currentUserId)) {
    ElMessage.error("无法购买自己发布的物品");
    return;
  }
  data.form={};
  data.form.goodsId = item.id;
  data.form.sellerId = item.userId || item.user_id;
  data.formVisible = true;
};
const save = () => {
  request.post("/order/add", data.form).then(res => {
    if (res.code === '0') {
      data.formVisible = false
      ElMessage.success('购买成功');
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}
</script>

<style scoped>
/* 整体容器 */
.detail-content {
  max-width: 1000px; /* 缩小整体宽度，避免太宽 */
  margin: 0 auto;
  padding: 20px;
}

/* 顶部店铺信息 */
.detail-seller {
  display: flex;
  align-items: center;
  padding: 10px;
  margin-bottom: 20px;
  background: #f5f5f5;
  border-radius: 8px;
}
.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 10px;
}
.info {
  flex: 1;
}

/* 商品详情主容器 */
.goods-detail {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

/* 商品主区域：弹性布局，让左右高度一致 */
.goods-main {
  display: flex;
  gap: 20px; /* 左右区域间距 */
  align-items: stretch; /* 强制左右区域高度一致 */
}

/* 左侧图片容器：限制高度，与右侧文字区域匹配 */
.goods-img-wrap {
  flex: 0 0 40%; /* 图片区域占比40% */
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f9f9f9; /* 图片占位背景 */
  border-radius: 8px;
  overflow: hidden;
  max-height: 400px; /* 限制最大高度，避免过高 */
}
.goods-img {
  width: 100%;
  height: 100%;
  object-fit: contain; /* 保持图片比例，不拉伸 */
}

/* 右侧文字信息容器：占比60%，自适应高度 */
.goods-info-wrap {
  flex: 1; /* 占剩余宽度 */
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 文字区域上下分布均匀 */
}

/* 商品名称 */
.name {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 12px;
  line-height: 1.3;
}

/* 标签样式 */
.tag {
  margin-right: 8px;
}

/* 信息行样式 */
.label {
  margin: 8px 0;
  font-size: 14px;
  line-height: 1.5;
}
.price {
  font-size: 26px;
  color: #f56c6c;
  font-weight: bold;
}
.price-icon {
  font-size: 18px;
  color: #f56c6c;
}
.interval {
  margin: 0 8px;
  color: #ccc;
}
.label-text {
  color: #909399;
  margin-right: 8px;
}

/* 按钮区域 */
.btn {
  margin-top: 20px;
  display: flex;
  gap: 10px;
}
.collect {
  cursor: pointer;
  padding: 6px 12px;
}

/* 详情标签页 */
.goods-tabs {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}
.detail-html {
  padding: 10px 0;
  line-height: 1.6;
}
</style>