<template>
<div>
  <div class="top-card">
<el-card>
  <el-input class="search-input"  v-model="data.name" placeholder="请输入物品名称" :prefix-icon="Search" ></el-input>
  <el-button type="primary"   @click="load">搜索</el-button>
  <el-button type="warning"  plain @click="reset">重置</el-button>

</el-card>
  </div>
  <el-card class="center-card">
    <el-table :data="data.tableData"
              style="width: 100%"
              :header-cell-style="{'text-align':'center'}"
              :cell-style="{'text-align':'center'}">
      <el-table-column label="图片">
        <template v-slot="scope">
          <el-image :src="scope.row.pic"  class="table-img"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="goodsName" label="物品名称" > </el-table-column>
      <el-table-column prop="price" label="价格" show-overflow-tooltip >  </el-table-column>
      <el-table-column prop="time" label="下单时间" ></el-table-column>
      <el-table-column prop="address" label="交易地点" show-overflow-tooltip> </el-table-column>
      <el-table-column prop="username" label="买家" > </el-table-column>
      <el-table-column prop="sellerName" label="卖家" > </el-table-column>
      <el-table-column prop="state" label="订单状态" >
        <template #default="scope">
          <el-tag type="warning" v-if="scope.row.state == '等待见面交易'">{{scope.row.state}}</el-tag>
          <el-tag type="success" v-if="scope.row.state == '已发货'">{{scope.row.state}}</el-tag>
          <el-tag type="info" v-if="scope.row.state == '已完成'">{{scope.row.state}}</el-tag>
          <el-tag type="primary" v-if="scope.row.state == '已评价'">{{scope.row.state}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column  label="操作" width="160" >
        <template #default="scope">
          <el-button type="danger"  @click="del(scope.row.id)" v-if="scope.row.state=='已评价' || scope.row.state == '已完成'">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="pagination">
    <el-pagination
        background
        v-model:current-page="data.pageNum"
        :page-size="data.pageSize"
        layout="total, prev, pager, next"
        :total="data.total"
        @current-change="load">
    </el-pagination>
    </div>
  </el-card>

</div>
</template>
<script setup>
import { reactive,ref } from 'vue';
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data = reactive({
  name: null,
  tableData:[],
  pageNum:1,
  pageSize:6,
  total:0,
  formVisible:false,
  form:{},
});
const formRef=ref()

const load=()=>{
  request.get('/order/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      goodsName:data.name
    }
  }).then(res=>{
    if (res.code=='0'){
      console.log(res.data)
      data.tableData = res.data.list
      data.total=res.data.total
    }
    else {
      ElMessage.error(res.msg)
    }
  })
}

load()

const reset=()=>{
  data.name=null
  load()
}

const handleAdd=()=>{
  data.formVisible=true
  data.form={}
}

const save=()=>{
  formRef.value.validate((valid)=>{
    if (valid) {
      if (!data.form.id) { add() }
      else{ update() }
    }
  })
}

const handleEdit = (row) => {
 console.log(row)
  data.form=JSON.parse(JSON.stringify(row))
  data.formVisible=true
}

const add = () => {
  request.post("/order/add", data.form).then(res => {
    if (res.code === '0') {
      data.formVisible = false
      ElMessage.success('保存成功');
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const update = () => {
  request.put("/order/update", data.form).then(res => {
    if (res.code === '0') {
      data.formVisible = false
      ElMessage.success('修改成功');
      load()
    } else {
      ElMessage.error(res.msg)
    }
  })
}

const del = (id) => {
  ElMessageBox.confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(response => {
    request.delete('/order/delete/' + id).then(res => {
      if (res.code === '0') {   // 表示操作成功
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)  // 弹出错误的信息
      }
    })
  }).catch(()=>{})
}
</script>