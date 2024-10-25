<template>
  <div>
    <div class="nav-bar">
      <div class="hamburger-container" @click="trigger">
        <i :class="isFold" />
      </div>
      <el-breadcrumb>
        <el-breadcrumb-item v-for="item of breadcrumbList" :key="item.path">
          <span>{{ isEn ? item.meta.nameEn : item.name }}</span>
        </el-breadcrumb-item>
      </el-breadcrumb>
      <div class="right-menu">
        <div class="screen-full" @click="fullScreen">
          <i class="el-icon-full-screen" />
        </div>
        <el-dropdown @command="handleCommand">
          <a :href="homeUrl" target="_blank"
            ><el-avatar :src="this.$store.state.avatar" :size="40"
          /></a>
          <i class="el-icon-caret-bottom" />
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="change">
              <i class="el-icon-refresh" />{{ $t("switchTip2") }}
            </el-dropdown-item>
            <el-dropdown-item command="personal" divided>
              <i class="el-icon-postcard" />{{ $t("navBar.personal") }}
            </el-dropdown-item>
            <el-dropdown-item command="logout" divided>
              <i class="el-icon-warning-outline" />{{ $t("navBar.logout") }}
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </div>
    <div class="tabs-view-container">
      <span
        :class="isActive(item)"
        v-for="item of this.$store.state.tabList"
        :key="item.path"
        @click="goTo(item)"
      >
        {{ isEn ? item.meta.nameEn : item.name }}
        <i
          class="el-icon-close"
          v-if="item.path !== '/'"
          @click.stop="removeTab(item)"
        />
      </span>
      <span class="tabs-view-item" style="float:right" @click="closeAllTab">
        {{ $t("navBar.closeAll") }}
      </span>
      <span class="tabs-view-item" style="float:right" @click="closeOtherTab">
        {{ $t("navBar.closeOther") }}
      </span>
    </div>
  </div>
</template>

<script>
import { resetRouter } from "../../router";
export default {
  created() {
    this.tab = {
      path: this.$route.path,
      name: this.$route.name,
      mata: this.$route.meta
    };
    this.$store.commit("saveCurrentTab", this.tab);
    this.breadcrumbList = this.$route.matched.filter(item => item.name);
    this.$store.commit("saveTab", this.$route);
  },
  data: function() {
    return {
      tab: {},
      breadcrumbList: [],
      fullscreen: false
    };
  },
  methods: {
    goTo(tab) {
      this.tab = {
        path: tab.path,
        name: tab.name,
        meta: tab.meta
      };
      this.$router.push({ path: tab.path });
      this.$store.commit("saveCurrentTab", tab);
    },
    trigger() {
      this.$store.commit("trigger");
    },
    removeTab(tab) {
      this.$store.commit("removeTab", tab);
      if (tab.path === this.$route.path) {
        let tabList = this.$store.state.tabList;
        this.$router.push({ path: tabList[tabList.length - 1].path });
      }
    },
    fullScreen() {
      let element = document.documentElement;
      if (this.fullscreen) {
        if (document.exitFullscreen) {
          document.exitFullscreen();
        } else if (document.webkitCancelFullScreen) {
          document.webkitCancelFullScreen();
        } else if (document.mozCancelFullScreen) {
          document.mozCancelFullScreen();
        } else if (document.msExitFullscreen) {
          document.msExitFullscreen();
        }
      } else {
        if (element.requestFullscreen) {
          element.requestFullscreen();
        } else if (element.webkitRequestFullScreen) {
          element.webkitRequestFullScreen();
        } else if (element.mozRequestFullScreen) {
          element.mozRequestFullScreen();
        } else if (element.msRequestFullscreen) {
          element.msRequestFullscreen();
        }
      }
      this.fullscreen = !this.fullscreen;
    },
    closeAllTab() {
      this.$store.commit("resetTab");
      this.$router.push({ path: "/" });
    },
    closeOtherTab() {
      this.$store.commit("resetTab");
      if (this.tab.path !== "/") {
        this.$store.state.tabList.push({
          name: this.tab.name,
          path: this.tab.path,
          meta: this.tab.meta
        });
      }
    },
    handleCommand(command) {
      if (command === "personal") {
        this.$router.push({ path: "/personal" });
      } else if (command === "logout") {
        this.axios.post("/api/logout").then(({ data }) => {
          if (data.flag) {
            this.$store.commit("removeToken");
            this.$store.commit("logout");
            this.$store.commit("resetTab");
            resetRouter();
            this.$router.push({ path: "/login" });
            this.$message.success(data.message);
          } else {
            this.$message.error(data.message);
          }
        });
      } else if (command === "change") {
        if (this.$i18n.locale === "en_US") {
          localStorage.setItem("lang", "zh_CN");
          this.$i18n.locale = "zh_CN";
        } else {
          localStorage.setItem("lang", "en_US");
          this.$i18n.locale = "en_US";
        }
      }
    }
  },
  computed: {
    isFold() {
      return this.$store.state.collapse ? "el-icon-s-unfold" : "el-icon-s-fold";
    },
    isActive() {
      return function(tab) {
        if (tab.path === this.$route.path) {
          return "tabs-view-item-active";
        }
        return "tabs-view-item";
      };
    },
    homeUrl() {
      let url = process.env.VUE_APP_HOME_URL;
      if (this.$store.state.weight <= 400) {
        url = url + "/" + this.$store.state.userId;
      }
      return this.$store.state.token
        ? url +
            "?token=" +
            this.$store.state.token +
            "&loginUserDTO=" +
            JSON.stringify({
              userId: this.$store.state.userId,
              intro: this.$store.state.intro,
              email: this.$store.state.email,
              avatar: this.$store.state.avatar,
              gender: this.$store.state.gender,
              weight: this.$store.state.weight,
              website: this.$store.state.website,
              nickname: this.$store.state.nickname,
              modifiedFlag: this.$store.state.modifiedFlag,
              articleLikeSet: this.$store.state.articleLikeSet,
              commentLikeSet: this.$store.state.commentLikeSet
            })
        : url;
    },
    isEn() {
      return this.$i18n.locale === "en_US";
    }
  }
};
</script>

