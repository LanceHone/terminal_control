<template>
  <div class="app-container">

    <el-dialog title="系统更新" :visible.sync="upload_popup">

      <el-upload ref="upload" :limit="1" :action="upload.url" :headers="upload.headers" :file-list="upload.fileList" :data="{'md5':md5}" accept=".zip"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" :on-error="upload.isUploading=false">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      </el-upload>
      <el-input v-model="md5" autocomplete="off" style="margin-top: 30px;"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">取 消</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" :loading="upload.isUploading"
          @click="submitUpload">上传</el-button>
      </div>

    </el-dialog>

    <el-row>
      <el-col :sm="24" class="card-box" style="padding-left: 20px">
        <div style="display:flex;justify-content: space-between;align-items: center;width:100%">
          <h2>安全管理系统测试环境</h2>
          <p>
            <el-button size="mini" icon="el-icon-upload" plain @click="upload_popup = true">产品升级</el-button>
          </p>
        </div>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span><i class="el-icon-time"></i> 系统时间</span>
            <el-button style="float: right; padding: 3px 0" @click="onSyncChange" type="text">开启同步</el-button>
            <el-button style="float: right; padding: 3px 50px;margin: 0 30px" @click="onSetTz"
              type="text">使用中国时区</el-button>

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
                    <div class="cell">自动同步</div>
                  </th>
                </tr>
              </thead>
              <tbody v-if="server.clock">
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ server.clock["Local time"] }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ "Tue 2025-03-18 12:10:51 CST" }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ "Tue 2025-03-18 12:10:51" }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ server.clock["Time zone"] }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ server.clock["NTP service"] }}</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span><i class="el-icon-cpu"></i> CPU</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">属性</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">值</div>
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">核心数</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.cpu">
                      {{ server.cpu.cpuNum }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">用户使用率</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.cpu">
                      {{ server.cpu.used }}%
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">系统使用率</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.cpu">
                      {{ server.cpu.sys }}%
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">当前空闲率</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.cpu">
                      {{ server.cpu.free }}%
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="12" class="card-box">
        <el-card>
          <div slot="header">
            <span><i class="el-icon-tickets"></i> 内存</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">属性</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">内存</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">JVM</div>
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">总内存</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.mem">
                      {{ server.mem.total }}G
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.total }}M
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">已用内存</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.mem">
                      {{ server.mem.used }}G
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.used }}M
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">剩余内存</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.mem">
                      {{ server.mem.free }}G
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.free }}M
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">使用率</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.mem" :class="{ 'text-danger': server.mem.usage > 80 }">
                      {{ server.mem.usage }}%
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm" :class="{ 'text-danger': server.jvm.usage > 80 }">
                      {{ server.jvm.usage }}%
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span><i class="el-icon-receiving"></i> 磁盘状态</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="el-table__cell el-table__cell is-leaf">
                    <div class="cell">盘符路径</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">文件系统</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">盘符类型</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">总大小</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">可用大小</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">已用大小</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">已用百分比</div>
                  </th>
                </tr>
              </thead>
              <tbody v-if="server.sysFiles">
                <tr v-for="(sysFile, index) in server.sysFiles" :key="index">
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.dirName }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.sysTypeName }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.typeName }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.total }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.free }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ sysFile.used }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" :class="{ 'text-danger': sysFile.usage > 80 }">
                      {{ sysFile.usage }}%
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span><i class="el-icon-odometer"></i> 网口接口</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <thead>
                <tr>
                  <th class="el-table__cell el-table__cell is-leaf">
                    <div class="cell">名称</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">总发送</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">总接受</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">上行速度</div>
                  </th>
                  <th class="el-table__cell is-leaf">
                    <div class="cell">下行速度</div>
                  </th>
                </tr>
              </thead>
              <tbody v-if="server.networkIFs">
                <tr v-for="(net, index) in server.networkIFs" :key="index">
                  <td class="el-table__cell is-leaf">
                    <el-tooltip class="item" effect="dark" :content="net.displayName" placement="top">
                      <div class="cell">{{ net.name }}</div>
                    </el-tooltip>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ net.bytesSent }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ net.bytesRecv }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ net.upSpeed }}</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">{{ net.downSpeed }}</div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span><i class="el-icon-monitor"></i> 服务器信息</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%">
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">服务器名称</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.computerName }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">操作系统</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.osName }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">服务器IP</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.computerIp }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">系统架构</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.osArch }}
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>

      <el-col :span="24" class="card-box">
        <el-card>
          <div slot="header">
            <span><i class="el-icon-coffee-cup"></i> Java虚拟机信息</span>
          </div>
          <div class="el-table el-table--enable-row-hover el-table--medium">
            <table cellspacing="0" style="width: 100%; table-layout: fixed">
              <tbody>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">Java名称</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.name }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">Java版本</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.version }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">启动时间</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.startTime }}
                    </div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell">运行时长</div>
                  </td>
                  <td class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.runTime }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1" class="el-table__cell is-leaf">
                    <div class="cell">安装路径</div>
                  </td>
                  <td colspan="3" class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.home }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1" class="el-table__cell is-leaf">
                    <div class="cell">项目路径</div>
                  </td>
                  <td colspan="3" class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.sys">
                      {{ server.sys.userDir }}
                    </div>
                  </td>
                </tr>
                <tr>
                  <td colspan="1" class="el-table__cell is-leaf">
                    <div class="cell">运行参数</div>
                  </td>
                  <td colspan="3" class="el-table__cell is-leaf">
                    <div class="cell" v-if="server.jvm">
                      {{ server.jvm.inputArgs }}
                    </div>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getServer, timeSync, setTimezone } from "@/api/monitor/server";
import { getToken } from "@/utils/auth";

export default {
  name: "Server",
  data() {
    return {
      // 服务器信息
      server: [],
      intervalID: null,
      loading: false,

      upload_popup: false,
      md5: '',
      upload: {
        // 是否禁用上传
        isUploading: false,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/monitor/server/upload",
        // 上传的文件列表
        fileList: []
      },
    };
  },
  created() {
    this.getList();
    this.intervalID = setInterval(this.getList, 3000);
    this.openLoading();
  },
  destroyed() {
    this.intervalID && clearInterval(this.intervalID);
  },
  methods: {
    /** 查询服务器信息 */
    getList() {
      getServer().then((response) => {
        this.server = response.data;
        this.$modal.closeLoading();
      });
    },
    // 打开加载层
    openLoading() {
      this.$modal.loading("正在加载服务监控数据，请稍候！");
    },
    onSyncChange() {
      this.$modal.confirm("设置打开NTP同步？", () => {
        this.$modal.loading("正在打开同步，请稍候！");
        timeSync().then((res) => {
          this.$modal.closeLoading();
          this.$modal.msgSuccess("设置成功");
        });
      });
    },
    onSetTz() {
      this.$modal.confirm("设置时区为中国？", () => {
        this.$modal.loading("正在设置时区，请稍候！");
        setTimeZone().then((res) => {
          this.$modal.closeLoading();
          this.$modal.msgSuccess("设置成功");
        });
      });
    },
    // 文件提交处理
    submitUpload() {
      if (this.md5.length == 0) {
        this.$modal.alert("校验码不能为空");
      } else
        this.$refs.upload.submit();
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      if (response.code != 200) {
        this.$modal.msgError(response.msg);
      } else {
        this.form.filePath = response.url;
        this.msgSuccess(response.msg);
      }
      this.upload.isUploading = false;
    },

    cancel() {
      this.upload_popup = false
      this.upload.isUploading = false
    }
  },
};
</script>
