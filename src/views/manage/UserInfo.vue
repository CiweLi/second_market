<template>
  <div>
    <el-card class="userinfo-card">
      <el-form :model="data.user" label-width="100px" >
        <div >
          <el-upload
              action="http://localhost:9090/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess">
            <img v-if="data.user.avatar" :src="data.user.avatar" class="avatar" />
          </el-upload>
        </div>
        <el-form-item label="名字" prop="username">
          <el-input v-model="data.user.name" placeholder="名字"></el-input>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="data.user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="data.user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="data.user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <div class="userInfo-btn">
          <el-button type="primary"  @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import request from "@/utils/request.js";
import {reactive} from "vue";
import {ElMessage} from "element-plus";
const data = reactive({
  user: JSON.parse(localStorage.getItem('userInfo') || '{}'),
  form:{},
});
const handleAvatarSuccess=(res)=>{
  data.user.avatar=res.data
}

const update = () => {
  request.put('user/update', data.user).then(res => {
  if (res.code === '0') {
    // 更新浏览器缓存的用户信息
    localStorage.setItem('userInfo', JSON.stringify(data.user))
    alert('修改成功')
    window.location.reload()
  } else {
    ElMessage.error(res.msg)
  }
})
}
</script>

