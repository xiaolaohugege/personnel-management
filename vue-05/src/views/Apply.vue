<template>
  <div>
    <div class="card" style="margin-bottom: 5px">
      <el-input clearable @clear="load" style="width: 260px; margin-right: 5px" v-model="data.title" placeholder="标题" :prefix-icon="Search"></el-input>
      <el-button type="primary" @click="load">查 询</el-button>
      <el-button @click="reset">重 置</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
      <el-button type="primary" @click="handleAdd">新 增</el-button>
      <el-button type="danger" @click="deleteBatch">批量删除</el-button>
<!--      <el-button type="success">批量导入</el-button>-->
      <el-button type="info">批量导出</el-button>
    </div>
    <div class="card" style="margin-bottom: 5px">
    <el-table :data="data.tableData" style="width: 100%" @selection-change="handleSelectionChange"
              :header-cell-style="{ color: '#333', backgroundColor: '#eaf4ff' }">
      <el-table-column prop="title" label="请假标题" />
      <el-table-column prop="content" label="请假说明" />
      <el-table-column prop="username" label="请假人" />
      <el-table-column prop="time" label="提交时间" />
      <el-table-column prop="status" label="审核状态">
        <template v-slot="scope">
          <el-tag type="warning" v-if="scope.row.status == 1">审核通过</el-tag>
          <el-tag type="success" v-if="scope.row.status == 0">审核拒绝</el-tag>
          <el-tag type="danger" v-if="scope.row.status == 2">待审核</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="reason" label="审核说明" />
      <el-table-column label="操作" width="100">
        <template #default="scope" v-if="data.user.role === 'USER'">
          <el-button :disabled="scope.row.status !== 2" type="primary" icon="Edit" circle @click="handleEdit(scope.row)"></el-button>
          <el-button :disabled="scope.row.status !== 2" type="danger" icon="Delete" circle @click="del(scope.row.id)"></el-button>
        </template>
        <template #default="scope" v-if="data.user.role === 'ADMIN'">
          <el-button :disabled="scope.row.status !== 2" type="primary" @click="handleEdit(scope.row)">审核</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
    <div class="card">
      <el-pagination
          v-model:current-page="data.pageNum"
          v-model:page-size="data.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :page-sizes="[5, 10, 20]"
          :total="data.total"
          @current-change="load"
          @size-change="load"
      />
    </div>

    <el-dialog title="请假申请" v-model="data.formVisible" width="30%" destroy-on-close>
      <el-form ref="formRef" :model="data.form" :rules="data.rules" label-width="80px" style="padding: 20px 30px 10px 0">
        <el-form-item prop="title" label="请假标题" v-if="data.user.role === 'USER'">
          <el-input v-model="data.form.title" autocomplete="off" placeholder="请输入请假标题" />
        </el-form-item>
        <el-form-item prop="content" label="请假说明" v-if="data.user.role === 'USER'">
          <el-input type="textarea" :rows="3" v-model="data.form.content" autocomplete="off" placeholder="请输入请假说明" />
        </el-form-item>
        <el-form-item prop="status" label="审核状态" v-if="data.user.role === 'ADMIN'">
          <el-radio-group v-model="data.form.status">
            <el-radio-button label="待审核" :value="2" />
            <el-radio-button label="审核通过" :value="1" />
            <el-radio-button label="审核拒绝" :value="0" />
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="reason" label="审核说明" v-if="data.user.role === 'ADMIN' && data.form.status === 0">
          <el-input v-model="data.form.reason" autocomplete="off" placeholder="请输入拒绝说明" />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="data.formVisible = false">取 消</el-button>
          <el-button type="primary" @click="save">保 存</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import {Search} from "@element-plus/icons-vue";
import request from "@/utils/request.js";
import {ElMessage, ElMessageBox} from "element-plus";

const data = reactive({
  user : JSON.parse(localStorage.getItem("code_user") || '{}'),
  title: null,
  pageNum: 1,
  pageSize: 5,
  total: 0,
  tableData: [],
  formVisible:false,
  form:{},
  rows: [],
  rules: {
    title: [
      { required: true, message: '请填写请假标题', trigger: 'blur' }
    ],
    content: [
      { required: true, message: '请填写请假说明', trigger: 'blur' }
    ]
  },
})




const formRef = ref()

const load = () => {
  request.get('/apply/selectPage', {
    params: {
      pageNum: data.pageNum,
      pageSize: data.pageSize,
      title: data.title,
    }
  }).then(res => {
    if (res.code === '200') {
      data.tableData = res.data
      data.total = res.data.length
    } else {
      ElMessage.error(res.msg)
    }
  })
}
load()

const reset = () => {
  data.title = null
  load()
}

const handleAdd = () =>{
  data.formVisible = true
  data.form = {}
}

const add = () =>{
  console.log('提交的数据：', data.form);
  request.post("/apply/addApply", data.form).then(res=>{
        if (res.code === "200"){
          data.formVisible = false
          ElMessage.success(res.msg)
          load()
        }else {
          ElMessage.error(res.msg)
        }
      }
  )
}

const update = () => {
  // formRef 是表单的引用
  formRef.value.validate((valid) => {
    if (valid) {   // 验证通过的情况下
      request.put('/apply/update', data.form).then(res => {
        if (res.code === '200') {
          data.formVisible = false
          ElMessage.success('修改成功')
          load()
        } else {
          ElMessage.error(res.msg)
        }
      })
    }
  })
}

const save = () => {
  data.form.id ? update() : add()
}

const handleEdit = (row) => {
  data.form = JSON.parse(JSON.stringify(row))  // 深度拷贝数据
  data.formVisible = true
}

const del = (id) => {
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/apply/deleteApplyById/' + id).then(res => {
      if (res.code === '200') {
        ElMessage.success('删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

const handleSelectionChange = (rows) => {  // rows 就是实际选择的数组
  data.rows = rows
}

const deleteBatch = () => {
  if (data.rows.length === 0) {
    ElMessage.warning('请选择数据')
    return
  }
  ElMessageBox.confirm('删除后无法恢复，您确认删除吗？', '删除确认', { type: 'warning' }).then(res => {
    request.delete('/apply/deleteBatch', { data: data.rows }).then(res => {
      if (res.code === '200') {
        ElMessage.success('批量删除成功')
        load()
      } else {
        ElMessage.error(res.msg)
      }
    })
  }).catch(err => {})
}

//
// import axios from 'axios';
// axios.get('http://localhost:8082/admin/getListAdmin').then(res => {
//   console.log(res)
// })

// request.get('/admin/getListAdmin').then(res => {
//   console.log(res)
// })

</script>