<template>
  <el-card class="main-card">
    <el-tabs v-model="activeName">
      <el-tab-pane label="基本信息" name="info">
        <el-form label-width="80px" :model="userForm">
          <el-form-item label="昵称">
            <el-input
              v-model="userForm.nickname"
              size="small"
              style="width: 200px"
              :maxLength="50"
            />
            <span style="color: red;"> *</span>
          </el-form-item>
          <el-form-item label="简介">
            <el-input
              v-model="userForm.intro"
              size="small"
              style="width: 200px"
              :maxLength="50"
            />
          </el-form-item>
          <el-form-item label="网站">
            <el-input
              v-model="userForm.website"
              size="small"
              style="width: 200px"
              :maxLength="255"
            />
          </el-form-item>
          <el-button
            @click="updateInfo"
            type="primary"
            size="medium"
            style="margin-left:8rem"
          >
            修改
          </el-button>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="修改密码" name="password">
        <el-form label-width="80px" :model="passwordForm">
          <el-form-item label="旧密码">
            <el-input
              v-model="passwordForm.oldPassword"
              ref="input"
              size="small"
              style="width: 200px"
              show-password
              @keyup.native="oldPasswordInputChange"
            />&nbsp;
            <span
              v-if="oldPasswordStatus === 1"
              class="el-icon-error"
              style="color: red;"
            >
              密码长度至少6位!</span
            >
            <span
              v-if="oldPasswordStatus === 2"
              class="el-icon-success"
              style="color: green;"
            ></span>
          </el-form-item>
          <el-form-item label="新密码">
            <el-input
              v-model="passwordForm.newPassword"
              size="small"
              style="width: 200px"
              show-password
              @keyup.native="passwordInputChange(true)"
            />&nbsp;
            <span
              v-if="newPasswordStatus === 1"
              class="el-icon-error"
              style="color: red;"
            >
              密码长度至少6位!</span
            >
            <span
              v-if="newPasswordStatus === 2"
              class="el-icon-success"
              style="color: green;"
            ></span>
          </el-form-item>
          <el-form-item label="确认密码">
            <el-input
              v-model="passwordForm.confirmPassword"
              size="small"
              style="width: 200px"
              show-password
              @keyup.native="passwordInputChange()"
            />&nbsp;
            <span
              v-if="confirmPasswordStatus === 2"
              class="el-icon-success"
              style="color: green;"
            ></span>
            <span
              v-if="confirmPasswordStatus === 1"
              class="el-icon-error"
              style="color: red;"
            >
              前后密码不一致!</span
            >
          </el-form-item>
          <el-button
            :disabled="
              oldPasswordStatus !== 2 ||
                newPasswordStatus !== 2 ||
                confirmPasswordStatus !== 2
            "
            type="primary"
            size="medium"
            style="margin-left:8rem"
            @click="updatePassword"
          >
            修改
          </el-button>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="修改头像" name="avatar">
        <el-form label-width="80px" :model="avatarForm" style="width: 200px">
          <el-form-item label="头像">
            <el-button
              id="pick-avatar"
              style="border: none;background-color: white;"
            >
              <el-avatar :size="200" :src="avatarForm.avatar"> </el-avatar>
            </el-button>
            <avatar-cropper
              @uploaded="uploadedAvatar"
              trigger="#pick-avatar"
              upload-url="/api/user/avatar"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
import md5 from "js-md5";
import AvatarCropper from "vue-avatar-cropper";
export default {
  components: { AvatarCropper },
  data: function() {
    return {
      oldPasswordStatus: 0,
      newPasswordStatus: 0,
      confirmPasswordStatus: 0,
      userId: this.$store.state.userId,
      activeName: "info",
      userForm: {
        intro: this.$store.state.intro,
        website: this.$store.state.website,
        nickname: this.$store.state.nickname
      },
      avatarForm: {
        avatar: this.$store.state.avatar
      },
      passwordForm: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: ""
      }
    };
  },
  methods: {
    updateInfo() {
      if (this.userForm.nickname.trim() === "") {
        this.$message.error("昵称不能为空");
        return false;
      }
      this.axios.put("/api/user", this.userForm).then(({ data }) => {
        if (data.flag) {
          this.$message.success(data.message);
          this.$store.commit("updateUserInfo", this.userForm);
        } else {
          this.$message.error(data.message);
        }
      });
    },
    uploadedAvatar(data) {
      if (data.flag) {
        this.$message.success(data.message);
        this.$store.commit("updateAvatar", data.data);
      } else {
        this.$message.error(data.message);
      }
    },
    updatePassword() {
      let param = {
        oldPassword: md5(this.passwordForm.oldPassword),
        newPassword: md5(this.passwordForm.newPassword)
      };
      this.axios.put("/api/back/user/password", param).then(({ data }) => {
        if (data.flag) {
          this.passwordForm.oldPassword = "";
          this.passwordForm.newPassword = "";
          this.passwordForm.confirmPassword = "";
          this.$message.success(data.message);
        } else {
          this.$message.error(data.message);
        }
      });
    },
    passwordInputChange(flag = false) {
      if (
        this.passwordForm.newPassword.trim() === "" &&
        this.passwordForm.confirmPassword.trim() === ""
      ) {
        this.newPasswordStatus = 0;
        this.confirmPasswordStatus = 0;
        return;
      }
      if (flag) {
        if (this.passwordForm.newPassword.trim().length < 6) {
          this.newPasswordStatus = 1;
          return;
        }
        this.newPasswordStatus = 2;
      }
      if (
        this.passwordForm.newPassword.trim() !==
        this.passwordForm.confirmPassword.trim()
      ) {
        this.confirmPasswordStatus = 1;
        return;
      }
      this.confirmPasswordStatus = 2;
    },
    oldPasswordInputChange() {
      if (this.passwordForm.oldPassword.trim() === "") {
        this.oldPasswordStatus = 0;
        return;
      }
      if (this.passwordForm.oldPassword.trim().length < 6) {
        this.oldPasswordStatus = 1;
        return;
      }
      this.oldPasswordStatus = 2;
    }
  }
};
</script>

<style scoped>
.avatar-container {
  text-align: center;
}
.el-icon-message-solid {
  color: #f56c6c;
  margin-right: 0.3rem;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
}
.info-container {
  display: flex;
  align-items: center;
  margin-left: 20%;
  margin-top: 5rem;
}
/deep/ .el-upload .el-upload-dragger {
  width: 200px;
  height: 200px;
}
</style>
