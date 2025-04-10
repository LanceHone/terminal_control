<template>
  <div class="app-container">
    <el-table :data="options" >
      <el-table-column label="应用类型" align="center" prop="name">
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
import { http,telnet,ftp,status } from '../../../api/access/network/tcp';


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
      status().then(res => {
        this.options = res.data
      })
    },
    handleStatusChange(row,value) {
      if (row.name == 'http') {
        http(value).then(res => {
          this.refresh()
        })
      }
      if (row.name == 'telnet') {
        telnet(value).then(res => {
          this.refresh()
        })
      }
      if (row.name == 'ftp') {
        ftp(value).then(res => {
          this.refresh()
        })
      }
    }
  }
}
</script>
