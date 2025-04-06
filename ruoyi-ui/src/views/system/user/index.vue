<template>
  <div class="app-container">
    <el-row :gutter="20">
      <splitpanes :horizontal="this.$store.getters.device === 'mobile'" class="default-theme">
        <!--部门数据-->
        <pane size="16" v-if="showDeptTree">
          <el-col>
            <div class="head-container">
              <el-input v-model="deptName" placeholder="请输入部门名称" clearable size="small"
                        prefix-icon="el-icon-search" style="margin-bottom: 20px"
              />
            </div>
            <div class="head-container">
              <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false"
                       :filter-node-method="filterNode" ref="tree" node-key="id" default-expand-all highlight-current
                       @node-click="handleNodeClick"
              />
            </div>
          </el-col>
        </pane>
        <!--用户数据-->
        <pane size="84">
          <el-col>
            <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                     label-width="68px"
            >
              <!--搜索表单在此省略，保留原搜索相关代码-->
            </el-form>

            <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                           v-hasPermi="['system:user:add']"
                >新增
                </el-button>
              </el-col>
            </el-row>

            <el-table v-loading="loading" :data="userList" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="50" align="center"/>
              <el-table-column label="工号" align="center" prop="userName"/>
              <!--              <el-table-column label="用户名称" align="center" prop="nickName"/>-->
              <el-table-column label="组别" align="center">
                <template #default="scope">
                  <span v-if="scope.row.group">
                  {{ scope.row.group.name }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="角色" align="center">
                <template #default="scope">
                  <span v-if="scope.row.roles && scope.row.roles.length">
                  {{ scope.row.roles[0].roleName }}
                  </span>
                </template>
              </el-table-column>
              <!--其他列省略-->
              <el-table-column label="操作" align="center">
                <template #default="scope">
                  <el-button @click="handleUpdate(scope.row)" type="primary" size="small">编辑</el-button>
                  <el-button @click="handleDelete(scope.row)" type="danger" size="small">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

            <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                        :limit.sync="queryParams.pageSize" @pagination="getList"
            />
          </el-col>
        </pane>
      </splitpanes>
    </el-row>

    <!-- 添加或修改用户配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="工号" prop="userName">
              <el-input v-model="form.userName" maxlength="20"/>
            </el-form-item>
          </el-col>
<!--          <el-col :span="12">-->
<!--            <el-form-item label="用户名" prop="nickName">-->
<!--              <el-input v-model="form.nickName" maxlength="20"/>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户角色" prop="roleIds">
              <el-select label="用户角色" v-model="form.roleIds" placeholder="请选择角色">
                <el-option v-for="item in roleOptions" :key="item.roleId" :label="item.roleName" :value="item.roleId"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户组别" prop="groupId">
              <el-select v-model="form.groupId" placeholder="请选择组别">
                <el-option v-for="item in groupOptions" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">
                  {{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"></el-input>
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
// 引入原代码中的相关功能和数据
import {
  listUser,
  getUser,
  delUser,
  addUser,
  updateUser,
  resetUserPwd,
  changeUserStatus,
  deptTreeSelect, getAuthRole
} from '@/api/system/user'
import { listRole } from '@/api/system/role'
import { listGroup } from '@/api/system/group'
import { getToken } from '@/utils/auth'
import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'
import { getConfigValueByKey } from '@/api/system/config'

export default {
  name: 'User',
  dicts: ['sys_normal_disable', 'sys_user_sex'],
  components: { Treeselect, Splitpanes, Pane },
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
      // 用户表格数据
      userList: null,
      // 弹出层标题
      title: '',
      // 所有部门树选项
      deptOptions: undefined,
      // 过滤掉已禁用部门树选项
      enabledDeptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 组别选项
      groupOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: '',
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: 'Bearer ' + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/system/user/importData'
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: undefined,
        nickName: undefined,
        phonenumber: undefined,
        status: undefined,
        deptId: undefined
      },
      // 列信息
      columns: [
        { key: 0, label: `工号`, visible: true },
        { key: 1, label: `用户名称`, visible: true },
        { key: 2, label: `组别`, visible: true },
        { key: 3, label: `角色`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `状态`, visible: true },
        { key: 6, label: `创建时间`, visible: true }
      ],
      // 表单校验
      rules: {
        userName: [
          { required: true, message: '工号不能为空', trigger: 'blur' },
          { min: 2, max: 20, message: '工号长度必须介于 2 和 20 之间', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '用户密码不能为空', trigger: 'blur' },
          { min: 8, max: 50, message: '用户密码长度必须介于 8 和 50 之间', trigger: 'blur' },
          {
            pattern: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*!)[A-Za-z\d!]{8,}$/,
            message: '密码包括大写、小写、数字和!',
            trigger: 'blur'
          }
          // { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符：< > \" ' \\\ |", trigger: "blur" }
        ],
        email: [
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ],
        phonenumber: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ],
        groupIds: [
          { required: true, message: '组别不能为空', trigger: 'change' }
        ],
        roleIds: [
          { required: true, message: '角色不能为空', trigger: 'change' }
        ]
      },
      showDeptTree: false
    }
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val)
    }
  },
  created() {
    this.getRoleOptions()
    this.getList()
    this.getDeptTree()
    this.getGroupOptions()
    this.getConfigValueByKey('sys.user.initPassword').then(response => {
      this.initPassword = response.msg
    })
  },
  methods: {
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listUser(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
          this.userList = response.rows
          this.total = response.total
          this.loading = false
        }
      )
    },
    // 表单重置
    reset() {
      this.form = {
        userId: undefined,
        deptId: undefined,
        groupId: undefined,
        userName: undefined,
        nickName: undefined,
        password: undefined,
        phonenumber: undefined,
        email: undefined,
        sex: undefined,
        status: '0',
        remark: undefined,
        postIds: [],
        roleIds: []
      }
      this.resetForm('form')
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.userName + '"的新密码', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        closeOnClickModal: false,
        inputPattern: /^.{8,50}$/,
        inputErrorMessage: '用户密码长度必须介于 8 和 50 之间',
        inputValidator: (value) => {
          if (/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*!)[A-Za-z\d!]{8,}$/.test(value)) {
            return '密码包括大写、小写、数字和!'
          }
        }
      }).then(({ value }) => {
        resetUserPwd(row.userId, value).then(response => {
          this.$modal.msgSuccess('修改成功，新密码是：' + value)
        })
      }).catch(() => {
      })
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      deptTreeSelect().then(response => {
        this.deptOptions = response.data
        this.enabledDeptOptions = this.filterDisabledDept(JSON.parse(JSON.stringify(response.data)))
      })
    },
    // 过滤禁用的部门
    filterDisabledDept(deptList) {
      return deptList.filter(dept => {
        if (dept.disabled) {
          return false
        }
        if (dept.children && dept.children.length) {
          dept.children = this.filterDisabledDept(dept.children)
        }
        return true
      })
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = []
      this.resetForm('queryForm')
      this.queryParams.deptId = undefined
      this.queryParams.groupId = undefined
      // this.$refs.tree.setCurrentKey(null)
      this.handleQuery()
    },
    // 获取组别选项
    getGroupOptions() {
      listGroup({ status: '0' }).then(response => {
        this.groupOptions = response.rows
      })
    },
    // 重载部分方法，比如新增用户时需要多选组别
    handleAdd() {
      this.reset()
      getUser().then(response => {
        this.roleOptions = response.roles
        // this.groupOptions = response.groups // 假设后台返回了组别选项
        this.open = true
        this.title = '添加用户'
        this.form.password = this.initPassword
      })
    },
    // 用户保存时，组别和角色的批量处理
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (this.form.roleIds) {
          this.$set(this.form, 'roleIds', [this.form.roleIds])
        }
        if (valid) {
          if (this.form.userId != undefined) {
            // 更新用户时附加组别关系
            updateUser(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            this.$set(this.form, 'nickName', 'u_' + this.form.userName);
            // 新增用户时附加组别关系
            addUser(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    }
    ,
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.userId)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    getRoleOptions() {
      listRole({ status: '0' }).then(response => {
        this.roleOptions = response.rows
      })
    },
    // 修改用户
    handleUpdate(row) {
      this.reset()
      const userId = row.userId || this.ids
      getUser(userId).then(response => {
        this.form = response.data
        console.log(response)
        if (response.roleIds && response.roleIds.length > 0) {
          this.$nextTick(() => {
            this.$set(this.form, 'roleIds', response.roleIds[0])
          })
        }
        this.open = true
      })
      this.title = '修改角色'
    },
    // 删除用户
    handleDelete(row) {
      this.$confirm('是否确认删除用户' + row.userId + '?', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delUser(row.userId).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      })
    },
    cancel() {
      this.open = false
      this.reset()
    }
  }
}
</script>
