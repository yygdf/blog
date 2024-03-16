<template>
  <v-dialog v-model="registerFlag" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right" @click="registerFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <v-text-field
          v-model="username"
          label="用户名"
          autofocus="autofocus"
          maxlength="50"
          placeholder="请输入您的用户名"
          @keyup.enter="register"
          clearable
        />
        <v-text-field
          v-model="email"
          label="昵称"
          maxlength="50"
          placeholder="请输入您的昵称"
          @keyup.enter="register"
          clearable
        />
        <v-text-field
          v-model="email"
          label="邮箱"
          maxlength="50"
          placeholder="请输入您的邮箱"
          @keyup.enter="register"
          clearable
        />
        <div class="mt-7 send-wrapper">
          <v-text-field
            v-model="code"
            label="验证码"
            maxlength="6"
            placeholder="请输入6位验证码"
            @keyup.enter="register"
          />
          <v-btn text small :disabled="flag" @click="sendCode">
            {{ codeMsg }}
          </v-btn>
        </div>
        <v-text-field
          v-model="password"
          :type="show ? 'text' : 'password'"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          class="mt-7"
          label="密码"
          placeholder="请输入您的密码"
          @keyup.enter="register"
          @click:append="show = !show"
        />
        <v-btn
          class="mt-7"
          color="red"
          style="color:#fff"
          @click="register"
          block
        >
          注册
        </v-btn>
        <div class="mt-10 login-tip">
          已有账号？<span @click="openLogin">登录</span>
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
      code: "",
      email: "",
      username: "",
      password: "",
      flag: true,
      show: false,
      time: 60,
      codeMsg: "发送"
    };
  },
  methods: {
    openLogin() {
      this.$store.state.registerFlag = false;
      this.$store.state.loginFlag = true;
    },
    sendCode() {
      const that = this;
      // eslint-disable-next-line no-undef
      var captcha = new TencentCaptcha(this.config.TENCENT_CAPTCHA, function(
        res
      ) {
        if (res.ret === 0) {
          //发送邮件
          that.countDown();
          that.axios
            .get("/api/users/code", {
              params: { username: that.username }
            })
            .then(({ data }) => {
              if (data.flag) {
                that.$toast({ type: "success", message: data.message });
              }
            });
        }
      });
      // 显示验证码
      captcha.show();
    },
    countDown() {
      this.flag = true;
      this.timer = setInterval(() => {
        this.time--;
        this.codeMsg = this.time + "s";
        if (this.time <= 0) {
          clearInterval(this.timer);
          this.codeMsg = "发送";
          this.time = 60;
          this.flag = false;
        }
      }, 1000);
    },
    register() {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (!reg.test(this.username)) {
        this.$toast({ type: "error", message: "邮箱格式不正确" });
        return false;
      }
      if (this.code.trim().length != 6) {
        this.$toast({ type: "error", message: "请输入6位验证码" });
        return false;
      }
      if (this.password.trim().length < 6) {
        this.$toast({ type: "error", message: "密码不能少于6位" });
        return false;
      }
      const user = {
        username: this.username,
        password: md5(this.password),
        code: this.code
      };
      this.axios.post("/api/users", user).then(({ data }) => {
        if (data.flag) {
          let param = new URLSearchParams();
          param.append("username", user.username);
          param.append("password", user.password);
          this.axios.post("/api/login", param).then(({ data }) => {
            this.username = "";
            this.password = "";
            this.$store.commit("login", data.data);
            this.$store.commit("closeModel");
          });
          this.$toast({ type: "success", message: data.message });
        }
      });
    }
  },
  computed: {
    registerFlag: {
      set(value) {
        this.$store.state.registerFlag = value;
      },
      get() {
        return this.$store.state.registerFlag;
      }
    },
    isMobile() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return false;
      }
      return true;
    }
  },
  watch: {
    username(value) {
      var reg = /^[A-Za-z0-9\u4e00-\u9fa5]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
      if (reg.test(value)) {
        this.flag = false;
      } else {
        this.flag = true;
      }
    }
  }
};
</script>
