<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="80px">
    <el-form-item label="工号" prop="nickName">
      {{form.userName}}
<!--      <el-input v-model="form.userName" maxlength="30" />-->
    </el-form-item>
  </el-form>
</template>

<script>
import { updateUserProfile } from "@/api/system/user";

export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      form: {},
      // 表单校验
      rules: {
        // userName: [
        //   { required: true, message: "工号不能为空", trigger: "blur" }
        // ],
        // email: [
        //   { required: true, message: "邮箱地址不能为空", trigger: "blur" },
        //   {
        //     type: "email",
        //     message: "请输入正确的邮箱地址",
        //     trigger: ["blur", "change"]
        //   }
        // ],
        // phonenumber: [
        //   { required: true, message: "手机号码不能为空", trigger: "blur" },
        //   {
        //     pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
        //     message: "请输入正确的手机号码",
        //     trigger: "blur"
        //   }
        // ]
      }
    };
  },
  watch: {
    user: {
      handler(user) {
        if (user) {
          this.form = { userName: user.userName, groupName: user.groupName, roleName: user.roleName };
        }
      },
      immediate: true
    }
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateUserProfile(this.form).then(response => {
            this.$modal.msgSuccess("修改成功");
            this.user.phonenumber = this.form.phonenumber;
            this.user.email = this.form.email;
          });
        }
      });
    },
    close() {
      this.$tab.closePage();
    }
  }
};
</script>
