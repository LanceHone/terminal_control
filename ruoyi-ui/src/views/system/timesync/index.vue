<template>

    <div class="timesync">
        <el-card>
          <div slot="header">
            <span><i class="el-icon-time"></i> 系统时间</span>
          </div>

          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="el-table__cell el-table__cell is-leaf">
                    <div class="cell">本地时间</div>
                  </th>
                  <th class="el-table__cell el-table__cell is-leaf">
                    <div class="cell">UTC时间</div>
                  </th>
                  <th class="el-table__cell el-table__cell is-leaf">
                    <div class="cell">RTC时间</div>
                  </th>
                  <th class="el-table__cell el-table__cell is-leaf">
                    <div class="cell">时区</div>
                  </th>
                  <th class="el-table__cell el-table__cell is-leaf">
                    <div class="cell">NTP可用</div>
                  </th>
                </tr>
              </thead>
              <tbody v-if="clock">
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ clock["Local time"] }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ clock["Universal time"] }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ clock["RTC time"] }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ clock["Time zone"] }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ clock["NTP enabled"] || clock['NTP service'] }}</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
        <el-card class="card" shadow="hover">
            <div class="card-header" style="margin-bottom: 20px;">时间同步</div>
            <div class="card-body" v-for="(item, index) in dict.type.ntp_server" :key="index">
                <el-form label-width="120px" ref="formRef" size="small">
                    <el-form-item label="时间服务器">
                        <el-input v-model="item.value" placeholder="服务器地址"></el-input>
                    </el-form-item>
                </el-form>
                <div class="btns" style="margin-top: 20px;">
                    <el-button type="primary" @click="submit(item)">保存并同步</el-button>
                </div>
            </div>
        </el-card>
    </div>
</template>

<script>
import { timeSync,getClockInfo } from "@/api/monitor/server";
export default {
    name: 'TimeSync',
    data() {
        return {
            clock: null,
        }
    },
    mounted() {
        this.getClock()
    },
    dicts: ['ntp_server'],
    methods: {
        getClock() {
            this.$modal.loading("正在获取时间信息，请稍候");
            getClockInfo().then(res => {
              this.clock = res
            }).finally(() => {
                this.$modal.closeLoading();
            })
        },
        submit(item) {
            if (item.value) {
                item.raw.dictValue = item.value
                this.$modal.loading("正在设置NTP同步，请稍候");
                timeSync(item.raw).then(() => {
                    this.$modal.closeLoading();
                    this.$modal.msgSuccess("设置成功");
                }).finally(() => {
                    this.$modal.closeLoading();
                })
            } else {
                this.$message.error('请检查输入')
            }
        }
    }
}
</script>