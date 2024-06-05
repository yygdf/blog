<template>
  <el-card class="main-card">
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane label="基本信息" name="info">
        <el-form label-width="80px" :model="userForm">
          <el-form-item label="昵称">
            <el-input
              v-model="userForm.nickname"
              ref="input"
              size="small"
              class="word-limit-input form-input-width"
              maxlength="50"
              placeholder="请输入昵称"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="介绍">
            <el-input
              v-model="userForm.intro"
              size="small"
              class="word-limit-input form-input-width"
              maxlength="50"
              placeholder="请输入介绍(可为空)"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="网站">
            <el-input
              v-model="userForm.website"
              size="small"
              class="word-limit-input2 form-input-width"
              maxlength="255"
              placeholder="请输入网站(可为空)"
              show-word-limit
            />
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="userForm.gender">
              <el-radio :label="1">男</el-radio>
              <el-radio :label="2">女</el-radio>
              <el-radio :label="3">可男可女</el-radio>
              <el-radio :label="4">非男非女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-button
            @click="updateInfo"
            type="primary"
            size="medium"
            style="margin-left:12rem"
          >
            修改
          </el-button>
        </el-form>
      </el-tab-pane>
      <el-tab-pane
        label="修改用户名"
        name="username"
        v-if="!this.$store.state.modifiedFlag"
      >
        <el-form label-width="80px" :model="usernameForm">
          <el-form-item label="旧用户名">
            <el-input
              v-model="usernameForm.usernameOrigin"
              size="small"
              class="form-input-width"
              disabled
            />
          </el-form-item>
          <el-form-item label="新用户名">
            <el-input
              v-model="usernameForm.username"
              ref="input"
              size="small"
              class="word-limit-input form-input-width"
              maxlength="50"
              placeholder="每位用户仅可修改一次!"
              show-word-limit
            />
          </el-form-item>
          <el-button
            type="danger"
            size="medium"
            style="margin-left:12rem"
            @click="updateUsername"
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
              class="form-input-width"
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
              class="form-input-width"
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
              class="form-input-width"
              show-password
              @keyup.native="passwordInputChange(false)"
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
            type="danger"
            size="medium"
            style="margin-left:12rem"
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
              @changed="beforeUpload"
              @uploaded="uploadedAvatar"
              trigger="#pick-avatar"
              upload-url="/api/user/avatar"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane
        label="关于我"
        name="about"
        v-if="this.$store.state.weight <= 400"
      >
        <mavon-editor
          :toolbars="toolbars"
          v-model="aboutContent"
          style="height:calc(100vh - 300px);"
        />
        <el-button
          type="primary"
          size="medium"
          class="edit-btn"
          @click="updateAbout"
        >
          修改
        </el-button>
      </el-tab-pane>
    </el-tabs>
  </el-card>
</template>

