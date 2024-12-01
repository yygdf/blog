<template>
  <el-card class="main-card">
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane :label="$t('personal.profile')" name="info">
        <el-form label-width="80px" :model="userForm">
          <el-form-item :label="$t('table.nickname')">
            <el-input
              v-model="userForm.nickname"
              ref="input"
              size="small"
              class="word-limit-input form-input-width"
              maxlength="50"
              :placeholder="$t('user.inputNickname')"
              show-word-limit
            />
          </el-form-item>
          <el-form-item :label="$t('user.intro')">
            <el-input
              v-model="userForm.intro"
              size="small"
              class="word-limit-input form-input-width"
              maxlength="50"
              :placeholder="$t('user.inputIntro')"
              show-word-limit
            />
          </el-form-item>
          <el-form-item :label="$t('user.website')">
            <el-input
              v-model="userForm.website"
              size="small"
              class="word-limit-input2 form-input-width"
              maxlength="255"
              :placeholder="$t('user.inputWebsite')"
              show-word-limit
            />
          </el-form-item>
          <el-form-item :label="$t('user.gender')">
            <el-radio-group v-model="userForm.gender">
              <el-radio :label="1">{{ $t("option.gender1") }}</el-radio>
              <el-radio :label="2">{{ $t("option.gender2") }}</el-radio>
              <el-radio :label="3">{{ $t("option.gender3") }}</el-radio>
              <el-radio :label="4">{{ $t("option.gender4") }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-button
            @click="updateInfo"
            type="primary"
            size="medium"
            style="margin-left:12rem"
          >
            {{ $t("button.edit") }}
          </el-button>
        </el-form>
      </el-tab-pane>
      <el-tab-pane
        :label="$t('personal.edit1')"
        name="username"
        v-if="!this.$store.state.modifiedFlag"
      >
        <el-form label-width="120px" :model="usernameForm">
          <el-form-item :label="$t('personal.oldUsername')">
            <el-input
              v-model="usernameForm.usernameOrigin"
              size="small"
              class="form-input-width"
              disabled
            />
          </el-form-item>
          <el-form-item :label="$t('personal.newUsername')">
            <el-input
              v-model="usernameForm.username"
              ref="input"
              size="small"
              class="word-limit-input form-input-width"
              maxlength="50"
              :placeholder="$t('personal.tip1')"
              show-word-limit
            />
          </el-form-item>
          <el-button
            type="warning"
            size="medium"
            style="margin-left:14rem"
            @click="updateUsername"
          >
            {{ $t("button.edit") }}
          </el-button>
        </el-form>
      </el-tab-pane>
      <el-tab-pane :label="$t('personal.edit2')" name="password">
        <el-form label-width="120px" :model="passwordForm">
          <el-form-item :label="$t('personal.oldPassword')">
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
              {{ $t("login.passwordRule2") }}</span
            >
            <span
              v-if="oldPasswordStatus === 2"
              class="el-icon-success"
              style="color: green;"
            ></span>
          </el-form-item>
          <el-form-item :label="$t('personal.newPassword')">
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
              {{ $t("login.passwordRule2") }}</span
            >
            <span
              v-if="newPasswordStatus === 2"
              class="el-icon-success"
              style="color: green;"
            ></span>
          </el-form-item>
          <el-form-item :label="$t('auth.password2')">
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
              {{ $t("auth.passwordRule1") }}</span
            >
          </el-form-item>
          <el-button
            :disabled="
              oldPasswordStatus !== 2 ||
                newPasswordStatus !== 2 ||
                confirmPasswordStatus !== 2
            "
            type="warning"
            size="medium"
            style="margin-left:14rem"
            @click="updatePassword"
          >
            {{ $t("button.edit") }}
          </el-button>
        </el-form>
      </el-tab-pane>
      <el-tab-pane :label="$t('personal.edit3')" name="avatar">
        <el-form label-width="80px" :model="avatarForm" style="width: 200px">
          <el-form-item :label="$t('table.avatar')">
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
              :labels="{
                submit: $t('button.save'),
                cancel: $t('button.cancel')
              }"
              :upload-headers="{
                Lang: this.$i18n.locale,
                Token: this.$store.state.token
              }"
            />
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane
        :label="$t('personal.aboutMe')"
        name="about"
        v-if="this.$store.state.weight <= 400"
      >
        <mavon-editor
          :toolbars="toolbars"
          v-model="aboutContent"
          :language="this.$i18n.locale === 'en_US' ? 'en' : 'zh-CN'"
          style="height:calc(100vh - 300px);"
        />
        <el-button
          type="primary"
          size="medium"
          class="edit-btn"
          @click="updateAbout"
        >
          {{ $t("button.edit") }}
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
        this.$message.error(this.$t("user.nicknameRule1"));
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
            title: this.$t("success"),
            message: data.message
          });
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
          title: this.$t("success"),
          message: data.message
        });
      } else {
        this.$notify.error({
          title: this.$t("failure"),
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
        this.$message.error(this.$t("article.coverRule1"));
        return false;
      }
      if (file.size >>> 20 > 5) {
        file.status = false;
        this.$message.error(this.$t("article.coverRule2"));
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
            title: this.$t("success"),
            message: data.message
          });
        } else {
          this.$notify.error({
            title: this.$t("failure"),
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
              title: this.$t("success"),
              message: data.message
            });
          } else {
            this.$notify.error({
              title: this.$t("failure"),
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
              title: this.$t("success"),
              message: data.message
            });
          } else {
            this.$notify.error({
              title: this.$t("failure"),
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
