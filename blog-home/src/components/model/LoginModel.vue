<template>
  <v-dialog
    v-model="loginFlag"
    :fullscreen="this.$store.state.mobileFlag"
    max-width="460"
  >
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right" @click="loginFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <v-text-field
          v-model="username"
          :autofocus="username == null"
          label="用户名"
          maxlength="50"
          placeholder="请输入您的用户名"
          @keyup.enter="validLogin"
          clearable
        />
        <v-text-field
          v-model="password"
          :type="show ? 'text' : 'password'"
          :autofocus="username != null"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          class="mt-7"
          label="密码"
          maxlength="50"
          placeholder="请输入您的密码"
          @keyup.enter="validLogin"
          @click:append="show = !show"
        />
        <v-btn
          class="mt-7"
          block
          color="blue"
          style="color:#fff"
          @click="validLogin"
        >
          登录
        </v-btn>
        <div class="mt-10 login-tip">
          <span @click="openRegister">立即注册</span>
          <span @click="openForget" class="float-right">忘记密码?</span>
        </div>
        <div class="social-login-title">社交账号登录</div>
        <div class="social-login-wrapper">
          <a
            class="iconfont my-icon-qq"
            style="color:#00AAEE"
            @click="qqLogin"
          />
        </div>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
import md5 from "js-md5";
export default {
  data: function() {
    return {
      username: localStorage.getItem("username"),
      password: "",
      show: false
    };
  },
  computed: {
    loginFlag: {
      set(value) {
        this.password = "";
        this.$store.commit("updateLoginFlag", value);
      },
      get() {
        return this.$store.state.loginFlag;
      }
    }
  },
  methods: {
    openRegister() {
      this.$store.commit("updateLoginFlag", false);
      this.$store.commit("updateRegisterFlag", true);
    },
    openForget() {
      this.$store.commit("updateLoginFlag", false);
      this.$store.commit("updateForgetFlag", true);
    },
    validLogin() {
      if (this.username.trim().length === 0) {
        this.$toast({ type: "error", message: "用户名不能为空" });
        return false;
      }
      if (this.password.trim().length === 0) {
        this.$toast({ type: "error", message: "密码不能为空" });
        return false;
      }
      const that = this;
      if (this.config.TENCENT_CAPTCHA) {
        // eslint-disable-next-line no-undef
        let captcha = new TencentCaptcha(this.config.TENCENT_CAPTCHA, function(
          res
        ) {
          if (res.ret === 0) {
            that.login();
          }
        });
        captcha.show();
      } else {
        this.login();
      }
    },
    login() {
      let param = new URLSearchParams();
      param.append("username", this.username);
      param.append("password", md5(this.password));
      this.axios.post("/api/login", param).then(({ data }) => {
        if (data.flag) {
          localStorage.setItem("username", this.username);
          this.password = "";
          if (!data.data.avatar) {
            data.data.avatar = require("../../assets/img/default/avatar.png");
          }
          this.$store.commit("login", data.data);
          this.$store.commit("updateLoginFlag", false);
          this.$toast({ type: "success", message: data.message });
        }
      });
    },
    qqLogin() {
      this.$store.commit("saveLoginUrl", this.$route.path);
      if (
        navigator.userAgent.match(
          /(iPhone|iPod|Android|ios|iOS|iPad|Backerry|WebOS|Symbian|Windows Phone|Phone)/i
        )
      ) {
        // eslint-disable-next-line no-undef
        QC.Login.showPopup({
          appId: this.config.QQ_APP_ID,
          redirectURI: this.config.QQ_REDIRECT_URI
        });
      } else {
        window.open(
          "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&client_id=" +
            +this.config.QQ_APP_ID +
            "&response_type=token&scope=all&redirect_uri=" +
            this.config.QQ_REDIRECT_URI,
          "_self"
        );
      }
    }
  }
};
</script>

<style scoped>
.social-login-title {
  margin-top: 1.5rem;
  color: #b5b5b5;
  font-size: 0.75rem;
  text-align: center;
}
.social-login-title::before {
  content: "";
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}
.social-login-title::after {
  content: "";
  display: inline-block;
  background-color: #d8d8d8;
  width: 60px;
  height: 1px;
  margin: 0 12px;
  vertical-align: middle;
}
.social-login-wrapper {
  margin-top: 1rem;
  font-size: 2rem;
  text-align: center;
}
.social-login-wrapper a {
  text-decoration: none;
}
</style>
