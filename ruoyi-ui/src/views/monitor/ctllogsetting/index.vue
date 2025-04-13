<template>
  <div class="app-container">


    <el-dialog :title="currentRow.label" :visible.sync="show">
      <el-input v-model="value" autocomplete="off" style="margin-top: 30px;"></el-input>
      <div slot="footer" class="dialog-footer">
        <el-button @click="show = !show">取 消</el-button>
        <el-button @click="confirm">确定</el-button>
      </div>
    </el-dialog>

    <el-table :data="dict.type.sys_settings" style="width: 100%">
      <el-table-column prop="label" label="设置项目"></el-table-column>
      <el-table-column label="阈值" style="width: 100%;">
        <template slot-scope="scope">
          <div @click="click(scope.row)" autocomplete="off">{{ scope.row.value }}</div>
        </template>
      </el-table-column>
    </el-table>

  </div>
</template>

<script>
import { list, delLogininfor, cleanLogininfor, unlockLogininfor } from "@/api/monitor/logininfor";
import {threshold} from "@/api/access/logs";

export default {
  name: "Logininfor",
  dicts: ['sys_settings'],
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
      // 选择用户名
      selectName: "",
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      list: [],
      // 日期范围
      dateRange: [],
      // 默认排序
      defaultSort: { prop: 'loginTime', order: 'descending' },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        ipaddr: undefined,
        userName: undefined,
        status: undefined
      },
      value: '',
      show: false,
      tableData: [],
      currentRow: {},
    };
  },
  created() {
    this.tableData = this.dict.type.sys_settings
  },
  methods: {
    confirm(row) {
      this.$modal.confirm('是否确认设置项"' + this.currentRow.label + '"的数据项？').then(() =>{
        return threshold(this.currentRow);
      }).then((res) => {
        this.tableData = res.data
        this.$modal.msgSuccess("设置成功");
        this.show = false;
      }).catch(() => { });
    },
    click(row) {
      this.currentRow = row;
      this.show = true;
    },
    /** 查询登录日志列表 */
    getList() {
      this.loading = true;
      list(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.list = response.rows;
        this.total = response.total;
        this.loading = false;
      }
      );
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.queryParams.pageNum = 1;
      this.$refs.tables.sort(this.defaultSort.prop, this.defaultSort.order)
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.infoId)
      this.single = selection.length != 1
      this.multiple = !selection.length
      this.selectName = selection.map(item => item.userName);
    },
    /** 排序触发事件 */
    handleSortChange(column, prop, order) {
      this.queryParams.orderByColumn = column.prop;
      this.queryParams.isAsc = column.order;
      this.getList();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const infoIds = row.infoId || this.ids;
      this.$modal.confirm('导出并清空').then(function () {
        console.log("todo");
      }).then(() => {
      }).catch(() => { });
    },
    /** 清空按钮操作 */
    handleClean() {
      this.$modal.confirm('是否确认清空所有登录日志数据项？').then(function () {
        return cleanLogininfor();
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("清空成功");
      }).catch(() => { });
    },
    /** 解锁按钮操作 */
    handleUnlock() {
      const username = this.selectName;
      this.$modal.confirm('是否确认解锁用户"' + username + '"数据项?').then(function () {
        return unlockLogininfor(username);
      }).then(() => {
        this.$modal.msgSuccess("用户" + username + "解锁成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('monitor/logininfor/export', {
        ...this.queryParams
      }, `logininfor_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
