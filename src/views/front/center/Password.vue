<template>
  <div class="div-password">
      <el-form :model="data.user" label-width="80px" class="form-input" :rules="data.rules" ref="formRef">
        <el-form-item label="原密码" prop="password">
          <el-input  v-model="data.user.password" placeholder="原密码" ></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input  v-model="data.user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input  v-model="data.user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
        <div  class="password-btn">
          <el-button  type="primary" @click="updatePassword">修改密码</el-button>
        </div>
      </el-form>
  </div>
</template>

<script setup>
import request from "@/utils/request.js";
import {reactive,ref} from "vue";
import {ElMessage} from "element-plus";
import router from "@/router/index.js";
// 检查俩次密码是否一致



const data = reactive({
  user: JSON.parse(localStorage.getItem('userInfo') || '{}'),
  rules: {
    password: [
      { required: true,  message: "请输入密码",trigger: "blur" },
    ],
    newPassword: [
      { required: true, message: "请输入新密码", trigger: "blur" },
    ],
    confirmPassword: [
      { required: true,trigger: "blur",message: "请输入确认密码",},
      { validator: (rule,value, callback) => {
          if (value === "") {
            callback(new Error('请再次输入密码'));
          }
          else if (value !== data.data.user.newPassword) {
            callback(new Error("输入的密码不一致"));
          } else {
            callback();
          }
        }
      }
    ],
  }
})
const formRef=ref()
const updatePassword = () => {
  formRef.value.validate(valid=>{
    if (valid){
      request.put('/account/updatePassword', data.user).then(res => {
        if (res.code === '0') {
          // 成功更新
          ElMessage.success('修改密码成功,请重新登录')
          router.push('/login')
        } else {
          ElMessage.error(res.msg)
        }
      })}

  })
}
</script>
