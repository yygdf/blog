<template>
  <el-container class="myScrollbar">
    <el-aside width="auto">
      <SideBar />
    </el-aside>
    <el-container :class="'main-container ' + isHide">
      <el-header height="84px" style="padding:0">
        <NavBar :key="$route.fullPath" />
      </el-header>
      <el-main style="background:#F7F9FB">
        <div class="fade-transform-box">
          <transition name="fade-transform" mode="out-in">
            <router-view :key="$route.fullPath" />
          </transition>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import NavBar from "./components/NavBar";
import SideBar from "./components/SideBar";
export default {
  components: {
    NavBar,
    SideBar
  },
  created() {
    if (document.documentElement.clientWidth < 960) {
      this.$store.state.collapse = true;
    }
  },
  computed: {
    isHide() {
      return this.$store.state.collapse ? "hideSideBar" : "";
    }
  }
};
</script>

<style scoped>
.main-container {
  transition: margin-left 0.45s;
  margin-left: 210px;
}
.hideSideBar {
  margin-left: 60px;
}
.fade-transform-enter-active,
.fade-transform-leave-active {
  transition: all 0.5s ease 0s;
}
.fade-transform-enter {
  opacity: 0;
  transform: translateX(-30px);
}
.fade-transform-leave-to {
  opacity: 0;
  transform: translateX(30px);
}
.fade-transform-box {
  position: relative;
  top: 0;
  bottom: 0;
  width: 100%;
  overflow: hidden;
}
</style>
