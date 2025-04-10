<template>
  <div class="app-container">
    <!-- <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="源IP" prop="sourceIp">
        <el-input
          v-model="queryParams.sourceIp"
          placeholder="请输入源IP"
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
    </el-form> -->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['access:tcp:add']"
        >新增</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-sort"
          size="mini"
          @click="toggleExpandAll"
        >展开/折叠</el-button>
      </el-col> -->
      <!-- <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar> -->
    </el-row>

    <el-table
      v-if="refreshTable"
      v-loading="loading"
      :data="tcpList"
      row-key="id"
      :default-expand-all="isExpandAll"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column prop="sourceIp" label="源IP" ></el-table-column>
      <el-table-column prop="targetIp" label="目的IP"></el-table-column>
      <el-table-column prop="sourcePort" label="源端口"></el-table-column>
      <el-table-column prop="targetPort" label="目的端口"></el-table-column>
      <el-table-column prop="status" label="状态">
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.status === '0'"
            @change="(value) => handleStatusChange(scope.row, value)"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="正常"
            inactive-text="禁用"
          />
<!--          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>-->
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建时间" align="center" prop="createTime" width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['access:tcp:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['access:tcp:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改网络层访问控制对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="源IP" prop="sourceIp">
              <el-input v-model="form.sourceIp" placeholder="请输入源IP" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="目的IP" prop="targetIp">
              <el-input v-model="form.targetIp" placeholder="请输入目的IP" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="源端口" prop="sourcePort">
              <el-input-number v-model="form.sourcePort" controls-position="right" :min="1" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="目的端口" prop="targetPort">
              <el-input-number v-model="form.targetPort" controls-position="right" :min="1" />
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
                >{{dict.label}}</el-radio>
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
import { listTcp, getTcp, delTcp, addTcp, updateTcp, updateStatus } from "@/api/access/network/tcp";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Tcp",
  dicts: ['sys_normal_disable'],
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 表格树数据
      tcpList: [],
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
        sourceIp: undefined,
        targetIp: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sourceIp: [
          { required: true, message: "源IP不能为空", trigger: "blur" }
        ],
        targetIp: [
          { required: true, message: "目的IP不能为空", trigger: "blur" }
        ],
        sourcePort: [
          { required: true, message: "源端口不能为空", trigger: "blur" }
        ],
        targetPort: [
          { required: true, message: "目的端口不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询网络层访问控制列表 */
    getList() {
      this.loading = true;
      listTcp(this.queryParams).then(response => {
        this.tcpList = this.handleTree(response.data, "id");
        this.loading = false;
      });
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增网络层访问控制";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      getTcp(row.id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改网络层访问控制";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateTcp(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addTcp(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除名称为"' + row.sourceIp + '->' + row.targetIp + '"的数据项？').then(function() {
        return delTcp(row.id);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
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
        sourceIp: undefined,
        targetIp: undefined,
        sourcePort: undefined,
        targetPort: undefined,
        status: "0"
      };
      this.resetForm("form");
    },
    /** 重置表单并取消 */
    cancel() {
      this.reset();
      this.open = false;
    },
    /** 修改状态 */
    handleStatusChange(row, value) {
      const status = value ? '0' : '1';
      // 调用接口更新状态
      updateStatus({ id: row.id, status }).then(() => {
        this.$message.success("状态更新成功");
        this.getList(); // 刷新列表
      }).catch(err => {
        this.$message.error("状态更新失败");
      });
    },
  }
};
</script>
