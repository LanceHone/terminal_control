<template>
  <div class="app-container">
    <el-table :data="options" >
      <el-table-column label="控制类型" align="center" prop="name">
      </el-table-column>
      <el-table-column label="启用" align="center" prop="enable">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.enable"
            @change="(value) => handleStatusChange(scope.row, value)"
          > 
          </el-switch>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { setting_flood_status,set_flood } from '../../../api/access/network/tcp';


export default {
  name: "Ipctl",
  data() {
    return {
      options : [
      ]
    }
  },
  mounted(){
    this.refresh()
  },
  methods: {
    refresh(){
      setting_flood_status().then(res => {
        this.options = res.data
      })
    },
    handleStatusChange(row,value) {
      let key = row.name
      switch (row.name) {
        case '抗SYN_FLOOD攻击':
          key = 'syn'
          break;
        case '抗UDP_FLOOD攻击':
          key = 'udp'
          break;
        case '抗ICMP_FLOOD攻击':
          key = 'icmp'
          break;
        case '抗PING_OF_DEATH攻击':
          key = 'ping'
          break;
        default:
          key = 'net'
          break;
      }
      set_flood(key,value).then(res => {
        this.$message({
          message: "设置成功",
          type: 'success'
        })
        this.refresh()
      })
    }
  }
}
</script>
