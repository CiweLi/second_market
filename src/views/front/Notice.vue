<template>
  <div class="front-notice">
    <el-card>
      <div class="title">系统信息</div>
      <div  v-for="item in data.noticeData" >
        <p class="headline">{{item.title}}</p>
        <p class="notice-detail">
          <el-tooltip :content="item.content"  popper-class="atooltip" >
            <span class="text">{{item.content}}</span>
          </el-tooltip>
          <span class="time" style="">{{item.time}}</span>
        </p>
      </div>
      <div  class="pagination">
        <el-pagination
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
import { reactive } from 'vue';
import request from "@/utils/request.js";
import {ElMessage} from "element-plus";
const data = reactive({
  name: null,
  noticeData:[],
  pageNum:1,
  pageSize:5,
  total:0,
})
const load=()=>{
  request.get('/notice/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
    }
  }).then(res=>{
    if (res.code=='0'){
      console.log(res.data)
      data.noticeData = res.data.list
      data.total=res.data.total
    }
    else {
      ElMessage.error(res.msg)
    }
  })
}
load()

</script>
