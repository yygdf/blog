<template>
  <v-app-bar app :class="navClass" hide-on-scroll flat height="60">
    <div class="d-md-none nav-mobile-container">
      <div style="font-size:18px;font-weight:bold">
        <a href="https://www.iksling.com" target="_blank" v-html="logo"></a>
      </div>
      <div style="margin-left:auto">
        <a @click="openSearch"><i class="iconfont my-icon-search"/></a>
        <a @click="openDrawer" style="margin-left:10px;font-size:20px">
          <i class="iconfont my-icon-hamburger" />
        </a>
      </div>
    </div>
    <div class="d-md-block d-none nav-container">
      <div class="float-left blog-title">
        <a href="https://www.iksling.com" target="_blank" v-html="logo"></a>
      </div>
      <div class="float-right nav-title">
        <div class="menus-btn">
          <a @click="openSearch"><i class="iconfont my-icon-search" /> 搜索</a>
        </div>
        <div class="menus-item">
          <router-link :to="rootUri === '' ? '/' : rootUri">
            <i class="iconfont my-icon-home" /> 首页
          </router-link>
        </div>
        <div class="menus-item">
          <router-link :to="rootUri + '/archives'">
            <i class="iconfont my-icon-archives" /> 归档
          </router-link>
        </div>
        <div class="menus-item">
          <router-link :to="rootUri + '/categories'">
            <i class="iconfont my-icon-sort" /> 分类
          </router-link>
        </div>
        <div class="menus-item">
          <router-link :to="rootUri + '/tags'">
            <i class="iconfont my-icon-label" /> 标签
          </router-link>
        </div>
        <div class="menus-item">
          <router-link :to="rootUri + '/friendLinks'">
            <i class="iconfont my-icon-link" /> 友链
          </router-link>
        </div>
        <div class="menus-item">
          <router-link :to="rootUri + '/about'">
            <i class="iconfont my-icon-paper-plane" /> 关于
          </router-link>
        </div>
        <div class="menus-item">
          <router-link :to="rootUri + '/messages'">
            <i class="iconfont my-icon-comment-group" /> 留言
          </router-link>
        </div>
        <div class="user-btn">
          <a v-if="this.$store.state.userId == null" @click="openLogin">
            <i class="iconfont my-icon-login" /> 登录
          </a>
          <template v-else>
            <img
              class="user-avatar"
              :src="avatar"
              height="30"
              width="30"
              alt=""
            />
            <ul class="user-submenu">
              <li>
                <router-link :to="rootUri + '/personal'">
                  <i class="iconfont my-icon-personal" /> 个人中心
                </router-link>
              </li>
              <li>
                <a @click="openReset">
                  <i class="iconfont my-icon-success" /> 修改密码
                </a>
              </li>
              <li>
                <a @click="openEmailModel">
                  <i class="iconfont my-icon-success" /> 换绑邮箱
                </a>
              </li>
              <li>
                <a @click="logout">
                  <i class="iconfont my-icon-exit" /> 退出
                </a>
              </li>
            </ul>
          </template>
        </div>
      </div>
    </div>
  </v-app-bar>
</template>

<script>
export default {
  mounted() {
    window.addEventListener("scroll", this.scroll);
  },
  data: function() {
    return {
      navClass: ""
    };
  },
  computed: {
    avatar() {
      return this.$store.state.avatar
        ? this.$store.state.avatar
        : require("../../assets/img/default/avatar.png");
    },
    logo() {
      if (this.$store.state.nickname) {
        return this.$store.state.nickname;
      }
      return (
        "<img src='" +
        require("../../assets/img/logo.png") +
        "' style='width: 64px;height: 64px;margin-top: 0.5rem;' alt=''/>"
      );
    },
    rootUri() {
      return this.$store.state.rootUri;
    }
  },
  methods: {
    scroll() {
      this.scrollTop =
        window.pageYOffset ||
        document.documentElement.scrollTop ||
        document.body.scrollTop;
      if (this.scrollTop > 60) {
        this.navClass = "nav-fixed";
      } else {
        this.navClass = "nav";
      }
    },
    openSearch() {
      this.$store.commit("updateSearchFlag", true);
    },
    openDrawer() {
      this.$store.commit("updateDrawerFlag", true);
    },
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
      if (this.$route.path === this.$store.state.rootUri + "/personal") {
        this.$router.go(-1);
      }
      this.axios.post("/api/logout").then(({ data }) => {
        if (data.flag) {
          this.$store.commit("logout");
          this.$toast({ type: "success", message: data.message });
        }
      });
    }
  }
};
</script>

