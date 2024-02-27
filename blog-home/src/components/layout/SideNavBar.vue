<template>
  <v-navigation-drawer
    app
    v-model="drawer"
    width="250"
    disable-resize-watcher
    right
    overlay-opacity="0.8"
  >
    <div class="blogger-info">
      <v-avatar size="110" style="margin-bottom:0.5rem">
        <img :src="bloggerInfo.avatar" />
      </v-avatar>
    </div>
    <div class="blog-info-wrapper">
      <div class="blog-info-data">
        <router-link to="/archives">
          <div style="font-size: 0.875rem">文章</div>
          <div style="font-size: 1.125rem;">
            {{ bloggerInfo.articleCount }}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link to="/categories">
          <div style="font-size: 0.875rem">分类</div>
          <div style="font-size: 1.125rem">
            {{ bloggerInfo.categoryCount }}
          </div>
        </router-link>
      </div>
      <div class="blog-info-data">
        <router-link to="/tags">
          <div style="font-size: 0.875rem">标签</div>
          <div style="font-size: 1.125rem">
            {{ bloggerInfo.tagCount }}
          </div>
        </router-link>
      </div>
    </div>
    <hr />
    <div class="menu-container">
      <div class="menus-item">
        <router-link to="/">
          <i class="iconfont my-icon-home" /> 首页
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/archives">
          <i class="iconfont my-icon-archives" /> 归档
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/categories">
          <i class="iconfont my-icon-sort" /> 分类
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/tags">
          <i class="iconfont my-icon-label" /> 标签
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/links">
          <i class="iconfont my-icon-link" /> 友链
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/about">
          <i class="iconfont my-icon-paper-plane" /> 关于
        </router-link>
      </div>
      <div class="menus-item">
        <router-link to="/messages">
          <i class="iconfont my-icon-comment-group" /> 留言
        </router-link>
      </div>
      <div class="menus-item" v-if="!this.$store.state.avatar">
        <a @click="openLogin"><i class="iconfont my-icon-login" /> 登录 </a>
      </div>
      <div v-else>
        <div class="menus-item">
          <router-link to="/user">
            <i class="iconfont my-icon-personal" /> 个人中心
          </router-link>
        </div>
        <div class="menus-item">
          <a @click="openResetPassword">
            <i class="iconfont my-icon-success" /> 修改密码
          </a>
        </div>
        <div class="menus-item">
          <a @click="logout"><i class="iconfont my-icon-exit" /> 退出</a>
        </div>
      </div>
    </div>
  </v-navigation-drawer>
</template>

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

<script>
export default {
  computed: {
    drawer: {
      set(value) {
        this.$store.state.drawer = value;
      },
      get() {
        return this.$store.state.drawer;
      }
    },
    bloggerInfo() {
      return this.$store.state.bloggerInfo;
    }
  },
  methods: {
    openLogin() {
      this.$store.state.loginFlag = true;
    },
    openResetPassword() {
      this.$store.state.resetFlag = true;
    },
    logout() {
      if (this.$route.path == "/user") {
        this.$router.go(-1);
      }
      this.axios.get("/api/logout").then(({ data }) => {
        if (data.flag) {
          this.$store.commit("logout");
          this.$toast({ type: "success", message: data.message });
        } else {
          this.$toast({ type: "error", message: data.message });
        }
      });
    }
  }
};
</script>
