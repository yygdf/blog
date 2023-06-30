<template>
  <el-menu
    class="side-nav-bar"
    router
    :collapse="isShow"
    :default-active="this.$route.path"
    background-color="#304156"
    text-color="#BFCBD9"
    active-text-color="#409EFF"
  >
    <el-scrollbar style="height: 100%;">
      <template v-for="route of this.$store.state.userMenuList">
        <template v-if="!route.children[0].path">
          <el-menu-item :index="route.path" :key="route.path" :disabled="route.isDisabled == 1">
            <i :class="route.icon" />
            <span>{{ route.children[0].name }}</span>
            <template slot="title">
              <span v-show="isShow">{{ route.children[0].name }}</span>
            </template>
          </el-menu-item>
        </template>
        <template v-else>
          <el-submenu :key="route.path" :index="route.path">
            <template slot="title">
              <i :class="route.icon" />
              <span>{{ route.name }}</span>
            </template>
            <template v-for="(item, index) of route.children">
              <el-menu-item v-if="!item.hidden" :key="index" :index="item.path">
                <i :class="item.icon" />
                <span slot="title">{{ item.name }}</span>
              </el-menu-item>
            </template>
          </el-submenu>
        </template>
      </template>
    </el-scrollbar>
  </el-menu>
</template>

<style scoped>
.side-nav-bar:not(.el-menu--collapse) {
  width: 210px;
}
.side-nav-bar {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
}
.side-nav-bar i {
  margin-right: 1rem;
}
.el-menu--collapse {
  width: 60px;
}
</style>

<script>
export default {
  created() {
    if (document.documentElement.clientWidth < 960) {
      this.$store.state.collapse = true;
    }
  },
  computed: {
    isShow() {
      return this.$store.state.collapse;
    }
  }
};
</script>