<script>
import md5 from "js-md5";
import AvatarCropper from "vue-avatar-cropper";
import { resetRouter } from "../../router";
export default {
  components: { AvatarCropper },
  data: function() {
    return {
      oldPasswordStatus: 0,
      newPasswordStatus: 0,
      confirmPasswordStatus: 0,
      activeName: "info",
      userForm: {
        intro: this.$store.state.intro,
        gender: this.$store.state.gender,
        website: this.$store.state.website,
        nickname: this.$store.state.nickname
      },
      userFormOrigin: {
        intro: this.$store.state.intro,
        gender: this.$store.state.gender,
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
      },
      usernameForm: {
        username: "",
        usernameOrigin: localStorage.getItem("username")
      },
      aboutContent: "",
      aboutContentOrigin: "",
      loadAboutContent: true,
      toolbars: {
        bold: true,
        italic: true,
        header: true,
        underline: true,
        strikethrough: true,
        mark: true,
        superscript: true,
        subscript: true,
        quote: true,
        ol: true,
        ul: true,
        link: true,
        imagelink: false,
        code: true,
        table: true,
        fullscreen: true,
        readmodel: true,
        htmlcode: true,
        help: true,
        undo: true,
        redo: true,
        trash: true,
        save: true,
        navigation: true,
        alignleft: true,
        aligncenter: true,
        alignright: true,
        subfield: true,
        preview: true
      }
    };
  },
  methods: {
    updateInfo() {
      if (this.userForm.nickname.trim() === "") {
        this.$message.error("昵称不能为空");
        return false;
      }
      let param = this.$commonMethod.skipIdenticalValue(
        this.userForm,
        this.userFormOrigin
      );
      if (Object.keys(param).length === 0) {
        return false;
      }
      this.axios.put("/api/user", param).then(({ data }) => {
        if (data.flag) {
          this.$store.commit("updateUserInfo", this.userForm);
          this.userFormOrigin = JSON.parse(JSON.stringify(this.userForm));
          this.$notify.success({
            title: "成功",
            message: data.message
          });
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
    },
    uploadedAvatar(data) {
      if (data.flag) {
        this.avatarForm.avatar = data.data;
        this.$store.commit("updateAvatar", data.data);
        this.$notify.success({
          title: "成功",
          message: data.message
        });
      } else {
        this.$notify.error({
          title: "失败",
          message: data.message
        });
      }
    },
    beforeUpload(file) {
      let contentType = file.type;
      if (
        contentType !== "image/jpeg" &&
        contentType !== "image/png" &&
        contentType !== "image/gif"
      ) {
        this.$message.error("上传的图片只能是jpg, png, gif格式");
        return false;
      }
      if (file.size >>> 20 > 5) {
        file.status = false;
        this.$message.error("上传图片的大小不能超过5MB");
        return false;
      }
      return true;
    },
    updatePassword() {
      let param = {
        oldPassword: md5(this.passwordForm.oldPassword),
        newPassword: md5(this.passwordForm.newPassword)
      };
      this.axios.put("/api/userAuth/password", param).then(({ data }) => {
        if (data.flag) {
          this.passwordForm.oldPassword = "";
          this.passwordForm.newPassword = "";
          this.passwordForm.confirmPassword = "";
          this.oldPasswordStatus = 0;
          this.newPasswordStatus = 0;
          this.confirmPasswordStatus = 0;
          this.$notify.success({
            title: "成功",
            message: data.message
          });
        } else {
          this.$notify.error({
            title: "失败",
            message: data.message
          });
        }
      });
    },
    passwordInputChange(flag) {
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
    },
    handleTabClick(tab) {
      if (tab.name === "about" && this.loadAboutContent) {
        this.getAbout();
        this.loadAboutContent = false;
      }
    },
    getAbout() {
      this.axios.get("/api/about").then(({ data }) => {
        this.aboutContent = data.data;
        this.aboutContentOrigin = data.data;
      });
    },
    updateAbout() {
      if (this.aboutContent.trim() === this.aboutContentOrigin.trim()) {
        return;
      }
      this.axios
        .put("/api/back/about", { aboutContent: this.aboutContent })
        .then(({ data }) => {
          if (data.flag) {
            this.aboutContentOrigin = this.aboutContent;
            this.$notify.success({
              title: "成功",
              message: data.message
            });
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
        });
    },
    updateUsername() {
      if (
        this.usernameForm.username.trim() === "" ||
        this.usernameForm.username.trim() ===
          this.usernameForm.usernameOrigin.trim()
      ) {
        return;
      }
      this.axios
        .put("/api/back/userAuth/username", {
          username: this.usernameForm.username
        })
        .then(({ data }) => {
          if (data.flag) {
            this.axios.post("/api/logout");
            this.$store.commit("logout");
            this.$store.commit("resetTab");
            resetRouter();
            this.$router.push({ path: "/login" });
            this.$notify.success({
              title: "成功",
              message: data.message
            });
          } else {
            this.$notify.error({
              title: "失败",
              message: data.message
            });
          }
        });
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
.edit-btn {
  float: right;
  margin: 1rem 0;
}
>>> .add-image-link .title {
  font-size: 16px !important;
  margin-top: -20px !important;
}
</style>
