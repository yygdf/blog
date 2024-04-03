<template>
  <v-dialog
    v-model="emailFlag"
    :fullscreen="this.$store.state.mobileFlag"
    max-width="460"
  >
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right" @click="emailFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <v-text-field v-model="username" label="用户名" disabled />
        <v-text-field
          v-model="email"
          :rules="[rules.email]"
          label="邮箱"
          maxlength="50"
          placeholder="请输入您的新邮箱"
          @keyup.enter="modifyUserEmail"
          clearable
        />
        <v-text-field
          v-model="password"
          :rules="[rules.required]"
          :type="show ? 'text' : 'password'"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          class="mt-7"
          label="密码"
          placeholder="请输入您的密码"
          @keyup.enter="modifyUserEmail"
          @click:append="show = !show"
        />
        <div class="mt-7 send-wrapper">
          <v-text-field
            v-model="code"
            label="验证码"
            maxlength="6"
            placeholder="请输入6位验证码"
            @keyup.enter="modifyUserEmail"
          />
          <v-btn
            text
            small
            rounded
            :disabled="flag || status"
            @click="sendEmailCode"
          >
            {{ codeMsg }}
          </v-btn>
        </div>
        <v-btn
          class="mt-7"
          color="blue"
          style="color:#fff"
          @click="modifyUserEmail"
          block
        >
          绑定
        </v-btn>
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
      username: localStorage.getItem("username"),
      password: "",
      flag: true,
      show: false,
      status: false,
      time: 60,
      codeMsg: "发送",
      rules: {
        required: value => value.length >= 6 || "至少6个字符!",
        email: value => {
          const pattern = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
          if (pattern.test(value)) {
            this.flag = false;
            return true;
          }
          this.flag = true;
          return "邮箱格式不正确!";
        }
      }
    };
  },
  computed: {
    emailFlag: {
      set(value) {
        this.$store.commit("updateEmailFlag", value);
      },
      get() {
        return this.$store.state.emailFlag;
      }
    }
  },
  methods: {
    sendEmailCode() {
      const that = this;
      if (process.env.TENCENT_CAPTCHA) {
        // eslint-disable-next-line no-undef
        let captcha = new TencentCaptcha(process.env.TENCENT_CAPTCHA, function(
          res
        ) {
          if (res.ret === 0) {
            that.sendCode();
          }
        });
        captcha.show();
      } else {
        this.sendCode();
      }
    },
    sendCode() {
      this.countDown();
      this.axios
        .post("/api/user/email/code", {
          email: this.email,
          type: 3
        })
        .then(({ data }) => {
          if (data.flag) {
            this.$toast({ type: "success", message: data.message });
          }
        });
    },
    countDown() {
      this.status = true;
      this.timer = setInterval(() => {
        this.time--;
        this.codeMsg = this.time + "s";
        if (this.time <= 0) {
          clearInterval(this.timer);
          this.codeMsg = "发送";
          this.time = 60;
          this.status = false;
        }
      }, 1000);
    },
    modifyUserEmail() {
      const pattern = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
      if (!pattern.test(this.email)) {
        this.$toast({ type: "error", message: "邮箱格式不正确" });
        return false;
      }
      if (this.password.trim().length < 6) {
        this.$toast({ type: "error", message: "密码不能少于6位" });
        return false;
      }
      if (this.code.trim().length !== 6) {
        this.$toast({ type: "error", message: "请输入6位验证码" });
        return false;
      }
      let user = {
        code: this.code,
        email: this.email,
        password: md5(this.password)
      };
      this.axios.put("/api/user/email", user).then(({ data }) => {
        if (data.flag) {
          this.code = "";
          this.email = "";
          this.password = "";
          this.$store.commit("updateEmailFlag", false);
          this.$toast({ type: "success", message: data.message });
        }
      });
    }
  }
};
</script>

<style scoped>
@media (min-width: 760px) {
  .login-container {
    padding: 1rem;
    border-radius: 4px;
    height: 400px;
  }
}
</style>
