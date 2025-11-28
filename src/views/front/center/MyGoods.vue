<template>
  <div>
    <el-input class="search-input" v-model="data.name" placeholder="请输入物品名称" ></el-input>
    <el-button type="success" @click="load">搜索</el-button>
    <el-button type="primary" @click="handleAdd" class="float-right">新增</el-button>

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
      <el-table-column label="操作" width="240">
        <template #default="scope">
          <el-button
              type="warning"
              icon="ArrowUp"
              circle
              v-if="scope.row.state=='已通过'&&scope.row.shelve=='否'"
              @click="handleUp(scope.row)"
              title="上架"
          />
          <el-button
              type="warning"
              icon="ArrowDown"
              circle
              v-if="scope.row.state=='已通过'&&scope.row.shelve=='是'"
              @click="handleDown(scope.row)"
              title="下架"
          />
          <el-button type="primary" icon="Edit" circle @click="handleEdit(scope.row)" title="编辑"/>
          <el-button type="danger" icon="Delete" circle @click="del(scope.row.id)" title="删除"/>
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
    <el-dialog v-model="data.formVisible" title="物品信息" width="45%" destroy-on-close>
      <el-form :model="data.form" ref="formRef" :rules="data.rules"  class="form">
        <el-form-item label="物品名称" label-width="80px" prop="name" >
          <el-input v-model="data.form.name"/>
        </el-form-item>
        <el-form-item label="分类" label-width="80px" prop="typeId">
          <el-select v-model="data.form.typeId">
            <el-option :label="item.name" :value="item.id" v-for="item in data.typeData"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格" label-width="80px" prop="price">
          <el-input v-model="data.form.price" type="number" placeholder="请输入数字"/>
        </el-form-item>
        <el-form-item label="成色" label-width="80px" prop="fineness">
          <el-select v-model="data.form.fineness">
            <el-option label="全新" value="全新"></el-option>
            <el-option label="几乎全新" value="几乎全新"></el-option>
            <el-option label="轻微使用痕迹" value="轻微使用痕迹"></el-option>
            <el-option label="明显使用痕迹" value="明显使用痕迹"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="图片" label-width="80px" prop="pic">
          <el-upload
              :show-file-list="false"
              class="avatar-uploader"
              action="http://localhost:9090/files/upload"
              :on-success="handleAvatarSuccess">
            <img v-if="data.form.pic" :src="data.form.pic" class="avatar">
            <el-icon v-else class="avatar-uploader-icon"><Plus/></el-icon>
          </el-upload>
        </el-form-item>
        <el-form-item label="内容" label-width="80px" prop="content">
          <div style="border:1px solid #e7e7e7;width:100%;margin-top: 10px;">
            <Toolbar
                style="border-bottom: 1px solid #ccc"
                :editor="editorRef"
                :mode="mode"
                :key="mode"
            />
            <Editor
                style="height:300px;overflow-y:auto"
                v-model="data.form.content"
                :defaultConfig="editorConfig"
                :mode="mode"
                @onCreated="handleCreated"
                :key="mode"
            />
          </div>
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
import '@wangeditor/editor/dist/css/style.css'
import { Editor, Toolbar } from '@wangeditor/editor-for-vue' // 确保导入正确
import request from "@/utils/request.js";
import { reactive, onBeforeUnmount, ref, shallowRef } from 'vue'
import { ElMessage, ElMessageBox } from "element-plus";
import { Search, Plus, ArrowUp, ArrowDown } from "@element-plus/icons-vue";
const data = reactive({
  goods: JSON.parse(localStorage.getItem('goodsInfo') || '{}'),
  form:{},
  formVisible:false,
  pageNum:1,
  pageSize:6,
  total:0,
  name:null,
  tableData:[],
  token:localStorage.getItem('token'),
  rules: {
    name: [
      { required: true, message: "请输入物品名称", trigger: "blur" },
    ],
    typeId: [
      { required: true,  message: "请选择分类",trigger: "change" },
    ],
    price: [
      { required: true, message: "请输入价格", trigger: "blur" },
    ],
    fineness: [
      { required: true,  message: "请选择物品成色",trigger: "change" },
    ],
    pic: [
      { required: true,  message: "请上传图片",trigger: "blur" },
    ],
  },
  typeData:[],
});
// 富文本编辑器配置
const editorRef = shallowRef() // 存储编辑器实例
const mode = 'default' // 编辑器模式（default 或 simple）
const editorConfig = {
  MENU_CONF: {} // 正确：MENU_CONF（单数）
}

// 图片上传配置（修复拼写错误后）
editorConfig.MENU_CONF['uploadImage'] = {
  headers: { token: data.token }, // 携带登录 token
  server: "http://localhost:9090/files/wang/upload", // 后端图片上传接口
  fieldName: 'file', // 上传参数名（与后端接口接收参数一致）
  maxFileSize: 2 * 1024 * 1024, // 限制图片大小（2MB，可选）
  accept: ['image/jpeg', 'image/png', 'image/gif'], // 支持的图片格式（可选）
}

// 组件卸载时销毁编辑器（修复销毁方法名）
onBeforeUnmount(() => {
  const editor = editorRef.value
  if (editor) {
    editor.destroy() // 正确：destroy（美式拼写）
    editorRef.value = null
  }
})

// 编辑器创建成功回调
const handleCreated = (editor) => {
  editorRef.value = editor;
  if (data.form.content) {
    editor.setHtml(data.form.content);
  }
};

const load=()=>{
  request.get('/goods/selectPage',{
    params:{
      pageNum:data.pageNum,
      pageSize:data.pageSize,
      title:data.name
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
const handleAdd=()=>{
  data.formVisible=true
  data.form={}
}

const handleAvatarSuccess=(res)=>{
  data.form.pic=res.data
}
const formRef=ref()
const save=()=>{
  formRef.value.validate((valid)=>{
    if (valid) {
      if (!data.form.id) { add() }
      else{ update() }
    }
  })
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
const handleEdit = (row) => {
  data.formVisible=true
  data.form=JSON.parse(JSON.stringify(row))
}
const handleUp = (row) => {
  request.put("/goods/up", row).then(res => {
      if (res.code === '0') {
        ElMessage.success('上架成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
}
const  handleDown = (row) => {
  ElMessageBox.confirm('您确定下架吗？', '确认', {type: "warning"}).then(() => {  // 正确
    request.put("/goods/down", row).then(res => {
      if (res.code === '0') {
        ElMessage.success('已下架')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(()=>{})
}
const del = (id) => {
  ElMessageBox.confirm('您确定删除吗？', '确认删除', {type: "warning"}).then(() => {  // 正确
    request.delete('/goods/delete/' + id).then(res => {
      if (res.code === '0') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(()=>{})
}
</script>