<style scoped>
.nav-bar {
  display: flex;
  align-items: center;
  padding-left: 15px;
  padding-right: 30px;
  height: 50px;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
}
.hamburger-container {
  font-size: 1.25rem;
  cursor: pointer;
  margin-right: 24px;
}
.tabs-view-container {
  padding-left: 10px;
  padding-right: 10px;
  height: 33px;
  background: #fff;
  border-bottom: 1px solid #d8dce5;
  box-shadow: 0 1px 3px 0 rgba(0, 0, 0, 0.12), 0 0 3px 0 rgba(0, 0, 0, 0.04);
}
.tabs-view-item {
  display: inline-block;
  cursor: pointer;
  height: 25px;
  line-height: 25px;
  border: 1px solid #d8dce5;
  color: #495060;
  background: #fff;
  padding: 0 8px;
  font-size: 12px;
  margin-top: 4px;
  margin-left: 5px;
}
.tabs-view-item-active {
  display: inline-block;
  cursor: pointer;
  height: 26px;
  line-height: 26px;
  padding: 0 8px;
  font-size: 12px;
  margin-top: 4px;
  margin-left: 5px;
  background-color: #42b983;
  color: #fff;
  border-color: #42b983;
}
.tabs-view-item-active:before {
  content: "";
  background: #fff;
  display: inline-block;
  width: 8px;
  height: 8px;
  border-radius: 50%;
  position: relative;
  margin-right: 2px;
}
.el-icon-close {
  padding: 0.1rem;
}
.el-icon-close:hover {
  border-radius: 50%;
  background: #b4bccc;
  transition-duration: 0.3s;
}
.right-menu {
  margin-left: auto;
  display: flex;
  align-items: center;
}
.el-icon-caret-bottom {
  margin-left: 0.5rem;
  font-size: 0.75rem;
}
.screen-full {
  cursor: pointer;
  margin-right: 1rem;
  font-size: 1.25rem;
}
</style>
