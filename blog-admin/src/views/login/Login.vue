<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-title">欢迎登录</div>
      <el-form
        status-icon
        :model="loginForm"
        :rules="rules"
        ref="ruleForm"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            prefix-icon="el-icon-user-solid"
            placeholder="用户名"
            @keyup.enter.native="validLogin"
            :autofocus="!loginForm.username"
          />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            prefix-icon="el-icon-lock"
            placeholder="密码"
            show-password
            @keyup.enter.native="validLogin"
            :autofocus="loginForm.username"
          />
        </el-form-item>
      </el-form>
      <el-button type="primary" @click="validLogin">登录</el-button>
      <div class="operation-container">
        <el-checkbox
          v-model="remember"
          true-label="on"
          false-label=""
          style="color: rgba(255,255,255,0.8)"
          disabled
        >
          记住我
        </el-checkbox>
        <el-link
          type="info"
          style="color: rgba(255,255,255,0.8);margin-left:auto"
          >忘记账号或密码?点击此处!</el-link
        >
      </div>
    </div>
  </div>
</template>

<script>
import { generateMenu } from "../../assets/js/menu";
import md5 from "js-md5";
export default {
  created() {
    this.loginForm.username = this.$cookie.getCookie("username");
    this.$store.commit("logout");
  },
  data: function() {
    return {
      remember: "",
      loginForm: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          {
            required: true,
            whitespace: true,
            message: "用户名不能为空",
            trigger: "blur"
          },
          {
            max: 50,
            message: "用户名最长50位",
            trigger: "blur"
          }
        ],
        password: [
          {
            required: true,
            whitespace: true,
            message: "密码不能为空",
            trigger: "blur"
          },
          {
            min: 6,
            message: "密码至少6位",
            trigger: "blur"
          },
          {
            max: 50,
            message: "密码最长50位",
            trigger: "blur"
          }
        ]
      }
    };
  },
  methods: {
    validLogin() {
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          const that = this;
          if (that.$config.TENCENT_CAPTCHA) {
            // eslint-disable-next-line no-undef
            let captcha = new TencentCaptcha(
              that.$config.TENCENT_CAPTCHA,
              function(res) {
                if (res.ret === 0) {
                  that.login(that);
                }
              }
            );
            captcha.show();
          } else {
            that.login(that);
          }
        } else {
          return false;
        }
      });
    },
    login(that) {
      let param = new URLSearchParams();
      param.append("username", that.loginForm.username);
      param.append("password", md5(that.loginForm.password));
      // param.append("remember-me", this.remember);
      that.axios
        .post("/api/login", param, {
          headers: {
            "Login-Platform": "true"
          }
        })
        .then(({ data }) => {
          if (data.flag) {
            that.$store.commit("login", data.data);
            generateMenu().then(() => {
              that.$cookie.setCookie({ username: that.loginForm.username });
              that.$message.success("登录成功");
              that.$router.push({
                path: that.$route.query.url ? that.$route.query.url : "/"
              });
            });
          } else {
            that.$message.error(data.message);
          }
        });
    }
  }
};
</script>

<style scoped>
.login-container {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  left: 0;
  background: url(../../assets/img/login.jpg) center center / cover no-repeat;
}
.login-card {
  position: absolute;
  top: 45%;
  left: 50%;
  margin-top: -185px;
  margin-left: -235px;
  background: rgba(0, 0, 0, 0.3);
  padding: 50px 60px 20px;
  width: 350px;
  box-shadow: 0 15px 25px rgba(0, 0, 0, 0.3);
  border-radius: 20px;
}
.login-title {
  color: rgba(255, 255, 255, 0.8);
  font-weight: bold;
  font-size: 1rem;
}
.login-form {
  margin-top: 1.2rem;
}
.login-card button {
  margin-top: 1rem;
  width: 100%;
}
</style>
