<template>
  <div class="login">
    <el-form v-if="pwdReset" ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" :disabled="globalLocking" >
<!--    <el-form v-if="pwdReset" ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" >-->
      <h3 class="title">
        <el-image
          style="width: 28px; height: 28px"
          :src="url"
          fit="fill">
        </el-image>
        <div style="height: 28px; line-height: 30px; margin-left: 10px;">
          工控网络隔离装置
        </div>
      </h3>
      <el-form-item prop="username">
        <el-input
          v-model="loginForm.username"
          type="text"
          auto-complete="off"
          placeholder="工号"
        >
          <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon"/>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          v-model="loginForm.password"
          type="password"
          auto-complete="off"
          placeholder="密码"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon"/>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
          v-model="loginForm.code"
          auto-complete="off"
          placeholder="验证码"
          style="width: 63%"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon"/>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img"/>
        </div>
      </el-form-item>
      <!-- <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox> -->
      <el-form-item style="width:100%;">
        <el-button
          :loading="loading"
          size="medium"
          type="primary"
          style="width:100%;"
          @click.native.prevent="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <div style="float: right;" v-if="register">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>
      </el-form-item>
      <el-form-item v-if="globalLocking" >
        <span>页面锁定</span>
      </el-form-item>
    </el-form>

    <div v-if="!pwdReset">
      <h3 class="title">{{ pwdResetTitle }}</h3>
      <!--      <h3 class="title">首次登录需要修改密码</h3>-->
      <resetPwd ref="resetPwd" @on-updated="afterResetPwd"/>
    </div>

    <!--  底部  -->
    <div class="el-login-footer">
      <!-- <span>Copyright © 2018-2025 ruoyi.vip All Rights Reserved.</span> -->
    </div>
  </div>
</template>

<script>
import { getCodeImg, globalLocked } from '@/api/login'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'
import resetPwd from '@/views/system/user/profile/resetPwd.vue'
import logoImg from '@/assets/logo/logo1.png'

export default {
  name: 'Login',
  components: { resetPwd },
  data() {
    return {
      pwdReset: true,
      pwdResetTitle: '',
      codeUrl: '',
      url: logoImg,
      loginForm: {
        username: '',
        password: '',
        rememberMe: false,
        code: '',
        uuid: ''
      },
      loginRules: {
        username: [
          { required: true, trigger: 'blur', message: '请输入您的账号' }
        ],
        password: [
          { required: true, trigger: 'blur', message: '请输入您的密码' }
        ],
        code: [{ required: true, trigger: 'change', message: '请输入验证码' }]
      },
      loading: false,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined,
      globalLocking: false,
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCode()
    this.getCookie()
    this.globalLockedMonitoring()

  },
  methods: {
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.codeUrl = 'data:image/gif;base64,' + res.img
          this.loginForm.uuid = res.uuid
        }
      })
    },
    getCookie() {
      const username = Cookies.get('username')
      const password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set('username', this.loginForm.username, { expires: 30 })
            Cookies.set('password', encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          this.$store.dispatch('Login', this.loginForm).then(c => {
            // console.log(`cccccc:${JSON.stringify(c)}`);
            this.pwdReset = c.pwdReset ;
            if (this.pwdReset) {
              this.$router.push({ path: this.redirect || '/' }).catch(() => {
              })
            } else {//TODO 刷新时会进入主页
              Cookies.remove('username')
              Cookies.remove('password')
              this.pwdResetTitle = '首次登录需要修改密码';
              if(c.pwdExpired){
                this.pwdResetTitle = '密码已过期，请重置.';
              }
            }

          }).catch(e => {
            console.log('login error');
            this.loading = false
            // if (e == '4031') {
            //   this.pwdReset = false;
            //   this.pwdResetTitle = '密码已过期，请重置.';
            //   return
            // }
            if (this.captchaEnabled) {
              this.getCode()
            }
          })
        }
      })
    },
    /**
     * 重设密码之后的回调函数
     */
    afterResetPwd() {
      this.$router.push({ path: this.redirect || '/' }).catch(() => {});
      // const cookiUsername = Cookies.get('username');
      // console.log(cookiUsername);
      // if( !cookiUsername){
      //   this.$set(this.loginForm, 'username', this.$refs.resetPwd.user.newPassword);
      //   this.$store.dispatch('Login', this.loginForm).then(c => {
      //     this.pwdReset = c.pwdReset
      //     if (this.pwdReset) {
      //       this.$router.push({ path: this.redirect || '/' }).catch(() => {
      //       })
      //     } else {
      //       this.pwdResetTitle = '与初始密码相同'
      //     }
      //   })
      // }else {
      //   this.$router.push({ path: this.redirect || '/' }).catch(() => {})
      // }
    },
    /**
     * 全局锁定监听
     */
    globalLockedMonitoring() {
      let that = this;
      globalLocked().then(res=>{
        that.globalLocking = res.globalLocked;
      });
      setInterval(() => {
        globalLocked().then(res=>{
          that.globalLocking = res.globalLocked;
        });
      }, 60000);
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background-image: url("../assets/images/login-background.jpg");
  background-size: cover;
}

.title {
  margin: 0px auto 30px auto;
  text-align: center;
  color: #707070;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }

  .input-icon {
    height: 39px;
    width: 14px;
    margin-left: 2px;
  }
}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 38px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.login-code-img {
  height: 38px;
}
</style>
