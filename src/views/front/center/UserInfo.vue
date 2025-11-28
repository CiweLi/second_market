<template>

    <div class="userInfo-card">

      <el-form :model="data.user" label-width="80px" class="form-input">
        <div class="userInfo-div">
          <el-upload
              action="http://localhost:9090/files/upload"
              :show-file-list="false"
              :on-success="handleAvatarSuccess">
            <img v-if="data.user.avatar" :src="data.user.avatar" class="userInfo-avatar"/>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input  v-model="data.user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input  v-model="data.user.name" placeholder="姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input  v-model="data.user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input  v-model="data.user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="性别" prop="sex">
          <el-select v-model="data.user.sex" placeholder="性别"  style="width: 100% " class="input-bar">
            <el-option label="男" value="男"></el-option>
            <el-option label="女" value="女"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="年龄" prop="age">
          <el-input  v-model="data.user.age" placeholder="年龄"></el-input>
        </el-form-item>
        <div class="userInfo-btn">
          <el-button  type="primary" @click="update">保存修改</el-button>
        </div>
      </el-form>
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
  request.put('/user/update', data.user).then(res => {
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