<style scoped>
i {
  margin-right: 4px;
}
ul {
  list-style: none;
}
.nav {
  background: rgba(0, 0, 0, 0) !important;
}
.nav a {
  color: #eee !important;
}
.nav .menus-item a {
  text-shadow: 0.05rem 0.05rem 0.1rem rgba(0, 0, 0, 0.3);
}
.nav .blog-title a {
  text-shadow: 0.1rem 0.1rem 0.2rem rgba(0, 0, 0, 0.15);
}
.theme--light.nav-fixed {
  background: rgba(255, 255, 255, 0.8) !important;
  box-shadow: 0 5px 6px -5px rgba(133, 133, 133, 0.6);
}
.theme--dark.nav-fixed {
  background: rgba(18, 18, 18, 0.8) !important;
}
.theme--dark.nav-fixed a {
  color: rgba(255, 255, 255, 0.8) !important;
}
.theme--light.nav-fixed a {
  color: #4c4948 !important;
}
.nav-fixed .menus-item a,
.nav-fixed .menus-btn a,
.nav-fixed .blog-title a {
  text-shadow: none;
}
.nav-container {
  font-size: 14px;
  width: 100%;
  height: 100%;
}
.nav-mobile-container {
  width: 100%;
  display: flex;
  align-items: center;
}
.blog-title,
.nav-title {
  display: flex;
  align-items: center;
  height: 100%;
}
.blog-title a {
  font-size: 18px;
  font-weight: bold;
}
.user-btn,
.menus-btn,
.menus-item {
  position: relative;
  display: inline-block;
  margin: 0 0 0 0.875rem;
}
.menus-btn a,
.menus-item a {
  transition: all 0.2s;
}
.nav-fixed .menus-btn a:hover,
.nav-fixed .menus-item a:hover {
  color: #49b1f5 !important;
}
.menus-item a:hover:after {
  width: 100%;
}
.menus-item a:after {
  position: absolute;
  bottom: -5px;
  left: 0;
  z-index: -1;
  width: 0;
  height: 3px;
  background-color: #80c8f8;
  content: "";
  transition: all 0.3s ease-in-out;
}
.user-btn a {
  transition: all 0.2s;
}
.user-avatar {
  cursor: pointer;
  border-radius: 50%;
}
.user-btn:hover .user-submenu {
  display: block;
}
.user-submenu {
  position: absolute;
  display: none;
  right: 0;
  width: max-content;
  margin-top: 0;
  margin-right: -35px;
  box-shadow: 0 5px 20px -4px rgba(0, 0, 0, 0.5);
  background-color: rgba(255, 255, 255, 1);
  animation: submenu 0.3s 0.1s ease both;
  border-radius: 10px;
}
.user-submenu li:nth-of-type(1) a:hover {
  border-radius: 10px 10px 0 0;
}
.user-submenu li:nth-last-of-type(1) a:hover {
  border-radius: 0 0 10px 10px;
}
.theme--dark.nav-fixed .user-submenu {
  background: rgba(18, 18, 18, 0.8) !important;
}
.theme--dark.nav .user-submenu {
  background: rgba(0, 0, 0, 0) !important;
}
.theme--dark.nav .user-submenu a {
  color: #eee !important;
}
.user-submenu:before {
  position: absolute;
  top: -8px;
  left: 0;
  width: 100%;
  height: 20px;
  content: "";
}
.user-submenu a {
  line-height: 2;
  color: #4c4948 !important;
  text-shadow: none;
  display: block;
  padding: 6px 14px;
}
.user-submenu a:hover {
  background: #4ab1f4;
}
@keyframes submenu {
  0% {
    opacity: 0;
    filter: alpha(opacity=0);
    transform: translateY(10px);
  }
  100% {
    opacity: 1;
    filter: none;
    transform: translateY(0);
  }
}
</style>
