<template>
  <div>
    <div class="user-banner banner" :style="cover">
      <h1 class="banner-title">个人中心</h1>
    </div>
    <v-card class="blog-container">
      <div>
        <span class="info-title">基本信息</span>
      </div>
      <v-row class="info-wrapper">
        <v-col md="3" cols="12">
          <button id="pick-avatar">
            <v-avatar size="140">
              <img :src="avatar" alt="" />
            </v-avatar>
          </button>
          <avatar-cropper
            @changed="beforeUpload"
            @uploaded="uploadedAvatar"
            trigger="#pick-avatar"
            upload-url="/api/user/avatar"
          />
        </v-col>
        <v-col md="7" cols="12">
          <v-text-field
            v-model="userForm.nickname"
            label="昵称"
            maxlength="50"
            placeholder="请输入您的昵称"
            clearable
          />
          <v-text-field
            v-model="userForm.intro"
            class="mt-7"
            label="简介"
            maxlength="50"
            placeholder="介绍下自己吧"
            clearable
          />
          <v-text-field
            v-model="userForm.website"
            class="mt-7"
            label="个人网站"
            maxlength="255"
            placeholder="https://你的网址"
            clearable
          />
          <div v-if="!email" class="mt-7 binding">
            <v-text-field
              disabled
              v-model="email"
              label="邮箱号"
              placeholder="请绑定邮箱"
            />
            <v-btn v-if="email" text small @click="openEmailModel">
              修改绑定
            </v-btn>
            <v-btn v-else text small @click="openEmailModel">
              绑定邮箱
            </v-btn>
          </div>
          <v-btn @click="updateInfo" outlined class="mt-5">修改</v-btn>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
import AvatarCropper from "vue-avatar-cropper";
export default {
  components: { AvatarCropper },
  created() {
    if (this.$store.state.userId == null) {
      this.$router.go(-1);
    }
  },
  data: function() {
    return {
      userForm: {
        intro: this.$store.state.intro,
        website: this.$store.state.website,
        nickname: this.$store.state.nickname
      },
      avatar: this.$store.state.avatar,
      email: this.$store.state.email
    };
  },
  methods: {
    updateInfo() {
      let param = {};
      if (this.userForm.intro !== this.$store.state.intro) {
        param.intro = this.userForm.intro;
      }
      if (this.userForm.website !== this.$store.state.website) {
        param.website = this.userForm.website;
      }
      if (this.userForm.nickname !== this.$store.state.nickname) {
        param.nickname = this.userForm.nickname;
      }
      if (Object.keys(param).length === 0) {
        return false;
      }
      this.axios.put("/api/user", param).then(({ data }) => {
        if (data.flag) {
          this.$store.commit("updateUserInfo", this.userForm);
          this.$toast({ type: "success", message: data.message });
        }
      });
    },
    uploadedAvatar(data) {
      if (data.flag) {
        this.avatar = data.data;
        this.$store.commit("updateAvatar", data.data);
        this.$toast({ type: "success", message: data.message });
      } else {
        this.$toast({ type: "error", message: data.message });
      }
    },
    beforeUpload(file) {
      let contentType = file.type;
      if (
        contentType !== "image/jpeg" &&
        contentType !== "image/png" &&
        contentType !== "image/gif"
      ) {
        this.$toast({
          type: "error",
          message: "上传的图片只能是jpg, png, gif格式"
        });
        return false;
      }
      if (file.size >>> 20 > 5) {
        file.status = false;
        this.$toast({ type: "error", message: "上传图片的大小不能超过5MB" });
        return false;
      }
      return true;
    },
    openEmailModel() {
      this.$store.state.emailFlag = true;
    }
  },
  computed: {
    cover() {
      return (
        "background: url(" +
        this.$store.state.blogConfig.personal_banner_cover +
        ") center center / cover no-repeat"
      );
    }
  }
};
</script>

<style scoped>
.user-banner {
  background: #49b1f5;
}
.info-title {
  font-size: 1.25rem;
  font-weight: bold;
}
.info-wrapper {
  margin-top: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
#pick-avatar {
  outline: none;
}
.binding {
  display: flex;
  align-items: center;
}
</style>
