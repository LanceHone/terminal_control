<template>
  <div class="app-container">
    <!-- 数据库监控页面 -->
    <el-form :model="params" ref="queryForm" size="small" :inline="true">
      <!-- 访问控制开关 -->
      <el-form-item label="访问控制开关" prop="status">
        <el-switch
          v-model="switchStatus"
          active-color="#13ce66"
          inactive-color="#ff4949"
          active-text="正常"
          inactive-text="禁用"
          @change="handleStatusChange"
        />
      </el-form-item>
    </el-form>
  </div>

</template>

<script>
import { getConfigInfoByKey, updateConfig } from '@/api/system/config'

export default {
  name: 'AccessSettings',
  data() {
    return {
      params: {
        configKey: 'access.switch',
        status: '1' // 初始化状态（"0"正常，"1"禁用）
      }
    }
  },
  computed: {
    // 将 switch 的状态与 params.status 关联
    switchStatus: {
      get() {
        return !this.params.status
        // return this.params.status === '0'
      },
      set(value) {
        this.params.status = !value
        // this.params.status = value ? '0' : '1'
      }
    }
  },
  methods: {
    // 处理开关状态改变
    handleStatusChange(status) {
      // 这里可以添加接口调用逻辑
      // 更新服务器状态
      console.log('当前开关状态:', status ? '正常' : '禁用')
      this.updateAccessStatus(this.params.status)
    },
    // 更新访问控制状态的方法
    updateAccessStatus(status) {
      console.log('updateAccessStatus')
      // 调用接口更新状态
      const payload = {
        configId:100,//Fixme
        configName:"访问控制开关",
        configKey: 'access.switch',
        configValue: status?'0':'1'
      }

      // 模拟接口请求
      // 实际项目中替换为 axios 或其他请求方式
      updateConfig(payload).then(response => {
        // this.params.status = response.msg
      })
    },
    // 加载数据
    loadData() {
      // 模拟接口请求
      // 实际项目中替换为 axios 或其他请求方式
      getConfigInfoByKey('access.switch').then(response => {
        console.log('getConfigInfoByKey')
        this.params.status = response.data.configValue=='0'
      });
    }
  },
  created() {
    // 页面加载时初始化数据
    this.loadData()
  }
}
</script>

<style scoped>
.access-settings {
  margin-bottom: 20px;
}

.other-content {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  min-height: 300px;
}
</style>
