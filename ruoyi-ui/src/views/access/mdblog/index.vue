<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="时间" prop="ts">
        <el-date-picker clearable
          v-model="queryParams.ts"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
          placeholder="请选择时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="tid" prop="tid">
        <el-input
          v-model="queryParams.tid"
          placeholder="请输入tid"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="pid" prop="pid">
        <el-input
          v-model="queryParams.pid"
          placeholder="请输入pid"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="长度" prop="len">
        <el-input
          v-model="queryParams.len"
          placeholder="请输入长度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="设备id" prop="uid">
        <el-input
          v-model="queryParams.uid"
          placeholder="请输入设备id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="功能码" prop="func">
        <el-input
          v-model="queryParams.func"
          placeholder="请输入功能码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作对象" prop="addr">
        <el-input
          v-model="queryParams.addr"
          placeholder="请输入操作对象"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="操作值" prop="number">
        <el-input
          v-model="queryParams.number"
          placeholder="请输入操作值"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <!-- <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['access:logs:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['access:logs:edit']"
        >修改</el-button>
      </el-col> -->
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
          v-hasPermi="['access:logs:remove']"
        >清空</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['access:logs:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="logsList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="" align="center" prop="id" /> -->
      <el-table-column label="时间" align="center" prop="ts" />
      <el-table-column label="动作" align="center" prop="type" />
      <el-table-column label="tid" align="center" prop="tid" />
      <el-table-column label="pid" align="center" prop="pid" />
      <el-table-column label="长度" align="center" prop="len" />
      <el-table-column label="设备id" align="center" prop="uid" />
      <el-table-column label="功能码" align="center" prop="func" />
      <el-table-column label="操作对象" align="center" prop="addr" />
      <el-table-column label="操作值" align="center" prop="number" />
      <!-- <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['access:logs:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['access:logs:remove']"
          >删除</el-button>
        </template>
      </el-table-column> -->
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改modbus控制日志对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="时间" prop="ts">
          <el-input v-model="form.ts" placeholder="请输入时间" />
        </el-form-item>
        <el-form-item label="tid" prop="tid">
          <el-input v-model="form.tid" placeholder="请输入tid" />
        </el-form-item>
        <el-form-item label="pid" prop="pid">
          <el-input v-model="form.pid" placeholder="请输入pid" />
        </el-form-item>
        <el-form-item label="长度" prop="len">
          <el-input v-model="form.len" placeholder="请输入长度" />
        </el-form-item>
        <el-form-item label="设备id" prop="uid">
          <el-input v-model="form.uid" placeholder="请输入设备id" />
        </el-form-item>
        <el-form-item label="功能码" prop="func">
          <el-input v-model="form.func" placeholder="请输入功能码" />
        </el-form-item>
        <el-form-item label="操作对象" prop="addr">
          <el-input v-model="form.addr" placeholder="请输入操作对象" />
        </el-form-item>
        <el-form-item label="操作值" prop="number">
          <el-input v-model="form.number" placeholder="请输入操作值" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listLogs, getLogs, delLogs, addLogs, updateLogs,clear } from "@/api/access/indu/logs";

export default {
  name: "Logs",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // modbus控制日志表格数据
      logsList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ts: null,
        type: null,
        tid: null,
        pid: null,
        len: null,
        uid: null,
        func: null,
        addr: null,
        number: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询modbus控制日志列表 */
    getList() {
      this.loading = true;
      listLogs(this.queryParams).then(response => {
        this.logsList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        ts: null,
        type: null,
        tid: null,
        pid: null,
        len: null,
        uid: null,
        func: null,
        addr: null,
        number: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加modbus控制日志";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLogs(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改modbus控制日志";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLogs(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLogs(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认清空？').then(function() {
        return clear();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('access/logs/mdb/export', {
        ...this.queryParams
      }, `logs_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
