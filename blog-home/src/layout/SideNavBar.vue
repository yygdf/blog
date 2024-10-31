<template>
  <v-navigation-drawer
    app
    v-model="drawerFlag"
    width="250"
    disable-resize-watcher
    right
    overlay-opacity="0.8"
  >
    <div class="blogger-info">
      <v-avatar size="110" style="margin-bottom:0.5rem">
        <img :src="bloggerInfo.avatar" alt="" />
      </v-avatar>
    </div>
    <div class="blog-info-wrapper">
      <div class="blog-info-data">
        <router-link :to="rootUri + '/archives'">
          <div style="font-size: 0.875rem">{{ $t("navBar.article") }}</div>
          <div style="font-size: 1.125rem;">
            {{ bloggerInfo.articleCount }}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link :to="rootUri + '/categories'">
          <div style="font-size: 0.875rem">{{ $t("navBar.category") }}</div>
          <div style="font-size: 1.125rem">
            {{ bloggerInfo.categoryCount }}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link :to="rootUri + '/tags'">
          <div style="font-size: 0.875rem">{{ $t("navBar.tag") }}</div>
          <div style="font-size: 1.125rem">
            {{ bloggerInfo.tagCount }}
          </div>
        </router-link>
      </div>
    </div>
    <hr />
    <div class="menu-container">
      <div class="menus-item">
        <router-link :to="rootUri === '' ? '/' : rootUri">
          <i class="iconfont my-icon-home" /> {{ $t("navBar.home") }}
        </router-link>
      </div>
      <div class="menus-item">
        <router-link :to="rootUri + '/archives'">
          <i class="iconfont my-icon-archives" /> {{ $t("navBar.archive") }}
        </router-link>
      </div>
      <div class="menus-item">
        <router-link :to="rootUri + '/categories'">
          <i class="iconfont my-icon-sort" /> {{ $t("navBar.category") }}
        </router-link>
      </div>
      <div class="menus-item">
        <router-link :to="rootUri + '/tags'">
          <i class="iconfont my-icon-label" /> {{ $t("navBar.tag") }}
        </router-link>
      </div>
      <div class="menus-item">
        <router-link :to="rootUri + '/friendLinks'">
          <i class="iconfont my-icon-link" /> {{ $t("navBar.friendLink") }}
        </router-link>
      </div>
      <div class="menus-item">
        <router-link :to="rootUri + '/about'">
          <i class="iconfont my-icon-paper-plane" /> {{ $t("navBar.about") }}
        </router-link>
      </div>
      <div class="menus-item">
        <router-link :to="rootUri + '/messages'">
          <i class="iconfont my-icon-comment-group" />
          {{ $t("navBar.message") }}
        </router-link>
      </div>
      <div class="menus-item" v-if="this.$store.state.userId == null">
        <a @click="openLogin"
          ><i class="iconfont my-icon-login" /> {{ $t("navBar.login") }}
        </a>
      </div>
      <div v-else>
        <div class="menus-item">
          <router-link :to="rootUri + '/personal'">
            <i class="iconfont my-icon-personal" /> {{ $t("navBar.personal") }}
          </router-link>
        </div>
        <div class="menus-item">
          <a @click="openReset">
            <i class="iconfont my-icon-success" /> {{ $t("navBar.resetPWD") }}
          </a>
        </div>
        <div class="menus-item">
          <a @click="openEmailModel">
            <i class="iconfont my-icon-success" />
            {{ $t("navBar.changeEmail") }}
          </a>
        </div>
        <div class="menus-item">
          <a @click="logout"
            ><i class="iconfont my-icon-exit" /> {{ $t("navBar.logout") }}</a
          >
        </div>
      </div>
    </div>
  </v-navigation-drawer>
</template>

<script>
export default {
  computed: {
    drawerFlag: {
      set(value) {
        this.$store.commit("updateDrawerFlag", value);
      },
      get() {
        return this.$store.state.drawerFlag;
      }
    },
    bloggerInfo() {
      return this.$store.state.bloggerInfo;
    },
    rootUri() {
      return this.$store.state.rootUri;
    }
  },
  methods: {
    openLogin() {
      this.$store.commit("updateLoginFlag", true);
    },
    openReset() {
      this.$store.commit("updateResetFlag", true);
    },
    openEmailModel() {
      this.$store.commit("updateEmailFlag", true);
    },
    logout() {
      if (this.$route.path === this.rootUri + "/personal") {
        this.$router.go(-1);
      }
      this.axios.post("/api/logout").then(({ data }) => {
        if (data.flag) {
          this.$store.commit("logout");
          this.$store.commit("removeToken");
          this.$toast({ type: "success", message: data.message });
        }
      });
    }
  }
};
</script>

<style scoped>
.blogger-info {
  padding: 26px 30px 0;
  text-align: center;
}
.blog-info-wrapper {
  display: flex;
  align-items: center;
  padding: 12px 10px 0;
}
.blog-info-data {
  flex: 1;
  line-height: 2;
  text-align: center;
}
hr {
  border: 2px dashed #d2ebfd;
  margin: 20px 0;
}
.menu-container {
  padding: 0 10px 40px;
  animation: 0.8s ease 0s 1 normal none running sidebarItem;
}
.menus-item a {
  padding: 6px 30px;
  display: block;
  line-height: 2;
}
.menus-item i {
  margin-right: 2rem;
}
@keyframes sidebarItem {
  0% {
    transform: translateX(200px);
  }
  100% {
    transform: translateX(0);
  }
}
</style>
