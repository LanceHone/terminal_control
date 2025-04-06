<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="服务名称" prop="serverName">
        <el-input
            v-model="queryParams.serverName"
            placeholder="请输入服务名称"
            clearable
            @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="源IP" prop="sourceIp">
        <el-input
          v-model="queryParams.sourceIp"
          placeholder="请输入源IP"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="源端口" prop="sourcePort">
        <el-input
          v-model="queryParams.sourcePort"
          placeholder="请输入源端口"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目的IP" prop="targetIp">
        <el-input
          v-model="queryParams.targetIp"
          placeholder="请输入目的IP"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目的端口" prop="targetPort">
        <el-input
          v-model="queryParams.targetPort"
          placeholder="请输入目的端口"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="目录" prop="directory">
        <el-input
          v-model="queryParams.directory"
          placeholder="请输入目录"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="状态" clearable>
          <el-option
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
            type="primary"
            plain
            icon="el-icon-plus"
            size="mini"
            @click="handleAdd"
            v-hasPermi="['access:appl:ftp:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
            type="info"
            plain
            icon="el-icon-sort"
            size="mini"
            @click="toggleExpandAll"
        >展开/折叠
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
        v-if="refreshTable"
        v-loading="loading"
        :data="ftpList"
        row-key="id"
        :default-expand-all="isExpandAll"
    >
      <el-table-column prop="serverName" label="服务名称" width="200"></el-table-column>
      <el-table-column prop="sourceIp" label="源IP" width="100"></el-table-column>
      <el-table-column prop="sourcePort" label="源端口" width="100"></el-table-column>
      <el-table-column prop="targetIp" label="目的IP" width="100"></el-table-column>
      <el-table-column prop="targetPort" label="目的端口" width="100"></el-table-column>
      <el-table-column prop="directory" label="目录" width="200"></el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleUpdate(scope.row)"
              v-hasPermi="['access:appl:ftp:edit']"
          >修改
          </el-button>
          <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDelete(scope.row)"
              v-hasPermi="['access:appl:ftp:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改FTP访问控制对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="服务名称" prop="serverName">
              <el-input v-model="form.serverName" placeholder="请输入服务名称"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="目录" prop="directory">
              <DirectorySelector v-model="form.directory"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="源IP" prop="sourceIp">
              <el-input v-model="form.sourceIp" placeholder="请输入源IP"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="源端口" prop="sourcePort">
              <el-input-number v-model="form.sourcePort" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="目标IP" prop="targetIp">
              <el-input v-model="form.targetIp"  placeholder="请输入目标IP"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="目标端口" prop="targetPort">
              <el-input-number v-model="form.targetPort" controls-position="right" :min="1"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio
                    v-for="dict in dict.type.sys_normal_disable"
                    :key="dict.value"
                    :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {addFtp, delFtp, getFtp, listFtp, updateFtp} from "@/api/access/appl/ftp";
import DirectorySelector from '@/components/DirectorySelector/index.vue'
import {parseTime} from "../../../../utils/ruoyi";

export default {
  name: "Ftp",
  dicts: ['sys_normal_disable'],
  components: {DirectorySelector},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格数据
      ftpList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      // 查询参数
      queryParams: {
        serverName: undefined,
        directory: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        serverName: [
          {required: true, message: "服务名称不能为空", trigger: "blur"}
        ],
        directory: [
          {required: true, message: "目录不能为空", trigger: "change"}
        ],
        status: [
          {required: true, message: "状态不能为空", trigger: "blur"}
        ]
      },
      // 目录选择器
      directorySelectorVisible: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    parseTime,
    /** 查询FTP访问控制列表 */
    getList() {
      this.loading = true;
      listFtp(this.queryParams).then(response => {
        this.ftpList = response.data;
        this.loading = false;
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增FTP访问控制";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getFtp(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改FTP访问控制";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateFtp(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFtp(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      this.$modal.confirm('是否确认删除名称为"' + row.serverName + '"的数据项？').then(function () {
        return delFtp(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: undefined,
        serverName: undefined,
        directory: undefined,
        sourceIp: undefined,
        sourcePort: undefined,
        targetIp: undefined,
        targetPort: undefined,
        status: "0"
      };
      this.resetForm("form");
    },
    /** 打开目录选择器 */
    openDirectorySelector() {
      this.directorySelectorVisible = true;
    },
    /** 目录选择器选择事件 */
    onDirectorySelect(selectedNode) {
      this.form.directory = selectedNode.path;
    },
    /** 重置表单并取消 */
    cancel() {
      this.reset();
      this.open = false;
    }
  }
};
</script>
