<template>
  <v-dialog
    v-model="registerFlag"
    :fullscreen="this.$store.state.mobileFlag"
    max-width="460"
  >
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right" @click="registerFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <v-form ref="validForm">
          <v-text-field
            v-model="username"
            :rules="[rules.username]"
            :label="$t('email.username')"
            autofocus="autofocus"
            maxlength="50"
            :placeholder="$t('register.inputUsername')"
            @keyup.enter="register"
            clearable
          />
          <v-text-field
            v-model="email"
            :rules="[rules.email]"
            :label="$t('email.email')"
            maxlength="50"
            :placeholder="$t('email.inputEmail')"
            @keyup.enter="register"
            clearable
          />
          <v-text-field
            v-model="password"
            :rules="[rules.required]"
            :type="show ? 'text' : 'password'"
            :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
            class="mt-7"
            :label="$t('email.password')"
            :placeholder="$t('email.inputPwd')"
            @keyup.enter="register"
            @click:append="show = !show"
          />
          <div class="mt-7 send-wrapper">
            <v-text-field
              v-model="code"
              maxlength="6"
              :label="$t('email.captcha')"
              :placeholder="$t('email.inputCaptcha')"
              @keyup.enter="register"
            />
            <v-btn
              outlined
              color="primary"
              small
              :disabled="flag || status"
              @click="sendEmailCode"
            >
              {{ codeMsg }}
            </v-btn>
          </div>
          <v-btn
            class="mt-7"
            color="red"
            style="color:#fff"
            @click="register"
            block
          >
            {{ $t("button.register") }}
          </v-btn>
        </v-form>
        <div class="mt-10 login-tip">
          {{ $t("register.already") }}?
          <span @click="openLogin">{{ $t("navBar.login") }}</span>
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
      status: false,
      time: 60,
      codeMsg: this.$t("button.get"),
      rules: {
        required: value =>
          !value || value.length >= 6 || this.$t("reset.passwordRule1"),
        username: value =>
          !value || value.length >= 6 || this.$t("register.usernameRule1"),
        email: value => {
          const pattern = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
          if (pattern.test(value)) {
            this.flag = false;
            return true;
          }
          this.flag = true;
          return this.$t("email.emailRule1");
        }
      }
    };
  },
  computed: {
    registerFlag: {
      set(value) {
        this.$store.commit("updateRegisterFlag", value);
      },
      get() {
        return this.$store.state.registerFlag;
      }
    }
  },
  watch: {
    "$i18n.locale"() {
      if (this.$refs.validForm) {
        this.$refs.validForm.reset();
        this.codeMsg = this.$t("button.get");
      }
    }
  },
  methods: {
    openLogin() {
      this.$store.commit("updateRegisterFlag", false);
      this.$store.commit("updateLoginFlag", true);
    },
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
          email: this.email
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
          this.codeMsg = this.$t("button.get");
          this.time = 60;
          this.status = false;
        }
      }, 1000);
    },
    register() {
      if (this.username.trim().length < 6) {
        this.$toast({
          type: "error",
          message: this.$t("register.usernameRule1")
        });
        return false;
      }
      const pattern = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
      if (!pattern.test(this.email)) {
        this.$toast({ type: "error", message: this.$t("email.emailRule2") });
        return false;
      }
      if (this.password.trim().length < 6) {
        this.$toast({ type: "error", message: this.$t("email.passwordRule1") });
        return false;
      }
      if (this.code.trim().length !== 6) {
        this.$toast({ type: "error", message: this.$t("email.captchaRule1") });
        return false;
      }
      let user = {
        code: this.code,
        email: this.email,
        username: this.username,
        password: md5(this.password)
      };
      this.axios.post("/api/user/register", user).then(({ data }) => {
        if (data.flag) {
          this.code = "";
          this.email = "";
          this.username = "";
          this.password = "";
          this.$store.commit("updateRegisterFlag", false);
          let param = new URLSearchParams();
          param.append("username", user.username);
          param.append("password", user.password);
          this.axios.post("/api/login", param).then(({ data }) => {
            if (data.flag) {
              localStorage.setItem("username", user.username);
              if (!data.data.avatar) {
                data.data.avatar =
                  process.env.VUE_APP_STATIC_URL + "img/avatar.png";
              }
              this.$store.commit("login", data.data);
            }
          });
          this.$toast({ type: "success", message: data.message });
        }
      });
    }
  }
};
</script>
