<template>
  <div class="app-container">
    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="HTTP 控制" name="http">
        <http-control
            @update="updateControlValue"
            :check="checkControl"
            :control="controlValueObj"
            ref="httpControl"
        />
      </el-tab-pane>
      <el-tab-pane label="FTP 控制" name="ftp">
        <ftp-control
            @update="updateControlValue"
            :check="checkControl"
            :control="controlValueObj"
            ref="ftpControl"
        />
      </el-tab-pane>
      <el-tab-pane label="TELNET 控制" name="telnet">
        <telnet-control
            @update="updateControlValue"
            :check="checkControl"
            :control="controlValueObj"
            ref="telnetControl"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import HttpControl from './http/index.vue';
import FtpControl from './ftp/index.vue';
import TelnetControl from './telnet/index.vue';

export default {
  name: "ApplicationAccessControl",
  components: {
    HttpControl,
    FtpControl,
    TelnetControl
  },
  data() {
    return {
      activeTab: "http", // 默认选中 HTTP 控制
      controlValueObj: {}, // 控制对象
    };
  },
  methods: {
    handleTabClick(tab, event) {
      // 处理 Tab 切换事件
      console.log(`当前选中的 Tab 是: ${tab.name}`);
    },
    updateControlValue(value) {
      // 更新控制对象
      this.controlValueObj = value;
    },
    checkControl(control) {
      // 检查控制逻辑
      return true; // 示例逻辑，实际应根据需求实现
    }
  }
};
</script>
