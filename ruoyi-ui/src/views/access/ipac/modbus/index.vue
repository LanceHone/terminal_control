<!-- src/views/access/modbus/index.vue -->
<template>
  <div class="app-container">
    <!-- 搜索区域 -->
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="设备ID" prop="deviceId">
        <el-input
          v-model="queryParams.deviceId"
          placeholder="请输入设备ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="寄存器地址" prop="registerAddress">
        <el-input
          v-model="queryParams.registerAddress"
          placeholder="请输入寄存器地址"
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

    <!-- 操作按钮区域 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['access:modbus:add']"
        >新增
        </el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-star-off"
          size="mini"
          @click="handleTest"
        >用例
        </el-button>
      </el-col> -->
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="modbusList">
      <el-table-column label="设备ID" prop="deviceId" align="center"/>
      <el-table-column label="功能码类型" prop="functionCode"  align="center">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.access_modbus_function_code" :value="scope.row.functionCode"/>
        </template>
      </el-table-column>
      <el-table-column label="读写操作" prop="rw" align="center">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.access_modbus_rw" :value="scope.row.rw"/>
        </template>
      </el-table-column>
      <el-table-column label="寄存器地址" prop="registerAddress" align="center"/>
      <el-table-column label="值范围" prop="valueRange"  align="center"/>
      <el-table-column label="状态" prop="status"  align="center">
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.status === '1'"
            @change="(value) => handleStatusChange(scope.row, value)"
            active-color="#13ce66"
            inactive-color="#ff4949"
            active-text="正常"
            inactive-text="禁用"
          />
<!--          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>-->
        </template>
      </el-table-column>
      <!-- <el-table-column label="创建时间" align="center" prop="createTime" width="160">
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
            v-hasPermi="['access:modbus:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['access:modbus:remove']"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 新增/修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="设备ID" prop="deviceId">
              <el-input v-model="form.deviceId" placeholder="请输入设备ID"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="功能码类型" prop="functionCode">
              <el-select v-model="form.functionCode" placeholder="请选择" clearable>
                <el-option
                  v-for="dict in dict.type.access_modbus_function_code"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="读写操作" prop="rw">
              <el-radio-group v-model="form.rw">
                <el-radio
                  v-for="dict in dict.type.access_modbus_rw"
                  :key="dict.value"
                  :label="dict.value"
                >{{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="寄存器地址" prop="registerAddress">
              <el-input v-model="form.registerAddress" placeholder="例如：40001～4003"/>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="值范围" prop="valueRange">
              <el-input v-model="form.valueRange" placeholder="例如：0～65535"/>
            </el-form-item>
          </el-col>
<!--          <el-col :span="24">-->
<!--            <el-form-item label="状态" prop="status">-->
<!--              <el-radio-group v-model="form.status">-->
<!--                <el-radio-->
<!--                  v-for="dict in dict.type.sys_normal_disable"-->
<!--                  :key="dict.value"-->
<!--                  :label="dict.value"-->
<!--                >{{ dict.label }}-->
<!--                </el-radio>-->
<!--              </el-radio-group>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <el-dialog title="測試用例" :visible.sync="showCases" width="600px" append-to-body  @on-close="showCases = false">
      <usercases  />
    </el-dialog>
  </div>
</template>

<script>
import { listModbus, getModbus, delModbus, addModbus, updateModbus,updateModbusStatus } from '@/api/access/indu/modbus'
import usercases from '@/views/access/ipac/modbus/usercases.vue'

export default {
  name: 'Modbus',
  dicts: ['access_modbus_function_code', 'access_modbus_rw'],
  components: {usercases},
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 表格数据
      modbusList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceId: undefined,
        registerAddress: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceId: [
          { required: true, message: '设备ID不能为空', trigger: 'blur' }
        ],
        functionCode: [
          { required: true, message: '功能码类型不能为空', trigger: 'change' }
        ],
        // rw: [
        //   { required: true, message: '读写操作不能为空', trigger: 'change' }
        // ],
        registerAddress: [
          { required: true, message: '寄存器地址不能为空', trigger: 'blur' }
        ],
        status: [
          { required: true, message: '状态不能为空', trigger: 'change' }
        ],
        valueRange: [
          { required: true, message: '值范围不能为空', trigger: 'blur' }
        ]
      },
      showCases: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true
      listModbus(this.queryParams).then(response => {
        this.modbusList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        deviceId: undefined,
        functionCode: undefined,
        rw: '0',
        registerAddress: undefined,
        valueRange: undefined,
        status: '0'
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加Modbus策略'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getModbus(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改Modbus策略'
      })
    },
    /** 提交表单 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          // 移除 status 字段
          const data = { ...this.form };
          delete data.status;
          if (this.form.id != null) {
            updateModbus(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addModbus(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除设备ID为"' + row.deviceId + '"的策略？').then(function() {
        return delModbus(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 重置表单并取消 */
    cancel() {
      this.reset();
      this.open = false;
    },
    /** 打开测试用例 */
    handleTest() {
      console.log('打开测试用例');
      this.showCases = true;
    },
    /** 修改状态 */
    handleStatusChange(row, value) {
      const status = value ? '1' : '0';
      // 调用接口更新状态
      updateModbusStatus({ id: row.id, status }).then(() => {
        this.$message.success("状态更新成功");
        this.getList(); // 刷新列表
      }).catch(err => {
        this.$message.error("状态更新失败");
      });
    },
  }
}
</script>

<style scoped>
.el-input-number {
  width: 100%;
}
</style>
