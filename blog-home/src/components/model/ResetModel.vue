<template>
  <v-dialog
    v-model="resetFlag"
    :fullscreen="this.$store.state.mobileFlag"
    max-width="460"
  >
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right" @click="resetFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <v-text-field
          v-model="passwordForm.oldPassword"
          :rules="[rules.required]"
          :type="show ? 'text' : 'password'"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          class="mt-7"
          label="旧密码"
          maxlength="50"
          autofocus="autofocus"
          placeholder="请输入您的旧密码"
          @keyup.enter="resetPassword"
          @click:append="show = !show"
        />
        <v-text-field
          v-model="passwordForm.newPassword"
          :rules="[rules.required]"
          :type="show1 ? 'text' : 'password'"
          :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
          class="mt-7"
          label="新密码"
          maxlength="50"
          placeholder="请输入您的新密码"
          @keyup.enter="resetPassword"
          @click:append="show1 = !show1"
        />
        <v-text-field
          v-model="passwordForm.confirmPassword"
          :rules="[rules.required]"
          :type="show2 ? 'text' : 'password'"
          :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
          class="mt-7"
          label="确认密码"
          maxlength="50"
          placeholder="请再次输入您的新密码"
          @keyup.enter="resetPassword"
          @click:append="show2 = !show2"
        />
        <v-btn
          class="mt-7"
          block
          color="red"
          style="color:#fff"
          @click="resetPassword"
        >
          修改
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
      passwordForm: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      },
      show: false,
      show1: false,
      show2: false,
      rules: {
        required: value => value.length >= 6 || "至少6个字符!"
      }
    };
  },
  computed: {
    resetFlag: {
      set(value) {
        this.$store.commit("updateResetFlag", value);
      },
      get() {
        return this.$store.state.resetFlag;
      }
    }
  },
  methods: {
    resetPassword() {
      if (this.passwordForm.oldPassword.trim().length < 6) {
        this.$toast({ type: "error", message: "旧密码不能少于6位" });
        return false;
      }
      if (this.passwordForm.newPassword.trim().length < 6) {
        this.$toast({ type: "error", message: "新密码不能少于6位" });
        return false;
      }
      if (
        this.passwordForm.newPassword.trim() !==
        this.passwordForm.confirmPassword.trim()
      ) {
        this.$toast({ type: "error", message: "两次密码输入不一致" });
        return false;
      }
      let param = {
        oldPassword: md5(this.passwordForm.oldPassword),
        newPassword: md5(this.passwordForm.newPassword)
      };
      this.axios.put("/api/userAuth/password", param).then(({ data }) => {
        if (data.flag) {
          this.passwordForm.oldPassword = "";
          this.passwordForm.newPassword = "";
          this.passwordForm.confirmPassword = "";
          this.$store.commit("updateResetFlag", false);
          if (this.$route.path === this.rootUri + "/personal") {
            this.$router.go(-1);
          }
          this.$toast({ type: "success", message: data.message });
        }
      });
    }
  }
};
</script>
