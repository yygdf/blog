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
        <v-form ref="validForm">
          <v-text-field
            v-model="username"
            :autofocus="username == null"
            :label="$t('email.username')"
            maxlength="50"
            :placeholder="$t('register.inputUsername')"
            @keyup.enter="validLogin"
            clearable
          />
          <v-text-field
            v-model="password"
            :type="show ? 'text' : 'password'"
            :autofocus="username != null"
            :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
            class="mt-7"
            :label="$t('email.password')"
            maxlength="50"
            :placeholder="$t('email.inputPwd')"
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
            {{ $t("navBar.login") }}
          </v-btn>
        </v-form>
        <div class="mt-10 login-tip">
          <span @click="openRegister">{{ $t("login.register") }}</span>
          <span @click="openForget" class="float-right"
            >{{ $t("login.forgot") }}?</span
          >
        </div>
        <div class="social-login-title">{{ $t("login.social") }}</div>
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
  watch: {
    "$i18n.locale"() {
      if (this.$refs.validForm) {
        this.$refs.validForm.reset();
        this.$nextTick(() => {
          this.username = localStorage.getItem("username");
        });
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
        this.$toast({ type: "error", message: this.$t("login.usernameRule") });
        return false;
      }
      if (this.password.trim().length === 0) {
        this.$toast({ type: "error", message: this.$t("login.passwordRule") });
        return false;
      }
      const that = this;
      if (process.env.TENCENT_CAPTCHA) {
        // eslint-disable-next-line no-undef
        let captcha = new TencentCaptcha(process.env.TENCENT_CAPTCHA, function(
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
          if (!data.data.loginUserDTO.avatar) {
            data.data.loginUserDTO.avatar =
              process.env.VUE_APP_STATIC_URL + "img/avatar.png";
          }
          this.$store.commit("login", data.data.loginUserDTO);
          this.$store.commit("saveToken", data.data.token);
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
          appId: process.env.VUE_APP_QQ_APP_ID,
          redirectURI: process.env.VUE_APP_QQ_REDIRECT_URI
        });
      } else {
        window.open(
          "https://graph.qq.com/oauth2.0/show?which=Login&display=pc&client_id=" +
            +process.env.VUE_APP_QQ_APP_ID +
            "&response_type=token&scope=all&redirect_uri=" +
            process.env.VUE_APP_QQ_REDIRECT_URI,
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
