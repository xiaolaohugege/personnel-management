<template>
  <div>
    个人中心
    <el-form ref="formRef" :model="data.user" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
      <el-form-item prop="username" label="账号">
        <el-input v-model="data.user.username" autocomplete="off" />
      </el-form-item>
      <el-form-item prop="name" label="名称">
        <el-input v-model="data.user.name" autocomplete="off" />
      </el-form-item>
      <el-form-item prop="phone" label="电话">
        <el-input v-model="data.user.phone" autocomplete="off" />
      </el-form-item>
      <el-form-item prop="email" label="邮箱">
        <el-input v-model="data.user.email" autocomplete="off" />
      </el-form-item>
      <el-form-item prop="avatar" label="头像">
        <el-upload
            action="http://localhost:8081/file/upload"
            :headers="{ token: data.user.token }"
            :on-success="handleFileSuccess"
            list-type="picture"
        >
          <el-button type="primary">上传头像</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <div style="text-align: center">
      <el-button type="primary" style="padding: 18px 35px" @click="update">保 存</el-button>
    </div>

  </div>
</template>
<script setup>
import {reactive} from "vue";
import request from "@/utils/request.js";
const data = reactive({
  user : JSON.parse(localStorage.getItem("code_user") || '{}')
})

const handleFileSuccess = (res) => {
  data.user.avatar = res.data
}

const emit = defineEmits(['updateUser'])
const update = () => {
  let url
  if (data.user.role === 'ADMIN') {
    url = '/admin/update'
  }
  if (data.user.role === 'USER') {
    url = '/user/update'
  }
  request.put(url, data.user).then(res => {
    if (res.code === '200') {
      ElMessage.success('更新成功')
      window.location.reload();
      localStorage.setItem("code_user", JSON.stringify(data.user))
      emit('updateUser')
    }
  })

}
</script>
