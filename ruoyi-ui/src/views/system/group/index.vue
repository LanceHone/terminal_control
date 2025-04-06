<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" type="primary" @click="handleCreate">新增组别</el-button>
    </div>
    <el-table
      v-loading="loading"
      :data="list"
      border
      fit
      highlight-current-row
    >
      <el-table-column prop="code" label="组别代码" align="center" />
      <el-table-column prop="name" label="组别名称" align="center" />
      <el-table-column label="操作" align="center">
        <template #default="scope">
          <el-button @click="handleUpdate(scope.row)" type="primary" size="small">编辑</el-button>
          <el-button @click="handleDelete(scope.row)" type="danger" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 对话框（新增/编辑） -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogFormVisible">
      <el-form ref="form" :model="form" :rules="rules" label-position="left" label-width="100px">
        <el-form-item label="组别代码" prop="code">
          <el-input v-model="form.code" placeholder="请输入组别代码"/>
        </el-form-item>
        <el-form-item label="组别名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入组别名称"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="dialogFormVisible = false">取 消</el-button>
      </div>
<!--      <template #footer>-->
<!--        <span class="dialog-footer">-->
<!--          <el-button @click="dialogFormVisible = false">取消</el-button>-->
<!--          <el-button type="primary" @click="submitForm">确定</el-button>-->
<!--        </span>-->
<!--      </template>-->
    </el-dialog>
  </div>
</template>

<script>
import { listGroup, addGroup, updateGroup, deleteGroup } from "@/api/system/group";

export default {
  data() {
    return {
      // 遮罩层
      loading: true,
      list: [], // 组别列表
      dialogFormVisible: false, // 弹框显示状态
      form: {
        id: null,
        code: "",
        name: ""
      },
      rules: {
        code: [{ required: true, message: "组别代码不能为空", trigger: "blur" }],
        name: [{ required: true, message: "组别名称不能为空", trigger: "blur" }]
      },
      dialogTitle: "" // 弹框标题
    };
  },
  methods: {
    // 获取组别列表
    getList() {
      this.loading = true
      listGroup().then(response => {
        this.list = response.rows;
        this.total = response.total
        this.loading = false
      });
    },
    // 新增组别
    handleCreate() {
      console.log("新增组别");
      this.dialogTitle = "新增组别";
      this.form = {
        id: null,
        code: "",
        name: ""
      };
      this.dialogFormVisible = true;
    },
    // 修改组别
    handleUpdate(row) {
      this.dialogTitle = "修改组别";
      this.form = {
        id: row.id,
        code: row.code,
        name: row.name
      };
      this.dialogFormVisible = true;
    },
    // 提交表单
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          if (this.form.id) {
            // 修改
            updateGroup(this.form).then(() => {
              this.dialogFormVisible = false;
              this.$message.success("修改成功");
              this.getList();
            });
          } else {
            // 新增
            addGroup(this.form).then(() => {
              this.dialogFormVisible = false;
              this.$message.success("新增成功");
              this.getList();
            });
          }
        }
      });
    },
    // 删除组别
    handleDelete(row) {
      this.$confirm("是否确认删除组别名称为" + row.name + "的数据项?", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteGroup(row.id).then(() => {
          this.$message.success("删除成功");
          this.getList();
        });
      });
    }
  },
  async created() {
    this.getList(); // 初始化加载组别列表
  }
};
</script>
