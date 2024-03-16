<template>
  <v-dialog v-model="resetFlag" :fullscreen="isMobile" max-width="460">
    <v-card class="login-container" style="border-radius:4px">
      <v-icon class="float-right" @click="resetFlag = false">
        mdi-close
      </v-icon>
      <div class="login-wrapper">
        <v-text-field
          v-model="passwordForm.oldPassword"
          label="旧密码"
          placeholder="请输入您的旧密码"
          class="mt-7"
          @keyup.enter="resetPassword"
          :append-icon="show ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show ? 'text' : 'password'"
          @click:append="show = !show"
          autofocus="autofocus"
        />
        <!-- 新密码 -->
        <v-text-field
          v-model="passwordForm.newPassword"
          class="mt-7"
          label="新密码"
          placeholder="请输入您的新密码"
          @keyup.enter="resetPassword"
          :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show1 ? 'text' : 'password'"
          @click:append="show1 = !show1"
        />
        <!-- 确认密码 -->
        <v-text-field
          v-model="passwordForm.confirmPassword"
          class="mt-7"
          label="确认密码"
          placeholder="请再次输入您的新密码"
          @keyup.enter="resetPassword"
          :append-icon="show2 ? 'mdi-eye' : 'mdi-eye-off'"
          :type="show2 ? 'text' : 'password'"
          @click:append="show2 = !show2"
        />
        <!-- 重置按钮 -->
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
      show2: false
    };
  },
  methods: {
    resetPassword() {
      if (this.passwordForm.oldPassword.trim() == "") {
        this.$toast({ type: "error", message: "旧密码不能为空" });
        return false;
      }
      if (this.passwordForm.newPassword.trim() == "") {
        this.$toast({ type: "error", message: "新密码不能为空" });
        return false;
      }
      if (this.passwordForm.newPassword.length < 6) {
        this.$toast({ type: "error", message: "新密码不能少于6位" });
        return false;
      }
      if (this.passwordForm.newPassword != this.passwordForm.confirmPassword) {
        this.$toast({ type: "error", message: "两次密码输入不一致" });
        return false;
      }
      var param = {
        oldPassword: md5(this.passwordForm.oldPassword),
        newPassword: md5(this.passwordForm.newPassword)
      };
      this.axios.put("/api/users/reset", param).then(({ data }) => {
        if (data.flag) {
          this.passwordForm.oldPassword = "";
          this.passwordForm.newPassword = "";
          this.passwordForm.confirmPassword = "";
          this.resetFlag = false;
          this.$toast({ type: "success", message: data.message });
        }
      });
    }
  },
  computed: {
    resetFlag: {
      set(value) {
        this.$store.state.resetFlag = value;
      },
      get() {
        return this.$store.state.resetFlag;
      }
    },
    isMobile() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return false;
      }
      return true;
    }
  }
};
</script>
