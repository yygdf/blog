<template>
  <div>
    <div class="user-banner banner" :style="cover">
      <h1 class="banner-title">{{ $t("navBar.personal") }}</h1>
    </div>
    <v-card class="blog-container">
      <div>
        <span class="info-title">{{ $t("personal.profile") }}</span>
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
            :labels="{
              submit: $t('button.save'),
              cancel: $t('button.no')
            }"
            :upload-headers="{
              Lang: this.$i18n.locale,
              Token: this.$store.state.token
            }"
          />
        </v-col>
        <v-col md="7" cols="12">
          <v-text-field
            v-model="username"
            :label="$t('email.username')"
            disabled
          />
          <v-text-field
            v-model="userForm.nickname"
            :label="$t('personal.nickname')"
            maxlength="50"
            :placeholder="$t('personal.inputNickname')"
            clearable
          />
          <v-text-field
            v-model="userForm.intro"
            class="mt-7"
            :label="$t('personal.intro')"
            maxlength="50"
            :placeholder="$t('personal.inputIntro')"
            clearable
          />
          <v-text-field
            v-model="userForm.website"
            class="mt-7"
            :label="$t('personal.website')"
            maxlength="255"
            placeholder="https://"
            clearable
          />
          <div class="mt-7 binding">
            <v-text-field
              v-model="email"
              :label="$t('personal.email')"
              :placeholder="$t('personal.bindEmail')"
              disabled
            />
            <v-btn
              v-if="email"
              outlined
              color="primary"
              small
              @click="openEmailModel"
            >
              {{ $t("button.change") }}
            </v-btn>
            <v-btn
              v-else
              outlined
              color="primary"
              small
              @click="openEmailModel"
            >
              {{ $t("button.bind") }}
            </v-btn>
          </div>
          <v-btn @click="updateInfo" style="left: 45%" outlined class="mt-5">{{
            $t("button.edit")
          }}</v-btn>
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
      email: this.$store.state.email,
      username: localStorage.getItem("username")
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
          message: this.$t("personal.tip1")
        });
        return false;
      }
      if (file.size >>> 20 > 5) {
        file.status = false;
        this.$toast({ type: "error", message: this.$t("personal.tip2") });
        return false;
      }
      return true;
    },
    openEmailModel() {
      this.$store.commit("updateEmailFlag", true);
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
