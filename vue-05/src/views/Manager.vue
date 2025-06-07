<template>
<!--  头部区域开始-->
  <div>
    <div style="height: 60px; background-color: #f6f1f1; display: flex">
      <div>中原工学院</div>
      <div style="flex: 1"></div>
      <div style="width: fit-content;padding-right: 20px; display: flex; align-items: center">
        <el-dropdown>
          <div style="display: flex; align-items: center">
            <img v-if = "data.user?.avatar" style="width: 40px;height: 40px" :src="data.user?.avatar" alt="">
            <img v-else style="width: 40px;height: 40px" src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" alt="">
            <span style="font-size: 15px">{{data.user?.name}}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="router.push('/manager/person')">个人中心</el-dropdown-item>
              <el-dropdown-item @click="logout">修改密码</el-dropdown-item>
              <el-dropdown-item @click="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
    </div>

    </div>
      <!--    头部区域结束-->

      <!--    下方区域开始-->
    <div style="display: flex">
      <!--      菜单区域开始-->
      <div style="width: 240px;">
        <el-menu router :default-openeds="['1']" :default-active="router.currentRoute.value.path" style="min-height: calc(100vh - 60px)">
          <el-menu-item index="/manager/children">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-sub-menu index="1">
            <template #title>
              <el-icon><location /></el-icon>
              <span>数据</span>
            </template>
            <el-menu-item index="/manager/home">二级菜单</el-menu-item>
            <el-sub-menu index="1-4">
              <template #title>人员信息</template>
              <el-menu-item index="/manager/children">管理员信息</el-menu-item>
              <el-menu-item index="/manager/user">普通用户信息</el-menu-item>
            </el-sub-menu>
            <el-sub-menu index="1-5">
              <template #title>提交申请</template>
              <el-menu-item index="/manager/apply">请假申请</el-menu-item>
            </el-sub-menu>
          </el-sub-menu>

        </el-menu>
      </div>
      <!--      菜单区域结束-->

      <!--      数据渲染区域开始-->
      <div style="flex: 1;width: 0; padding: 10px;background-color: #e5e5f8">
        <div>
          <RouterView />
        </div>
      </div>
      <!--      数据渲染区域结束-->
    </div>
      <!--    下方区域结束-->
    </div>



</template>


<script setup>

import router from "@/router/index.js";
import {reactive} from "vue";


const data = reactive({
  user : JSON.parse(localStorage.getItem("code_user") || '{}')
})


const logout =() =>{
  localStorage.removeItem('code_user')
  location.href = ('/')
}
</script>

