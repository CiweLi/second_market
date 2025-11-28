<template>
<div>
  <div class="top-card">
<el-card>
  <el-input class="search-input"  v-model="data.title" placeholder="请输入物品名称" :prefix-icon="Search" ></el-input>
  <el-button type="primary"   @click="load">搜索</el-button>
  <el-button type="warning"  plain @click="reset">重置</el-button>

</el-card>
  </div>
  <el-card class="center-card">
    <el-table :data="data.tableData"
              style="width: 100%;margin-top: 20px"
              :header-cell-style="{'text-align':'center'}"
              :cell-style="{'text-align':'center'}">
      <el-table-column label="图片">
        <template v-slot="scope">
          <el-image :src="scope.row.pic"  class="table-img" v-if="scope.row.pic"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="物品名称" show-overflow-tooltip ></el-table-column>
      <el-table-column prop="typeName" label="分类"></el-table-column>
      <el-table-column prop="price" label="价格"></el-table-column>
      <el-table-column prop="fineness" label="成色"></el-table-column>
      <el-table-column  label="内容">
        <template #default="scope">
          <el-tag type="primary" @click="handleSee(scope.row.content)" style="cursor: pointer">查看</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="time" label="发布时间"></el-table-column>
      <el-table-column prop="username" label="发布人"></el-table-column>
      <el-table-column prop="state" label="审核状态">
        <template #default="scope">
          <el-tag type="warning" v-if="scope.row.state == '待审核'">{{scope.row.state}}</el-tag>
          <el-tag type="danger" v-if="scope.row.state == '未通过'">{{scope.row.state}}</el-tag>
          <el-tag type="info" v-if="scope.row.state == '已通过'">{{scope.row.state}}</el-tag>
          <el-tag type="primary" v-if="scope.row.state == '已售出'">{{scope.row.state}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="shelve" label="上架">
        <template #default="scope">
          <el-tag type="success" v-if="scope.row.shelve == '是'">{{scope.row.shelve}}</el-tag>
          <el-tag type="danger" v-if="scope.row.shelve == '否'">{{scope.row.shelve}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160">
        <template #default="scope">
          <el-button type="primary"  @click="handleEdit(scope.row)">审核</el-button>
          <el-button type="danger"@click="del(scope.row.id)">删除</el-button>
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

  <el-dialog v-model="data.formVisible" title="审核" width="40%" destroy-on-close>
    <el-form :model="data.form" ref="formRef" :rules="data.rules"  class="form">
      <el-form-item label="状态" label-width="80px" prop="state" >
        <el-select v-model="data.form.state" placeholder="请选择">
          <el-option label="待审核" value="待审核"></el-option>
          <el-option label="未通过" value="未通过"></el-option>
          <el-option label="已通过" value="已通过"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="data.formVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </span>
    </template>
  </el-dialog>
  <el-dialog v-model="data.seeVisible" title="物品详情内容" width="40%" destroy-on-close>
    <div v-html="data.content"></div>
  </el-dialog>
</div>
</template>
<script setup>
import { reactive,ref } from 'vue';
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";
const data = reactive({
  title: null,
  tableData:[],
  pageNum:1,
  pageSize:6,
  total:0,
  formVisible:false,
  seeVisible:false,
  form:{},
  rules: {
    title: [
      { required: true, message: "请输入标题", trigger: "blur" },
    ],
    content: [
      { required: true,  message: "请输入内容",trigger: "blur" },
    ],
  },
  content:null,
});
const formRef=ref()

const load=()=>{
  request.get('/goods/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      title:data.title
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
  data.title=null
  load()
}

const handleSee=(content)=>{
  data.content=content
  data.seeVisible=true
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
  data.form=JSON.parse(JSON.stringify(row))
  data.formVisible=true
}

const add = () => {
  request.post("/goods/add", data.form).then(res => {
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
  request.put("/goods/update", data.form).then(res => {
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
    request.delete('/goods/delete/' + id).then(res => {
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